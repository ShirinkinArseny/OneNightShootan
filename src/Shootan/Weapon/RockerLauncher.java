package Shootan.Weapon;

import Shootan.Bullets.AbstractBullet;
import Shootan.Bullets.Rocket;
import Shootan.Units.Unit;

public class RockerLauncher extends Weapon {

    public RockerLauncher() {
        super();
    }

    @Override
    protected float getShotDelay() {
        return 1;
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
