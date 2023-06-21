package FootballTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


class Team implements Comparable<Team> {

    private String name;
    private int givenGoals;
    private int takenGoals;
    private int wins;
    private int draw;
    private int lost;

    public Team(String name) {
        this.name = name;
        this.givenGoals = 0;
        this.takenGoals = 0;
        this.wins = 0;
        this.draw = 0;
        this.lost = 0;
    }

    public void gaveGoals(int howMany) {
        this.givenGoals += howMany;
    }

    public void tookGoals(int howMany) {
        this.takenGoals += howMany;
    }

    public void win() {
        this.wins++;
    }

    public void lost() {
        this.lost++;
    }

    public void draw() {
        this.draw++;
    }

    public int points() {
        return (this.wins * 3) + this.draw;
    }

    public int gamesPlayed() {
        return this.wins + this.draw + this.lost;
    }

    @Override
    public int compareTo(Team other) {
        int rv = -(Integer.compare(this.points(), other.points()));
        if (rv == 0) {
            int golRazlika = -(Integer.compare(this.givenGoals - this.takenGoals, other.givenGoals - other.takenGoals));
            if (golRazlika == 0) {
                return this.name.compareTo(other.name);
            }
            return golRazlika;
        }
        return rv;
    }

    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d", this.name, this.gamesPlayed(), this.wins, this.draw, this.lost,
                this.points());
    }
}

class FootballTable {

    private HashMap<String, Team> teams;

    public FootballTable() {
        this.teams = new HashMap<>();
    }

    private Team checkInTable(String name) {
        if (this.teams.containsKey(name)) {
            return this.teams.get(name);
        }
        Team t = new Team(name);
        this.teams.put(name, t);
        return t;
    }

    private void goals(Team g, int me, int them) {
        g.gaveGoals(me);
        g.tookGoals(them);
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {

        Team home = checkInTable(homeTeam);
        Team away = checkInTable(awayTeam);

        goals(home, homeGoals, awayGoals);
        goals(away, awayGoals, homeGoals);

        if (homeGoals > awayGoals) {
            home.win();
            away.lost();
        } else if (homeGoals < awayGoals) {
            home.lost();
            away.win();
        } else {
            home.draw();
            away.draw();
        }

    }

    public void printTable() {

        List<Team> table = new ArrayList<>(this.teams.values());
        Collections.sort(table);
        for (int i = 0; i < table.size(); i++) {
            System.out.printf("%2d. %s\n", i + 1, table.get(i));
        }

    }


}

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}


