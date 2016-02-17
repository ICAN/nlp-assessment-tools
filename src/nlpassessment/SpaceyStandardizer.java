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
public class SpaceyStandardizer implements Standardizer {

    //TODO: Fix tokenizer
    @Override
    public void standardizePOS(String inputFile, String outputFile) {
        ArrayList<String> raw = IO.readFileAsLines(inputFile);
        ArrayList<Token> tokens = tokenizeRawPOS(raw);
        simplifyPOSTags(tokens);
        IO.writeFile(IO.tokensToLines(tokens), outputFile);
    }

    //TODO: Write this
    @Override
    public void standardizeNER(String inputFile, String outputFile) {

    }

    //TODO: Write this
    @Override
    public void standardizeSentenceSplits(String inputFile, String outputFile) {

    }

    //PRIVATE METHODS
    //POS-TAGGING
    //TODO: Write this
    private static ArrayList<Token> tokenizeRawPOS(ArrayList<String> lines) {

        ArrayList<Token> taggedTokens = new ArrayList<Token>();

        for (String line : lines) {

            String[] split = line.split("\\s+");

            if (split.length == 4) {
                taggedTokens.add(new Token(split[1], split[3]));
            } else if (split.length == 3) {
                taggedTokens.add(new Token(split[1], split[2]));
            } else if (split.length == 2) {
                    //Ignore these, looks like they're always spaces
                //Weirdly, they are sometimes tagged "SPACE" and others "PUNCT"
            } else {
                System.out.println("Invalid line (incorrect split) " + line);
            }

        }

        return taggedTokens;

    }

    private static void simplifyPOSTags(ArrayList<Token> tokens) {
        for (Token token : tokens) {
            token.tag = simplifyPOSTag(token.tag);
        }
    }

    //TODO: Check
    private static String simplifyPOSTag(String tag) {

        if (tag.matches("NOUN")) {
            return "NN";
        } else if (tag.matches("ADJ")) {
            return "JJ";
        } else if (tag.matches("VERB")) {
            return "VB";
        } else if (tag.matches("ADV")) {
            return "RB";
        } else {
            return "Other";
        }
    }

}
