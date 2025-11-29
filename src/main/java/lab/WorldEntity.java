package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class WorldEntity implements DrawableSimulable, Warpable, Collisionable, Serializable {

	protected GameScene gameScene;

    protected transient Point2D position;
    protected transient Point2D velocity;
    protected transient Polygon shape;

    protected double rotationFactor = 3;
    protected double angularVelocity = 0;
    protected double rotationPower = 200;

	protected WorldEntity(GameScene gameScene, Point2D position, Polygon shape) {
		this.gameScene = gameScene;
		this.position = position;
        this.velocity = new Point2D(0, 0);
        this.shape = shape;
        this.shape.setTranslateX(position.getX());
        this.shape.setTranslateY(position.getY());
	}

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        out.writeDouble(position.getX());
        out.writeDouble(position.getY());

        out.writeDouble(velocity.getX());
        out.writeDouble(velocity.getY());

        out.writeDouble(angularVelocity);

        out.writeDouble(shape.getRotate());
        List<Double> points = new ArrayList<>(shape.getPoints());
        out.writeObject(points);
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Načte netransientní data

        double posX = in.readDouble();
        double posY = in.readDouble();
        this.position = new Point2D(posX, posY);

        double velX = in.readDouble();
        double velY = in.readDouble();
        this.velocity = new Point2D(velX, velY);

        this.angularVelocity = in.readDouble();

        double rotate = in.readDouble();
        List<Double> points = (List<Double>) in.readObject();

        this.shape = new Polygon();
        this.shape.getPoints().addAll(points);
        this.shape.setRotate(rotate);

        this.shape.setTranslateX(posX);
        this.shape.setTranslateY(posY);
    }

    public Point2D getPosition() {
        return position;
    }

    public Polygon getShape() {
        return shape;
    }

    public void rotateLeft(double deltaTime) {
        this.angularVelocity -= rotationPower * deltaTime;
    }

    public void rotateRight(double deltaTime) {
        this.angularVelocity += rotationPower * deltaTime;
    }

    public void applyRotationPhysics(double deltaTime) {
        this.shape.setRotate(this.shape.getRotate() + angularVelocity * deltaTime);

        //drag - čím vyšší číslo tím měnší drag
        double dragFactor = Math.pow(0.95, deltaTime * 60);
        this.angularVelocity *= dragFactor;

        if (Math.abs(angularVelocity) < 0.5) {
            angularVelocity = 0;
        }
    }

    public void move(double deltaTime){
        //anti-spiking - měl by řešit problém s fps
        double maxDelta = 0.05;
        deltaTime = Math.min(deltaTime, maxDelta);

        position = position.add(velocity.multiply(deltaTime));
        position = warp(gameScene, position);

        applyRotationPhysics(deltaTime);

        shape.setTranslateX(position.getX());
        shape.setTranslateY(position.getY());
    }

    public void accelerate(double deltaTime) {
        double angle = Math.toRadians(shape.getRotate());
        double thrustPower = 200;

        Point2D thrust = new Point2D(
            Math.cos(angle) * thrustPower * deltaTime,
            Math.sin(angle) * thrustPower * deltaTime
        );

        velocity = velocity.add(thrust);
    }

    public void applyDrag(double deltaTime) {
        double dragFactor = Math.pow(0.995, deltaTime * 60); // scale drag by frame
        velocity = velocity.multiply(dragFactor);

        if (velocity.magnitude() < 0.5) {
            velocity = new Point2D(0, 0);
        }
    }

    public Polygon getBoundingBox() {
        Polygon bounds = new Polygon();
        double angle = Math.toRadians(shape.getRotate());

        for (int i = 0; i < shape.getPoints().size(); i += 2) {
            double x = shape.getPoints().get(i);
            double y = shape.getPoints().get(i + 1);

            double rotatedX = x * Math.cos(angle) - y * Math.sin(angle);
            double rotatedY = x * Math.sin(angle) + y * Math.cos(angle);

            rotatedX += position.getX();
            rotatedY += position.getY();

            bounds.getPoints().addAll(rotatedX, rotatedY);
        }

        return bounds;
    }

    @Override
    public boolean intersect(Polygon other) {
        return (Shape.intersect(this.getShape(), other)).getBoundsInLocal().getWidth() != -1;
    }

	@Override
	public final void draw(GraphicsContext gc) {
		gc.save();
		drawInternal(gc);
		gc.restore();
	}

    public void drawInternal(GraphicsContext gc) {
        gc.translate(position.getX(), position.getY());

        gc.rotate(shape.getRotate());

        double[] xPoints = new double[shape.getPoints().size() / 2];
        double[] yPoints = new double[shape.getPoints().size() / 2];

        for (int i = 0; i < shape.getPoints().size(); i += 2) {
            xPoints[i / 2] = shape.getPoints().get(i);
            yPoints[i / 2] = shape.getPoints().get(i + 1);
        }

        //Draw only the outline - vector based asteroids
        gc.setStroke(Color.BLACK);
        gc.strokePolygon(xPoints, yPoints, xPoints.length);
        gc.setFill(getColor());
        gc.fillPolygon(xPoints, yPoints, xPoints.length);
    }

    public abstract Color getColor();

}
