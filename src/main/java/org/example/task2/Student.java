package org.example.task2;

import lombok.Getter;
import lombok.ToString;

import java.util.*;

@Getter
@ToString
public class Student {
    private final String name;
    private final String group;
    private final Map<String, List<Integer>> marks = new HashMap<>();

    public Student(String name, String group, String[] subjects) {
        this.name = name;
        this.group = group;
        generateMarks(subjects);
    }

    public Student(String name, String group) {
        this.name = name;
        this.group = group;
    }

    private void generateMarks(String[] subjects) {
        for(String sub: subjects) {
            marks.put(sub, new ArrayList<>());
        }
    }

    public synchronized boolean addMark(int mark, String subject) {
        return marks.get(subject).add(mark);
    }

    public List<Integer> getMarks(String subject) {
        return marks.get(subject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(name, student.name) && Objects.equals(group, student.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, group);
    }


}
