package Shootan.Weapon;

import Shootan.Bullets.Bullet;
import Shootan.Bullets.BigBullet;
import Shootan.Units.Unit;

public class SniperRifle extends Weapon {

    public SniperRifle(Unit parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "SniperRifle";
    }

    @Override
    protected int getType() {
        return 3;
    }

    @Override
    protected float getShotDelay() {
        return 0.5f;
    }

    @Override
    protected int getDefaultBulletsNumber() {
        return 40;
    }

    @Override
    protected int getCageSize() {
        return 10;
    }

    @Override
    protected float getCageReloadDelay() {
        return 4f;
    }

    @Override
    protected boolean getIsInSingleShotMode() {
        return true;
    }

    @Override
    protected Bullet shot() {
        return new BigBullet(owner.getId(), owner.getX(), owner.getY(), owner.getViewAngle());
    }
}
