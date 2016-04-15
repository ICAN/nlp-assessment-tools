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
    
    //Removes unique splitLines; ignores the first token in each unstemmedLine
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
    public static void stemAll(String testFile) {

        stemming.Stemmer porter1Stemmer = new Porter1();
        stemming.Stemmer porter2Stemmer = new Porter2();
        stemming.Stemmer lancasterStemmer = new Lancaster();

        //Get the stemming test case
        ArrayList<String> lines = IO.readFileAsLines(testFile);

        ArrayList<String[]> splitLines = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String[] line = lines.get(i).split("\\s+");
            splitLines.add(line);
        }

        ArrayList<String[]> porter1Stemmed = new ArrayList<>();
        ArrayList<String[]> porter2Stemmed = new ArrayList<>();
        ArrayList<String[]> lancasterStemmed = new ArrayList<>();

        //For each unstemmedLine
        for (int i = 0; i < splitLines.size(); i++) {
            String[] unstemmedLine = splitLines.get(i);
            String[] porter1Line = new String[unstemmedLine.length];
            String[] porter2Line = new String[unstemmedLine.length];
            String[] lancasterLine = new String[unstemmedLine.length];

            //Keep the initial unstemmedLine-marking token <stem>
            porter1Line[0] = "" + unstemmedLine[0];
            porter2Line[0] = "" + unstemmedLine[0];
            lancasterLine[0] = "" + unstemmedLine[0];

            //Stem each word, add to the appropriate unstemmedLine & list
            for (int j = 1; j < unstemmedLine.length; j++) {
                porter1Line[j] = porter1Stemmer.stem(unstemmedLine[j]).replaceAll("[^\\w]", "");
                porter2Line[j] = porter2Stemmer.stem(unstemmedLine[j]).replaceAll("[^\\w]", "");
                lancasterLine[j] = lancasterStemmer.stem(unstemmedLine[j]).replaceAll("[^\\w]", "");
//                System.out.println("raw: " + unstemmedLine[j] +"  p1: " + porter1Line[j] + "  p2: " + porter2Line[j] + "  lan: " + lancasterLine[j]);
            }
            
            //Add current stemmed lines to their respective lists
            porter1Stemmed.add(porter1Line);
            porter2Stemmed.add(porter2Line);
            lancasterStemmed.add(lancasterLine);
        }
        
        //Prep for file output
        ArrayList<String> porter1Combined = new ArrayList<>();
        ArrayList<String> porter2Combined = new ArrayList<>();
        ArrayList<String> lancasterCombined = new ArrayList<>();
        
        for(String[] line : porter1Stemmed) {
            porter1Combined.add(IO.arrayToString(line, true));
        }
        for(String[] line : porter2Stemmed) {
            porter2Combined.add(IO.arrayToString(line, true));
        }
        for(String[] line : lancasterStemmed) {
            lancasterCombined.add(IO.arrayToString(line, true));
        }
        
        IO.writeFile(porter1Combined, "porter1-stemmed.txt");
        IO.writeFile(porter2Combined, "porter2-stemmed.txt");
        IO.writeFile(lancasterCombined, "lancaster-stemmed.txt");
        
    }
    
    
    public static void collapseAll (){
    
        collapse("porter1-stemmed.txt", "porter1-uniqueStems.txt");
        collapse("porter2-stemmed.txt", "porter2-uniqueStems.txt");
        collapse("lancaster-stemmed.txt", "lancaster-uniqueStems.txt");
        
    }
    
    private static void collapse(String inputFile, String outputFile) {
        
        ArrayList<String> lines = IO.readFileAsLines(inputFile);        
        
        ArrayList<String> collapsedLines = new ArrayList<>();
        
        for(String line : lines) {
            
            String[] splitLine = line.split("\\s+");
            String collapsedLine = splitLine[0];
            
            HashSet<String> uniqueStems = new HashSet<>();
            
            //Put each stem in the set to ensure uniqueness
            for(int i = 1; i < splitLine.length; i++) {
                uniqueStems.add(splitLine[i].toLowerCase().trim());
            }
          
//            for(String uniqueStem : uniqueStems) {
//                System.out.print(uniqueStem + " ");
//            }
//            System.out.println("\n");
            
            //
            for(String uniqueStem : uniqueStems) {
                if(!uniqueStem.isEmpty()) {
                    collapsedLine += (" " + uniqueStem);
                }
            }
            
            collapsedLines.add(collapsedLine);
            
        }
        
        IO.writeFile(collapsedLines, outputFile);
    }
    
    
    
    
    
    
    //Takes a stemmed & collapsed test file & creates a report
    public static void buildReport(String testFile, String reportFile) {
        
        ArrayList<String> input = IO.readFileAsLines(testFile);
        
        
        
        
    }
    

}
