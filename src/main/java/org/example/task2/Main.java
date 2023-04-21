package org.example.task2;

import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class Main {
    static long expansionCoefficient = 10000000;
    static String[] subjects = {
            "Jesseology"
    };
    static Student[] st = {
            new Student("Jessica", "A1"),
            new Student("Jessic", "A1"),
            new Student("Jessi", "A1"),
            new Student("Jess", "A1"),
            new Student("Jes", "A1"),
            new Student("Je", "A1"),

            new Student("Jessica", "A2"),
            new Student("Jessic", "A2"),
            new Student("Jessi", "A2"),
            new Student("Jess", "A2"),
            new Student("Jes", "A2"),
            new Student("Je", "A2"),

            new Student("Jessica", "A3"),
            new Student("Jessic", "A3"),
            new Student("Jessi", "A3"),
            new Student("Jess", "A3"),
            new Student("Jes", "A3"),
            new Student("Je", "A3"),
    };

    static Mark[] mk3 = {
            new Mark(st[2], subjects[0], 69),
            new Mark(st[7], subjects[0], 67),
            new Mark(st[6], subjects[0], 60),
            new Mark(st[9], subjects[0], 67),
            new Mark(st[4], subjects[0], 66),
            new Mark(st[0], subjects[0], 64),
    };
    static Mark[] mk4 = {
            new Mark(st[2], subjects[0], 79),
            new Mark(st[4], subjects[0], 77),
            new Mark(st[6], subjects[0], 70),
            new Mark(st[7], subjects[0], 77),
            new Mark(st[9], subjects[0], 76),
            new Mark(st[0], subjects[0], 74),
    };
    static Mark[] mk2 = {
            new Mark(st[12], subjects[0], 89),
            new Mark(st[13], subjects[0], 87),
            new Mark(st[14], subjects[0], 80),
            new Mark(st[15], subjects[0], 87),
            new Mark(st[16], subjects[0], 86),
            new Mark(st[17], subjects[0], 84),
    };
    static Mark[] mk1 = {
            new Mark(st[1], subjects[0], 99),
            new Mark(st[3], subjects[0], 97),
            new Mark(st[5], subjects[0], 90),
            new Mark(st[10], subjects[0], 97),
            new Mark(st[8], subjects[0], 96),
            new Mark(st[11], subjects[0], 94),
    };

    public static void main(String[] args) {
        setUpTask3();
    }

    private static void setUpTask3() {

        EJournal journalRun = getEJournal();
        EJournal journalRecurring = getEJournal();
        EJournal journalSequential = getEJournal();

        List<Mark> expanded1 = expandMarks(mk1, expansionCoefficient);
        List<Mark> expanded2 = expandMarks(mk2, expansionCoefficient);
        List<Mark> expanded3 = expandMarks(mk3, expansionCoefficient);
        List<Mark> expanded4 = expandMarks(mk4, expansionCoefficient);

        Runnable[] teachersRun = {
                new TeacherRunnable(journalRun, expanded1),
                new TeacherRunnable(journalRun, expanded2),
                new TeacherRunnable(journalRun, expanded3),
                new TeacherRunnable(journalRun, expanded4)
        };

        Runnable[] teachersSequential = {
                new TeacherRunnable(journalSequential, expanded1),
                new TeacherRunnable(journalSequential, expanded2),
                new TeacherRunnable(journalSequential, expanded3),
                new TeacherRunnable(journalSequential, expanded4)
        };

        TeacherRecurring[] teachersRecur = {
                new TeacherRecurring(journalRecurring, expanded1),
                new TeacherRecurring(journalRecurring, expanded2),
                new TeacherRecurring(journalRecurring, expanded3),
                new TeacherRecurring(journalRecurring, expanded4)
        };

        System.out.println("Reached point of execution");

        measureTimeOfExecution("Fork join thread", () -> {
            try (ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors())) {
                Arrays.stream(teachersRecur).forEach(pool::submit);
            }
        });
        journalRecurring.clear();
        System.gc();

        measureTimeOfExecution("Runnable addition", () -> {
            List<Thread> started = new ArrayList<>();

            Arrays.stream(teachersRun).map(Thread::new).forEach(thread -> {
                thread.start();
                started.add(thread);
            });

            for (Thread teacher : started) {
                try {
                    teacher.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        journalRun.clear();
        System.gc();

        measureTimeOfExecution("Single thread addition", () -> {
            Arrays.stream(teachersSequential).forEach(Runnable::run);
        });

    }

    private static void measureTimeOfExecution(String taskName, Runnable task) {
        long currTime = System.currentTimeMillis();
        task.run();
        long execTime = System.currentTimeMillis() - currTime;
        System.out.println("Executed " + taskName + " in " + execTime + " milliseconds");
    }

    private static List<Mark> expandMarks(Mark[] marks, long expandBy) {
        List<Mark> markList = new ArrayList<>(Arrays.stream(marks).toList());
        for (int i = 0; i < expandBy - 1; i++) {
            markList.addAll(Arrays.stream(marks).toList());
        }
        return markList;
    }

    private static EJournal getEJournal() {
        Map<Student, Student> studentMap = new HashMap<>();
        List<Student> students = Arrays.stream(st).toList();
        students.forEach(student -> studentMap.put(student, new Student(student.getName(), student.getGroup(), subjects)));
        return new EJournal(studentMap);
    }
}
