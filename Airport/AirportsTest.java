package Airport;

import com.sun.source.tree.Tree;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.IntStream;

class Airport {
    String name;
    String country;
    String code;
    int passengers;
    TreeSet<Flight> flights;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        flights = new TreeSet<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name + " (" + code + ")" + "\n");
        sb.append(country + "\n" + passengers + "\n");
        List<Flight> f1 = new ArrayList<>();
        f1.addAll(flights);
        IntStream.range(0, flights.size()).forEach(i -> {
            sb.append(i + 1 + ". " + f1.get(i));
        });
        return sb.toString();
    }
}

class Flight implements Comparable<Flight> {
    String from;
    String to;
    int time;
    int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    @Override
    public String toString() {
        LocalTime initialTime = LocalTime.of(0, 0);
        LocalTime timeBegin = initialTime.plusMinutes(time);
        LocalTime timeEnd = timeBegin.plusMinutes(duration);
        Duration d = Duration.between(timeBegin, timeEnd);
        long hours = d.toHours() % 24;
        long minutes = d.toMinutes() % 60;
        if (hours < 0) {
            return String.format("%s-%s %s-%s +1d%2dh%02dm\n", from, to, timeBegin, timeEnd,
                    duration / 60, duration % 60);
        } else {
            return String.format("%s-%s %s-%s%2dh%02dm\n", from, to, timeBegin, timeEnd, duration / 60, duration % 60);
        }
    }

    @Override
    public int compareTo(Flight o) {
        int res = to.compareTo(o.to);
        if (res == 0) {
            return Integer.compare(time, o.time);
        }
        return res;
    }
}

class Airports {

    List<Airport> airports;

    public Airports() {
        airports = new ArrayList<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        Airport airport = new Airport(name, country, code, passengers);
        airports.add(airport);
    }

    public void addFlights(String from, String to, int time, int duration) {
        Flight flight = new Flight(from, to, time, duration);
        airports.stream().forEach(airport -> {
            if (airport.code.equals(from)) {
                airport.flights.add(flight);
            }
        });
    }

    public void showFlightsFromAirport(String from) {
        airports.stream().filter(airport -> airport.code.equals(from))
                .forEach(System.out::print);
    }

    public void showDirectFlightsFromTo(String from, String to) {
        StringBuilder sb = new StringBuilder();
        airports.stream().filter(airport -> airport.code.equals(from))
                .forEach(airport -> {
                    airport.flights.forEach(flight -> {
                        if (flight.to.equals(to)) {
                            sb.append(flight);
                        }
                    });
                });
        if (sb.length() == 0) {
            System.out.println("No flights from " + from + " to " + to);
        } else {
            System.out.print(sb.toString());
        }
    }

    public void showDirectFlightsTo(String to) {
        TreeSet<Flight> fl = new TreeSet<>();
        airports.stream().forEach(airport -> {
            airport.flights.forEach(flight -> {
                if (flight.to.equals(to)) {
                    fl.add(flight);
                }
            });
        });
        fl.stream().forEach(System.out::print);
    }
}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde


