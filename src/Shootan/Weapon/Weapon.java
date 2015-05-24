package Shootan.Weapon;

import Shootan.Bullets.AbstractBullet;
import Shootan.Units.Unit;

public abstract class Weapon {

    protected Unit owner;

    public Weapon(Unit owner) {
        this.owner = owner;
    }

    protected abstract float getShotDelay();

    private float lashShotTime=0;

    public float getTimeToNextShot() {
        if (lashShotTime<0) return 0;
        return lashShotTime/getShotDelay();
    }

    public AbstractBullet getNewBullet(float dt) {
        if (lashShotTime>0) {
            lashShotTime-=dt;
        }

        if (wannaShot) {

            if (lashShotTime<=0) {
                if (getIsInSingleShotMode()) {
                    wannaShot = false;
                }
                lashShotTime=getShotDelay();
                return shot();
            }
        }
        return null;
    }

    protected abstract boolean getIsInSingleShotMode();

    private boolean wannaShot=false;

    public void setWannaShot(boolean yeah) {
        wannaShot=yeah;
    }

    protected abstract AbstractBullet shot();

}
