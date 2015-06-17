package Shootan.GameEssences.Units;

import Shootan.GameEssences.Weapon.Weapon;

import java.util.List;

public interface ControlledUnit extends VisibleUnit {

    void setMotionAngle(float angle);

    List<Weapon> getWeapons();

    int getCurrentWeapon();

    void selectPreviousWeapon();

    void selectNextWeapon();

    void setWannaShot(boolean wannaShot);


}