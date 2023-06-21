package Napredno;

import java.util.*;
import java.util.stream.Collectors;

class Student {
    String index;
    String ime;
    int poeniPrv;
    int poeniVtor;
    int labPoeni;

    public Student(String index, String ime) {
        this.index = index;
        this.ime = ime;
        this.poeniPrv = 0;
        this.poeniVtor = 0;
        this.labPoeni = 0;
    }

    public String getIndex() {
        return index;
    }

    public String getIme() {
        return ime;
    }

    public void setPoeniPrv(int poeniPrv) {
        this.poeniPrv = poeniPrv;
    }

    public void setPoeniVtor(int poeniVtor) {
        this.poeniVtor = poeniVtor;
    }

    public void setLabPoeni(int labPoeni) {
        this.labPoeni = labPoeni;
    }

    public double summaryPoints() {
        return poeniPrv * 0.45 + poeniVtor * 0.45 + labPoeni;
    }

    public int grade() {
        double points = summaryPoints();
        int grade = 0;
        if (points < 50) grade = 5;
        else if (points >= 50 && points < 60) {
            grade = 6;
        } else if (points >= 60 && points < 70) {
            grade = 7;
        } else if (points >= 70 && points < 80) {
            grade = 8;
        } else if (points >= 80 && points < 90) {
            grade = 9;
        } else if (points >= 90 && points <= 100) {
            grade = 10;
        }
        return grade;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d", index, ime, poeniPrv, poeniVtor, labPoeni, summaryPoints(), grade());
    }
}

class AdvancedProgrammingCourse {
    List<Student> students;

    public AdvancedProgrammingCourse() {
        students = new ArrayList<>();
    }

    public void addStudent(Student s) {
        students.add(s);
    }

    public void updateStudent(String idNumber, String activity, int points) {
        for (Student student : students) {
            if (student.getIndex().equals(idNumber)) {
                switch (activity) {
                    case "midterm1":
                        student.setPoeniPrv(points);
                        break;
                    case "midterm2":
                        student.setPoeniVtor(points);
                        break;
                    case "labs":
                        student.setLabPoeni(points);
                        break;
                }
            }
        }
    }

    public List<Student> getFirstNStudents(int n) {
        return students.stream().sorted(Comparator.comparing(Student::summaryPoints).reversed()).limit(n).collect(Collectors.toList());
    }

    public Map<Integer, Integer> getGradeDistribution() {
        Map<Integer, Integer> gradeDistribution = new HashMap<>();
        int count5 = (int) students.stream().filter(s -> s.grade() == 5).count();
        int count6 = (int) students.stream().filter(s -> s.grade() == 6).count();
        int count7 = (int) students.stream().filter(s -> s.grade() == 7).count();
        int count8 = (int) students.stream().filter(s -> s.grade() == 8).count();
        int count9 = (int) students.stream().filter(s -> s.grade() == 9).count();
        int count10 = (int) students.stream().filter(s -> s.grade() == 10).count();
        gradeDistribution.put(5, count5);
        gradeDistribution.put(6, count6);
        gradeDistribution.put(7, count7);
        gradeDistribution.put(8, count8);
        gradeDistribution.put(9, count9);
        gradeDistribution.put(10, count10);
        return gradeDistribution;
    }

    public void printStatistics() {
        DoubleSummaryStatistics doubleSummaryStatistics = students.stream().filter(s->s.grade()>5).mapToDouble(s -> s.summaryPoints()).summaryStatistics();
        System.out.println(String.format("Count: %d Min: %.2f Average: %.2f Max: %.2f", doubleSummaryStatistics.getCount(), doubleSummaryStatistics.getMin(), doubleSummaryStatistics.getAverage(), doubleSummaryStatistics.getMax()));
    }
}

public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                advancedProgrammingCourse.updateStudent(idNumber, activity, points);
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}
