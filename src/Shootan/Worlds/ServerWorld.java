package Shootan.Worlds;

import Shootan.Blocks.Block;
import Shootan.Blocks.Brick;
import Shootan.Blocks.Floor;
import Shootan.Bullets.Bullet;
import Shootan.Units.Human;
import Shootan.Units.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static Shootan.Utils.ByteUtils.twoBytesToUInt;
import static Shootan.Utils.ByteUtils.uIntToBytes;
import static Shootan.Utils.GeometryUtils.getQuadDistFromPointToLine;
import static Shootan.Utils.GeometryUtils.getQuadIntersectsCircle;

public class ServerWorld extends World {

    private static final int SIZE = 1000;

    private CopyOnWriteArrayList<Unit> units = new CopyOnWriteArrayList<>();
    private Block[][] blocks = new Block[SIZE][SIZE];
    private AtomicInteger[][] visibility = new AtomicInteger[SIZE][SIZE];
    private CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<>();




    public Block getBlock(int x, int y) {
        if (x>=0 && x<SIZE && y>=0 && y<=SIZE)
        return blocks[y][x];
        return null;
    }

    public int isVisible(int x, int y) {
        if (!(x >= 0 && x < SIZE && y >= 0 && y < SIZE)) return 0;
        return visibility[y][x].get();
    }

    public ArrayList<Byte> createWorldDump() {
        ArrayList<Byte> res=new ArrayList<>();
        res.addAll(uIntToBytes(units.size()));
        for (Unit u: units)
            res.addAll(u.fullSerialise());
        res.addAll(uIntToBytes(bullets.size()));
        for (Bullet b: bullets)
            res.addAll(b.serialize());
        return res;
    }

    public void acceptUnitChangedState(ArrayList<Byte> data) {
        int id=twoBytesToUInt(data.get(0), data.get(1));
        for (Unit u: units) {
            if (u.getId()==id) {
                u.deserializeState(data);
                break;
            }
        }
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

    @Override
    public List<Bullet> getBullets() {
        return bullets;
    }


    @Override
    public List<Unit> getUnits() {
        return units;
    }

    private void updateBulletsCollisions(float dt) {
        ArrayList<Bullet> aliveAfterUnitsBullets = new ArrayList<>(bullets.size());
        for (Bullet b : bullets) {
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
                                    //onKilling.accept(u, b);
                                }
                                ArrayList<Bullet> explosion = b.explode();
                                if (explosion != null) {
                                    aliveAfterUnitsBullets.addAll(explosion);
                                }
                                break;
                            }
                        }
                    }
                }
            } else {
                ArrayList<Bullet> explosion = b.explode();
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
                        ArrayList<Bullet> explosion = b.explode();
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
            Bullet b = u.getWeapon().getNewBullet(dt);
            if (b != null) {
                bullets.add(b);
            }
        }
    }

    private void moveBullets(float dt) {
        for (Bullet b: bullets) {
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

                Unit u=new Human(10, 10);
                u.setId(units.get(i).getId());
                units.add(u);

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

    public ServerWorld() {

        for (int i=0; i<10; i++)
        units.add(new Human(10, 10));

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                blocks[i][j] = new Brick();
                visibility[i][j]=new AtomicInteger(4);
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

    }

}