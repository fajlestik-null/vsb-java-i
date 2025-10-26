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


public class GameController {
    private DrawingThread timer;
    private GameScene gameScene;
    private Stage stage;

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
    private void gameOver() {
        stop();

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/lab/gameOver.fxml"));
                Parent gameOverRoot = loader.load();

                GameOverController controller = loader.getController();
                controller.setFinalScore((int) gameScene.getPlayer().getScore());

                stage.setScene(new Scene(gameOverRoot));
                stage.setTitle("Game Over");
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
        if (gameScene.getPlayer().getLives() <= 0) {
            gameOver();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
