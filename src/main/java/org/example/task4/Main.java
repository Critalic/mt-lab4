package org.example.task4;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

public class Main {
    static String[] programmingWords = {
            "IT",
            "tech",
            "java",
            "processing"
    };

    public static void main(String[] args) {
        List<Result> commonWords = executeTask4("F:\\Real Intellij projects\\mt-lab4\\src\\main\\resources\\task4");
        commonWords.forEach(System.out::println);
    }

    private static List<Result> executeTask4(String folder) {
        List<Result> fileContents = Collections.synchronizedList(new ArrayList<>());
        Set<String> itWords = new HashSet<>(List.of(programmingWords));
        File file = new File(folder);
        try (ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors())) {
            if (file.isDirectory() && !isNull(file.listFiles())) {
                pool.submit(new FileSearch(fileContents, itWords, file)).join();
            } else {
                System.out.println("Given path isn't a directory");
            }
        }
        return fileContents;
    }
}
