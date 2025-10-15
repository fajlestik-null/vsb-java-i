package lab;

import java.util.Random;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class LochNess extends WorldEntity implements Collisionable {

	private static final Random RANDOM = new Random();

	private Point2D speed;
	private Image image;
    private double yLevel;

	public LochNess(GameScene gameScene) {
		super(gameScene, new Point2D(0, 0));
		image = new Image(LochNess.class.getResourceAsStream("LochNess.gif"));
		position = new Point2D(
            RANDOM.nextDouble(gameScene.getSize().getWidth() * 0.3, gameScene.getSize().getWidth()),
            RANDOM.nextDouble(gameScene.getSize().getHeight() * 0.5, gameScene.getSize().getHeight() - image.getHeight()));
		speed = new Point2D(-RANDOM.nextDouble(50, 150), 0);
        yLevel = position.getY();
	}

	@Override
	public void drawInternal(GraphicsContext gc) {
		gc.drawImage(image, position.getX(), position.getY());
		gc.strokeRect(position.getX(), position.getY(), image.getWidth(), image.getHeight());
	}

	@Override
	public void simulate(double deltaTime) {
		position = position.add(speed.multiply(deltaTime));
        if(position.getY() < yLevel) {
            speed= speed.add(0, 5);
        } else {
            speed= new Point2D(speed.getX(), 0);
        }
		if (position.getX() + image.getWidth() < 0) {
			position = new Point2D(gameScene.getSize().getWidth(), position.getY());
		}
		if (position.getX() > gameScene.getSize().getWidth() + 5) {
			position = new Point2D(this.gameScene.getSize().getWidth(), position.getY());
			speed = speed.multiply(-1);
		}
	}

	@Override
	public Rectangle2D getBoundingBox() {
		return new Rectangle2D(position.getX(), position.getY(), image.getWidth(), image.getHeight());
	}

	public void changeDirection() {
		speed = speed.multiply(-1);
	}

    public void jump() {
        speed = speed.add(0, -10);
    }

	@Override
	public boolean intersect(Rectangle2D another) {
		return getBoundingBox().intersects(another);
	}

	@Override
	public void hitBy(Collisionable another) {
		if (another instanceof Boat) {
			changeDirection();
		} else if(another instanceof Rock){
            jump();
        }

	}
}
