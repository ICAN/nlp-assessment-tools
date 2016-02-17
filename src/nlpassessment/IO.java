/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        return linesToTokens(readFileAsLines(fileName));
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
    
    public static ArrayList<Token> linesToTokens(ArrayList<String> lines) {
        
        ArrayList<Token> tokens = new ArrayList<>();
        
        for(String line : lines) {
            String[] split = line.split("\\s");
            if(split.length == 2) {
                tokens.add((new Token(split[0], split[1])));
            } else {
                System.out.println("Invalid line: " + line);
            }
        }
        return tokens;
    }
            
            
    public static ArrayList<String> tokensToLines(ArrayList<Token> tokens) {
        ArrayList<String> lines = new ArrayList<>();
        
        for(Token token : tokens) {
            lines.add(token.token + "\t " + token.tag);
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
