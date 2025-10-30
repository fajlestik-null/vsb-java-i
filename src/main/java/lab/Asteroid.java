package lab;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.Random;

public class Asteroid extends WorldEntity{
    private double rotationSpeed;
    private double points;
    private double size;

    public Asteroid(GameScene gameScene, Point2D position, double size){
        super(gameScene,position ,new AsteroidFactory().createAsteroid(size));

        this.size = size;

        Random rand = new Random();
        double speed = 20 + rand.nextDouble() * 40;
        double angle = rand.nextDouble() * Math.PI * 2;
        velocity = new Point2D(Math.cos(angle) * speed, Math.sin(angle) * speed);

        rotationSpeed = (rand.nextDouble() - 0.5) * 60;

        points = speed + rotationSpeed + size;
    }

    @Override
    public Color getColor(){
        return Color.GRAY;
    }

    @Override
    public void simulate(double deltaTime) {
        move(deltaTime);

        shape.setRotate(shape.getRotate() + rotationSpeed * deltaTime);
    }

    @Override
    public void hitBy(Collisionable other) {
        if(other instanceof Player player) {
            gameScene.removeEntity(this);
            player.decreaseLives();
            player.fireLives();
        }
        if(other instanceof Bullet bullet) {
            gameScene.removeEntity(this);
            gameScene.removeEntity(bullet);
            gameScene.getPlayer().addScore(points);
            gameScene.getPlayer().fireScore();
        }
    }

    public double getSize() {
        return size;
    }

//    public double getPoints(){
//        return points;
//    }
}
