package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FileReader {
    private FileReader() {
    }

    private static final Logger logger = Logger.getLogger(FileReader.class.getName());

    public static List<String> readFile(String pathToFile) throws FileNotFoundException {
        List<String> text = new ArrayList<>();
        File file = new File(pathToFile);
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replace("\n", "");
                line = line.trim();
                if (!line.isEmpty()) {
                    text.add(line);
                }
            }
            return text;
        } catch (IOException e) {
            logger.info(pathToFile);
            logger.severe("Ошибка чтения файла");
        }
        return new ArrayList<>();
    }
}
