package org.example.task4;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class Result {
    private final String fileName;
    private Set<String> matchingWords;
    private int fileSize;
    private boolean indicator;

    public Result(String fileName, Set<String> matching, int fileSize) {
        this.fileName = fileName;
        this.matchingWords = matching;
        this.fileSize = fileSize;
        if (fileSize < matching.size() * 5) {
            indicator = true;
        } else {
            indicator = false;
        }
    }

    @Override
    public String toString() {
        return "Result{" +
                "fileName='" + fileName + '\'' +
                ", matchingWords=" + matchingWords.size() +
                ", fileSize=" + fileSize +
                ", indicator=" + indicator +
                '}';
    }
}
