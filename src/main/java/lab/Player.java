package lab;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.LinkedList;
import java.util.List;

public class Player extends WorldEntity {
    private int lives = 3;
    private double score = 0;
    private List<ScoreListener> scoreListeners = new LinkedList<>();
    private List<LivesListener> livesListeners = new LinkedList<>();


    public Player(GameScene gameScene,  Point2D position) {
    super(gameScene, position, new Polygon(-10,-10,20,0,-10,10));
    }

    @Override
    public Color getColor(){
        return Color.WHITE;
    }

    @Override
    public void simulate(double deltaTime) {
        if (gameScene.isKeyPressed(KeyCode.W)) {
            accelerate(deltaTime);
        }
        if (gameScene.isKeyPressed(KeyCode.A)) {
            rotateLeft();
        }
        if (gameScene.isKeyPressed(KeyCode.D)) {
            rotateRight();
        }

        move(deltaTime);
        applyDrag(deltaTime);
    }

    @Override
    public void hitBy(Collisionable other) {
        //not used
    }

    public void decreaseLives() {
        lives--;
    }

    public int getLives(){
        return lives;
    }

    public void addScore(double score) {
        this.score += score;
    }

    public double getScore() {
        return score;
    }

    public void addScoreListener(ScoreListener scoreListener)
    {
        scoreListeners.add(scoreListener);
    }

//    public boolean removeScoreListener(ScoreListener scoreListener)
//    {
//        return scoreListeners.remove(scoreListener);
//    }

    protected void fireScore()
    {
        for (ScoreListener scoreListener : scoreListeners)
        {
            scoreListener.scoreChanged();
        }
    }

    public void addLivesListener(LivesListener livesListener)
    {
        livesListeners.add(livesListener);
    }

//    public boolean removeLivesListener(LivesListener livesListener)
//    {
//            return livesListeners.remove(livesListener);
//    }

    protected void fireLives()
    {
        for (LivesListener livesListener : livesListeners)
        {
            livesListener.livesChanged();
        }
    }
}
