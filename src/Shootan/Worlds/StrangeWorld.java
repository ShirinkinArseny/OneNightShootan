package Shootan.Worlds;

import Shootan.Blocks.Block;
import Shootan.Blocks.Brick;
import Shootan.Blocks.Floor;
import Shootan.Blocks.UnitBlock;
import Shootan.Bullets.AbstractBullet;
import Shootan.Geometry.Utils;
import Shootan.Units.Unit;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static Shootan.Geometry.Utils.getQuadDistFromPointToLine;

public class StrangeWorld extends World {

    private static final int SIZE = 1000;

    private Unit me;
    private ArrayList<Unit> units = new ArrayList<>();
    private Block[][] blocks = new Block[SIZE][SIZE];
    private AtomicInteger[][] visibility = new AtomicInteger[SIZE][SIZE];

    public Block getBlock(int x, int y) {
        return blocks[y][x];
    }

    public int isVisible(int x, int y) {
        if (!(x >= 0 && x < SIZE && y >= 0 && y < SIZE)) return 0;
        return visibility[y][x].get();
    }

    private boolean getHasHardBlocks(float fromX, float fromY, float toX, float toY) {

        if (blocks[(int)fromY][(int)fromX].getIsHard()) return true;

        float idx=toX-fromX;
        float idy=toY-fromY;

        float length= (float) Math.sqrt(idx*idx+idy*idy);

        float dx=idx/length;
        float dy=idy/length;

        float x=fromX;
        float y=fromY;

        for (int i=0; i<length; i++) {
            if (blocks[(int)y][(int)x].getIsHard())
                return true;
            x+=dx;
            y+=dy;
        }
        return false;
    }

    private boolean getHasHardBlocksIgnoringStart(float fromX, float fromY, float toX, float toY) {
        float idx=toX-fromX;
        float idy=toY-fromY;

        float length= (float) Math.sqrt(idx*idx+idy*idy);

        float dx=idx/length;
        float dy=idy/length;

        float x=fromX;
        float y=fromY;

        for (int i=0; i<length; i++) {

            if (!((int)y==(int)fromY && (int)x==(int)fromX)) {
                if (blocks[(int)y][(int)x].getIsHard())
                    return true;
            }

            x+=dx;
            y+=dy;
        }
        return false;
    }

    private int updateBlockVisibility(float cameraBlockX, float cameraBlockY, int blockX, int blockY) {


        boolean isVisible1=!getHasHardBlocksIgnoringStart(blockX, blockY, cameraBlockX, cameraBlockY);

        boolean isVisible2=!getHasHardBlocksIgnoringStart(blockX+1, blockY, cameraBlockX, cameraBlockY);

        boolean isVisible3=!getHasHardBlocksIgnoringStart(blockX, blockY+1, cameraBlockX, cameraBlockY);

        boolean isVisible4=!getHasHardBlocksIgnoringStart(blockX+1, blockY+1, cameraBlockX, cameraBlockY);


        int res=
                (isVisible1?1:0)+
                (isVisible2?1:0)+
                (isVisible3?1:0)+
                (isVisible4?1:0);

        if (res>=2) return 2;
        return res;
    }

    private void updateVisibilityMap(float cameraX, float cameraY) {

        int cameraXI= (int) cameraX;
        int cameraYI= (int) cameraY;

        new Thread(() -> {
            for (int i=Math.max(0, cameraXI-getPotentialViewDistance); i<Math.min(SIZE, cameraXI + getPotentialViewDistance); i++) {
                for (int j=Math.max(0, cameraYI-getPotentialViewDistance); j < Math.min(SIZE, cameraYI + getPotentialViewDistance); j++) {
                    visibility[j][i].set(updateBlockVisibility(cameraX, cameraY, i, j));
                }
            }
        }).start();


    }


    private ArrayList<AbstractBullet> bullets = new ArrayList<>();

    @Override
    public ArrayList<AbstractBullet> getBullets() {
        return bullets;
    }


    @Override
    public ArrayList<Unit> getUnits() {
        return units;
    }

    public Unit getMe() {
        return me;
    }

    private boolean possibleToMoveUnit(int x, int y) {
        return !getBlock(x, y).getIsHard();
    }

    private ArrayList<Block> highlighted = new ArrayList<>(10);

    private boolean isRightToMove(UnitBlock block) {
        //Удаляем подсветку
        for (Block b : highlighted)
            b.hiLight = false;
        highlighted.clear();
        //Получаем все ближайшие блоки
        int x0 = block.getBlockX();
        int y0 = block.getBlockY();
        for (int x = x0 - 1; x <= x0 + 2; x++)
            for (int y = y0 - 1; y <= y0 + 2; y++)
                if (x >= 0 && x < SIZE && y >= 0 && y < SIZE && !(x == x0 && y == y0)) {
                    //Подсвечиваем
                    getBlock(x, y).hiLight = true;
                    highlighted.add(getBlock(x, y));
                }
        return true;
    }

    private void updateBulletsCollisions(float dt) {
        ArrayList<AbstractBullet> aliveAfterUnitsBullets = new ArrayList<>(bullets.size());
        for (AbstractBullet b : bullets) {
            boolean alive = !b.hasFallen();

            float newX=b.getX()+b.getDX()*dt;
            float newY=b.getY()+b.getDY()*dt;

            if (alive) {
                for (Unit u : units) {
                    if (u.getId() != b.getAuthor()) {

                        if (getQuadDistFromPointToLine(b.getX(), b.getY(), newX, newY, u.getX(), u.getY())<u.getRadiusQuad()) {
                            alive = false;
                            u.damage(b.getDamage());
                            ArrayList<AbstractBullet> explosion = b.explode();
                            if (explosion != null) {
                                aliveAfterUnitsBullets.addAll(explosion);
                            }
                            break;
                        }
                    }
                }
            } else {
                ArrayList<AbstractBullet> explosion = b.explode();
                if (explosion != null) {
                    aliveAfterUnitsBullets.addAll(explosion);
                }
            }

            if (alive) {
                aliveAfterUnitsBullets.add(b);
            }

        }
        bullets.clear();
        for (AbstractBullet b : aliveAfterUnitsBullets) {
            if (b.getX() > 0 && b.getY() > 0) {
                if (getHasHardBlocks(
                        b.getBlockX(),
                        b.getBlockY(),
                        b.getBlockX()+b.getDX() * dt,
                        b.getBlockY()+b.getDY() * dt)
                        ) {
                    ArrayList<AbstractBullet> explosion = b.explode();
                    if (explosion != null) {
                        bullets.addAll(explosion);
                    }
                } else {
                    bullets.add(b);
                }
            }
        }
    }

    private void checkForNewShotings(float dt) {
        for (Unit u : units) {
            AbstractBullet b = u.getWeapon().getNewBullet(dt);
            if (b != null) {
                bullets.add(b);
            }
        }
    }

    private void moveBullets(float dt) {
        for (AbstractBullet b: bullets) {
            b.move(dt);
        }
    }

    @Override
    public void update(float deltaTime) {

        int cameraBlockX=getMe().getBlockX();
        int cameraBlockY =getMe().getBlockY();

        for (Unit u : units) {
            UnitBlock newBlock = u.tryMove(deltaTime);
            if (newBlock != null && possibleToMoveUnit(newBlock.getBlockX(), newBlock.getBlockY())) {
                isRightToMove(newBlock);
                u.applyMove(newBlock);
            }
        }

        checkForNewShotings(deltaTime);
        updateBulletsCollisions(deltaTime);
        moveBullets(deltaTime);






    }

    public StrangeWorld(Unit me) {
        this.me = me;
        units.add(me);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                blocks[i][j] = new Brick();
                visibility[i][j]=new AtomicInteger(2);
            }
        }

        for (int i = 5; i < 15; i++) {
            for (int j = 5; j < 15; j++) {
                blocks[i][j] = new Floor();
            }
        }

        for (int j = 5; j < 40; j++) {
            blocks[5][j] = new Floor();
            blocks[6][j] = new Floor();
        }

        for (int j = 5; j < 40; j++) {
            blocks[j][5] = new Floor();
            blocks[j][6] = new Floor();
        }


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                updateVisibilityMap(getMe().getX(), getMe().getY());
            }
        }, 0, 200);
    }

}
