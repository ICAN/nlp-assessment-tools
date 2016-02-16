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
public class StanfordCore {


    /*
    
     */
    public static boolean validateToken(String string) {
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

    public static ArrayList<Token> tokenizeStanfordPOS(ArrayList<String> lines) {

        ArrayList<Token> taggedTokens = new ArrayList<Token>();

        for (String line : lines) {
            if (validateToken(line)) {
                String[] split = line.split("\\s+");
                String token = split[1];
                String tag = split[3];
                System.out.println(token + " " + tag);

                taggedTokens.add(new Token(token, tag));

            }
        }

        return taggedTokens;

    }

    //Ignores lines it can't validate
    public static ArrayList<String> simplifyStanfordPOS(ArrayList<String> lines) {

        ArrayList<String> simplifiedLines = new ArrayList<>();

        int adj = 0, noun = 0, adv = 0, verb = 0;

        for (String line : lines) {
            if (validateToken(line)) {
                String[] split = line.split("\\s+");
                String token = split[1];
                String tag = split[3];
//               System.out.println(token + " " + tag);

                tag = simplifyStanfordTag(tag);

                String simplified = token + "\t\t" + tag;

                simplifiedLines.add(simplified);
                
                //Keep counts.
               if(tag.equalsIgnoreCase("NN")) {
                   noun++;
               } else if (tag.equalsIgnoreCase("JJ")) {
                   adj++;
               } else if (tag.equalsIgnoreCase("RB")) {
                   adv++;
               } else if (tag.equalsIgnoreCase("VB")) {
                   verb++;
               }
                
               if(noun > 120 && adj > 120 && adv > 120 && verb > 120) {
                   System.out.println("Found " + noun + " nouns, " + verb + " verbs,\n" +
                           adj + " adjectives, and " + adv + " adverbs.");
                   break;
               }
               
                
            } else {
                simplifiedLines.add(line);
                if (!line.matches("\\s")) {
                    System.out.println("Couldn't validate: " + line);
                }
            }
            
            
            
        }
        return simplifiedLines;
    }

    public static String simplifyStanfordTag(String tag) {

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

    public static void reportResults(String key, int matches, int misses, int falsePositives, int totalTokens) {
        System.out.println("Tagged " + key + " for " + matches + " matches and " + misses + " mismatches."
                + "\n" + falsePositives + " false positives from " + totalTokens + " total tokens."
                + "Sensitivity: " + (double) matches / ((double) misses + (double) matches)
                + "   Specificity: " + (double) (totalTokens - falsePositives) / (double) totalTokens);
    }

    public static void compareStanfordPOSResults(ArrayList<Token> results, ArrayList<Token> goldStandard, String key) {

        if (results.size() != goldStandard.size()) {
            System.out.println("Tokens list lengths differ");
            System.exit(-1);
        }

        int misses = 0;
        int matches = 0;
        int falsePositives = 0;

        for (int i = 0; i < results.size(); i++) {
            if (!results.get(i).token.equalsIgnoreCase(goldStandard.get(i).token)) {
                System.out.println("Token mismatch");
                System.exit(-1);
            }

            if (results.get(i).tag.equalsIgnoreCase(key)
                    && goldStandard.get(i).tag.equalsIgnoreCase(key)) {
                matches++;
            } else if (goldStandard.get(i).tag.equalsIgnoreCase(key)
                    && !results.get(i).tag.equalsIgnoreCase(key)) {

                misses++;
            }

        }

        reportResults(key, matches, misses, falsePositives, results.size());

    }

}
