package nlpassessment;

import java.util.ArrayList;

/**
 *
 * @author Neal
 */
public class ComparisonTool {
    
    
    
    public static void reportResults(String key, int truePositives, int falseNegatives, int falsePositives, int totalTokens) {
        System.out.println("Tagged " + key + " for " + truePositives + " matches and " + falseNegatives + " mismatches."
                + "\n" + falsePositives + " false positives from " + totalTokens + " total tokens."
                + "Sensitivity: " + (double) truePositives / ((double) falseNegatives + (double) truePositives)
                + "   Specificity: " + (double) (totalTokens - falsePositives) / (double) totalTokens);
    }

    
    
    public static void countTags(ArrayList<Token> tokens) {
        int adj = 0, noun = 0, adv = 0, verb = 0, other = 0;

        for (Token token : tokens) {
            if (token.tag.equalsIgnoreCase("NN")) {
                noun++;
            } else if (token.tag.equalsIgnoreCase("JJ")) {
                adj++;
            } else if (token.tag.equalsIgnoreCase("RB")) {
                adv++;
            } else if (token.tag.equalsIgnoreCase("VB")) {
                verb++;
            } else {
                other++;
            }
        }

    }
    
    
    public static void compareResults(ArrayList<Token> results, ArrayList<Token> goldStandard, String key) {

        if (results.size() != goldStandard.size()) {
            System.out.println("Tokens list lengths differ");
            System.exit(-1);
        }

        int falseNegatives = 0;
        int truePositives = 0;
        int falsePositives = 0;
        int tokenMismatches = 0;

        //Detect mismatches on tokens which differ between results and standard
        //Unless both are tagged "other"
        for (int i = 0; i < results.size(); i++) {
            if (!results.get(i).token.equalsIgnoreCase(goldStandard.get(i).token)
                    && !results.get(i).tag.equalsIgnoreCase("Other")
                    && !goldStandard.get(i).tag.equalsIgnoreCase("Other")) {
                tokenMismatches++;
                System.out.println("Mismatch: " + results.get(i).token + " \t\t " + goldStandard.get(i).token);
                System.out.println("Warning: Results invalid due to " + tokenMismatches + " token mismatches");
            }

            if (results.get(i).tag.equalsIgnoreCase(key)
                    && goldStandard.get(i).tag.equalsIgnoreCase(key)) {
                truePositives++;
            } else if (goldStandard.get(i).tag.equalsIgnoreCase(key)
                    && !results.get(i).tag.equalsIgnoreCase(key)) {
                falseNegatives++;
            } else if (results.get(i).tag.equalsIgnoreCase(key)
                    && !goldStandard.get(i).tag.equalsIgnoreCase(key)) {
                falsePositives++;
            }

        }

        reportResults(key, truePositives, falseNegatives, falsePositives, results.size());

    }

}
