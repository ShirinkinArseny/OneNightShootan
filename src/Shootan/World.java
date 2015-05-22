package Shootan;

import java.util.ArrayList;

public abstract class World {

    public static final int getPotentialViewDistance=50;

    public abstract Block[][] getBlocks();

    public abstract ArrayList<Unit> getUnits();

    public abstract Unit getMe();

}