package Shootan.Weapon;

import Shootan.Bullets.AbstractBullet;
import Shootan.Bullets.Rocket;
import Shootan.Bullets.SmallBullet;
import Shootan.Units.Unit;

public class MP40 extends Weapon {

    public MP40(Unit parent) {
        super(parent);
    }

    @Override
    protected float getShotDelay() {
        return 0.1f;
    }

    @Override
    protected boolean getIsInSingleShotMode() {
        return false;
    }

    @Override
    protected AbstractBullet shot() {
        return new SmallBullet(owner.getId(), owner.getX(), owner.getY(), owner.getViewAngle());
    }
}
