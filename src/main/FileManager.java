package main;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    // Helper method for reading a file from the system temp directory
    public static String[] readLinesFromTempFile() {
        Path tempFilePath = Paths.get(System.getProperty("java.io.tmpdir"), "settings.txt");

        try (BufferedReader reader = Files.newBufferedReader(tempFilePath)) {
            List<String> lines = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            return lines.toArray(new String[0]);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to read from temp file: " + tempFilePath);
            e.printStackTrace();
            return null;
        }
    }


    // Writes lines to a temporary file and returns the file path
    public static Path writeLinesToTempFile(String[] lines) {
        Path tempFile = Paths.get(System.getProperty("java.io.tmpdir"), "settings.txt");

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            return tempFile;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to write to temporary file.");
            e.printStackTrace();
            return null;
        }
    }
}
