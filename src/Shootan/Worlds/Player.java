package Shootan.Worlds;

public class Player {

    private int unitId;
    private String name;

    public int getUnitId() {
        return unitId;
    }

    public String getName() {
        return name;
    }

    public Player(String name, int unitId) {
        this.name=name;
        this.unitId=unitId;
    }
}
