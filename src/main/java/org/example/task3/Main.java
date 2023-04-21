package org.example.task3;

import java.io.File;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

public class Main {
    public static void main(String[] args) {
        Set<String> commonWords = executeTask3("F:\\Real Intellij projects\\mt-lab4\\src\\main\\resources\\task3");
        System.out.println(commonWords.size());
        commonWords.forEach(System.out::println);
    }

    private static Set<String> executeTask3(String folder) {
        List<Set<String>> filesContents = Collections.synchronizedList(new ArrayList<>());
        File file = new File(folder);

        try (ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors())) {
            if (file.isDirectory() && !isNull(file.listFiles())) {
                pool.submit(new FileSearch(filesContents, file)).join();
            } else {
                System.out.println("Given path isn't a directory");
            }
        }

        Set<String> response = new HashSet<>(filesContents.get(0));

        IntStream.range(1, filesContents.size()).forEach(i -> response.retainAll(filesContents.get(i)));
        return response;
    }
}
