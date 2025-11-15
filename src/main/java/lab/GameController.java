package lab;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    void initialize() {
        gameScene =  new GameScene(canvas.getWidth(), canvas.getHeight());
        gameScene.getPlayer().addLivesListener(this::changeLives);
        gameScene.getPlayer().addScoreListener(this::changeScore);
        timer = new DrawingThread(canvas, gameScene);
        timer.start();

        canvas.setFocusTraversable(true);

        canvas.addEventHandler(KeyEvent.KEY_PRESSED, e -> gameScene.keyPressed(e.getCode()));

        canvas.addEventHandler(KeyEvent.KEY_RELEASED, e -> gameScene.keyReleased(e.getCode()));
    }

    public void startGame() {
        timer = new DrawingThread(canvas, gameScene);
        timer.start();
        }


    public void stop() {
        timer.stop();
    }

    public void changeScore()
    {
        score.setText("SCORE: "+ (int)gameScene.getPlayer().getScore());
    }

    public void changeLives()
    {
        lives.setText("LIVES: "+ gameScene.getPlayer().getLives());
        if (!gameOverTriggered && gameScene.getPlayer().getLives() <= 0) {
            gameOverTriggered = true;
            stop();
            Platform.runLater(() -> {
                try {
                    app.switchToGameOver((int) gameScene.getPlayer().getScore());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void setApp(App app) {
        this.app = app;
    }
}
