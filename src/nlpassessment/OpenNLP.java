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
    //Works as of V5
    public static void standardizePOS(String inputFile, String outputFile) {
        ArrayList<String> raw = IO.readFileAsLines(inputFile);
        ArrayList<Token> tokens = tokenizeRawPOS(raw);
        simplifyPOSTags(tokens);
        IO.writeFile(IO.tokensToLines(tokens), outputFile);
    }

    //TODO: Finish components
    public static void standardizeNER(String inputFile, String outputFile) {
        ArrayList<String> raw = IO.readFileAsLines(inputFile);
        ArrayList<Token> tokens = tokenizeRawNER(raw);
        simplifyNERTags(tokens);
        IO.writeFile(IO.tokensToLines(tokens), outputFile);
    }

    //Looks good
    public static void standardizeSplits(String inputFile, String outputFile) {
        ArrayList<String> raw = IO.readFileAsLines(inputFile);
        ArrayList<Token> tokens = tokenizeRawSplits(raw);
        IO.writeFile(IO.tokensToLines(tokens), outputFile);
    }

    //TODO: Write this
    public static void standardizeLemmas(String inputFile, String outputFile) {

    }

    //PARTS OF SPEECH TAGGER - POS
    private static ArrayList<Token> tokenizeRawPOS(ArrayList<String> lines) {

        ArrayList<Token> taggedTokens = new ArrayList<>();
        int tokenCount = 0;
        for (String line : lines) {
            //Tokens and tags are separated by an underscore
            String[] split = line.split("_+");

            if (split.length == 2) {
                tokenCount++;
                taggedTokens.add(new Token(tokenCount, 0, split[0].trim(), split[1].trim(), true));
//                System.out.println("Tokenized + " + line + " as: " + split[0] + "\t" + split[1]);
            } else {
//                System.out.println("Failed to tokenize '" + line + "'\nTokens: " + split.length + "\n");
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

    //NAMED-ENTITY RECOGNITION - NER
    //TODO: Finish
    private static ArrayList<Token> tokenizeRawNER(ArrayList<String> lines) {

        ArrayList<Token> taggedTokens = new ArrayList<>();
        int tokenCount = 0;
        for (String line : lines) {
            //Tokens and tags are separated by an underscore
            String[] split = line.split("_+");

            if (split.length == 2) {
                tokenCount++;
                taggedTokens.add(new Token(tokenCount, 0, split[0].trim(), split[1].trim(), true));
//                System.out.println("Tokenized + " + line + " as: " + split[0] + "\t" + split[1]);
            } else {
//                System.out.println("Failed to tokenize '" + line + "'\nTokens: " + split.length + "\n");
            }

        }
        return taggedTokens;
    }

    private static void simplifyNERTags(ArrayList<Token> tokens) {
        for (Token token : tokens) {
            token.tag = simplifyPOSTag(token.tag);
        }
    }

    //TODO: Write
    private static String simplifyNERTag(String tag) {

        if (tag.matches("")) {
            return "None";
        } else {
            return "NE";
        }

    }

    //SENTENCE-SPLITTING
//Tokenizes by character, excluding all whitespace, numbering the characters
    //in each sentence
    private static ArrayList<Token> tokenizeRawSplits(ArrayList<String> lines) {
        ArrayList<Token> output = new ArrayList<>();

        int tokenCount = 1;
        for (String line : lines) {
            String[] split = line.split("\\s+");
            String combined = "";

            for (int i = 0; i < split.length; i++) {
                combined += split[i];
            }

            for (int i = 0; i < combined.length(); i++) {
               
                    output.add(new Token(tokenCount, i + 1, "" + combined.charAt(i), "C", true));
                    tokenCount++;
            
            }
        }
        return output;
    }

}
