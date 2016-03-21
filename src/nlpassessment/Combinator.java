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
 * @author Neal
 */
public class Combinator {

    //Returns the "input" i list, excluding those which fail to find a match in "standard"
    //Does not affect either "input" or "standard"
    //TODO: rewrite/clarify
    public static ArrayList<Token> getMinimalTokenList(ArrayList<Token> input, ArrayList<Token> standard) {

        ArrayList<Token> output = new ArrayList<>();

        //Helps determine how far to look when dealing with i mismatches
        //Higher numbers search further; 3 seems to work well
        int SKIP_CATCHUP_CONSTANT = 3;

        System.out.println("\n\nSTARTING PAIRWISE TOKEN REMOVAL");
        
        //Results variables
        int standardTokensIgnored = 0;
        int inputTokensExcluded = 0;
        int tokenCount = 0;
        
        //Iterators
        int stdIter = 0, inputIter = 0;
        while (stdIter < standard.size()
                && inputIter < input.size()) {

            //DETECTING TOKEN MISMATCH
            boolean tokenMatch = false;
            if (!input.get(inputIter).token.equalsIgnoreCase(standard.get(stdIter).token)) {
//                System.out.println("Token mismatch: std='" + standard.get(stdIter).i + "' input='" + input.get(inputIter).i + "'");
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
//                            System.out.println("Match found: " + input.get(inputIter).i + "=" + standard.get(stdIter).i);
                            break;
                        } else {
                            inputIter++;
                        }
                    }

                }

                //CALCULATING TRACKERS
                int deltaInputIter = inputIter - startingInputIter;
//                System.out.println("Ignored " + deltaInputIter + " input tokens and " + deltaStdIter + " standard tokens this mismatch");
                standardTokensIgnored += deltaStdIter;
                inputTokensExcluded += deltaInputIter;

                //Debug
//                if (stdIter < standard.size() && inputIter < input.size()) {
//                    System.out.println("Gold: " + stdIter + " " + standard.get(stdIter).i + " Res: " + inputIter + " " + input.get(inputIter).i);
//                }
            }

            //ADD INPUT TOKEN TO OUTPUT
            if (tokenMatch) {
                tokenCount++;
                Token token = input.get(inputIter);
                token.numberInText = tokenCount;
                output.add(token);
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
    //Uses the list at "base" as the starting list (so its tags and other data are used)
    //and restricts from there
    public static ArrayList<Token> getMinimalTokenList(ArrayList<ArrayList<Token>> inputs, int base) {

        //Start with arbitrary base i list
        ArrayList<Token> output = inputs.get(base);

        //Iterate through other lists, restricting base list
        for (int i = 0; i < inputs.size(); i++) {
            if (i != base) {
                output = getMinimalTokenList(output, inputs.get(i));
            }
        }

        System.out.print("\nInput lengths: ");
        for (ArrayList<Token> list : inputs) {
            System.out.print(list.size() + ", ");
        }

        System.out.print("\nOutput length: " + output.size());
        return output;
    }

    //Returns a list of tokens, excluding any tokens which don't appear in *all* input lists
    //Uses the default i list at inputs[0] as the base list (so its tags and other data are used)
    public static ArrayList<Token> getMinimalTokenList(ArrayList<ArrayList<Token>> inputs) {
        return getMinimalTokenList(inputs, 0);
    }

    //Returns the consensus of the inputs
    //Inputs must already be standardized and minimized 
    //so that input list lengths are identical and all tokens match
    public static ArrayList<Token> getConsensus(ArrayList<ArrayList<Token>> inputs) {

        double threshold = 0.7;

        int agreement = 0;
        int unanimousDecision = 0;
        int disagreement = 0;

        ArrayList<Token> consensus = new ArrayList<>();

        

        //For each token i
        for (int i = 0; i < inputs.get(0).size(); i++) {

            HashMap<String, Integer> tagCount = new HashMap<>();
            Token token = new Token(i, 0, inputs.get(0).get(i).token, "??");
            System.out.print("\nToken: " + token.token + " ");

            //Count instances of each tag for this i
            for (ArrayList<Token> list : inputs) {

                //Tokens must match
                assert list.get(i).token.equalsIgnoreCase(list.get((i + 1) % inputs.size()).token);

                String tag = list.get(i).tag;
                System.out.print(tag + " ");
                if (tagCount.containsKey(tag)) {
                    tagCount.put(tag, tagCount.get(tag) + 1);
                } else {
                    tagCount.put(tag, 1);
                }
            }

            //Find & set consensus tag
            for (String tag : tagCount.keySet()) {
                if (tagCount.get(tag) > inputs.size() * threshold) {
                    token.tag = tag;
                    System.out.print(" : " + tag);
                    agreement++;
                    if (tagCount.get(tag) == inputs.size()) {
                        unanimousDecision++;
                    }
                }
            }

            if (token.tag.equalsIgnoreCase("??")) {
                System.out.print(" : ????");
                disagreement++;
            }
            consensus.add(token);
        }

        assert (agreement + disagreement) == inputs.get(0).size();

        System.out.println("\nTokens: " + (agreement + disagreement)
                + "\nDisagreement: " + disagreement
                + "\nAgreements: " + agreement
                + "\nUnanimous: " + unanimousDecision);

        System.out.print("\nSizes: ");
        for (int i = 0; i < inputs.size(); i++) {
            System.out.print(inputs.get(i).size() +" ");
        }
        return consensus;
    }

}
