/*
 * The MIT License
 *
 * Copyright 2016 Neal Logan.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package nlpassessment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Neal Logan
 */
public class Comparator {

    public static HashMap<String, Integer> getTagCounts(ArrayList<Token> tokens) {

        HashMap<String, Integer> tagCounts = new HashMap<>();

        for (Token token : tokens) {
            if (tagCounts.keySet().contains(token.tagset)) {
                tagCounts.put(token.tagset, tagCounts.get(token.tagset) + 1);
            } else {
                tagCounts.put(token.tagset, 1);
            }
        }
        return tagCounts;
    }

    public static int getSemanticTokenCount(ArrayList<Token> tokens) {
        int count = 0;
        for (Token token : tokens) {
            if (token.semantic) {
                count++;
            }
        }
        return count;
    }

    public static void countTags(ArrayList<Token> tokens) {
        int adj = 0, noun = 0, adv = 0, verb = 0, other = 0;

        for (Token token : tokens) {
            if (token.tagset.equalsIgnoreCase("NN")) {
                noun++;
            } else if (token.tagset.equalsIgnoreCase("JJ")) {
                adj++;
            } else if (token.tagset.equalsIgnoreCase("RB")) {
                adv++;
            } else if (token.tagset.equalsIgnoreCase("VB")) {
                verb++;
            } else {
                other++;
            }
        }

    }

    //Compares tags of two standardized token lists
    //NOTE: list lengths and all contained tokens MUST match
    public static String compareTags(ArrayList<Token> results, ArrayList<Token> goldStandard, String key) {

        if (results.size() != goldStandard.size()) {
            System.out.println("Tokens list lengths differ");
        }

        System.out.println("\n\n\nSTARTING COMPARISON"
                + "\n");

        int trueNegatives = 0;
        int falseNegatives = 0;
        int truePositives = 0;
        int falsePositives = 0;
        int tokenMismatches = 0;

        //Detect mismatches on tokens which differ between results and standard
        //Unless both are tagged "other"
        for (int i = 0; i < results.size(); i++) {
            if (!results.get(i).token.equalsIgnoreCase(goldStandard.get(i).token) //                    && !results.get(goldIter).tagset.equalsIgnoreCase("Other")
                    //                    && !goldStandard.get(goldIter).tagset.equalsIgnoreCase("Other")
                    ) {
                tokenMismatches++;
                System.out.println("Mismatch: " + results.get(i).toString() + " \t\t " + goldStandard.get(i).toString());
                System.out.println("Warning: Results invalid due to " + tokenMismatches + " token mismatches");
            }

            if (results.get(i).tagset.equalsIgnoreCase(key)
                    && goldStandard.get(i).tagset.equalsIgnoreCase(key)) {

                truePositives++;

            } else if (goldStandard.get(i).tagset.equalsIgnoreCase(key)
                    && !results.get(i).tagset.equalsIgnoreCase(key)) {

                falseNegatives++;

            } else if (results.get(i).tagset.equalsIgnoreCase(key)
                    && !goldStandard.get(i).tagset.equalsIgnoreCase(key)) {
                falsePositives++;

            } else {
                trueNegatives++;
            }

        }

        assert results.size() == (trueNegatives + truePositives + falseNegatives + falsePositives);

        double sensitivity = (double) truePositives / (double) (falseNegatives + truePositives);
        double specificity = (double) trueNegatives / (double) (falsePositives + trueNegatives);

        String report = key;
        report += "\nSampleSize = " + results.size();
        report += "\nTruePos = " + truePositives;
        report += "\nFalseNeg = " + falseNegatives;
        report += "\nFalsePos = " + falsePositives;
        report += "\nSensitivity = " + sensitivity;
        report += "\nSpecificity = " + specificity;

        return report;

    }

    //Compares tagger results to a gold standard
    //Handles minor differences in tokenization
    //by ignoring mismatches using an improvement over the skip/catchup
    //method detail below
    //Note: DEPRECATED in favor of the similar restriction method
    public static String robustlyCompareTags(ArrayList<Token> results, ArrayList<Token> gold, String key) {

        //Helps determine how far to look when dealing with token mismatches
        int SKIP_CATCHUP_CONSTANT = 3;

        if (results.size() != gold.size()) {
            System.out.println("Tokens list lengths differ");
        }

        System.out.println("\n\n\nSTARTING COMPARISON"
                + "\n");

        //Results variables
        int trueNegatives = 0;
        int falseNegatives = 0;
        int truePositives = 0;
        int falsePositives = 0;
        int totalGoldTokensIgnored = 0;
        int totalResultsTokensIgnored = 0;

        //Iterators
        int goldIter = 0, resIter = 0;
        while (goldIter < gold.size()
                && resIter < results.size()) {

            //Debug
//            System.out.println("Gold Iterator: " + goldIter + "  Results Iterator: " + resIter);
            //DETECTING TOKEN MISMATCH
            boolean tokenMatch = false;
            if (!results.get(resIter).token.equalsIgnoreCase(gold.get(goldIter).token)) {
                System.out.println("Token mismatch: gold = '" + gold.get(goldIter).token + "' res = '" + results.get(resIter).token + "'");
            } else {
                tokenMatch = true;
            }

            //HANDLING TOKEN MISMATCH
            //Uses the gold iterator as an anchor, searches in increasingly wide regions
            //around the results iterator, but only forward of the starting point
            //The further the gold iterator has to increase, the further the results iterator is allowed to range
            //Neither is permitted to search backwards from their starting position at mismatch, 
            //since it can be assumed that immediately preceding tokens are properly matched
            if (!tokenMatch) {
                int startingResIter = resIter;

                //corrections for ugly algorithm below
                int deltaGoldIter = -1;
                goldIter--;

                //ITERATING FORWARD IN GOLD STANDARD
                while (!tokenMatch
                        && goldIter < gold.size() - 2) {
                    goldIter++;
                    deltaGoldIter++;

                    //SEARCHING INCREASINGLY WIDE IN RESULTS TO FIND MATCHING TOKEN
                    resIter = startingResIter + Math.max(0, deltaGoldIter / 2 - SKIP_CATCHUP_CONSTANT);
                    while (resIter < startingResIter + deltaGoldIter * 1.5 + SKIP_CATCHUP_CONSTANT
                            && resIter < results.size()) {
                        if (results.get(resIter).token.equalsIgnoreCase(gold.get(goldIter).token)) {
                            tokenMatch = true;

                            //Debug
                            System.out.println("Match found: " + results.get(resIter).token + "=" + gold.get(goldIter).token);

                            break;
                        } else {
                            resIter++;
                        }
                    }

                }

                //CALCULATING 
                int deltaResultsIter = resIter - startingResIter;
                System.out.println("Ignored " + deltaResultsIter + " results tokens and " + deltaGoldIter + " gold tokens ");
                totalGoldTokensIgnored += deltaGoldIter;
                totalResultsTokensIgnored += deltaResultsIter;

                //Debug
                if (goldIter < gold.size() && resIter < results.size()) {
                    System.out.println("Gold: " + goldIter + " " + gold.get(goldIter).token + " Res: " + resIter + " " + results.get(resIter).token);
                }

            }
            if (!tokenMatch) {
                System.out.println("MATCH FAILED");
                break;
            }

            //Debug
            if (goldIter >= gold.size()
                    || resIter >= results.size()) {
                System.out.println("ITERATOR OUT OF BOUNDS");
                System.out.println("Gold Iterator: " + goldIter + "/" + gold.size()
                        + "\n" + "Results Iterator: " + resIter + "/" + results.size());
                break;
            }

            //COMPARING TAGS
            if (results.get(resIter).tagset.equalsIgnoreCase(key)
                    && gold.get(goldIter).tagset.equalsIgnoreCase(key)) {

                truePositives++;

            } else if (gold.get(goldIter).tagset.equalsIgnoreCase(key)
                    && !results.get(resIter).tagset.equalsIgnoreCase(key)) {

                falseNegatives++;

            } else if (results.get(resIter).tagset.equalsIgnoreCase(key)
                    && !gold.get(goldIter).tagset.equalsIgnoreCase(key)) {

                falsePositives++;

            } else if (!results.get(resIter).tagset.equalsIgnoreCase(key)
                    && !gold.get(goldIter).tagset.equalsIgnoreCase(key)) {

                trueNegatives++;

            }
            goldIter++;
            resIter++;
        }

        assert results.size() == (trueNegatives + truePositives + falseNegatives + falsePositives);

        double sensitivity = (double) truePositives / (double) (falseNegatives + truePositives);
        double specificity = (double) trueNegatives / (double) (falsePositives + trueNegatives);

        String report = key;
        report += "\nSampleSize = " + results.size();
        report += "\nTruePos = " + truePositives;
        report += "\nFalseNeg = " + falseNegatives;
        report += "\nFalsePos = " + falsePositives;
        report += "\nSensitivity = " + sensitivity;
        report += "\nSpecificity = " + specificity;

        return report;

    }

}
