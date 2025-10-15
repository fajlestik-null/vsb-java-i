package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Background extends WorldEntity {

	private Point2D[] imagePosition;
	private Image[] image;
	private Point2D speed;
	private static final double[] speedMultiplier = new double[] { 0.6, 0.8, 1, 1 };

	public Background(GameScene gameScene) {
		super(gameScene, new Point2D(0, 0));
		image = new Image[4];
		imagePosition = new Point2D[4];
		image[0] = new Image(Background.class.getResourceAsStream("cityfar400.png"));
		image[1] = new Image(Background.class.getResourceAsStream("citymid400.png"));
		image[2] = new Image(Background.class.getResourceAsStream("cityclose400.png"));
		image[3] = new Image(Background.class.getResourceAsStream("cityreflection400.png"));
		imagePosition[0] = new Point2D(0, image[0].getHeight() - gameScene.getSize().getHeight());
		imagePosition[1] = new Point2D(0, image[1].getHeight() - gameScene.getSize().getHeight());
		imagePosition[2] = new Point2D(0, image[2].getHeight() - gameScene.getSize().getHeight());
		imagePosition[3] = new Point2D(0, image[3].getHeight() - gameScene.getSize().getHeight());
		speed = new Point2D(100, 0);
	}

	@Override
	public void drawInternal(GraphicsContext gc) {
		gc.setFill(Color.DARKBLUE);
		gc.fillRect(0, 0, gameScene.getSize().getWidth(), gameScene.getSize().getHeight());
		for (int i = 0; i < image.length; i++) {
			double w = gameScene.getSize().getWidth();
			double x = imagePosition[i].getX();
			double realWidth = x + w <= image[i].getWidth() ? w : image[i].getWidth() - x;
			gc.drawImage(image[i], x, imagePosition[i].getY(), realWidth, gameScene.getSize().getHeight(), 0, 0, realWidth,
					gameScene.getSize().getHeight());
			if (x + w > image[i].getWidth()) {
				gc.drawImage(image[i], 0, imagePosition[i].getY(), w - realWidth, gameScene.getSize().getHeight(),
						image[i].getWidth() - imagePosition[i].getX(), 0, w - realWidth, gameScene.getSize().getHeight());
			}
		}
	}

	@Override
	public void simulate(double deltaTime) {
		Point2D baseSpeed = speed.multiply(deltaTime);
		for (int i = 0; i < image.length; i++) {
			imagePosition[i] = imagePosition[i].add(baseSpeed.multiply(speedMultiplier[i]));
			if (imagePosition[i].getX() > image[i].getWidth()) {
				imagePosition[i] = new Point2D(0, imagePosition[i].getY());
			}
		}
	}
}
