package Stadium;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.IntStream;

class SeatTakenException extends Exception {

    public SeatTakenException() {
        super("SeatTakenException");
    }

}

class SeatNotAllowedException extends Exception {

    public SeatNotAllowedException() {
        super("SeatNotAllowedException");
    }

}

class Sector {
    String code;
    int numOfSeats;
    private HashMap<Integer, Integer> seats;
    private int restriction;

    public Sector(String code, int numOfSeats) {
        this.code = code;
        this.numOfSeats = numOfSeats;
        this.seats = new HashMap<>();
        this.restriction = 0;
    }

    public String getCode() {
        return code;
    }

    public int getNumOfSeats() {
        return numOfSeats;
    }

    public int freeSeats() {
        return numOfSeats - seats.size();
    }

    public void buyTicket(int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        if (this.seats.containsKey(seat))
            throw new SeatTakenException();
        if ((type == 1 && restriction == 2) || (type == 2 && restriction == 1))
            throw new SeatNotAllowedException();
        if (type != 0 && restriction == 0)
            restriction = type;
        this.seats.put(seat, type);
    }

    private double calculatePercentage() {
        return ((double) this.seats.size() / this.numOfSeats) * 100.0;
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", this.code, freeSeats(), this.numOfSeats, this.calculatePercentage());
    }
}

class Stadium {
    String name;
    HashMap<String, Sector> sectors;

    public Stadium(String name) {
        this.name = name;
        this.sectors = new HashMap<>();
    }

    public void createSectors(String[] sectorNames, int[] sizes) {
        IntStream.range(0, sectorNames.length)
                .forEach(i -> this.sectors.put(sectorNames[i], new Sector(sectorNames[i], sizes[i])));
    }

    public void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        sectors.get(sectorName).buyTicket(seat, type);
    }

    public void showSectors() {
        sectors.values().stream()
                .sorted(Comparator.comparing(Sector::freeSeats, Comparator.reverseOrder()).thenComparing(Sector::getCode))
                .forEach(System.out::println);
    }
}

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}

