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
public class ComparisonTool {
    
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
    
    
    public static void compareResults(ArrayList<Token> results, ArrayList<Token> goldStandard, String key) {

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
            if (!results.get(i).token.equalsIgnoreCase(goldStandard.get(i).token)
//                    && !results.get(i).tag.equalsIgnoreCase("Other")
//                    && !goldStandard.get(i).tag.equalsIgnoreCase("Other")
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
                if (ComparisonTool.DEBUG) System.out.println("TruePos: " + results.get(i).toString() + " matches standard " + goldStandard.get(i).toString());
                
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

}
