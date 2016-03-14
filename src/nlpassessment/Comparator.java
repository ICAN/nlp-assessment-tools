/*
 * The MIT License
 *
 * Copyright 2016 Neal.
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

/**
 *
 * @author Neal
 */
public class Comparator {

    public static boolean DEBUG = false;

    public static void reportResults(String key, int truePositives, int falseNegatives, int falsePositives, int totalTokens) {
        System.out.println("Tagged " + key + " for " + truePositives + " matches and " + falseNegatives + " mismatches."
                + "\n" + falsePositives + " false positives from " + totalTokens + " total tokens."
                + "Sensitivity: " + (double) truePositives / ((double) falseNegatives + (double) truePositives)
                + "   Specificity: " + (double) (totalTokens - falsePositives) / (double) totalTokens);
    }

    public static void countTags(ArrayList<Token> tokens) {
        int adj = 0, noun = 0, adv = 0, verb = 0, other = 0;

        for (Token token : tokens) {
            if (token.tag.equalsIgnoreCase("NN")) {
                noun++;
            } else if (token.tag.equalsIgnoreCase("JJ")) {
                adj++;
            } else if (token.tag.equalsIgnoreCase("RB")) {
                adv++;
            } else if (token.tag.equalsIgnoreCase("VB")) {
                verb++;
            } else {
                other++;
            }
        }

    }

    public static void compareTags(ArrayList<Token> results, ArrayList<Token> goldStandard, String key) {

        if (results.size() != goldStandard.size()) {
            System.out.println("Tokens list lengths differ");
        }

        System.out.println("\n\n\nSTARTING COMPARISON"
                + "\n");

        int falseNegatives = 0;
        int truePositives = 0;
        int falsePositives = 0;
        int tokenMismatches = 0;

        //Detect mismatches on tokens which differ between results and standard
        //Unless both are tagged "other"
        for (int i = 0; i < results.size(); i++) {
            if (!results.get(i).token.equalsIgnoreCase(goldStandard.get(i).token) //                    && !results.get(goldIter).tag.equalsIgnoreCase("Other")
                    //                    && !goldStandard.get(goldIter).tag.equalsIgnoreCase("Other")
                    ) {
                tokenMismatches++;
                System.out.println("Mismatch: " + results.get(i).toString() + " \t\t " + goldStandard.get(i).toString());
                System.out.println("Warning: Results invalid due to " + tokenMismatches + " token mismatches");
            }

            if (results.get(i).tag.equalsIgnoreCase(key)
                    && goldStandard.get(i).tag.equalsIgnoreCase(key)) {
                //Action
                truePositives++;

                //Debug
                if (Comparator.DEBUG) {
                    System.out.println("TruePos: " + results.get(i).toString() + " matches standard " + goldStandard.get(i).toString());
                }

            } else if (goldStandard.get(i).tag.equalsIgnoreCase(key)
                    && !results.get(i).tag.equalsIgnoreCase(key)) {

                falseNegatives++;

            } else if (results.get(i).tag.equalsIgnoreCase(key)
                    && !goldStandard.get(i).tag.equalsIgnoreCase(key)) {
                falsePositives++;
            }

        }

        reportResults(key, truePositives, falseNegatives, falsePositives, results.size());

    }

    //Handles minor differences in tokenization
    //by ignoring mismatches
    //Fast forwards to nearest end of sentence
    //NOTE: Inferior
    public static void skipSentenceRobustCompareTags(ArrayList<Token> results, ArrayList<Token> gold, String key) {

        if (results.size() != gold.size()) {
            System.out.println("Tokens list lengths differ");
        }

        System.out.println("\n\n\nSTARTING COMPARISON"
                + "\n");

        int totalComparisons = 0;
        int falseNegatives = 0;
        int truePositives = 0;
        int falsePositives = 0;
        int totalGoldTokensIgnored = 0;
        int totalResultsTokensIgnored = 0;
        int iterOffset = 0;

        //Detect mismatches on tokens which differ between results and standard
        //Unless both are tagged "other"
        for (int goldIter = 0; goldIter < results.size(); goldIter++) {
            int resIter = goldIter + iterOffset;
            System.out.println("Gold rebuilt: " + goldIter + "  Res rebuilt: " + resIter);

            String r = results.get(resIter).token;
            String g = gold.get(goldIter).token;

            //Handling token mismatches
            if(!r.equalsIgnoreCase(g)) {
                System.out.println("Token mismatch: gold = '" + gold.get(goldIter).token + "' res = '" + results.get(resIter).token);
                
                int resIgnoreTokens = 0, goldIgnoreTokens = 0;
                
                //Fast forward to end of sentence for gold std
                System.out.println("IGNORING RESULTS TOKENS");
                while(true) {
                    if (results.get(resIter).token.matches(General.END_OF_SENTENCE)) {
                        resIgnoreTokens++;
                        resIter++;
                        break;
                    } else {
                        System.out.println("Ignoring " + results.get(resIter).token);
                        resIgnoreTokens++;
                        resIter++;
                    }
                }
                
                //Fast forward to end of sentence for gold std
                System.out.println("IGNORING GOLD TOKENS");
                while(true) {
                    if (gold.get(goldIter).token.matches(".*" + General.END_OF_SENTENCE)) {
                        goldIgnoreTokens++;
                        goldIter++;
                        break;
                    } else {
                        System.out.println("Ignoring " + gold.get(goldIter).token);
                        goldIgnoreTokens++;
                        goldIter++;
                    }
                }
                
                System.out.println("Ignoring " + resIgnoreTokens + " results tokens and " + goldIgnoreTokens + " gold tokens ");
                totalGoldTokensIgnored += goldIgnoreTokens;
                totalResultsTokensIgnored += resIgnoreTokens;
                iterOffset = resIter - goldIter;
                System.out.println("Gold: " + goldIter + "  Res: " + resIter);
            }
            
            
            //Comparing
            totalComparisons++;
            if (results.get(resIter).tag.equalsIgnoreCase(key)
                    && gold.get(goldIter).tag.equalsIgnoreCase(key)) {
                //Action
                truePositives++;

                //Debug
                if (Comparator.DEBUG) {
                    System.out.println("TruePos: " + results.get(goldIter).toString() + " matches standard " + gold.get(goldIter).toString());
                }

            } else if (gold.get(goldIter).tag.equalsIgnoreCase(key)
                    && !results.get(resIter).tag.equalsIgnoreCase(key)) {

                falseNegatives++;

            } else if (results.get(resIter).tag.equalsIgnoreCase(key)
                    && !gold.get(goldIter).tag.equalsIgnoreCase(key)) {
                falsePositives++;
            }

        }

        
        System.out.println("Ignored " + totalGoldTokensIgnored + " gold std tokens and " + totalResultsTokensIgnored + " results tokens." );
        reportResults(key, truePositives, falseNegatives, falsePositives, totalComparisons);
        
    }

    
    //Handles minor differences in tokenization
    //by ignoring mismatches
    //Fast forwards to nearest end of sentence
    public static void skipCatchupRobustCompareTags(ArrayList<Token> results, ArrayList<Token> gold, String key) {

        int IGNORE_RANGE = 3;
        
        if (results.size() != gold.size()) {
            System.out.println("Tokens list lengths differ");
        }

        System.out.println("\n\n\nSTARTING COMPARISON"
                + "\n");

        int totalComparisons = 0;
        int falseNegatives = 0;
        int truePositives = 0;
        int falsePositives = 0;
        int totalGoldTokensIgnored = 0;
        int totalResultsTokensIgnored = 0;

        //Detect mismatches on tokens which differ between results and standard
        //Unless both are tagged "other"
        int goldIter = 0;
        int resIter = 0;
        
        
        while (goldIter < gold.size()
                && resIter < results.size()) {

            System.out.println("Gold rebuilt: " + goldIter + "  Res rebuilt: " + resIter);



            //Handling token mismatches
            if(!results.get(resIter).token.equalsIgnoreCase(gold.get(goldIter).token)) {
                System.out.println("Token mismatch: gold = '" + gold.get(goldIter).token + "' res = '" + results.get(resIter).token);
                
                
                
                //Fast forward to end of sentence for gold std
                System.out.println("IGNORING GOLD TOKENS");
                int deltaGoldIter = 0;
                while (deltaGoldIter < IGNORE_RANGE
                        && goldIter < gold.size() - 1) {
                    System.out.println(gold.get(goldIter).token);
                    goldIter++;
                    deltaGoldIter++;
                }
                                
                //Fast forward to end of sentence for gold std
                System.out.println("IGNORING RESULTS TOKENS");
                int deltaResultsIter = 0;
                while(!results.get(resIter).token.equalsIgnoreCase(gold.get(goldIter).token)
                        && deltaResultsIter < IGNORE_RANGE * 2
                        && resIter < results.size() - 1) {
                    System.out.println(results.get(resIter).token);
                    resIter++;
                    deltaResultsIter++;
                }
                
                                               
                System.out.println("Ignoring " + deltaResultsIter + " results tokens and " + deltaGoldIter + " gold tokens ");
                totalGoldTokensIgnored += deltaGoldIter;
                totalResultsTokensIgnored += deltaResultsIter;
                System.out.println("Gold: " + goldIter + "  Res: " + resIter);
            }
            
            
            //Comparing
            totalComparisons++;
            if (results.get(resIter).tag.equalsIgnoreCase(key)
                    && gold.get(goldIter).tag.equalsIgnoreCase(key)) {
                //Action
                truePositives++;

                //Debug
                if (Comparator.DEBUG) {
                    System.out.println("TruePos: " + results.get(goldIter).toString() + " matches standard " + gold.get(goldIter).toString());
                }

            } else if (gold.get(goldIter).tag.equalsIgnoreCase(key)
                    && !results.get(resIter).tag.equalsIgnoreCase(key)) {

                falseNegatives++;

            } else if (results.get(resIter).tag.equalsIgnoreCase(key)
                    && !gold.get(goldIter).tag.equalsIgnoreCase(key)) {
                falsePositives++;
            }

            goldIter++;
            resIter++;
        }

        
        System.out.println("Ignored " + totalGoldTokensIgnored + " gold std tokens and " + totalResultsTokensIgnored + " results tokens." );
        reportResults(key, truePositives, falseNegatives, falsePositives, totalComparisons);
        
    }
    
    //Handles minor differences in tokenization
    //by ignoring mismatches
    //Fast forwards to nearest end of sentence
    public static void improvedSkipCatchupRobustCompareTags(ArrayList<Token> results, ArrayList<Token> gold, String key) {

        int SKIP_CATCHUP_RANGE = 3;
        
        if (results.size() != gold.size()) {
            System.out.println("Tokens list lengths differ");
        }

        System.out.println("\n\n\nSTARTING COMPARISON"
                + "\n");

        int totalComparisons = 0;
        int falseNegatives = 0;
        int truePositives = 0;
        int falsePositives = 0;
        int totalGoldTokensIgnored = 0;
        int totalResultsTokensIgnored = 0;        


        int goldIter = 0, resIter = 0;
        while (goldIter < gold.size()
                && resIter < results.size()) {

//            System.out.println("Gold Iterator: " + goldIter + "  Results Iterator: " + resIter);



            //Handling token mismatches
            if(!results.get(resIter).token.equalsIgnoreCase(gold.get(goldIter).token)) {
                System.out.println("Token mismatch: gold = '" + gold.get(goldIter).token + "' res = '" + results.get(resIter).token);
                
                int deltaGoldIter = 0, deltaResultsIter = 0;
                
                while(deltaGoldIter < SKIP_CATCHUP_RANGE) {
                    
                    
                    
                    
                    deltaGoldIter++;
                }
                
                
                
                
                                               
                System.out.println("Ignoring " + deltaResultsIter + " results tokens and " + deltaGoldIter + " gold tokens ");
                totalGoldTokensIgnored += deltaGoldIter;
                totalResultsTokensIgnored += deltaResultsIter;
                System.out.println("Gold: " + goldIter + "  Res: " + resIter);
            }
            
            
            //Comparing
            totalComparisons++;
            if (results.get(resIter).tag.equalsIgnoreCase(key)
                    && gold.get(goldIter).tag.equalsIgnoreCase(key)) {
                //Action
                truePositives++;

                //Debug
                if (Comparator.DEBUG) {
                    System.out.println("TruePos: " + results.get(goldIter).toString() + " matches standard " + gold.get(goldIter).toString());
                }

            } else if (gold.get(goldIter).tag.equalsIgnoreCase(key)
                    && !results.get(resIter).tag.equalsIgnoreCase(key)) {

                falseNegatives++;

            } else if (results.get(resIter).tag.equalsIgnoreCase(key)
                    && !gold.get(goldIter).tag.equalsIgnoreCase(key)) {
                falsePositives++;
            }

            goldIter++;
            resIter++;
        }

        
        System.out.println("Ignored " + totalGoldTokensIgnored + " gold std tokens and " + totalResultsTokensIgnored + " results tokens." );
        reportResults(key, truePositives, falseNegatives, falsePositives, totalComparisons);
        
    }
    
    
}
