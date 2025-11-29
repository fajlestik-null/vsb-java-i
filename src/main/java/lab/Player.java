package lab;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

public class Player extends WorldEntity {
    private int lives = 3;
    private double score = 0;
    private double timeSinceLastShot = 0;
    private final double SHOOT_DELAY = 1.0;
    private transient List<ScoreListener> scoreListeners = new LinkedList<>();
    private transient List<LivesListener> livesListeners = new LinkedList<>();


    public Player(GameScene gameScene,  Point2D position) {
    super(gameScene, position, new Polygon(-10,-10,20,0,-10,10));
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        this.scoreListeners = new LinkedList<>();
        this.livesListeners = new LinkedList<>();
    }

    @Override
    public Color getColor(){
        return Color.WHITE;
    }

    @Override
    public void simulate(double deltaTime) {
        timeSinceLastShot += deltaTime;

        if (gameScene.isKeyPressed(KeyCode.W)) {
            accelerate(deltaTime);
        }
        if (gameScene.isKeyPressed(KeyCode.A)) {
            rotateLeft(deltaTime);
        }
        if (gameScene.isKeyPressed(KeyCode.D)) {
            rotateRight(deltaTime);        }

        if (gameScene.isKeyPressed(KeyCode.SPACE)) {
            if (timeSinceLastShot >= SHOOT_DELAY) {
                shoot();
                timeSinceLastShot = 0;
            }
        }

        move(deltaTime);
        applyDrag(deltaTime);
    }

    private void shoot() {
        double angle = shape.getRotate();

        double rad = Math.toRadians(angle);

        double distance = 20;

        Point2D bulletPos = position.add(
            Math.cos(rad) * distance,
            Math.sin(rad) * distance
        );

        Bullet b = new Bullet(gameScene, bulletPos, angle);

        gameScene.addEntity(b);
    }

    @Override
    public void hitBy(Collisionable other) {
        //not used
    }

    public void decreaseLives() {
        lives--;
        fireLives();
    }

    public int getLives(){
        return lives;
    }

    public void addScore(double score) {
        this.score += score;
        fireScore();
    }

    public double getScore() {
        return score;
    }

    public void addScoreListener(ScoreListener scoreListener)
    {
        if (scoreListeners == null) {
            scoreListeners = new LinkedList<>();
        }
        scoreListeners.add(scoreListener);
    }

    protected void fireScore()    {
        if (scoreListeners == null) return;
        for (ScoreListener scoreListener : scoreListeners) {
            scoreListener.scoreChanged();
        }
    }

    public void addLivesListener(LivesListener livesListener)    {
        if (livesListeners == null) {
            livesListeners = new LinkedList<>();
        }
        livesListeners.add(livesListener);
    }

    protected void fireLives()
    {
        if (livesListeners == null) return;
        for (LivesListener livesListener : livesListeners) {
            livesListener.livesChanged();
        }
    }
}
