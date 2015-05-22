package Shootan.Worlds;

import Shootan.Blocks.Block;
import Shootan.Blocks.Brick;
import Shootan.Blocks.Floor;
import Shootan.Blocks.UnitBlock;
import Shootan.Units.Unit;

import java.util.ArrayList;

public class StrangeWorld extends World {

    private static final int SIZE = 1000;

    private Unit me;
    private ArrayList<Unit> units=new ArrayList<>();
    private Block[][] blocks=new Block[SIZE][SIZE];

    public Block getBlock(int x, int y) {
        return blocks[y][x];
    }

    public boolean isVisible(int x, int y) {
        return (x>=0 && x<SIZE && y>=0 && y<SIZE);
    }

    @Override
    public ArrayList<Unit> getUnits() {
        return units;
    }

    public Unit getMe() {
        return me;
    }

    private boolean possibleToMoveUnit(int x, int y) {
        return !getBlock(x,y).getIsHard();
    }

    private ArrayList<Block> highlighted = new ArrayList<>(10);
    private boolean isRightToMove(UnitBlock block) {
        //Удаляем подсветку
        for (Block b : highlighted)
            b.hiLight = false;
        highlighted.clear();
        //Получаем все ближайшие блоки
        int x0 = block.getBlockX(); int y0 = block.getBlockY();
        for (int x=x0-1; x<=x0+2; x++)
            for (int y=y0-1; y<=y0+2; y++)
                if (x>=0 && x<SIZE && y>=0 && y<SIZE && !(x==x0 && y==y0)) {
                    //Подсвечиваем
                    getBlock(x,y).hiLight=true;
                    highlighted.add(getBlock(x,y));
                }
        return true;
    }

    @Override
    public void update(float deltaTime) {


        for (Unit u: units) {
            UnitBlock newBlock = u.tryMove(deltaTime);
            if (newBlock!=null && possibleToMoveUnit(newBlock.getBlockX(),newBlock.getBlockY())) {
                isRightToMove(newBlock);
                u.applyMove(newBlock);
            }
        }

    }

    public StrangeWorld(Unit me) {
        this.me = me;
        units.add(me);

        for (int i=0; i<SIZE; i++) {
            for (int j=0; j<SIZE; j++) {
                blocks[i][j]=new Brick();
            }
        }

        for (int i=5; i<15; i++) {
            for (int j=5; j<15; j++) {
                blocks[i][j]=new Floor();
            }
        }

        for (int j=5; j<40; j++) {
            blocks[5][j]=new Floor();
            blocks[6][j]=new Floor();
        }

        for (int j=5; j<40; j++) {
            blocks[j][5]=new Floor();
            blocks[j][6]=new Floor();
        }

    }

}
