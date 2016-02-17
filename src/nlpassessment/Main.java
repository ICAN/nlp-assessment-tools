
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
        
        
        
        
        
        
        ComparisonTool.compareResults(
                IO.readFileAsTokens("stanford pos simplified.txt"), 
                IO.readFileAsTokens("nltk pos simplified.txt"), 
                "JJ");
        
    }
    
}
