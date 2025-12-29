package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Asteroid extends WorldEntity implements Scorable {
    private final double rotationSpeed;
    protected double points;
    private final double size;
    private static final double MIN_SPLIT_SIZE = 20.0;
    private static final Random RANDOM = new Random();
    private transient List<Polygon> craters = new ArrayList<>();

    public Asteroid(GameScene gameScene, Point2D position, double size){
        super(gameScene,position ,new AsteroidFactory().createAsteroid(size));

        this.size = size;

        Random rand = new Random();
        double speed = 20 + rand.nextDouble() * 40;
        double angle = rand.nextDouble() * Math.PI * 2;
        velocity = new Point2D(Math.cos(angle) * speed, Math.sin(angle) * speed);

        rotationSpeed = (rand.nextDouble() - 0.5) * 60;
        points = speed + rotationSpeed + size;

        AsteroidFactory factory = new AsteroidFactory();
        int craterCount = rand.nextInt(4);

        for (int i = 0; i < craterCount; i++) {
            double craterSize = size * (0.15 + rand.nextDouble() * 0.1);

            Polygon crater = factory.createCrater(craterSize);

            double dist = rand.nextDouble() * (size * 0.5);
            double dir = rand.nextDouble() * Math.PI * 2;

            crater.setTranslateX(Math.cos(dir) * dist);
            crater.setTranslateY(Math.sin(dir) * dist);

            craters.add(crater);
        }
    }

    @Override
    public Color getColor(){
        return Color.GRAY;
    }

    @Override
    public double getScore() {
        return this.points;
    }

    @Override
    public void drawInternal(GraphicsContext gc) {
        super.drawInternal(gc);

        gc.setFill(Color.BLACK);

        for (Polygon crater : craters) {
            double tx = crater.getTranslateX();
            double ty = crater.getTranslateY();

            double[] xPoints = new double[crater.getPoints().size() / 2];
            double[] yPoints = new double[crater.getPoints().size() / 2];

            for (int i = 0; i < crater.getPoints().size(); i += 2) {
                xPoints[i / 2] = crater.getPoints().get(i) + tx;
                yPoints[i / 2] = crater.getPoints().get(i + 1) + ty;
            }

            gc.fillPolygon(xPoints, yPoints, xPoints.length);
        }
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
            createExplosion();
            player.decreaseLives();
            player.fireLives();
        }
        if(other instanceof Bullet bullet) {
            gameScene.removeEntity(this);
            gameScene.removeEntity(bullet);
            createExplosion();
            split();
        }
    }

    private void split() {

        if (size < MIN_SPLIT_SIZE) {
            return;
        }

        if(RANDOM.nextDouble() > 0.5) {
            return;
        }

        int numberOfParts = 2 + RANDOM.nextInt(3);

        double newSize = this.size / numberOfParts;

        for (int i = 0; i < numberOfParts; i++) {
            Asteroid smallAsteroid;

            if (RANDOM.nextDouble() < 0.15)
            {
                smallAsteroid = new PowerUpAsteroid(gameScene, this.position, newSize);
            }
            else
            {
                smallAsteroid = new Asteroid(gameScene, this.position, newSize);
            }

            double angleOffset = (RANDOM.nextDouble() - 0.5) * Math.PI;
            double speedFactor = 1.0 + RANDOM.nextDouble();

            double currentAngle = Math.atan2(this.velocity.getY(), this.velocity.getX());
            double newAngle = currentAngle + angleOffset;
            double currentSpeed = this.velocity.magnitude();

            smallAsteroid.velocity = new Point2D(
                Math.cos(newAngle) * currentSpeed * speedFactor,
                Math.sin(newAngle) * currentSpeed * speedFactor
            );

            gameScene.addEntity(smallAsteroid);
        }
    }

    protected void createExplosion() {
        Explosion explosion = new Explosion(gameScene, this.position, this.size);
        gameScene.addEntity(explosion);
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        out.writeInt(craters.size());

        for (Polygon crater : craters) {
            out.writeDouble(crater.getTranslateX());
            out.writeDouble(crater.getTranslateY());

            List<Double> points = new ArrayList<>(crater.getPoints());
            out.writeObject(points);
        }
    }

    @Serial
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        this.craters = new ArrayList<>();
        int craterCount = in.readInt();

        for (int i = 0; i < craterCount; i++) {
            double tx = in.readDouble();
            double ty = in.readDouble();
            List<Double> points = (List<Double>) in.readObject();

            Polygon crater = new Polygon();
            crater.getPoints().addAll(points);
            crater.setTranslateX(tx);
            crater.setTranslateY(ty);

            this.craters.add(crater);
        }
    }

    public double getSize() {
        return size;
    }
}
