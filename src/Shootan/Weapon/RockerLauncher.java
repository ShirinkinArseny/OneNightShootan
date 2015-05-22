package Shootan.Weapon;

import Shootan.Bullets.AbstractBullet;
import Shootan.Bullets.Rocket;
import Shootan.Units.Unit;

public class RockerLauncher extends Weapon {

    public RockerLauncher(Unit owner) {
        super(owner);
    }

    @Override
    protected long getShotDelayMilliseconds() {
        return 1000;
    }

    @Override
    protected boolean getIsInSingleShotMode() {
        return true;
    }

    @Override
    protected AbstractBullet shot() {
        return new Rocket(owner.getId(), owner.getX(), owner.getY(), owner.getViewAngle());
    }
}
