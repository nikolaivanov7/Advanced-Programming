package StudentRecords;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * January 2016 Exam problem 1
 */

class Student {
    String code;
    List<Integer> grades;

    public Student(String code, List<Integer> grades) {
        this.code = code;
        this.grades = new ArrayList<>(grades.size());
        this.grades.addAll(grades);
    }

    public String getCode() {
        return code;
    }

    public double calculateAverage() {
        return grades.stream().mapToInt((g) -> g).average().orElse(0);
    }

    public Integer countGrades(int grade) {
        return Math.toIntExact(grades.stream()
                .filter((g) -> g == grade)
                .count());
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", code, calculateAverage());
    }
}

class Program {
    String name;
    TreeSet<Student> students;

    public Program(String name) {
        this.name = name;
        this.students = new TreeSet<>(Comparator.comparingDouble(Student::calculateAverage)
                .reversed()
                .thenComparing(Student::getCode));
    }

    public void addStudent(Student s) {
        students.add(s);
    }

    public String printAverage() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        students.forEach(s -> sb.append(s).append("\n"));
        return sb.toString();
    }

    public Integer getGradeDistribution(int grade) {
        return students.stream()
                .mapToInt(s -> s.countGrades(grade))
                .sum();
    }

    public String printGradeDistribution(int grade) {
        StringBuilder sb = new StringBuilder();
        int grades = getGradeDistribution(grade);
        int asteriks = (int) Math.ceil((double) grades / 10);

        for (int i = 0; i < asteriks; i++) {
            sb.append("*");
        }
        sb.append("(").append(grades).append(")");
        return sb.toString();
    }

    public String getName() {
        return name;
    }
}

class StudentRecords {
    private TreeMap<String, Program> records;

    public StudentRecords() {
        this.records = new TreeMap<>();
    }

    int readRecords(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        int readRecords = 0;

        try {
            while ((line = br.readLine()) != null) {
                if (line.equals("KRAJ")) break;
                String[] parts = line.split("\\s+");

                List<Integer> grades = IntStream.range(2, parts.length)
                        .mapToObj((i) -> Integer.parseInt(parts[i]))
                        .collect(Collectors.toList());

                Student newStudent = new Student(parts[0], grades);
                Program studentProgram =
                        records.computeIfAbsent(parts[1], (sr) -> new Program(parts[1]));
                studentProgram.addStudent(newStudent);

                readRecords++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readRecords;
    }

    void writeTable(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        records.values().forEach((p) -> pw.println(p.printAverage()));
        pw.flush();
    }

    void writeDistribution(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);

        records.values().stream()
                .sorted((p1, p2) -> -p1.getGradeDistribution(10).compareTo(p2.getGradeDistribution(10)))
                .forEach((p) -> {
                    pw.println(p.getName());
                    IntStream.range(6, 11)
                            .forEach((i) -> {
                                pw.printf("%2d | %s\n", i, p.printGradeDistribution(i));
                            });
                });
        pw.flush();
        pw.close();
    }
}


public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}
