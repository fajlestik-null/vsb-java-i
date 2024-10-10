package lab;

import java.util.Random;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class LochNess {

	private static final Random RANDOM = new Random();

	private Scene scene;
	private Point2D position;
	private Point2D speed;
	private Image image;

	public LochNess(Scene scene) {
		this.scene = scene;
		image = new Image(LochNess.class.getResourceAsStream("LochNess.gif"));
		position = new Point2D(RANDOM.nextDouble(scene.getSize().getWidth() * 0.3, scene.getSize().getWidth()),
				RANDOM.nextDouble(scene.getSize().getHeight() * 0.5, scene.getSize().getHeight() - image.getHeight()));
		speed = new Point2D(-RANDOM.nextDouble(50, 150), 0);
	}

	public void draw(GraphicsContext gc) {
		gc.drawImage(image, position.getX(), position.getY());
		gc.strokeRect(position.getX(), position.getY(), image.getWidth(), image.getHeight());
	}

	public void simulate(double deltaTime) {
		position = position.add(speed.multiply(deltaTime / 1_000_000_000));
		if (position.getX() + image.getWidth() < 0) {
			position = new Point2D(scene.getSize().getWidth(), position.getY());
		}
		if (position.getX() > scene.getSize().getWidth()+5) {
			position = new Point2D(scene.getSize().getWidth(), position.getY());
			speed = speed.multiply(-1);
		}
	}

	public Rectangle2D getBoundingBox() {
		return new Rectangle2D(position.getX(), position.getY(), image.getWidth(), image.getHeight());
	}

	public void changeDirection() {
		speed = speed.multiply(-1);
	}
}
