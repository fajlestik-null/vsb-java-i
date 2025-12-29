package lab;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class GameScene implements Serializable {
    private final double width;
    private final double height;
    private transient Dimension2D size;
    private final List<DrawableSimulable> entities = new ArrayList<>();
    private final List<DrawableSimulable> entitiesToRemove = new ArrayList<>();
    private transient List<DrawableSimulable> entitiesToAdd = new ArrayList<>();
    private final Player player;
    private transient Set<KeyCode> pressedKeys = new HashSet<>();
    private static final Random RANDOM = new Random();
    private double asteroidSpawnTimer = 0;
    private double asteroidSpawnInterval = 2.5;
    private double difficultyTimer = 0;
    private transient double startDelayTimer = 0;
    private transient Comparator<DrawableSimulable> comparator;
    private transient Background background;


    public GameScene(double width, double height){
        this.width = width;
        this.height = height;

        size = new Dimension2D(width, height);
        this.background = new Background(width, height);

        setStartDelay(3.0);

        player = new Player(this, new Point2D(400,300));
        entities.add(player);

        for(int i = 0; i < 10; i++) {
            spawnAsteroid();
        }

        initComparator();
        entities.sort(comparator);

        entities.addAll(entitiesToAdd);
        entitiesToAdd.clear();
        entities.sort(comparator);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        this.size = new Dimension2D(width, height);
        this.pressedKeys = new HashSet<>();

        if (this.entitiesToAdd == null) {
            this.entitiesToAdd = new ArrayList<>();
        }

        this.background = new Background(width, height);

        this.initComparator();

        this.setStartDelay(3.0);
    }

    private void initComparator() {
        comparator = ((Comparator<DrawableSimulable>) (o1, o2) -> {
            if (o1 == o2) return 0;
            if (o1 instanceof Explosion || o2 instanceof Player || o1 instanceof Bullet)
            {
                return -1;
            }
            if (o2 instanceof Explosion || o1 instanceof Player || o2 instanceof Bullet)
            {
                return 1;
            }
            if (o1 instanceof Asteroid a1 && o2 instanceof Asteroid a2) {
                return Double.compare(a1.getSize(), a2.getSize());
            }
            return 0;
        }).reversed();
    }

    public Dimension2D getSize() {
        if (size == null) {
            size = new Dimension2D(width, height);
        }
        return size;
    }

    public void draw(GraphicsContext gc) {
        if (background != null) background.draw(gc);
        entities.stream().forEach(entity -> entity.draw(gc));

        if (startDelayTimer > 0) {
            gc.save();
            gc.setFill(Color.GRAY);
            gc.setFont(new Font("8-bit Arcade In", 200));
            gc.setTextAlign(TextAlignment.CENTER);

            String text = String.valueOf((int) Math.ceil(startDelayTimer));
            gc.fillText(text, width / 2, height / 2);
            gc.restore();
        }
    }

    public void setStartDelay(double seconds) {
        this.startDelayTimer = seconds;
    }

    public void simulate(double deltaTime) {
        if (startDelayTimer > 0) {
            startDelayTimer -= deltaTime;
            return;
        }

        entities.stream().forEach(
            entity -> entity.simulate(deltaTime)
        );

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
            asteroidSpawnInterval = Math.max(0.8, asteroidSpawnInterval - 0.1);
        }

        if (!entitiesToAdd.isEmpty()) {
            entities.addAll(entitiesToAdd);
            entitiesToAdd.clear();
            entities.sort(comparator);
        }
    }

    private void spawnAsteroid() {
        double asteroidSize = 10 + RANDOM.nextDouble(50);
        double x = RANDOM.nextDouble() * getSize().getWidth();
        double y = RANDOM.nextDouble() * getSize().getHeight();

        Asteroid asteroid;
        if (RANDOM.nextDouble() < 0.05) {
            asteroid = new PowerUpAsteroid(this, new Point2D(x, y), asteroidSize);
        } else {
            asteroid = new Asteroid(this, new Point2D(x, y), asteroidSize);
        }

        if (asteroid.getPosition().distance(player.getPosition()) > 150) {
            this.addEntity(asteroid);
        }
    }

    private void checkCollisions() {
        for (int i = 0; i < entities.size(); i++) {
            DrawableSimulable e1 = entities.get(i);

            for (int j = i + 1; j < entities.size(); j++) {
                DrawableSimulable e2 = entities.get(j);

                if (e1 instanceof Collisionable c1 && e2 instanceof Collisionable c2 && c1.intersect(c2.getBoundingBox())) {
                        c1.hitBy(c2);
                        c2.hitBy(c1);
                    }

            }
        }

        entities.removeAll(entitiesToRemove);
        entitiesToRemove.clear();
    }

    public void addEntity(DrawableSimulable entity) {
        entitiesToAdd.add(entity);
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
