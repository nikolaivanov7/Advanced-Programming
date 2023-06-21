package F1Race;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class F1Race {
    List<F1> races;

    public F1Race() {
        races = new ArrayList<>();
    }


    public void readResults(InputStream in) {
        Scanner sc = new Scanner(in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            String name = parts[0];
            TreeSet<String> set = new TreeSet<>();
            for (int i = 1; i < parts.length; i++) {
                set.add(parts[i]);
            }
            F1 f1 = new F1(name, set);
            races.add(f1);
        }
        sc.close();
    }

    public void printSorted(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);
        races = races.stream()
                .sorted(Comparator.comparing(F1::getLowestLap))
                .collect(Collectors.toList());
        for (int i = 0; i < races.size(); i++) {
            pw.printf("%d. ", i + 1);
            pw.print(races.get(i) + "\n");
        }
        pw.flush();
    }
}

class F1 {
    String name;
    TreeSet<String> laps;

    public F1(String name, TreeSet<String> laps) {
        this.name = name;
        this.laps = laps;
    }

    public String getLowestLap() {
        return laps.stream().findFirst().get();
    }

    @Override
    public String toString() {
        return String.format("%-10s%10s", name, getLowestLap());
    }
}

