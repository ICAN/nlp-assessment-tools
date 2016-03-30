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
import java.util.HashMap;

/**
 *
 * @author Neal
 */
public class Combinator {
    //Returns the consensus of the inputs
    //Inputs must already be standardized and restricted to common tokens 
    //so that input list lengths are identical and all tokens match
    public static ArrayList<Token> getTagConsensus(ArrayList<ArrayList<Token>> inputs, double threshold) {

        int agreement = 0;
        int unanimousDecision = 0;
        int disagreement = 0;

        ArrayList<Token> consensus = new ArrayList<>();

        //For each token i
        for (int i = 0; i < inputs.get(0).size(); i++) {

            HashMap<String, Integer> tagCount = new HashMap<>();
            Token token = new Token(i, 0, inputs.get(0).get(i).token, "??", true);
            System.out.print("\nToken: " + token.token + " ");

            //Count instances of each tag for this token i
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
            System.out.print(inputs.get(i).size() + " ");
        }
        return consensus;
    }
}
