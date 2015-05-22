package Shootan.Weapon;

import Shootan.Bullets.AbstractBullet;
import Shootan.Units.Unit;

public abstract class Weapon {

    protected Unit owner;

    public Weapon(Unit owner) {
        this.owner = owner;
    }



    public abstract AbstractBullet shot();

}
