package lab;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

public abstract class WorldEntity implements DrawableSimulable {

	protected GameScene gameScene;
	protected Point2D position;

	protected WorldEntity(GameScene gameScene, Point2D position) {
		this.gameScene = gameScene;
		this.position = position;
	}

	public Point2D getPosition() {
		return position;
	}

	public GameScene getGameScene() {
		return gameScene;
	}

	@Override
	public final void draw(GraphicsContext gc) {
		gc.save();
		drawInternal(gc);
		gc.restore();
	}

	public abstract void drawInternal(GraphicsContext gc);
}
