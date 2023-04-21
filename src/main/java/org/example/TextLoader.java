package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TextLoader {
    public static List<String> getWordsFromFile(String fileName) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return getListOfWords(contentBuilder.toString());
    }

    public static List<String> getListOfWords(String text) {
        return Arrays.asList(text.trim().split("(\\s|\\p{Punct})+"));
    }
}
