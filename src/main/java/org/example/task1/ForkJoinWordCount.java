package org.example.task1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class ForkJoinWordCount extends RecursiveTask<Map<String, Integer>> {
    private final List<String> words;
    private static final int THRESHOLD = 2000;

    public ForkJoinWordCount(List<String> words) {
        this.words = words;
    }

    @Override
    protected Map<String, Integer> compute() {
        if (this.words.size() > THRESHOLD) {
            int split = Math.ceilDiv(words.size(), 2);
            ForkJoinWordCount f1 = new ForkJoinWordCount(words.subList(0, split));
            ForkJoinWordCount f2 = new ForkJoinWordCount(words.subList(split, words.size()));
            f1.fork();
            f2.fork();

            Map<String, Integer> response = f1.join();
            response.putAll(f2.join());
            return response;
        } else {
            return processing(words);
        }
    }

    private Map<String, Integer> processing(List<String> words) {
        Map<String, Integer> wordCounts = new HashMap<>();
        for (String word : words) {
            wordCounts.putIfAbsent(word, word.length());
        }
        return wordCounts;
    }
}
