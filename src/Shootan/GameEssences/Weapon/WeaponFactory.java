package Shootan.GameEssences.Weapon;

import Shootan.GameEssences.Units.Unit;

public class WeaponFactory {

    public static Weapon create(int type, Unit owner) {
        switch (type) {
            case 0: return new FireThrower(owner);
            case 1: return new MP40(owner);
            case 2: return new RockerLauncher(owner);
            case 3: return new SniperRifle(owner);
        }
        return null;
    }

}
