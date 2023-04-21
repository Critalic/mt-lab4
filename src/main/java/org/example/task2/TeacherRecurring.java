package org.example.task2;

import java.util.List;
import java.util.concurrent.RecursiveTask;

public class TeacherRecurring extends RecursiveTask<Boolean> {
    private EJournal journal;
    private List<Mark> marks;
    private long threshold = 40000000;

    public TeacherRecurring(EJournal journal, List<Mark> marks, long threshold) {
        this.journal = journal;
        this.marks = marks;
        this.threshold = threshold;
    }

    public TeacherRecurring(EJournal journal, List<Mark> marks) {
        this.journal = journal;
        this.marks = marks;
    }

    @Override
    protected Boolean compute() {
        if (marks.size() <= threshold) {
            marks.forEach(mark -> journal.setMarkForStudent(mark));
        } else {
            int split = Math.ceilDiv(marks.size(), 2);
            TeacherRecurring t1 = new TeacherRecurring(journal, marks.subList(0, split), threshold);
            TeacherRecurring t2 = new TeacherRecurring(journal, marks.subList(split, marks.size()), threshold);

            t1.fork();
            t2.fork();
            t1.join();
            t2.join();
        }
        return true;
    }
}
