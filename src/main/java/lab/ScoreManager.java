package lab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScoreManager {

    // PROBLEM 1: Add a private constructor to hide the implicit public one
    // This prevents anyone from writing 'new ScoreManager()' since this is a utility class.
    private ScoreManager() {
        throw new IllegalStateException("Utility class");
    }

    public static void save(List<Score> scores) throws ScoreException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("scoreboard.csv"))) {
            for (Score score : scores) {
                // Note: Ensure your Score record/class has .scorePoints() or .getScore()
                bw.write(String.format("%s;%d", score.nickName(), score.scorePoints()));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new ScoreException("Error saving scores to 'scoreboard.csv'", e);
        }
    }

    public static List<Score> load() throws ScoreException {
        List<Score> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("scoreboard.csv"))) {
            String line;
            // Standard way to read lines in a loop
            while ((line = br.readLine()) != null) {
                // PROBLEM 2: Extracted nested try-block logic to a separate method
                result.add(parseScoreLine(line));
            }
        } catch (IOException e) {
            throw new ScoreException("Error loading scores from 'scoreboard.csv'", e);
        }
        return result;
    }

    // Helper method to handle parsing logic and exceptions
    private static Score parseScoreLine(String line) throws ScoreException {
        String[] parts = line.split(";");
        if (parts.length != 2) {
            throw new ScoreException("Wrong number of columns in line: " + line);
        }
        try {
            int points = Integer.parseInt(parts[1]);
            return new Score(parts[0], points);
        } catch (NumberFormatException e) {
            throw new ScoreException("Points must be an integer in line: " + line, e);
        }
    }
}
