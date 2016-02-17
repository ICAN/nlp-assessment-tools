/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlpassessment;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Neal
 */
public class StanfordCoreStandardizer implements Standardizer {

    //PUBLIC METHODS
    public void standardizePOS(String inputFile, String outputFile) {
        //Simplify NLTK POS
        ArrayList<String> raw = IO.readFileAsLines(inputFile);
        ArrayList<Token> tokens = tokenizeRawPOS(raw);
        simplifyPOSTags(tokens);
        IO.writeFile(IO.tokensToLines(tokens), outputFile);
    }
    
    //TODO: Write this
    public void standardizeNER(String inputFile, String outputFile) {
        
    }
    
    //TODO: Write this
    public void standardizeSentenceSplits(String inputFile, String outputFile) {
        
    }
    
    
    
    //PRIVATE METHODS
    
    /*
    
     */
    private static boolean validateLine(String string) {
        if (string.matches("[\\S]+" //Token number in sentence
                + "[\\s]+[\\S]+" //Token
                + "[\\s]+_"
                + "[\\s]+[\\S]+" //Tag
                + "[\\s]+_.*")) {
            return true;
        } else {
            return false;
        }
    }
    
    //TODO: eliminate 's tokens
    
    public static ArrayList<Token> tokenizeRawPOS(ArrayList<String> lines) {

        ArrayList<Token> taggedTokens = new ArrayList<Token>();

        for (String line : lines) {
            if (validateLine(line)) {
                String[] split = line.split("\\s+");
                String token = split[1];
                String tag = split[3];
                System.out.println(token + " " + tag);

                taggedTokens.add(new Token(token, tag));

            }
        }

        return taggedTokens;

    }


    public static void simplifyPOSTags(ArrayList<Token> tokens) {

        for (Token token : tokens) {

            token.tag = simplifyPOSTag(token.tag);

        }
    }

    public static String simplifyPOSTag(String tag) {

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

}
