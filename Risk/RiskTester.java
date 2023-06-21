package Risk;

import java.io.InputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

class Risk {
    public void processAttacksData(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] attackData = line.split(";");
            int[] attackerDice = parseDiceResults(attackData[0]);
            int[] defenderDice = parseDiceResults(attackData[1]);
            Arrays.sort(attackerDice);
            Arrays.sort(defenderDice);
            int survivingAttackers = 0;
            int survivingDefenders = 0;
            for (int i = 0; i < attackerDice.length; i++) {
                if (attackerDice[i] > defenderDice[i]) {
                    survivingAttackers++;
                } else {
                    survivingDefenders++;
                }
            }

            System.out.println(survivingAttackers + " " + survivingDefenders);
        }
    }

    private int[] parseDiceResults(String diceResults) {
        String[] diceValues = diceResults.trim().split("\\s+");
        int[] dice = new int[diceValues.length];
        for (int i = 0; i < diceValues.length; i++) {
            dice[i] = Integer.parseInt(diceValues[i]);
        }
        return dice;
    }
}


public class RiskTester {
    public static void main(String[] args) throws IOException {
        Risk risk = new Risk();
        risk.processAttacksData(System.in);
    }
}
