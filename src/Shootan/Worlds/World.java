package Shootan.Worlds;

import Shootan.Blocks.Block;
import Shootan.Units.Unit;

import java.util.ArrayList;

public abstract class World {

    public static final int getPotentialViewDistance=50;

    public abstract Block[][] getBlocks();

    public abstract ArrayList<Unit> getUnits();

    public abstract Unit getMe();

    public void update(float deltaTime) {

    }

}