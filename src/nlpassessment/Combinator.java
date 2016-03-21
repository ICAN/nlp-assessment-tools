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
import java.util.HashSet;

/**
 *
 * @author Neal
 */
public class Combinator {
    
    //Returns the "input" token list, excluding those which fail to find a match in "standard"
    //Does not affect either "input" or "standard"
    //TODO: rewrite/clarify
    public static ArrayList<Token> getMinimalTokenList(ArrayList<Token> input, ArrayList<Token> standard) {

        ArrayList<Token> output = new ArrayList<>();

        //Helps determine how far to look when dealing with token mismatches
        //Higher numbers search further; 3 seems to work well
        int SKIP_CATCHUP_CONSTANT = 3;

        System.out.println("\n\nSTARTING TOKEN REMOVAL"
                + "\n");

        //Results variables
        int standardTokensIgnored = 0;
        int inputTokensExcluded = 0;

        //Iterators
        int stdIter = 0, inputIter = 0;
        while (stdIter < standard.size()
                && inputIter < input.size()) {

            //DETECTING TOKEN MISMATCH
            boolean tokenMatch = false;
            if (!input.get(inputIter).token.equalsIgnoreCase(standard.get(stdIter).token)) {
                System.out.println("Token mismatch: std='" + standard.get(stdIter).token + "' input='" + input.get(inputIter).token + "'");
            } else {
                tokenMatch = true;
            }

            //HANDLING TOKEN MISMATCH
            //Uses the standard iterator as an anchor, searches in increasingly wide regions
            //around the output iterator, but only forward of the starting point
            //The further the standard iterator has to increase, the further the input iterator is allowed to range
            //Neither is permitted to search backwards from their starting position at mismatch, 
            //since it can be assumed that immediately preceding tokens are properly matched
            if (!tokenMatch) {
                int startingInputIter = inputIter;

                //corrections for ugly algorithm below
                int deltaStdIter = -1;
                stdIter--;

                //ITERATING FORWARD IN GOLD STANDARD
                while (!tokenMatch
                        && stdIter < standard.size() - 2) {
                    stdIter++;
                    deltaStdIter++;

                    //SEARCHING INCREASINGLY WIDE IN RESULTS TO FIND MATCHING TOKEN
                    inputIter = startingInputIter + Math.max(0, deltaStdIter / 2 - SKIP_CATCHUP_CONSTANT);

                    while (inputIter < startingInputIter + deltaStdIter * 1.5 + SKIP_CATCHUP_CONSTANT
                            && inputIter < input.size()) {
                        if (input.get(inputIter).token.equalsIgnoreCase(standard.get(stdIter).token)) {
                            tokenMatch = true;

                            //Debug
                            System.out.println("Match found: " + input.get(inputIter).token + "=" + standard.get(stdIter).token);

                            break;
                        } else {
                            inputIter++;
                        }
                    }

                }

                //CALCULATING TRACKERS
                int deltaInputIter = inputIter - startingInputIter;
                System.out.println("Ignored " + deltaInputIter + " input tokens and " + deltaStdIter + " standard tokens this mismatch");
                standardTokensIgnored += deltaStdIter;
                inputTokensExcluded += deltaInputIter;

                //Debug
                if (stdIter < standard.size() && inputIter < input.size()) {
                    System.out.println("Gold: " + stdIter + " " + standard.get(stdIter).token + " Res: " + inputIter + " " + input.get(inputIter).token);
                }

            }

            //ADD INPUT TOKEN TO OUTPUT
            if (tokenMatch) {
                output.add(input.get(inputIter));
            } else {
                System.out.println("ERROR: MATCHING FAILED");
            }

//            //Debug
//            if (stdIter >= standard.size()
//                    || inputIter >= input.size()) {
//                System.out.println("ITERATOR OUT OF BOUNDS");
//                System.out.println("Standard Iterator: " + stdIter + "/" + standard.size()
//                        + "\n" + "Target Iterator: " + inputIter + "/" + input.size());
//                break;
//            }
            inputIter++;
            stdIter++;
        }

        assert (output.size() + inputTokensExcluded == input.size());

        System.out.println("\n" + standardTokensIgnored + "/" + standard.size() + " standard tokens unmatched "
                + "\n" + inputTokensExcluded + "/" + input.size() + " input tokens excluded");

        return output;

    }
    
    
    //Returns a list of tokens, excluding any tokens which don't appear in *all* input lists
    public static ArrayList<Token> getMinimalTokenList(ArrayList<ArrayList<Token>> inputs) {
        
        //Start with arbitrary base token list
        ArrayList<Token> output = inputs.get(0); 
        
        //Iterate through other lists, restricting base list
        for(int i = 1; i < inputs.size(); i++) {
            output = getMinimalTokenList(output, inputs.get(i));
        }
        
        System.out.print("\nInput lengths: ");
        for(ArrayList<Token> list : inputs) {
            System.out.print(list.size() + ", ");
        }
        
        System.out.print("\nOutput length: " + output.size());
        return output;
    }
    
}
