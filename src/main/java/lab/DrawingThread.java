package lab;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class DrawingThread extends AnimationTimer {

	private final Canvas canvas;
	private final GraphicsContext gc;
	private GameScene gameScene;
    private long lastFrame = 0;


	public DrawingThread(Canvas canvas, GameScene gameScene) {
            this.canvas = canvas;
            this.gc = canvas.getGraphicsContext2D();
            this.gameScene = gameScene;
	}

    /**
     * Draws objects into the canvas. Put you code here.
     */
    @Override
    public void handle(long now) {
        double delta = lastFrame == 0 ? 0 : (now - lastFrame) / 1_000_000_000D;
        lastFrame = now;
        gameScene.simulate(delta);
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gameScene.draw(gc);
        drawFps(delta);
    }

//    public GameScene getGameScene() {
//        return gameScene;
//    }

    private void drawFps(double delta) {
        int fps = calcFps(delta);
        gc.setFont(new Font("Arial", 30));
        gc.setFill(Color.WHITE);
        gc.fillText(String.format("FPS: %04d", fps), 10, canvas.getHeight() - 10);
    }

    private double fpsSum = 0;
    private double fpsCount = 0;
    private int averageFps = 0;

    private int calcFps(double delta) {
        fpsSum += 1 / delta;
        fpsCount += 1;
        if (fpsCount >= 100) {
            averageFps = (int) (fpsSum / fpsCount);
            fpsSum = 0;
            fpsCount = 0;
        }
        return averageFps;
    }
}
