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

/**
 *
 * @author Neal
 */
public class Main {
    
    
    public static boolean DEBUG = false;
    
    
    public static void standardizeAllPOS(String inputPath, String outputPath) {
        
        CoreNLP.standardizePOS(inputPath + "core pos output.txt", outputPath + "core pos std.txt");
        OpenNLP.standardizePOS(inputPath + "open pos output.txt", outputPath + "open pos std.txt");
//        NLTK.standardizePOS(inputPath + "nltk pos output.txt", outputPath + "nltk pos std.txt");
        Spacy.standardizePOS(inputPath + "spacy pos output.txt", outputPath + "spacy pos std.txt");
        
    }
    
    
    public static void standardizeAllSplits(String inputPath, String outputPath) {
        
        CoreNLP.standardizePOS(inputPath + "core split output.txt", outputPath + "core split std.txt");
        OpenNLP.standardizePOS(inputPath + "open split output.txt", outputPath + "open split std.txt");
        NLTK.standardizePOS(inputPath + "nltk split output.txt", outputPath + "nltk split std.txt");
        Spacy.standardizePOS(inputPath + "spacy split output.txt", outputPath + "spacy split std.txt");
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
 
        
        
        
        
//        CoreNLP.standardizePOS("stanford pos raw.txt", "stanford pos std.txt");
       
//        NLTK.standardizePOS("nltk pos raw.txt", "nltk pos std.txt");
        
//        Spacy.standardizePOS("spacy pos output.txt", "spacy pos std.txt");

//        standardizeAllPOS("","");

        
        
            Comparator.skipCatchupRobustCompareTags(
                IO.readFileAsTokens("core pos std.txt"), 
                IO.readFileAsTokens("open pos std.txt"), 
                "JJ");
    }
    
}
