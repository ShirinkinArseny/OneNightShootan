package Shootan.Weapon;

import Shootan.Bullets.AbstractBullet;
import Shootan.Bullets.BigBullet;
import Shootan.Bullets.Flame;
import Shootan.Units.Unit;

public class SniperRifle extends Weapon {

    public SniperRifle(Unit owner) {
        super(owner);
    }

    @Override
    protected float getShotDelay() {
        return 3;
    }

    @Override
    protected boolean getIsInSingleShotMode() {
        return false;
    }

    @Override
    protected AbstractBullet shot() {
        return new BigBullet(owner.getId(), owner.getX(), owner.getY(), owner.getViewAngle());
    }
}
