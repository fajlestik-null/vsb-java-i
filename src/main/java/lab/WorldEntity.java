package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class WorldEntity implements DrawableSimulable {

	protected Scene scene;
	protected Point2D position;

	public WorldEntity(Scene scene, Point2D position) {
		this.scene = scene;
		this.position = position;
	}

	public Point2D getPosition() {
		return position;
	}

	public Scene getScene() {
		return scene;
	}

	@Override
	public final void draw(GraphicsContext gc) {
		gc.save();
		drawInternal(gc);
		gc.restore();
	}

	public abstract void drawInternal(GraphicsContext gc);
}
