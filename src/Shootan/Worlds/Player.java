package Shootan.Worlds;

import Shootan.GameEssences.Units.Unit;

public class Player {

    private Unit unit;
    private String name;

    public Unit getUnit() {
        return unit;
    }

    public String getName() {
        return name;
    }

    public Player(String name, Unit unit) {
        this.name=name;
        this.unit=unit;
    }
}
