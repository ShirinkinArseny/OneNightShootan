package Shootan.Weapon;

import Shootan.Bullets.AbstractBullet;
import Shootan.Bullets.Flame;
import Shootan.Bullets.SmallBullet;
import Shootan.Units.Unit;

public class FireThrower extends Weapon {

    public FireThrower() {
        super();
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
        return new Flame(owner.getId(), owner.getX(), owner.getY(), owner.getViewAngle());
    }
}
