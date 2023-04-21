package org.example.task2;

import java.util.List;

public class TeacherRunnable implements Runnable {
    private EJournal journal;
    private List<Mark> marks;

    public TeacherRunnable(EJournal journal, List<Mark> marks) {
        this.journal = journal;
        this.marks = marks;
    }

    @Override
    public void run() {
        marks.forEach(mark -> journal.setMarkForStudent(mark));
    }
}
