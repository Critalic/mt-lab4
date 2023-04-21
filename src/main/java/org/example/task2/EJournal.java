package org.example.task2;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class EJournal {
    private final Map<Student, Student> students;

    public EJournal(Map<Student, Student> students) {
        this.students = students;
    }

    public boolean setMarkForStudent(Mark mark) {
        return students.get(mark.getStudent()).addMark(mark.getMark(), mark.getSubject());
    }

    public List<Integer> getMarksForStudent(Student student, String subject) {
        return students.get(student).getMarks(subject);
    }

    public Collection<Student> getStudents() {
        return students.values();
    }

    public void clear() {
        this.students.clear();
    }
}
