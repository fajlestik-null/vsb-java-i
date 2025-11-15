package lab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScoreManager {
    public static void save(List<Score> scores) throws ScoreException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("scoreboard.csv"))) {
            for (Score score : scores) {
                bw.write(String.format("%s;%d", score.getNickName(), score.getScore()));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new ScoreException("during saving scores into 'scoreboard.csv; something went wrong'",e);
        }
    }
    public static List<Score> load() throws ScoreException {
        List<Score> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("scoreboard.csv"))) {
            String line = br.readLine();
            while (line != null) {
                String[] parts = line.split(";");
                if (parts.length != 2) {
                    throw new ScoreException("Wrong number of columns");
                }
                try {
                    int points = Integer.parseInt(parts[1]);
                    result.add(new Score(parts[0], points));
                } catch (NumberFormatException e) {
                    throw new ScoreException("Points must be an integer");
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new ScoreException("during loading scores from 'scoreboard.csv; something went wrong'",e);
        }
        return result;
    }
}
