package lab;

import javafx.geometry.Point2D;

public class TripleShotWeapon implements Soundable, WeaponStrategy {
    @Override
    public void shoot(GameScene scene, Point2D position, double angle) {
        playSound("audio/tripleShot.mp3");

        createBullet(scene, position, angle);
        createBullet(scene, position, angle - 15);
        createBullet(scene, position, angle + 15);
    }

    private void createBullet(GameScene scene, Point2D position, double angle) {
        Point2D bulletPos = calculateBullet(position, angle);
        Bullet b = new Bullet(scene, bulletPos, angle);
        scene.addEntity(b);
    }
}
