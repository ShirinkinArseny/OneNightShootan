package Shootan.Worlds;

import Shootan.Blocks.Block;
import Shootan.Blocks.Brick;
import Shootan.Units.Unit;

import java.util.ArrayList;

public class StrangeWorld extends World {

    private Unit me;
    private ArrayList<Unit> units=new ArrayList<>();
    private Block[][] blocks=new Block[1000][1000];

    @Override
    public Block[][] getBlocks() {
        return blocks;
    }

    @Override
    public ArrayList<Unit> getUnits() {
        return units;
    }

    public Unit getMe() {
        return me;
    }

    public StrangeWorld(Unit me) {
        this.me = me;
        units.add(me);
        for (int i=0; i<1000; i++) {
            for (int j=0; j<1000; j++) {
                blocks[i][j]=new Brick();
            }
        }
    }

}
