package lab;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class MainMenuController {

    private App app;

    @FXML
    private Button play;

    @FXML
    private Button scoreboard;

    @FXML
    private void startGame(ActionEvent event) {
        try {
            app.switchToGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void openScoreboard()
    {
        try {
            app.switchToScoreboard();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setApp(App app) {
        this.app = app;
    }
}
