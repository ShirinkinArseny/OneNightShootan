package Shootan.Bullets;

public class BulletFactory {

    public static Bullet create(int type) {
        switch (type) {
            case 0: return new BigBullet();
            case 1: return new BrokenStuff();
            case 2: return new Flame();
            case 3: return new Rocket();
            case 4: return new SmallBullet();
            case 5: return new SmallFlame();
        }
        return null;
    }

}
