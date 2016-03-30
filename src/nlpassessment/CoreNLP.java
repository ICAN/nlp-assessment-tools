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

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class CoreNLP {

    //PUBLIC METHODS
    public static void standardizePOS(String inputFile, String outputFile) {
        //Simplify NLTK POS
        ArrayList<String> raw = IO.readFileAsLines(inputFile);
        ArrayList<Token> tokens = tokenizeRawPOS(raw);
        simplifyPOSTags(tokens);
        IO.writeFile(IO.tokensToStandardLines(tokens), outputFile);
    }

    //FUNCTION NOT SUPPORTED
    //TODO: Double-check
    public static void standardizeNER(String inputFile, String outputFile) {

    }

    //TODO: Test
    public static void standardizeSplits(String inputFile, String outputFile) {
        ArrayList<String> raw = IO.readFileAsLines(inputFile);
        ArrayList<Token> tokens = tokenizeRawSplits(raw);
        IO.writeFile(IO.tokensToStandardLines(tokens), outputFile);
    }

    //TODO: Write this
    public static void standardizeLemmas(String inputFile, String outputFile) {

    }

    //PRIVATE METHODS
    /*
    
     */
    private static boolean validateLine(String string) {
        String[] split = string.split("\\s+");
        if (split.length != 7) {
            return false;
        } else if (!string.matches("[\\S]+" //Token number in sentence
                + "[\\s]+[\\S]+" //Token
                + "[\\s]+_"
                + "[\\s]+[\\S]+" //Tag
                + "[\\s]+_.*")) {
            return false;
        }

        return true;
    }

    //PARTS OF SPEECH TAGGING - POS
    //TODO: eliminate 's tokens
    private static ArrayList<Token> tokenizeRawPOS(ArrayList<String> lines) {

        ArrayList<Token> taggedTokens = new ArrayList<Token>();
        int tokenCount = 0;

        for (String line : lines) {
            if (validateLine(line)) {
                tokenCount++;
                String[] split = line.split("\\s+");
                String token = split[1];
                String tag = split[3];
                taggedTokens.add(new Token(tokenCount, 0, token, tag, true));
//                System.out.println("Tokenized as: " + token + "\t" + tag);
            }
        }

        return taggedTokens;

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

    //NAMED ENTITY RECOGNITION - NER
    //SENTENCE SPLITTING
    //Tokenizes by whitespace; number tokens according to place in sentence
    //TODO: Finish/test
    private static ArrayList<Token> tokenizeRawSplits(ArrayList<String> lines) {
        ArrayList<Token> output = new ArrayList<>();

        int tokenCount = 1;
        int sentenceCount = 0;
        for (String line : lines) {

            if (line.matches("Sentence #[0-9]+.*")) {
                sentenceCount++;
            } else if (line.matches("\\[Text=.*")) {
                //Nothing for now...
            } else {
                String[] split = line.split("\\s+");

                String combined = "";

                for (int i = 0; i < split.length; i++) {
                    combined += split[i];
                }

                for (int i = 0; i < combined.length(); i++) {
                        output.add(new Token(tokenCount, i + 1, "" + combined.charAt(i), "_", true));
                        tokenCount++;

                }
            }
        }

        return output;
    }

    
}
