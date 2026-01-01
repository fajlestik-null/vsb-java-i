package lab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScoreManager {

    private static final String FILE_NAME = "scoreboard.csv";

    private ScoreManager() {
        throw new IllegalStateException("Utility class");
    }

    public static void save(List<Score> scores) throws ScoreException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Score score : scores) {
                bw.write(String.format("%s;%d", score.nickName(), score.scorePoints()));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new ScoreException("Error saving scores to 'scoreboard.csv'", e);
        }
    }

    public static List<Score> load() throws ScoreException {
        List<Score> result = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if (created) {
                    System.out.println("Created new empty scoreboard file: " + FILE_NAME);
                }
                return result;
            } catch (IOException e) {
                throw new ScoreException("Could not create new scoreboard file: " + e.getMessage(), e);
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.add(parseScoreLine(line));
            }
        } catch (IOException e) {
            throw new ScoreException("Error loading scores from 'scoreboard.csv'", e);
        }
        return result;
    }

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
