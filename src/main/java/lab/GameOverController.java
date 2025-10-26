package lab;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameOverController {
    @FXML
    private Label finalScore;

    public void setFinalScore(int score) {
        finalScore.setText("Your score: " + score);
    }

}
