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
public class OpenNLP {

    //PUBLIC METHODS
    //TODO: FIX
    public static void standardizePOS(String inputFile, String outputFile) {
        ArrayList<String> raw = IO.readFileAsLines(inputFile);
        ArrayList<Token> tokens = tokenizeRawPOS(raw);
        simplifyPOSTags(tokens);
        IO.writeFile(IO.tokensToLines(tokens), outputFile);
    }

    //TODO: Write this
    public static void standardizeNER(String inputFile, String outputFile) {

    }

    //TODO: Write this
    public static void standardizeSplits(String inputFile, String outputFile) {

    }

    //TODO: Write this
    public static void standardizeLemmas(String inputFile, String outputFile) {

    }

    //TODO: eliminate 's tokens
    private static ArrayList<Token> tokenizeRawPOS(ArrayList<String> lines) {

        ArrayList<Token> taggedTokens = new ArrayList<Token>();
        int tokenCount = 0;
        for (String line : lines) {
            if (validateLinePOS(line)) {
                String[] split = line.split("[\\s_]+");
                String token = split[1];
                String tag = split[2];
//                System.out.println("Tokenized as: " + token + "\t" + tag);
                if (!token.matches("'s")) {
                    tokenCount++;
                    taggedTokens.add(new Token(tokenCount, 0, token, tag));
                }
            }
        }
        return taggedTokens;
    }

    private static boolean validateLinePOS(String line) {

        String[] split = line.split("[\\s_]+");
        if (split.length != 3) {
            return false;
        } else if(!split[0].matches("[0-9]+")) {
            return false;
        } else if (split[1].equalsIgnoreCase("'s")) {
            return false;
        }
        

        return true;
    }

    private static void simplifyPOSTags(ArrayList<Token> tokens) {
        for (Token token : tokens) {
            token.tag = simplifyPOSTag(token.tag);
        }
    }

    private static String simplifyPOSTag(String tag) {

        if (tag.matches("NN.*")) {
            return "NN";
        } else if (tag.matches("JJ.*")) {
            return "JJ";
        } else if (tag.matches("V.*")) {
            return "VB";
        } else if (tag.matches("RB.*")) {
            return "RB";
        } else if (tag.matches("PR.*")) {
            return "PR";
        } else {
            return "Other";
        }
    }

}
