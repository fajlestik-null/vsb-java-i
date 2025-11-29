package lab;

import java.io.IOException;
import java.io.ObjectInputStream;
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

public class GameScene implements Serializable {
    private double width;
    private double height;
    private transient Dimension2D size;
    private List<DrawableSimulable> entities = new ArrayList<>();
    private List<DrawableSimulable> entitiesToRemove = new ArrayList<>();
    private List<DrawableSimulable> entitiesToAdd = new ArrayList<>();
    private Player player;
    private transient Set<KeyCode> pressedKeys = new HashSet<>();
    private Random rand;
    private double asteroidSpawnTimer = 0;
    private double asteroidSpawnInterval = 2.0;
    private double difficultyTimer = 0;
    private transient Comparator<DrawableSimulable> comparator;


    public GameScene(double width, double height){
        //pro důvod serializace
        this.width = width;
        this.height = height;

        size = new Dimension2D(width, height);
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

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // 1. Načte všechna normální data (player, entities, width...)

        this.size = new Dimension2D(width, height);
        this.pressedKeys = new HashSet<>();

        if (this.entitiesToAdd == null) {
            this.entitiesToAdd = new ArrayList<>();
        }

        this.initComparator();
    }

    private void initComparator() {
        comparator = new Comparator<DrawableSimulable>() {
            @Override
            public int compare(DrawableSimulable o1, DrawableSimulable o2) {
                if (o1 == o2) return 0;
                if (o1 instanceof Player) return 1;
                if (o2 instanceof Player) return -1;
                if (o1 instanceof Bullet) return -1;
                if (o2 instanceof Bullet) return 1;
                if (o1 instanceof Asteroid a1 && o2 instanceof Asteroid a2) {
                    return Double.compare(a1.getSize(), a2.getSize());
                }
                return 0;
            }
        }.reversed();
    }

    public Dimension2D getSize() {
        if (size == null) {
            size = new Dimension2D(width, height);
        }
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

        if (!entitiesToAdd.isEmpty()) {
            entities.addAll(entitiesToAdd);
            entitiesToAdd.clear();
            entities.sort(comparator);
        }
    }

    private void spawnAsteroid() {
        if (rand == null) rand = new Random();

        double asteroidSize = 10 + rand.nextDouble(50);
        double x = rand.nextDouble() * getSize().getWidth();
        double y = rand.nextDouble() * getSize().getHeight();
        Asteroid asteroid = new Asteroid(this, new Point2D(x, y), asteroidSize);

        if (asteroid.getPosition().distance(player.getPosition()) > 150) {
            this.addEntity(asteroid);
        }
    }

    private void checkCollisions() {
        for (int i = 0; i < entities.size(); i++) {
            DrawableSimulable e1 = entities.get(i);

            for (int j = i + 1; j < entities.size(); j++) {
                DrawableSimulable e2 = entities.get(j);

                if (e1 instanceof Collisionable c1 && e2 instanceof Collisionable c2) {
                    if (c1.intersect(c2.getBoundingBox())) {
                        c1.hitBy(c2);
                        c2.hitBy(c1);
                    }
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
