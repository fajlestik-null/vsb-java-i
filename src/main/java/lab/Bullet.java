package lab;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Bullet extends WorldEntity{
    private double lifetime = 2.0;
    private double delay;

    public Bullet(GameScene gameScene, Point2D position, double angle)
    {
        super(gameScene, position, new Polygon(2, -2, 2, 2, -2, 2, -2, -2));
        this.shape.setRotate(angle);

        double speed = 500;
        this.velocity = new Point2D(Math.cos(Math.toRadians(angle)) * speed,Math.sin(Math.toRadians(angle)) * speed);
    }

    @Override
    public Color getColor() {
        return Color.RED;
    }

    @Override
    public void hitBy(Collisionable other) {
        //not used
    }

    @Override
    public void simulate(double deltaTime){
        move(deltaTime);
        applyDrag(deltaTime);

        lifetime -= deltaTime;
        delay += deltaTime;
            if (lifetime <= 0) {
            gameScene.removeEntity(this);
        }
    }

    public boolean isAfterDelay(){
        return delay > 0.5;
    }
}
