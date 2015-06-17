package Shootan.GameEssences.Weapon;

import Shootan.GameEssences.Bullets.Bullet;
import Shootan.GameEssences.Bullets.SmallBullet;
import Shootan.GameEssences.Units.Unit;

public class MP40 extends Weapon {

    public MP40(Unit parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "MP40";
    }

    @Override
    protected int getType() {
        return 1;
    }

    @Override
    protected int getDefaultBulletsNumber() {
        return 240;
    }

    @Override
    protected int getCageSize() {
        return 30;
    }

    @Override
    protected float getCageReloadDelay() {
        return 4f;
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
    protected Bullet shot() {
        return new SmallBullet(owner.getId(), owner.getX(), owner.getY(), owner.getViewAngle());
    }
}
