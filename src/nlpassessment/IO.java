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

    public static int countNonemptyLines(String fileName) {
        ArrayList<String> lines = readFileAsLines(fileName);
        int lineCount = 0;
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                lineCount++;
            }
        }
        System.out.println("Non-empty lines in " + fileName + ": " + lineCount);

        return lineCount;
    }

    public static ArrayList<Token> readFileAsStandardTokens(String fileName) {
        return standardLinesToTokens(readFileAsLines(fileName));
    }

    public static ArrayList<Token> readFileAsShortTokens(String fileName, int lineLength) {
        return shortLinesToTokens(readFileAsLines(fileName), lineLength);
    }

    public static String listToString(ArrayList<String> lines, boolean insertLineBreaks) {
        String condensed = "";
        if (insertLineBreaks) {
            for (String line : lines) {
                condensed += ("\n" + line);
            }
        } else {
            for (String line : lines) {
                condensed += (line);
            }
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

    public static String readFileAsString(String fileName, boolean insertLineBreaks) {
        return listToString(readFileAsLines(fileName), insertLineBreaks);
    }

    //Input: Any standardized set of lines (one token per line, 4 fields per token)
    //Returns a list of tokens corresponding to the tokens on those lines
    //Assumes all tokens are semantic
    public static ArrayList<Token> standardLinesToTokens(ArrayList<String> lines) {

        ArrayList<Token> tokens = new ArrayList<>();
        for (String line : lines) {
            String[] split = line.split("\\s+");
            if (split.length == 4) {
                tokens.add((new Token(Integer.parseInt(split[0]), Integer.parseInt(split[1]), split[2], split[3])));
            } else {
                System.out.println("Invalid line: " + line + " has " + split.length + " tokens.");
            }
        }
        return tokens;
    }

    //Input: Lines in token-whitepace-tagset format
    //Output: Token list with indexInText set, indexInSentence default to 0, and semantic defaulted to true
    public static ArrayList<Token> shortLinesToTokens(ArrayList<String> lines, int lineLength) {
        ArrayList<Token> tokens = new ArrayList<>();
        int tokenCount = 0;
        for (String line : lines) {
            String[] split = line.split("\\s+");
            if (split.length == 3
                    && lineLength == 3) {
                tokenCount++;
                tokens.add((new Token(tokenCount, 0, split[0], split[1])));
            } else if (split.length == 2
                    && lineLength == 2) {
                tokenCount++;
                tokens.add((new Token(tokenCount, 0, split[0], split[1])));
            } else {
                System.out.println("Invalid line: " + line + " has " + split.length + " tokens.");
            }
        }
        return tokens;
    }

    //Uses Token.toString()
    public static ArrayList<String> tokensToStandardLines(ArrayList<Token> tokens) {
        ArrayList<String> lines = new ArrayList<>();
        for (Token token : tokens) {
            lines.add(token.toString());
        }
        return lines;
    }

    //Includes only Token.token and Token.tagset in each line
    public static ArrayList<String> tokensToShortLines(ArrayList<Token> tokens) {
        ArrayList<String> lines = new ArrayList<>();
        for (Token token : tokens) {
            lines.add(token.indexInText + "\t" + token.token + "\t" + token.tagset);
        }
        return lines;
    }

    public static void writeFile(String contents, String fileName) {
        try {
            File file = new File(fileName);
            FileWriter writer = null;
            writer = new FileWriter(file);
            writer.write(contents);
            writer.close();

        } catch (Exception e) {
            System.out.println("Failed to complete output file. Exiting.");
            System.exit(-1);
        }
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
