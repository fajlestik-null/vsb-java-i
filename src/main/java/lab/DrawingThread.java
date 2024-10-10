package lab;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawingThread extends AnimationTimer {

	private final Canvas canvas;
	private final GraphicsContext gc;
	private Scene scene;
	private int frameCount = 0;
	private  int fps = 0;
	private long lastTime;
	private long lastSecond = 0;

	public DrawingThread(Canvas canvas) {
		this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();
		scene = new Scene(canvas.getWidth(), canvas.getHeight());
		lastTime = System.nanoTime();
	}

	/**
	  * Draws objects into the canvas. Put you code here. 
	 */
	@Override
	public void handle(long now) {
		long timeDelta = now - lastTime;
		scene.draw(gc);
		scene.simulate(timeDelta);
		long currentSecond = now / 1_000_000_000;
		if(lastSecond == currentSecond) {
			frameCount ++;
		} else {
			lastSecond = currentSecond;
			fps = frameCount;
			frameCount = 0;
		}
		lastTime = now;
		//print FPS
		gc.setStroke(Color.RED);
		gc.strokeText(Integer.toString(fps), 10, 30);
		gc.strokeText(Double.toString(1_000_000_000d/timeDelta), 10, 50);
	}

}
