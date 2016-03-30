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
public class Splitting {
    
    
    //Takes standardized, common-token-restricted outputs 
    //Tags the last character in each sentence "SPLIT"
    public static void tagFinalCharacters(String inputFile, String outputFile) {
        ArrayList<Token> tokens = IO.standardLinesToTokens(IO.readFileAsLines(inputFile));

        int splits = 0;
        for(int i = 0; i < tokens.size()-1; i++) {
            //If the next token is earlier in its sentence than the current token:
            if(tokens.get(i+1).indexInSentence < tokens.get(i).indexInSentence) {
                splits++;
                //Tag the current token
                tokens.get(i).tag = "SPLIT";
                
            }
        }
        //Assume a sentence split at the end
        splits++;
        tokens.get(tokens.size()-1).tag = "SPLIT";
        
        IO.writeFile(IO.tokensToStandardLines(tokens), outputFile);
    }
    
    public static void condenseSentences(String inputFile, String outputFile) {
        ArrayList<Token> input = IO.standardLinesToTokens(IO.readFileAsLines(inputFile));
        
        ArrayList<Token> output = new ArrayList<>();
        
        String combined = "";
        int sentenceCount = 0;
        for(Token token : input) {
            combined+= token.token;
            if(token.tag.equalsIgnoreCase("SPLIT")) {
                sentenceCount++;
                output.add(new Token(sentenceCount, 0, combined, "_", false));
                combined = "";
            }
        }
        IO.writeFile(IO.tokensToShortLines(output), outputFile);
    }
    
    
    
    
    
}
