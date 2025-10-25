package lab;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public class GameScene {
    private Dimension2D size;
    private List<DrawableSimulable> entities = new ArrayList<>();
    private List<DrawableSimulable> entitiesToRemove = new ArrayList<>();
    private Bullet lastBullet;
    private Player player;
    private Set<KeyCode> pressedKeys = new HashSet<>();
    private Random rand;
    private double asteroidSpawnTimer = 0;
    private double asteroidSpawnInterval = 2.0;
    private double difficultyTimer = 0;




    public GameScene(double width, double height){
        size = new Dimension2D(width, height);
        player = new Player(this, new Point2D(400,300));
        entities.add(player);
        for(int i = 0; i < 10; i++){
            spawnAsteroid();
        }
    }

    public Dimension2D getSize() {
        return size;
    }

    public void draw(GraphicsContext gc) {
        for (DrawableSimulable entity : entities) {
            entity.draw(gc);
        }
    }

    public void simulate(double deltaTime) {
        for (DrawableSimulable drawableSimulable : entities) {
            drawableSimulable.simulate(deltaTime);
        }

        checkCollisions();

        asteroidSpawnTimer += deltaTime;
        if (asteroidSpawnTimer >= asteroidSpawnInterval) {
            asteroidSpawnTimer = 0;
            spawnAsteroid();
        }

        double difficultyIncreaseInterval = 10;
        difficultyTimer += deltaTime;
        if (difficultyTimer >= difficultyIncreaseInterval) {
            difficultyTimer = 0;
            asteroidSpawnInterval = Math.max(0.5, asteroidSpawnInterval - 0.2);
        }

        if (isKeyPressed(KeyCode.SPACE) && (lastBullet == null || lastBullet.isAfterDelay())) {
                spawnBullet();
        }
    }

    private void spawnBullet(){
        Point2D playerPos = player.getPosition();
        double angle = player.getShape().getRotate();

        double distanceFromCenter = 20; // adjust as needed
        double rad = Math.toRadians(angle);
        Point2D bulletPos = playerPos.add(
            Math.cos(rad) * distanceFromCenter,
            Math.sin(rad) * distanceFromCenter
        );

        Bullet bullet = new Bullet(this, bulletPos, angle);
        entities.add(bullet);
        lastBullet = bullet;
    }

    private void spawnAsteroid() {
        if (rand == null) rand = new Random();

        double asteroidSize = 10 + rand.nextDouble(50);
        double x = rand.nextDouble() * getSize().getWidth();
        double y = rand.nextDouble() * getSize().getHeight();
        Asteroid asteroid = new Asteroid(this, new Point2D(x, y), asteroidSize);

        if (asteroid.getPosition().distance(player.getPosition()) > 150) {
            entities.add(asteroid);
        }
    }

    private void checkCollisions() {

        for (DrawableSimulable entity : entities) {
            if (entity instanceof Collisionable c1) {
                for (DrawableSimulable entity_col : entities) {
                    if (entity_col instanceof Collisionable c2 && c1 != c2 && c1.intersect(c2.getBoundingBox())) {
                            c1.hitBy(c2);
                            c2.hitBy(c1);
                        }

                }
            }
        }

        if(entitiesToRemove.contains(lastBullet))
        {
            lastBullet = null;
        }
        entities.removeAll(entitiesToRemove);
        entitiesToRemove.clear();
    }

    public Player getPlayer() {
        return player;
    }

    public void keyPressed(KeyCode key) {
        pressedKeys.add(key);
    }

    public void keyReleased(KeyCode key) {
        pressedKeys.remove(key);
    }

    public boolean isKeyPressed(KeyCode key) {
        return pressedKeys.contains(key);
    }

    public void removeEntity(WorldEntity worldEntity)
    {
        entitiesToRemove.add(worldEntity);
    }
}
