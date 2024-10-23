package lab;

import java.util.Random;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Boat extends WorldEntity implements Collisionable {

	private static final Random RANDOM = new Random();
	private Point2D speed;
	private Image image;

	public Boat(Scene scene, Point2D position) {
		super(scene, position);
		image = new Image(Boat.class.getResourceAsStream("ship-boat.gif"));
		speed = new Point2D(0, 0);
	}

	@Override
	public void drawInternal(GraphicsContext gc) {
		gc.drawImage(image, position.getX(), position.getY());
		gc.strokeRect(position.getX(), position.getY(), image.getWidth(), image.getHeight());
	}

	@Override
	public void simulate(double deltaTime) {
		position = position.add(speed.multiply(deltaTime / 1_000_000_000));
		speed = speed.multiply(0.99);
	}

	@Override
	public Rectangle2D getBoundingBox() {
		return new Rectangle2D(position.getX(), position.getY(), image.getWidth(), image.getHeight());
	}

	@Override
	public boolean intersect(Rectangle2D another) {
		return getBoundingBox().intersects(another);
	}

	@Override
	public void hitBy(Collisionable another) {
		if(another instanceof LochNess lochNess) {
			int direction = RANDOM.nextBoolean()?-1:1;
			speed = new Point2D(-20, direction * RANDOM.nextDouble(50, 100));
		}
	}

}
