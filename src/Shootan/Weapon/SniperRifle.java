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
    protected long getShotDelayMilliseconds() {
        return 3000;
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
