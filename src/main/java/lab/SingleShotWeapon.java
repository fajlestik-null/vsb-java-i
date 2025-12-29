package lab;

import javafx.geometry.Point2D;

public class SingleShotWeapon implements WeaponStrategy, Soundable{
    @Override
    public void shoot(GameScene scene, Point2D position, double angle) {
        playSound("audio/singleShot.wav");
        Point2D bulletPos = calculateBullet(position, angle);

        Bullet b = new Bullet(scene, bulletPos, angle);
        scene.addEntity(b);
    }
}
