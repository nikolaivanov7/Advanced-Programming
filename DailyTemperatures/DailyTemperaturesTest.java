package DailyTemperatures;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * I partial exam 2016
 */
public class DailyTemperaturesTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}

class Temperature {
    Integer day;
    List<Double> temperatures;

    public Temperature(Integer day, List<Double> temperatures) {
        this.day = day;
        this.temperatures = temperatures;
    }

    public Integer getDay() {
        return day;
    }
}

class DailyTemperatures {
    List<Temperature> temperatures;
    List<Temperature> temperaturesbyC;
    List<Temperature> temperaturesByF;

    List<Temperature> temperatureList;

    public DailyTemperatures() {
        temperatures = new ArrayList<>();
        temperaturesbyC = new ArrayList<>();
        temperaturesByF = new ArrayList<>();
        temperatureList = new ArrayList<>();
    }


    public void readTemperatures(InputStream in) {
        Scanner sc = new Scanner(in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.trim().isEmpty()) {
                break; // Skip empty or whitespace-only lines
            }
            String[] parts = line.split("\\s+");
            Integer day = Integer.parseInt(parts[0]);
            List<Double> tmp = new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                String s = parts[i].substring(0, parts[i].length() - 1);
                Double d = Double.parseDouble(s);
                tmp.add(d);
            }
            String character = String.valueOf(parts[1].charAt(parts[1].length() - 1));
            Temperature temperature = new Temperature(day, tmp);
            if (character.equals("C")) {
                temperaturesbyC.add(temperature);
            } else {
                temperaturesByF.add(temperature);
            }
        }
        sc.close();
    }

    public void writeDailyStats(PrintStream out, char scale) {
        PrintWriter pw = new PrintWriter(out);
        if (scale == 'C') {
            temperaturesByF = temperaturesByF.stream().map(temperature -> {
                List<Double> modifiedValues = temperature.temperatures.stream()
                        .map(t -> (t - 32) * 5 / 9)
                        .collect(Collectors.toList());
                temperature.temperatures = modifiedValues;
                return temperature;
            }).collect(Collectors.toList());
            for (Temperature temperature : temperaturesByF) {
                temperatures.add(temperature);
            }
            for (Temperature temperature : temperaturesbyC) {
                temperatures.add(temperature);
            }
            temperatures = temperatures.stream().sorted(Comparator.comparing(Temperature::getDay)).collect(Collectors.toList());
            for (Temperature temperature : temperatures) {
                pw.printf("%3d: ", temperature.day);
                DoubleSummaryStatistics statistics = temperature.temperatures
                        .stream()
                        .mapToDouble(t -> t.doubleValue())
                        .summaryStatistics();
                pw.printf("Count: %3d ", statistics.getCount());
                pw.printf("Min: %6.2fC ", statistics.getMin());
                pw.printf("Max: %6.2fC ", statistics.getMax());
                pw.printf("Avg: %6.2fC\n", statistics.getAverage());
            }
        } else {
            temperaturesbyC = temperaturesbyC.stream().map(temperature -> {
                List<Double> modifiedValues = temperature.temperatures.stream()
                        .map(t -> (t * 9 / 5) + 32)
                        .collect(Collectors.toList());
                temperature.temperatures = modifiedValues;
                return temperature;
            }).collect(Collectors.toList());
            temperaturesByF = temperaturesByF.stream().map(temperature -> {
                List<Double> modifiedValues = temperature.temperatures.stream()
                        .map(t -> (t * 9 / 5) + 32)
                        .collect(Collectors.toList());
                temperature.temperatures = modifiedValues;
                return temperature;
            }).collect(Collectors.toList());
            for (Temperature temperature : temperaturesbyC) {
                temperatureList.add(temperature);
            }
            for (Temperature temperature : temperaturesByF) {
                temperatureList.add(temperature);
            }
            temperatureList = temperatureList.stream().sorted(Comparator.comparing(Temperature::getDay)).collect(Collectors.toList());
            for (Temperature temperature : temperatureList) {
                pw.printf("%3d: ", temperature.day);
                DoubleSummaryStatistics statistics = temperature.temperatures
                        .stream()
                        .mapToDouble(t -> t.doubleValue())
                        .summaryStatistics();
                pw.printf("Count: %3d ", statistics.getCount());
                pw.printf("Min: %6.2fF ", statistics.getMin());
                pw.printf("Max: %6.2fF ", statistics.getMax());
                pw.printf("Avg: %6.2fF\n", statistics.getAverage());
            }
        }
        pw.flush();
    }
}

