package lab;

import java.util.Random;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

public class Rock extends WorldEntity implements Collisionable {

    private static final Random RANDOM = new Random();
    protected Dimension2D size;
    private double angle = 0;

    public Rock(GameScene gameScene) {
        this(gameScene, new Point2D(RANDOM.nextDouble(gameScene.getSize().getWidth()),
                RANDOM.nextDouble(gameScene.getSize().getHeight() * 0.6, gameScene.getSize().getHeight() - 10)),
            new Dimension2D(20, 10));
    }

    public Rock(GameScene gameScene, Point2D position, Dimension2D size) {
        super(gameScene, position);
        this.size = size;
    }

    @Override
    public void drawInternal(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.setStroke(Color.GREEN);
        rotate(gc);
        gc.fillRect(position.getX(), position.getY(), size.getWidth(), size.getHeight());
        gc.strokeRect(position.getX(), position.getY(), size.getWidth(), size.getHeight());
    }

    protected void rotate(GraphicsContext gc) {
        Point2D center = position.add(size.getWidth() / 2, size.getHeight() / 2);
        Rotate rotateMatrix = Affine.rotate(angle, center.getX(), center.getY());
        gc.setTransform(new Affine(rotateMatrix));
    }

    public void simulate(double deltaTime) {
        angle += 0.5;
    }

    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), size.getWidth(), size.getHeight());
    }

    @Override
    public boolean intersect(Rectangle2D another) {
        return getBoundingBox().intersects(another);
    }

    @Override
    public void hitBy(Collisionable another) {
    }

}
