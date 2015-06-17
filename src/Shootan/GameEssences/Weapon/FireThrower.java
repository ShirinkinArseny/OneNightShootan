package Shootan.GameEssences.Weapon;

import Shootan.GameEssences.Bullets.Bullet;
import Shootan.GameEssences.Bullets.Flame;
import Shootan.GameEssences.Units.Unit;

public class FireThrower extends Weapon {

    public FireThrower(Unit parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "FireThrower";
    }

    @Override
    protected int getType() {
        return 0;
    }

    @Override
    protected int getCageSize() {
        return 100;
    }

    @Override
    protected float getCageReloadDelay() {
        return 10f;
    }


    @Override
    protected float getShotDelay() {
        return 0.1f;
    }

    @Override
    protected int getDefaultBulletsNumber() {
        return 1000;
    }

    @Override
    protected boolean getIsInSingleShotMode() {
        return false;
    }

    @Override
    protected Bullet shot() {
        return new Flame(owner.getId(), owner.getX(), owner.getY(), owner.getViewAngle(), haveBulletsInCage%6);
    }
}
