package org.example.task2;

import lombok.Data;

@Data
public class Mark {
    private Student student;
    private String subject;
    private int mark;

    public Mark(Student student, String subject, int mark) {
        this.student = student;
        this.subject = subject;
        this.mark = mark;
    }
}
