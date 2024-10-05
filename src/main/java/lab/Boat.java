package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Boat {

	private Scene scene;
	private Point2D position;
	private Image image;

	public Boat(Scene scene, Point2D position) {
		this.scene = scene;
		this.position = position;
		image = new Image(Boat.class.getResourceAsStream("ship-boat.gif"));
	}

	public void draw(GraphicsContext gc) {
		gc.drawImage(image, position.getX(), position.getY());
	}

	public void simulate(double deltaTime) {
	}

}
