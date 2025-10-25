package lab;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;


public class GameController {
    private DrawingThread timer;
    private GameScene gameScene;

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
    }

}
