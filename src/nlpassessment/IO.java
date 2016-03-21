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
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Neal
 */
public class IO {
    
    
    public static ArrayList<Token> readFileAsTokens(String fileName) {
        return standardLinesToTokens(readFileAsLines(fileName));
    }
    

    public static String concatenateAll(ArrayList<String> lines) {
        String condensed = "";
        for (String line : lines) {
            condensed += ("\n" + line);
        }
        return condensed;
    }

    /*
    Reads a file and returns its lines in an arraylist
    */
    public static ArrayList<String> readFileAsLines(String fileName) {
        ArrayList<String> lines = new ArrayList<>();
        Scanner inFile = null;

        try {
            System.out.println(new File("").getAbsolutePath());
            System.out.println(fileName);
            inFile = new Scanner(new FileReader(fileName));
        } catch (Exception e) {
            System.out.println("Failed to open input file. Exiting.");
            System.exit(-1);
        }

        while (inFile.hasNextLine()) {
            lines.add(inFile.nextLine());
        }
        return lines;
    }
    
    
    public static ArrayList<Token> standardLinesToTokens(ArrayList<String> lines) {
        
        ArrayList<Token> tokens = new ArrayList<>();
        for(String line : lines) {
            String[] split = line.split("\\s+");
            if(split.length == 4) {
                tokens.add((new Token(Integer.parseInt(split[0]), Integer.parseInt(split[1]), split[2], split[3])));
            } else {
                System.out.println("Invalid line: " + line + " has " + line.length() + " tokens.");
            }
        }
        return tokens;
    }
    
    //Also sets "numberInSentence" variable in Tokens
    public static ArrayList<StdSentence> standardTokensToSentences(ArrayList<Token> tokens) {
        ArrayList<StdSentence> sentences = new ArrayList<>();

        int tokenInSentence = 0;
        ArrayList<Token> currentSentence = new ArrayList<>();
        for (Token token : tokens) {
            tokenInSentence++;
            if (token.tag.equalsIgnoreCase("[.?!]+")) {
                token.numberInSentence = tokenInSentence;
                currentSentence.add(token);
                sentences.add(new StdSentence(currentSentence));
                currentSentence = new ArrayList<>();
                tokenInSentence = 0;
            } else {
                currentSentence.add(token);
            }

        }
        return sentences;
    }
            
    public static ArrayList<String> tokensToLines(ArrayList<Token> tokens) {
        ArrayList<String> lines = new ArrayList<>();
        for(Token token : tokens) {
            lines.add(token.toString());
        }
        return lines;
    }
    
    
    /*
        General file output method
    */
    public static void writeFile(ArrayList<String> lines, String fileName) {

        try {
            File file = new File(fileName);
            FileWriter writer = null;
            writer = new FileWriter(file);

            for (String line : lines) {
                writer.write(line + "\n");
            }

            writer.close();

        } catch (Exception e) {
            System.out.println("Failed to complete output file. Exiting.");
            System.exit(-1);
        }

    }

}
