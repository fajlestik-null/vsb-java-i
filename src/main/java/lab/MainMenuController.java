package lab;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import java.io.IOException;

public class MainMenuController {

    private App app;

    @FXML
    private Button play;

    @FXML
    private Button load;

    @FXML
    private Button scoreboard;

    @FXML
    private void startGame(ActionEvent event) {
        try {
            app.switchToGame(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void loadGame(ActionEvent event) {
        GameScene loadedScene = SaveManager.loadGame("GAME_SAVE.bin");

        if (loadedScene != null) {
            try {
                app.switchToGame(loadedScene);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Game cannot be loaded.");
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
