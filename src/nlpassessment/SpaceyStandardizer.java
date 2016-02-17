

package nlpassessment;

import java.util.ArrayList;

/**
 *
 * @author Neal
 */
public class SpaceyStandardizer implements Standardizer {
    
    //TODO: Write this
    public void standardizePOS(String inputFile, String outputFile) {
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
    
    //POS-TAGGING
    
    //TODO: Write
    private static ArrayList<Token> tokenizeRawPOS(ArrayList<String> lines) {

        ArrayList<Token> taggedTokens = new ArrayList<Token>();


        return taggedTokens;

    }

    private static void simplifyPOSTags(ArrayList<Token> tokens) {
        for (Token token : tokens) {
            token.tag = simplifyPOSTag(token.tag); 
        }
    }

    //TODO: Check
    private static String simplifyPOSTag(String tag) {

        if (tag.matches("NOUN")) {
            return "NN";
        } else if (tag.matches("ADJ")) {
            return "JJ";
        } else if (tag.matches("VERB")) {
            return "VB";
        } else if (tag.matches("ADV")) {
            return "RB";
        } else {
            return "Other";
        }
    }
    
    
    
    
    
    
}
