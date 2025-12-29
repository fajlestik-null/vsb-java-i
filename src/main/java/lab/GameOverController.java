package lab;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameOverController {
    private App app;
    private int scoreToSave;
    private static final Random RANDOM = new Random();


    @FXML
    private Label finalScore;

    @FXML
    private Button exit;

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

    private boolean isWorker(String name) {
        String normalizedName = normalizeName(name);
        String[] nameSectioned = normalizedName.split(" ");

        if (nameSectioned.length < 2) {
            return false;
        }

        List<String> workers = app.getAcademicWorkers();

        return workers.stream()
            .map(this::normalizeName) // Transform worker names
            .map(n -> n.split(" "))   // Split them
            .anyMatch(workerSectioned ->
                // Check if names match (normal or swapped)
                (nameSectioned[0].equals(workerSectioned[0]) && nameSectioned[1].equals(workerSectioned[1])) ||
                    (nameSectioned[1].equals(workerSectioned[0]) && nameSectioned[0].equals(workerSectioned[1]))
            );
    }

    public void setApp(App app) {
        this.app = app;

        app.buttonHoverSound(exit);
    }

    @FXML
    private void onBackToMenu() {
        app.buttonClickSound();

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            name = NameGenerator.getRandomNick();
        }

        Score score;

        if(isWorker(name))
        {   System.out.println("Be careful, academic worker is present!");
            System.out.println("Bribe function has been activated.");
             score = new Score(name, scoreToSave+RANDOM.nextInt(10000));

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
            System.err.println("Failed to save high scores: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            app.switchToMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
