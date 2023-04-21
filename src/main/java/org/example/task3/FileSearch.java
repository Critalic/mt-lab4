package org.example.task3;

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

public class FileSearch extends RecursiveTask<List<Set<String>>> {
    private final List<Set<String>> filesContents;
    private final File file;

    public FileSearch(List<Set<String>> filesContents, File file) {
        this.filesContents = filesContents;
        this.file = file;
    }

    @Override
    protected List<Set<String>> compute() {
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
                FileSearch fileSearch = new FileSearch(filesContents, file1);
                fileSearch.fork();
                forks.add(fileSearch);
            }
            forks.forEach(ForkJoinTask::join);
            System.out.println("Quit folder " + file.getName());
        } else {
            filesContents.add(getWordsFromFile(file));
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
