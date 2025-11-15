package lab;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class GameOverController {
    private App app;
    private int scoreToSave;

    @FXML
    private Label finalScore;

    @FXML
    private TextField nameField;

    public void setFinalScore(int score) {
        scoreToSave = score;
        finalScore.setText("Your score: " + score);
    }

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    private void onBackToMenu() {

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            name = NameGenerator.getRandomNick();
        }

        Score score = new Score(name, scoreToSave);

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
