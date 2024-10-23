package lab;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class Scene {

	private Dimension2D size;
	private DrawableSimulable[] sceneEntitites;

	public Scene(double width, double height) {
		size = new Dimension2D(width, height);
		sceneEntitites = new DrawableSimulable[8];
		sceneEntitites[0] = new Background(this);
		sceneEntitites[1] = new Rock(this, new Point2D(300, 300), new Dimension2D(30, 50));
		sceneEntitites[2] = new Boat(this, new Point2D(20, 200));
		for (int i = 3; i < sceneEntitites.length; i++) {
			sceneEntitites[i] = new LochNess(this);
		}
	}

	public Dimension2D getSize() {
		return size;
	}

	public void draw(GraphicsContext gc) {
		for (DrawableSimulable entity : sceneEntitites) {
			entity.draw(gc);
		}
	}

	public void simulate(double deltaTime) {
		for (DrawableSimulable drawableSimulable : sceneEntitites) {
			drawableSimulable.simulate(deltaTime);
		}
		for (int i = 0; i < sceneEntitites.length; i++) {
			if (sceneEntitites[i] instanceof Collisionable c1) {
				for (int j = i + 1; j < sceneEntitites.length; j++) {
					if (sceneEntitites[j] instanceof Collisionable c2) {
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
