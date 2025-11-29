package lab;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;


public class GameController {
    private DrawingThread timer;
    private GameScene gameScene;
    private App app;
    private boolean gameOverTriggered = false;

    @FXML
    private Canvas canvas;

    @FXML
    private Label lives;

    @FXML
    private Label score;

    @FXML
    private Button save;

    @FXML
    void initialize() {

        canvas.setFocusTraversable(true);

        canvas.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (gameScene != null) {
                gameScene.keyPressed(e.getCode());
            }
        });

        canvas.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            if (gameScene != null) {
                gameScene.keyReleased(e.getCode());
            }
        });
    }

    @FXML
    private void saveGame() {
        if (gameScene != null) {
            SaveManager.saveGame(gameScene, "GAME_SAVE.bin");
            System.out.println("Game was successfully saved!");
        }

        this.stop();

        try {
            app.switchToMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startGame(GameScene scene) {
        this.gameScene = scene;
        this.gameOverTriggered = false;

        Player p = gameScene.getPlayer();

        p.addLivesListener(this::changeLives);
        p.addScoreListener(this::changeScore);

        changeLives();
        changeScore();

        timer = new DrawingThread(canvas, gameScene);
        timer.start();
        canvas.requestFocus();
    }

    public void stop() {
        timer.stop();
    }

    public void changeScore() {
        Platform.runLater(() -> {
            if (gameScene != null) {
                score.setText("SCORE: " + (int) gameScene.getPlayer().getScore());
            }
        });
    }

    public void changeLives() {
        Platform.runLater(() -> {
            if (gameScene == null) return;

            lives.setText("LIVES: " + gameScene.getPlayer().getLives());

            if (!gameOverTriggered && gameScene.getPlayer().getLives() <= 0) {
                gameOverTriggered = true;
                stop();
                try {
                    app.switchToGameOver((int) gameScene.getPlayer().getScore());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void setApp(App app)
    {
        this.app = app;
    }
}
