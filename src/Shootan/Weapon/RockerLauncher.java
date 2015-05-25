package Shootan.Weapon;

import Shootan.Bullets.Bullet;
import Shootan.Bullets.Rocket;
import Shootan.Units.Unit;

public class RockerLauncher extends Weapon {

    public RockerLauncher(Unit parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "RocketLauncher";
    }

    @Override
    protected int getType() {
        return 2;
    }

    @Override
    protected int getDefaultBulletsNumber() {
        return 10;
    }

    @Override
    protected int getCageSize() {
        return 1;
    }

    @Override
    protected float getCageReloadDelay() {
        return 10f;
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
    protected Bullet shot() {
        return new Rocket(owner.getId(), owner.getX(), owner.getY(), owner.getViewAngle());
    }
}
