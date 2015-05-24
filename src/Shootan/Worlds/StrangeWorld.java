package Shootan.Worlds;

import Shootan.Blocks.Block;
import Shootan.Blocks.Brick;
import Shootan.Blocks.Floor;
import Shootan.Bullets.AbstractBullet;
import Shootan.Units.Human;
import Shootan.Units.Unit;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import static Shootan.Geometry.Utils.getQuadDistFromPointToLine;
import static Shootan.Geometry.Utils.getQuadIntersectsCircle;

public class StrangeWorld extends World {

    private static final int SIZE = 1000;

    private Unit me;
    private ArrayList<Unit> units = new ArrayList<>();
    private Block[][] blocks = new Block[SIZE][SIZE];
    private AtomicInteger[][] visibility = new AtomicInteger[SIZE][SIZE];
    private BiConsumer<Unit, AbstractBullet> onKilling;

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

        if (res>=2) res=2;

        if (res!=0) {

            float myAngle = getMe().getViewAngle();
            if (myAngle < 0) myAngle += 2 * Math.PI;

            float blockAngle = (float) Math.atan2(blockY - cameraBlockY, blockX - cameraBlockX);
            if (blockAngle < 0) blockAngle += 2 * Math.PI;

            float angle = Math.abs(myAngle - blockAngle);
            angle = (float) Math.min(angle, 2 * Math.PI - angle);

            float res2;
            if (angle < Math.PI / 3) res2 = 2;
            else if (angle < Math.PI * 2 / 5) res2 = 1;
            else
                res2 = 0;

            if (res2 == 0) {
                res = 0;
            } else {
                if (res2 == 1) {
                    res--;
                }
            }
        }

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

    private void updateBulletsCollisions(float dt) {
        ArrayList<AbstractBullet> aliveAfterUnitsBullets = new ArrayList<>(bullets.size());
        for (AbstractBullet b : bullets) {
            boolean alive = !b.hasFallen();

            float newX=b.getX()+b.getDX()*dt;
            float newY=b.getY()+b.getDY()*dt;

            float startX=Math.min(b.getX(), newX);
            float startY=Math.min(b.getY(), newY);
            float endX=Math.max(b.getX(), newX);
            float endY=Math.max(b.getY(), newY);

            if (alive) {
                for (Unit u : units) {
                    if (u.getId() != b.getAuthor()) {

                        if (u.getX()+u.getRadius()>=startX && u.getX()-u.getRadius()<=endX
                                &&
                                u.getY()+u.getRadius()>=startY && u.getY()-u.getRadius()<=endY
                                ) {
                            if (getQuadDistFromPointToLine(b.getX(), b.getY(), newX, newY, u.getX(), u.getY()) < u.getRadiusQuad()) {
                                alive = false;
                                u.damage(b.getDamage());
                                if (u.getHealth()<=0) {
                                    onKilling.accept(u, b);
                                }
                                ArrayList<AbstractBullet> explosion = b.explode();
                                if (explosion != null) {
                                    aliveAfterUnitsBullets.addAll(explosion);
                                }
                                break;
                            }
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
        aliveAfterUnitsBullets
                .stream()
                .filter(b -> b.getX() > 0 && b.getY() > 0)
                .forEach(b -> {
                    if (getHasHardBlocks(
                            b.getBlockX(),
                            b.getBlockY(),
                            b.getBlockX() + b.getDX() * dt,
                            b.getBlockY() + b.getDY() * dt)
                            ) {
                        ArrayList<AbstractBullet> explosion = b.explode();
                        if (explosion != null) {
                            bullets.addAll(explosion);
                        }
                    } else {
                        bullets.add(b);
                    }
                });
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

    private void moveUnits(float deltaTime) {
        for (Unit u : units) {
            if (u.isMoving()) {

                float newX = u.getX() + u.getDx() * deltaTime;
                float newY = u.getY() + u.getDy() * deltaTime;

                int fromX = (int) (newX - u.getRadius() - 1);
                int fromY = (int) (newY - u.getRadius() - 1);
                int toX = (int) (newX + u.getRadius() + 1);
                int toY = (int) (newY + u.getRadius() + 1);

                boolean acceptDx = true;
                boolean acceptDy = true;

                for (int i = fromX; i < toX; i++) {
                    for (int j = fromY; j < toY; j++) {
                        if (blocks[j][i].getIsHard()) {

                            if (getQuadIntersectsCircle(i, j, newX, newY, u.getRadiusQuad())) {

                                if (acceptDx) {
                                    if (getQuadIntersectsCircle(i, j, newX, u.getY(), u.getRadiusQuad())) {
                                        acceptDx = false;
                                    }
                                }

                                if (acceptDy) {
                                    if (getQuadIntersectsCircle(i, j, u.getX(), newY, u.getRadiusQuad())) {
                                        acceptDy = false;
                                    }
                                }
                            }
                            if (!acceptDx && !acceptDy) {
                                break;
                            }
                        }
                    }
                    if (!acceptDx && !acceptDy) {
                        break;
                    }
                }

                if (acceptDx || acceptDy) {
                    u.move(deltaTime, acceptDx, acceptDy);
                }
            }
        }
    }

    private void updateAlifeUnits() {

        for (int i=0; i<units.size(); i++) {
            if (units.get(i).getHealth()<=0) {

                if (units.get(i).getId()==me.getId()) {
                    me=new Human(10, 10);
                    units.add(me);
                    //RESPAWN
                }

                units.remove(i);
            }
        }

    }

    @Override
    public void update(float deltaTime) {
        moveUnits(deltaTime);
        checkForNewShotings(deltaTime);
        updateBulletsCollisions(deltaTime);
        moveBullets(deltaTime);
        updateAlifeUnits();
    }

    public StrangeWorld(BiConsumer<Unit, AbstractBullet> onKilling, Runnable onVisibilityMapUpdated) {
        this.onKilling = onKilling;
        this.me = new Human(10, 10);
        units.add(me);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                blocks[i][j] = new Brick();
                visibility[i][j]=new AtomicInteger(0);
            }
        }

        for (int i = 5; i < 15; i++) {
            for (int j = 5; j < 15; j++) {
                blocks[i][j] = new Floor();
            }
        }

        for (int i = 40; i < 50; i++) {
            for (int j = 5; j < 15; j++) {
                blocks[i][j] = new Floor();
            }
        }


        for (int i = 5; i < 15; i++) {
            for (int j = 40; j < 50; j++) {
                blocks[i][j] = new Floor();
            }
        }


        for (int i = 40; i < 50; i++) {
            for (int j = 40; j < 50; j++) {
                blocks[i][j] = new Floor();
            }
        }

        for (int j = 5; j < 40; j++) {
            blocks[5][j] = new Floor();
            blocks[6][j] = new Floor();
            blocks[7][j] = new Floor();
        }

        for (int j = 5; j < 40; j++) {
            blocks[j][5] = new Floor();
            blocks[j][6] = new Floor();
            blocks[j][7] = new Floor();
        }

        for (int j = 5; j < 40; j++) {
            blocks[47][j] = new Floor();
            blocks[48][j] = new Floor();
            blocks[49][j] = new Floor();
        }

        for (int j = 5; j < 40  ; j++) {
            blocks[j][47] = new Floor();
            blocks[j][48] = new Floor();
            blocks[j][49] = new Floor();
        }


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                updateVisibilityMap(getMe().getX(), getMe().getY());
            }
        }, 20, 20);


        new Thread(() -> {
            while (true) {
                onVisibilityMapUpdated.run();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}