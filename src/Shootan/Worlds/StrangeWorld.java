package Shootan.Worlds;

import Shootan.Blocks.Block;
import Shootan.Blocks.Brick;
import Shootan.Units.Unit;

import java.util.ArrayList;

public class StrangeWorld extends World {

    private Unit me;
    private ArrayList<Unit> units=new ArrayList<>();
    private Block[][] blocks=new Block[1000][1000];

    public Block getBlock(int x, int y) {
        return blocks[y][x];
    }

    @Override
    public ArrayList<Unit> getUnits() {
        return units;
    }

    public Unit getMe() {
        return me;
    }

    @Override
    public void update(float deltaTime) {


        for (Unit u: units) {
                u.move(deltaTime);
            /*if (COLLIDED) {
                u.unmove(deltaTime);
            }*/
        }

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
