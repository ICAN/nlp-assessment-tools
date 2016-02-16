
package nlpassessment;

import java.util.ArrayList;

/**
 *
 * @author Neal
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<String> stanfordGold = IO.readFileAsLines("stanford_pos_raw.txt");
        stanfordGold = StanfordCore.simplifyStanfordPOS(stanfordGold);
        IO.writeFile(stanfordGold, "stanford_pos_simplified.txt");
    }
    
}
