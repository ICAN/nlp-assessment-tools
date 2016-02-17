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
public class NLTKStandardizer implements Standardizer {

    //PUBLIC METHODS
    public void standardizePOS(String inputFile, String outputFile) {
        ArrayList<String> raw = IO.readFileAsLines(inputFile);
        ArrayList<Token> tokens = tokenizeRawPOS(raw);
        simplifyPOSTags(tokens);
        filterPOSTokens(tokens);
        IO.writeFile(IO.tokensToLines(tokens), outputFile);
    }
    
    //TODO: Write this
    public void standardizeNER(String inputFile, String outputFile) {
        
    }
    
    //TODO: Write this
    public void standardizeSentenceSplits(String inputFile, String outputFile) {
        
    }
    
    
    //PRIVATE METHODS
    //POS-TAGGING
    private static ArrayList<Token> tokenizeRawPOS(ArrayList<String> lines) {

        ArrayList<Token> taggedTokens = new ArrayList<Token>();

        //Tokenize
        for (String line : lines) {
            String[] split = line.split("\\s+");
            if (split.length == 3) {
                //NLTK token has form (u'token',  or  (u"token",   this handles both cases
                //Stripping additional characters to leave token
                split[1] = split[1].substring(3, split[1].length() - 2);
                //Stripping additional characters to leave tag
                split[2] = split[2].substring(1, split[2].length() - 1);
                
                //Validate line and add
                if ((split[1] + " " + split[2]).matches(".+\\s+[A-Z\\p{Punct}]+.*")) {
                    taggedTokens.add(new Token(split[1], split[2]));
                } else {
                    System.out.println("Failed to validate line " + " " + split[1] + " " + split[2]);
                }
            } else {
                System.out.println("Failed to convert " + line);
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
    
    private static ArrayList<Token> filterPOSTokens (ArrayList<Token> taggedTokens) {
        
        ArrayList<Token> filteredTokens = new ArrayList<Token>();

        //Collapse hastags to next token to match standard tokenization scheme
        boolean hashtag = false;
        for (Token token : taggedTokens) {
            if (token.token.equals("#")) {
                hashtag = true; //Produce hashtag flag
            } else if (hashtag) {
                token.token = "#" + token.token; //Consume hashtag flag
                filteredTokens.add(token); //Hashtagged token added
                hashtag = false;
            } else {
                filteredTokens.add(token); //Other
            }

        }
        taggedTokens = filteredTokens;
        
        filteredTokens = new ArrayList<Token>();
        for(Token token : taggedTokens) {
            if(!token.token.matches("'s")) {
                if(token.token.endsWith("'s")) {
                    System.out.print("\nReduced " + token.token); 
                    token.token = token.token.substring(0, token.token.length() - 2);
                    System.out.print(" to " + token.token +"\n");
                }
                
                filteredTokens.add(token);
                
            } else {
                System.out.println("Removed " + token.toString());
            }
        }
        taggedTokens = filteredTokens;
        
        
        return taggedTokens;
    }

    

    //NER-TAGGING
    
    
    
    
    //SENTENCE SPLITTING
    
    
    
    
    
    
}
