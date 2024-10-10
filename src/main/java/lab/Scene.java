package lab;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class Scene {

	private Dimension2D size;
	private Background background;
	private Rock rock;
	private Boat boat;
	private LochNess[] lochNesses;

	public Scene(double width, double height) {
		size = new Dimension2D(width, height);
		background = new Background(this);
		rock = new Rock(this, new Point2D(300, 300), new Dimension2D(30, 50));
		boat = new Boat(this, new Point2D(20, 200));
		lochNesses = new LochNess[5];
		for (int i = 0; i < lochNesses.length; i++) {
			lochNesses[i] = new LochNess(this);
		}
	}

	public Dimension2D getSize() {
		return size;
	}

	public void draw(GraphicsContext gc) {
		background.draw(gc);
		rock.draw(gc);
		boat.draw(gc);
		for (LochNess lochNess : lochNesses) {
			lochNess.draw(gc);
		}
	}

	public void simulate(double deltaTime) {
		background.simulate(deltaTime);
		rock.simulate(deltaTime);
		boat.simulate(deltaTime);
		for (LochNess lochNess : lochNesses) {
			lochNess.simulate(deltaTime);
			if (lochNess.getBoundingBox().intersects(boat.getBoundingBox())) {
				lochNess.changeDirection();
			}
		}
	}

}
