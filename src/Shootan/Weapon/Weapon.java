package Shootan.Weapon;

import Shootan.Bullets.Bullet;
import Shootan.Units.Unit;
import Shootan.Utils.IndexWrapper;

import java.util.ArrayList;
import java.util.List;

import static Shootan.Utils.ByteUtils.*;

public abstract class Weapon {

    protected Unit owner;

    public Weapon(Unit owner) {
        this.owner = owner;
        haveBullets=getDefaultBulletsNumber();
        haveBulletsInCage=getCageSize();
    }

    public ArrayList<Byte> serialize() {
        ArrayList<Byte> data=new ArrayList<>();
        data.addAll(uIntToBytes(getType()));
        data.addAll(uIntToBytes(haveBulletsInCage));
        data.addAll(uIntToBytes(haveBullets));
        data.addAll(coordToBytes(lashShotTime));
        return data;
    }

    public void deserialize(List<Byte> data, IndexWrapper index) {
        index.value+=2;
        haveBulletsInCage=twoBytesToUInt(data.get(index.value++), data.get(index.value++));
        haveBullets=twoBytesToUInt(data.get(index.value++), data.get(index.value++));
        lashShotTime=fourBytesToCoord(data.get(index.value++), data.get(index.value++), data.get(index.value++), data.get(index.value++));
    }

    public static Weapon createDeserialized(List<Byte> data, Unit owner, IndexWrapper index) {
        Weapon w=WeaponFactory.create(twoBytesToUInt(data.get(index.value), data.get(index.value+1)), owner);
        w.deserialize(data, index);
        return w;
    }

    public abstract String getName();

    protected abstract int getType();

    protected abstract float getShotDelay();

    protected abstract int getDefaultBulletsNumber();

    protected abstract int getCageSize();

    protected abstract float getCageReloadDelay();

    float lashShotTime=0;
    int haveBullets;

    public int getHaveBulletsInCage() {
        return haveBulletsInCage;
    }

    int haveBulletsInCage;

    public float getTimeToNextShot() {
        if (lashShotTime<0) return 0;
        if (haveBulletsInCage==0) {
            return lashShotTime/getCageReloadDelay();
        }
        return lashShotTime/getShotDelay();
    }

    public Bullet getNewBullet(float dt) {

        if (lashShotTime>0) {
            lashShotTime-=dt;
        }

        if (haveBullets>0 && lashShotTime<=0) {

            if (haveBulletsInCage == 0) {
                    haveBulletsInCage=Math.min(haveBullets, getCageSize());
            } else {

                if (owner.getWannaShot()) {

                    if (getIsInSingleShotMode()) {
                        owner.setWannaShot(false);
                    }
                    haveBullets--;
                    haveBulletsInCage--;
                    lashShotTime = haveBulletsInCage>0?getShotDelay():getCageReloadDelay();
                    return shot();
                }

            }


        }




        return null;
    }

    protected abstract boolean getIsInSingleShotMode();

    protected abstract Bullet shot();

    public int getHaveBullets() {
        return haveBullets;
    }
}
