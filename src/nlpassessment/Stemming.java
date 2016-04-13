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

import stemming.Porter1;
import stemming.Porter2;
import stemming.Lancaster;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Neal
 *
 * A family of methods for running stemmers and assessing results
 *
 */
public class Stemming {    
    
    public static String[] getUnique(String[] input) {
        HashSet<String> unique = new HashSet<>();
        
        for(String string : input) {
            unique.add("" + string);
        }        
        
        return (String[])unique.toArray();
    }
    
    //Removes unique words; ignores the first token in each line
    public static void cleanStemTest(String inputFile, String outputFile) {

        ArrayList<String> input = IO.readFileAsLines(inputFile);
        ArrayList<String> output = new ArrayList<>();

        for (int i = 0; i < input.size(); i++) {
            String[] inputLine = input.get(i).split("\\s");
            String outputLine = inputLine[0];
            inputLine[0] = "";
                        
            String[] unique = getUnique(inputLine);
                        
            if(!inputLine[0].matches("<.*>")) {
                System.out.println("ERROR: " + inputLine[0] + " IS NOT A VALID LINE MARKER");
                System.exit(-1);
            }
            
            for(String word : unique) {
                if(word.matches("<.*>")) {
                    System.out.println("ERROR: " + word + " IS NOT A VALID TOKEN");
                }
                outputLine += (" " + word);
            }
            output.add(outputLine);
        }

        IO.writeFile(output, outputFile);
    }

    //
    public static void stemAll(String testFile, String outFile) {

        stemming.Stemmer porter1Stemmer = new Porter1();
        stemming.Stemmer porter2Stemmer = new Porter2();
        stemming.Stemmer lancasterStemmer = new Lancaster();

        ArrayList<String> lines = IO.readFileAsLines(testFile);

        ArrayList<String[]> words = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String[] line = lines.get(i).split("\\s+");
            words.add(line);
        }

        ArrayList<String[]> porter1 = new ArrayList<>();
        ArrayList<String[]> porter2 = new ArrayList<>();
        ArrayList<String[]> lancaster = new ArrayList<>();

        for (int i = 0; i < words.size(); i++) {
            String[] line = words.get(i);
            String[] p1Line = new String[line.length];
            String[] p2Line = new String[line.length];
            String[] lanLine = new String[line.length];

            p1Line[0] = "" + line[0];
            p2Line[0] = "" + line[0];
            lanLine[0] = "" + line[0];

            for (int j = 1; j < line.length; j++) {
                p1Line[j] = porter1Stemmer.stem(line[j]);
                p2Line[j] = porter2Stemmer.stem(line[j]);
                lanLine[j] = lancasterStemmer.stem(line[j]);
            }

            porter1.add(p1Line);
            porter2.add(p2Line);
            lancaster.add(lanLine);
        }
    }
    
    //Takes a stemmed & collapsed test file & creates a report
    public static void buildReport(String testFile, String reportFile) {
        
        ArrayList<String> input = IO.readFileAsLines(testFile);
        
        
        
        
    }
    

}
