package lab;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.io.UncheckedIOException;

public class MainMenuController {

    private App app;

    @FXML
    private Button play;

    @FXML
    private Button load;

    @FXML
    private Button scoreboard;

    @FXML
    private void startGame() {
        app.buttonClickSound();
        try {
            app.switchToGame(null);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load game screen", e);        }
    }

    @FXML
    private void loadGame() {
        app.buttonClickSound();
        GameScene loadedScene = SaveManager.loadGame("GAME_SAVE.bin");

        if (loadedScene != null) {
            try {
                app.switchToGame(loadedScene);
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to load game scene from save", e);
            }
        } else {
            System.out.println("Game cannot be loaded. (Probably different code version)");
        }
    }

    @FXML
    private void openScoreboard()
    {
        app.buttonClickSound();
       try {
            app.switchToScoreboard();
        } catch (IOException e) {
           throw new UncheckedIOException("Failed to load scoreboard", e);        }
    }

    public void setApp(App app) {
        this.app = app;

        app.buttonHoverSound(play);
        app.buttonHoverSound(load);
        app.buttonHoverSound(scoreboard);
    }
}
