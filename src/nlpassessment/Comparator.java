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

    public static HashMap<String, Integer> getTagCounts(ArrayList<Token> tokens, String targetTag) {

        HashMap<String, Integer> tagCounts = new HashMap<>();

        for (Token token : tokens) {
            if (tagCounts.keySet().contains(token.tags.get(targetTag))) {
                tagCounts.put(token.tags.get(targetTag), tagCounts.get(token.tags.get(targetTag)) + 1);
            } else {
                tagCounts.put(token.tags.get(targetTag), 1);
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
            if (token.tags.get("pos").equalsIgnoreCase("NN")) {
                noun++;
            } else if (token.tags.get("pos").equalsIgnoreCase("JJ")) {
                adj++;
            } else if (token.tags.get("pos").equalsIgnoreCase("RB")) {
                adv++;
            } else if (token.tags.get("pos").equalsIgnoreCase("VB")) {
                verb++;
            } else {
                other++;
            }
        }

    }

    //Compares tags of two standardized token lists
    //NOTE: list lengths and all contained tokens MUST match
    public static String compareTags(ArrayList<Token> results, ArrayList<Token> goldStandard, String key, String tagType) {

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

            if (results.get(i).tags.get(tagType).equalsIgnoreCase(key)
                    && goldStandard.get(i).tags.get(tagType).equalsIgnoreCase(key)) {

                truePositives++;

            } else if (goldStandard.get(i).tags.get(tagType).equalsIgnoreCase(key)
                    && !results.get(i).tags.get(tagType).equalsIgnoreCase(key)) {

                falseNegatives++;

            } else if (results.get(i).tags.get(tagType).equalsIgnoreCase(key)
                    && !goldStandard.get(i).tags.get(tagType).equalsIgnoreCase(key)) {
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

  

}
