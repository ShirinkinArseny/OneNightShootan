package Shootan.GameEssences.Bullets;

public class BulletFactory {

    public static Bullet create(int type) {

        if (type==BigBullet.type) return new BigBullet();
        if (type==BrokenStuff.type) return new BrokenStuff();
        if (type==Flame.type) return new Flame();
        if (type==Rocket.type) return new Rocket();
        if (type==SmallBullet.type) return new SmallBullet();
        if (type==SmallFlame.type) return new SmallFlame();
        if (type==LongFlame.type) return new LongFlame();

        return null;
    }

}
