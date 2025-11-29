package lab;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GameOverController {
    private App app;
    private int scoreToSave;
    private List<String> academicWorkers =new ArrayList<>();
    private Random rand = new Random();


    @FXML
    private Label finalScore;

    @FXML
    private TextField nameField;

    public void setFinalScore(int score) {
        scoreToSave = score;
        finalScore.setText("Your score: " + score);
    }

    private String normalizeName(String name) {
        //set as case-insensitive, then find expression that contains (...) and replace them with ""(nothing)
        name = name.replaceAll("(?i)(doc\\.|ing\\.|mgr\\.|ph\\.d\\.|phd|prof\\.|bc\\.)", "");

        name = name.replace(",", "");

        // find white characters, where there are multiple next to each other (1+) and replace them with " "
        name = name.trim().replaceAll("\\s+", " ");

        return name.toLowerCase();
    }

    private boolean isWorker(String name)
    {
        String normalizedName = normalizeName(name);
        String[] nameSectioned = normalizedName.split(" ");

        if(nameSectioned.length < 2)
        {
            return false;
        }

        for(String worker:academicWorkers){
            String normalizedWorker = normalizeName(worker);
            String[] workerSectioned = normalizedWorker.split(" ");

            if(nameSectioned[0].equals(workerSectioned[0]) && nameSectioned[1].equals(workerSectioned[1])
            || nameSectioned[1].equals(workerSectioned[0]) && nameSectioned[0].equals(workerSectioned[1]))
            {
                return true;
            }
        }

        return false;
    }

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    private void onBackToMenu() {

        String url="https://www.fei.vsb.cz/460/cs/kontakt/lide/";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new URI(url).toURL().openStream()))){
            String fileContent = in.lines().collect(Collectors.joining("\n"));

            Pattern p = Pattern.compile("<a[^>]*>([^<]+)</a>");
            Matcher m = p.matcher(fileContent);

            while (m.find()) {
                String name = m.group(1).trim();

                String[] checker = name.split(" ");

                // Filtering (Name has at least 2 words and second word starts with uppercase)
                if (checker.length >= 2 && Character.isUpperCase(checker[1].charAt(0)))
                {
                    academicWorkers.add(name);
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            name = NameGenerator.getRandomNick();
        }

        Score score;

        if(isWorker(name))
        {   System.out.println("Be careful, academic worker is present!");
            System.out.println("Bribe function was activated.");
             score = new Score(name, scoreToSave+rand.nextInt(10000));

        }
        else
        {
            score = new Score(name, scoreToSave);
        }


        List<Score> scores;

        try {
            scores = ScoreManager.load();
        } catch (ScoreException e) {
            scores = new ArrayList<>();
        }

        scores.add(score);

        try {
            ScoreManager.save(scores);
        } catch (ScoreException e) {
            throw new RuntimeException(e);
        }

        try {
            app.switchToMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
