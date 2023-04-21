package org.example.task4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

public class FileSearch extends RecursiveTask<List<Result>> {
    private final List<Result> filesContents;
    private final Set<String> themeWords;
    private final File file;

    public FileSearch(List<Result> filesContents, Set<String> themeWords, File file) {
        this.filesContents = filesContents;
        this.themeWords = themeWords;
        this.file = file;
    }

    @Override
    protected List<Result> compute() {
        try {
            recurringSearch(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return filesContents;
    }

    private void recurringSearch(File file) throws FileNotFoundException {
        if (file.isDirectory() && !isNull(file.listFiles())) {
            System.out.println("In folder " + file.getName());
            List<FileSearch> forks = new ArrayList<>();
            for (File file1 : file.listFiles()) {
                FileSearch fileSearch = new FileSearch(filesContents, themeWords, file1);
                fileSearch.fork();
                forks.add(fileSearch);
            }
            forks.forEach(ForkJoinTask::join);
            System.out.println("Quit folder " + file.getName());
        } else {
            Set<String> wordsFromFile = getWordsFromFile(file);
            int uniqueWords = wordsFromFile.size();
            wordsFromFile.retainAll(themeWords);
            filesContents.add(new Result(file.getName(), wordsFromFile, uniqueWords));
        }
    }

    private static Set<String> getWordsFromFile(File file) {
        Stream<String> lines;
        try {
            lines = Files.lines(Path.of(file.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Set<String> words = lines.parallel()
                .flatMap(line -> Arrays.stream(line.split("\\W+")))
                .collect(Collectors.toSet());

        lines.close();
        return words;
    }
}
