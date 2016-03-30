/*
 * The MIT License
 *
 * Copyright 2016 Neal Logan.
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Neal
 */
public class Main {

    public static void printTagCounts(String fileName) {
        HashMap<String, Integer> tagCounts = Comparator.getTagCounts(
                IO.readFileAsStandardTokens(fileName));

        System.out.println("\nTags in " + fileName);
        for(String tag : tagCounts.keySet()) {
            System.out.println(tag + "\t" + tagCounts.get(tag));
        }
        
    }

    //First step in producing/comparing POS texts
    //Inptus: raw sentence splitter output "-out"
    //Outputs: standardized format for sentence splitting "-std"
    public static void standardizeAllSplits(String inputPath, String outputPath) {
        Spacy.standardizeSplits("spacy-split-out.txt", "spacy-split-std.txt");
        OpenNLP.standardizeSplits("open-split-out.txt", "open-split-std.txt");
        CoreNLP.standardizeSplits("core-split-out.txt", "core-split-std.txt");
        NLTK.standardizeSplits("nltk-split-out.txt", "nltk-split-std.txt");
        MBSP.standardizeSplits("mbsp-split-out.txt", "mbsp-split-std.txt");
    }

    //First step in producing/comparing POS texts
    //Inptus: raw POS-tagger output "-out"
    //Outputs: standardized format POS-tagger "-std"
    public static void standardizeAllPOS(String inputPath, String outputPath) {
        MBSP.standardizePOS(inputPath + "mbsp-pos-out.txt", outputPath + "mbsp-pos-std.txt");
        CoreNLP.standardizePOS(inputPath + "core-pos-out.txt", outputPath + "core-pos-std.txt");
        OpenNLP.standardizePOS(inputPath + "open-pos-out.txt", outputPath + "open-pos-std.txt");
        NLTK.standardizePOS(inputPath + "nltk-pos-out.txt", outputPath + "nltk-pos-std.txt");
        Spacy.standardizePOS(inputPath + "spacy-pos-out.txt", outputPath + "spacy-pos-std.txt");
    }
    
    //Second step in producing/comparing most texts
    //Inputs: A set of POS-tagger outputs in standarized format "-std"
    //Outputs: A corresponding set of POS-tagger outputs "-restricted", excluding any tokens
    //which weren't reflected in all outputs
    public static void produceMinimalCommonTokenLists(String tagger, int range) {

        HashMap<String, ArrayList<Token>> tokenLists = new HashMap<>();

        //Add lists
        tokenLists.put("mbsp", IO.readFileAsStandardTokens("mbsp-" + tagger + "-std.txt"));
        tokenLists.put("core", IO.readFileAsStandardTokens("core-" + tagger + "-std.txt"));
        tokenLists.put("open", IO.readFileAsStandardTokens("open-" + tagger + "-std.txt"));
        tokenLists.put("nltk", IO.readFileAsStandardTokens("nltk-" + tagger + "-std.txt"));
        tokenLists.put("spacy", IO.readFileAsStandardTokens("spacy-" + tagger + "-std.txt"));

        //Restrict lists iteratively
        int iterations = 0;
        while (true) {
            iterations++;
            int deltaSize = 0;
            HashMap<String, ArrayList<Token>> restrictedLists = new HashMap<>();

            for (String baseListName : tokenLists.keySet()) {
                ArrayList<ArrayList<Token>> currentLists = new ArrayList<>();
                currentLists.add(tokenLists.get(baseListName)); //Put the baseList baseListName at index 0

                //Add the rest of the lists, order immaterial
                for (String otherList : tokenLists.keySet()) {
                    if (!otherList.equalsIgnoreCase(baseListName)) {
                        currentLists.add(tokenLists.get(otherList));
                    }
                }

                //Minimize the current base list
                ArrayList<Token> baseList = Restrictor.getCommonlTokenList(currentLists, 0, range);
                restrictedLists.put(baseListName, baseList);

            }

            for (String listName : tokenLists.keySet()) {
                deltaSize += (tokenLists.get(listName).size() - restrictedLists.get(listName).size());
            }

            tokenLists = restrictedLists;

            if (deltaSize == 0) {
                System.out.println("Completed producing all minimized POS results after " + iterations + " iterations.");
                break;
            } else {
                System.out.println("Iteration " + iterations + ": eliminated " + deltaSize + " tokens.");
            }

        }

        //Write all output files
        for (String listName : tokenLists.keySet()) {
            IO.writeFile(IO.tokensToStandardLines(tokenLists.get(listName)), listName + "-" + tagger + "-restricted.txt");
        }
    }  
    
    
    //Takes a set of -restricted files and a -gold file
    //Produces sensitivity and specificity measurements for each specified tag
    //for each standardized, restricted output file as compared to the gold standard
    //and outputs these results to a a set of text files or something
    public static void testAllPOS() {
        
        //get tests
        String[] tests = { "core", "spacy", "nltk", "mbsp", "open" };
        
        HashMap<String, ArrayList<Token>> outputs = new HashMap<>();
        
        for(String test : tests) {
            outputs.put(test, IO.readFileAsStandardTokens(test + "-pos-restricted.txt"));
        }
        
        //get gold std
        ArrayList<Token> gold = IO.readFileAsStandardTokens("pos-gold-v5.txt");
        
        
        String[] keys = { "VB", "RB", "NN", "JJ", "PR", "Other" };
        
        //For each tool
        for(String test : outputs.keySet()) {
            //Start a report
            String report = test.toUpperCase() + " REPORT\n\n";
            //For each key, 
            for(String key : keys) {
                //add the tool- and key-specific report
                report += Comparator.compareTags(outputs.get(test), gold, key);
                report += "\n\n";
            }
            //When the tool report is finished for all keys, output the report

            IO.writeFile(report, test + "-pos-report.txt");
        }
        
        
    }
    
    
    //Takes standardized, restricted text and produces a machine consensus
    public static void producePOSConsensus(String inputPath, String outputPath, double threshold) {

        ArrayList<ArrayList<Token>> tokenLists = new ArrayList<>();

        tokenLists.add(IO.readFileAsStandardTokens("mbsp-pos-restricted.txt"));
        tokenLists.add(IO.readFileAsStandardTokens("core-pos-restricted.txt"));
        tokenLists.add(IO.readFileAsStandardTokens("open-pos-restricted.txt"));
        tokenLists.add(IO.readFileAsStandardTokens("nltk-pos-restricted.txt"));
        tokenLists.add(IO.readFileAsStandardTokens("spacy-pos-restricted.txt"));

        IO.writeFile(
                IO.tokensToStandardLines(Combinator.getTagConsensus(tokenLists, threshold)),
                "all-pos-consensus.txt");

    }

    public static void produceSplitConsensus(String inputPath, String outputPath, double threshold) {
        ArrayList<ArrayList<Token>> tokenLists = new ArrayList<>();

        tokenLists.add(IO.readFileAsStandardTokens("mbsp-split-tagged.txt"));
        tokenLists.add(IO.readFileAsStandardTokens("core-split-tagged.txt"));
        tokenLists.add(IO.readFileAsStandardTokens("open-split-tagged.txt"));
        tokenLists.add(IO.readFileAsStandardTokens("nltk-split-tagged.txt"));
        tokenLists.add(IO.readFileAsStandardTokens("spacy-split-tagged.txt"));

        IO.writeFile(
                IO.tokensToStandardLines(Combinator.getTagConsensus(tokenLists, threshold)),
                "all-split-consensus.txt");
    }

    public static void tagAllSplits(String inputPath, String outputPath) {
        Splitting.tagFinalCharacters("mbsp-split-restricted.txt", "mbsp-split-tagged.txt");
        Splitting.tagFinalCharacters("open-split-restricted.txt", "open-split-tagged.txt");
        Splitting.tagFinalCharacters("core-split-restricted.txt", "core-split-tagged.txt");
        Splitting.tagFinalCharacters("nltk-split-restricted.txt", "nltk-split-tagged.txt");
        Splitting.tagFinalCharacters("spacy-split-restricted.txt", "spacy-split-tagged.txt");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

//        standardizeAllPOS("","");
//        OpenNLP.standardizePOS("open-pos-out.txt", "open-pos-std.txt");
//        Spacy.standardizePOS("spacy-pos-out.txt", "spacy-pos-std.txt");
        //(Tested, Gold)
//            Comparator.robustlyCompareTags(
//                IO.readFileAsStandardTokens("nltk-pos-std.txt"), 
//                IO.readFileAsStandardTokens("core-pos-std.txt"), 
//                "RB");        
        //Produce minimal common token lists for all POS-taggers
//        produceMinimalCommonTokenLists("pos");
//        ArrayList<Token> filtered = produceMinimalCommonTokenLists();
//        System.out.println("\nFiltered size: " + filtered.size());
//        IO.writeFile(
//                IO.tokensToShortLines(filtered), "pos-filtered.txt");
//        producePOSConsensus("","",0.55);
//        standardizeAllSplits("","");
//        produceMinimalCommonTokenLists("split", 8);
//        tagAllSplits("", "");
//        produceSplitConsensus("", "", .85);
        
//        printTagCounts("all-split-consensus.txt");
//        printTagCounts("mbsp-split-tagged.txt");
//        printTagCounts("open-split-tagged.txt");
//        printTagCounts("nltk-split-tagged.txt");
//        printTagCounts("core-split-tagged.txt");
//        printTagCounts("spacy-split-tagged.txt");
        
        testAllPOS();
        
        
//        Splitting.condenseSentences("all-split-consensus.txt", "all-split-consensus-condensed.txt");
//        IO.countNonemptyLines("all-split-consensus-condensed.txt");
//        IO.countNonemptyLines("open-split-out.txt");
        
        
        
    }

}
