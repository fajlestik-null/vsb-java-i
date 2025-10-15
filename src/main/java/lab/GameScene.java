package lab;

import java.util.Random;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class GameScene {

    private static final Random RANDOM = new Random();
    private Dimension2D size;

    private DrawableSimulable[] entitites = new DrawableSimulable[12];

    public GameScene(double width, double height) {
        size = new Dimension2D(width, height);
        entitites[0] = new Background(this);
        entitites[1] = new Boat(this, new Point2D(20, 200));
        for (int i = 2; i < entitites.length / 2; i++) {
            entitites[i] = RANDOM.nextBoolean()? new Rock(this): new Buoy(this);
        }
        for (int i = entitites.length / 2; i < entitites.length; i++) {
            entitites[i] = new LochNess(this);
        }
    }

    public Dimension2D getSize() {
        return size;
    }

    public void draw(GraphicsContext gc) {
        for (DrawableSimulable entity : entitites) {
            entity.draw(gc);
        }
    }

    public void simulate(double deltaTime) {
        for (DrawableSimulable drawableSimulable : entitites) {
            drawableSimulable.simulate(deltaTime);
        }
        for (int i = 0; i < entitites.length; i++) {
            if (entitites[i] instanceof Collisionable c1) {
                for (int j = i + 1; j < entitites.length; j++) {
                    if (entitites[j] instanceof Collisionable c2) {
                        if (c1.intersect(c2.getBoundingBox())) {
                            c1.hitBy(c2);
                            c2.hitBy(c1);
                        }
                    }
                }
            }
        }
    }


}
