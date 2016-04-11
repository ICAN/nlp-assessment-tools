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

public class CoreNLP {

    //PUBLIC METHODS
    public static void standardizePOS(String inputFile, String outputFile) {
        //Simplify NLTK POS
        ArrayList<String> raw = IO.readFileAsLines(inputFile);
        ArrayList<Token> tokens = tokenizeRawPOS(raw);
        simplifyPOSTags(tokens);
        renormalizeBrackets(tokens);
        IO.writeFile(IO.tokensToStandardLines(tokens), outputFile);
    }

    //TODO: Double-check
    public static void standardizeNER(String inputFile, String outputFile) {
        ArrayList<String> raw = IO.readFileAsLines(inputFile);
        ArrayList<Token> tokens = tokenizeRawNER(raw);
        simplifyNERTags(tokens);
        renormalizeBrackets(tokens);
        IO.writeFile(IO.tokensToStandardLines(tokens), outputFile);
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
    //GENERAL CORENLP METHODS
    
    /*
        Confirms that the string is a valid line
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
    
    /*
        Converts Penn-standardized brackets to their correct single-character forms
    */
    private static void renormalizeBrackets(ArrayList<Token> tokens) {
        
        HashMap<String, String> mapping = new HashMap<>();
        mapping.put("-LRB-", "(");
        mapping.put("-RRB-", ")");
        mapping.put("-LCB-", "{");
        mapping.put("-RCB-", "}");
        mapping.put("-LSB-", "[");
        mapping.put("-RSB-", "]");
        
        for(Token token : tokens) {
            if(mapping.containsKey(token.token)) {
                token.token = mapping.get(token.token);
            }
        }
    }
    
    
    //PARTS OF SPEECH TAGGING - POS
    private static ArrayList<Token> tokenizeRawPOS(ArrayList<String> lines) {

        ArrayList<Token> taggedTokens = new ArrayList<Token>();
        int tokenCount = 0;

        for (String line : lines) {
            if (validateLine(line)) {
                tokenCount++;
                String[] split = line.split("\\s+");
                String token = split[1];
                String tag = split[3];
                taggedTokens.add(new Token(tokenCount, 0, token, tag));
//                System.out.println("Tokenized as: " + token + "\t" + tagset);
            }
        }

        return taggedTokens;

    }

    private static void simplifyPOSTags(ArrayList<Token> tokens) {
        for (Token token : tokens) {
            token.tagset = simplifyPOSTag(token.tagset);
        }
    }

    private static String simplifyPOSTag(String tag) {

        if (tag.matches("NN.*")) {
            return "NN";
        } else if (tag.matches("JJ.*")) {
            return "JJ";
        } else if (tag.matches("V.*")
                || tag.matches("MD")) {
            return "VB";
        } else if (tag.matches("RB.*")
                || tag.matches("WRB")){
            return "RB";
        } else if (tag.matches("PR.*")
                ||tag.matches("WP*")) {
            return "PR";
        } else {
            return "Other";
        }
    }

    //NAMED ENTITY RECOGNITION - NER
    //TODO: Test/Confirm
    private static ArrayList<Token> tokenizeRawNER(ArrayList<String> lines) {
        
        ArrayList<Token> taggedTokens = new ArrayList<Token>();
        int tokenCount = 0;

        for (String line : lines) {
            if (validateLine(line)) {
                tokenCount++;
                String[] split = line.split("\\s+");
                String token = split[2];
                String tag = split[4];
                taggedTokens.add(new Token(tokenCount, 0, token, tag));
            }
        }

        return taggedTokens;
    }
    
    //TODO: write
    private static void simplifyNERTags(ArrayList<Token> tokens) {
        for (Token token : tokens) {
            token.tagset = simplifyNERTag(token.tagset);
        }
    }
    
    //TODO: Write
    private static String simplifyNERTag(String tag) {

        if (tag.equalsIgnoreCase("O")) {
            return "_";
        } else if (tag.equalsIgnoreCase("LOCATION")) {
            return "LOC";
        } else if (tag.equalsIgnoreCase("")) {
            
        } else {
            System.out.println("Error simplifying CoreNLP NER tags");
            return "--ERROR--";
        }
        
        return "";
    }
    
    
    
    



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
                        output.add(new Token(tokenCount, i + 1, "" + combined.charAt(i), "_"));
                        tokenCount++;

                }
            }
        }

        return output;
    }

    
    
    
    
    
    
}
