package lab;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Buoy extends Rock {

    private Image image;

    public Buoy(GameScene gameScene) {
		super(gameScene);
        image = new Image(Buoy.class.getResourceAsStream("buoy-small.png"));
        size = new Dimension2D(image.getWidth(), image.getHeight());
	}

	@Override
	public void drawInternal(GraphicsContext gc) {
        rotate(gc);
		gc.drawImage(image, position.getX(), position.getY());
	}

}
