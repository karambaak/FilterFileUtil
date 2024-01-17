package org.example;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class FileWriter {
    private static final Logger logger = Logger.getLogger(FileWriter.class.getName());

    private FileWriter() {
    }

    public static void writeTheFile(String name, boolean append, List<String> textCollection) {
        try (java.io.FileWriter writer = new java.io.FileWriter(name, append)) {
            String text = String.join("\n", textCollection);
            writer.write(text);
            writer.flush();
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }
}
