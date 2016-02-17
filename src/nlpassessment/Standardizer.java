package nlpassessment;

/**
 *
 * @author Neal
 */
public interface Standardizer {
    
    public abstract void standardizePOS(String inputFile, String outputFile);
    
    public abstract void standardizeNER(String inputFile, String outputFile);
    
    public abstract void standardizeSentenceSplits(String inputFile, String outputFile);
    
}
