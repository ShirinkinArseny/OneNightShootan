package Shootan.Weapon;

import Shootan.Bullets.AbstractBullet;
import Shootan.Units.Unit;

public abstract class Weapon {

    protected Unit owner;

    public Weapon(Unit owner) {
        this.owner = owner;
    }

    protected abstract long getShotDelayMilliseconds();

    private long lashShotTime=0;

    public AbstractBullet getNewBullet() {
        if (wannaShot) {

            if (System.currentTimeMillis()-lashShotTime>=getShotDelayMilliseconds()) {
                if (getIsInSingleShotMode()) {
                    wannaShot = false;
                }
                lashShotTime=System.currentTimeMillis();
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
