package Shootan.Weapon;

import Shootan.Bullets.AbstractBullet;
import Shootan.Bullets.Rocket;
import Shootan.Bullets.SmallBullet;
import Shootan.Units.Unit;

public class MP40 extends Weapon {

    public MP40(Unit owner) {
        super(owner);
    }

    @Override
    protected long getShotDelayMilliseconds() {
        return 100;
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
