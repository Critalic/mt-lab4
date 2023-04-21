package org.example.task1;

import org.example.TextLoader;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        List<String> words = TextLoader.getWordsFromFile("F:\\Real Intellij projects\\mt-lab4\\src\\main\\resources\\text.txt");

        System.out.printf("Number of words: %d\n\n", words.size());


        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        long currTime = System.currentTimeMillis();
        Map<String, Integer> res = pool.submit(new ForkJoinWordCount(words)).join();
        long currTimeForkJoin = System.currentTimeMillis() - currTime;

        double mean = res.values().stream().mapToDouble(i -> i).sum()
                / res.values().stream().mapToDouble(i -> i).count();
        double variance = Math.sqrt(res.values().stream().mapToDouble(i -> Math.pow(i - mean, 2)).sum() /
                res.size());

        System.out.printf("Execution time (ForkJoin): %d\n", currTimeForkJoin);

        System.out.printf("Number of uniq words: %d\n", res.keySet().size());
        System.out.printf("Mean length: %f\n", mean);
        System.out.printf("Var length: %f\n", variance);
        System.out.println();
    }
}