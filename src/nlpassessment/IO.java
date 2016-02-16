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
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Neal
 */
public class IO {
        public static String readFileAsString(String fileName) {
        return concatenateAll(readFileAsLines(fileName));
    }

    public static String concatenateAll(ArrayList<String> lines) {
        String condensed = "";
        for (String line : lines) {
            condensed += ("\n" + line);
        }
        return condensed;
    }

    //Reads a file and returns its lines in an arraylist
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
    
    //OUTPUT METHOD
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
