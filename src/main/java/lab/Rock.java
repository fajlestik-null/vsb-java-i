package lab;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

public class Rock {

	private Scene scene;
	private Point2D position;
	private Dimension2D size;
	private double angle = 0;

	public Rock(Scene scene) {
		this(scene, new Point2D(350, 200), new Dimension2D(60, 10));
	}

	public Rock(Scene scene, Point2D position, Dimension2D size) {
		this.scene = scene;
		this.position = position;
		this.size = size;
	}

	public void draw(GraphicsContext gc) {
		gc.save();
		gc.setFill(Color.GRAY);
		gc.setStroke(Color.GREEN);
		Point2D center = position.add(size.getWidth()/2, size.getHeight()/2);
		Rotate rotateMatrix = Affine.rotate(angle, center.getX(), center.getY());
		gc.setTransform(new Affine(rotateMatrix));
		gc.fillRect(position.getX(), position.getY(), size.getWidth(), size.getHeight());
		gc.strokeRect(position.getX(), position.getY(), size.getWidth(), size.getHeight());
		gc.restore();
	}

	public void simulate(double deltaTime) {
		angle += 0.5;
	}

}
