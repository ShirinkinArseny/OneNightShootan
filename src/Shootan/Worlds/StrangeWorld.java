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

import static Shootan.Utils.GeometryUtils.getQuadDistFromPointToLine;
import static Shootan.Utils.GeometryUtils.getQuadIntersectsCircle;

public abstract class StrangeWorld extends World {

    public static final int SIZE = 100;

    protected CopyOnWriteArrayList<Unit> units = new CopyOnWriteArrayList<>();
    protected Block[][] blocks = new Block[SIZE][SIZE];
    protected AtomicInteger[][] visibility = new AtomicInteger[SIZE][SIZE];
    protected CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<>();

    public void onKilled(Unit killedUnit, Bullet killer) {}

    @Override
    public List<Bullet> getBullets() {
        return bullets;
    }


    @Override
    public List<Unit> getUnits() {
        return units;
    }

    public Block getBlock(int x, int y) {
        if (x>=0 && x<SIZE && y>=0 && y<=SIZE)
            return blocks[y][x];
        return null;
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

    protected void updateBulletsCollisions(float dt) {
        ArrayList<Bullet> aliveAfterUnitsBullets = new ArrayList<>(bullets.size());
        for (Bullet b : bullets) {
            boolean alive = !b.hasFallen();

            if (alive) {

                float newX=b.getX()+b.getDX()*dt;
                float newY=b.getY()+b.getDY()*dt;

                float startX=Math.min(b.getX(), newX);
                float startY=Math.min(b.getY(), newY);
                float endX=Math.max(b.getX(), newX);
                float endY=Math.max(b.getY(), newY);

                for (Unit u : units) {
                    if (u.getId() != b.getAuthor() || b.getIsSecondary()) {

                        if (u.getX()+u.getRadius()>=startX && u.getX()-u.getRadius()<=endX
                                &&
                                u.getY()+u.getRadius()>=startY && u.getY()-u.getRadius()<=endY
                                ) {
                            if (getQuadDistFromPointToLine(b.getX(), b.getY(), newX, newY, u.getX(), u.getY()) < u.getRadiusQuad()) {
                                alive = false;
                                u.damage(b.getDamage());
                                if (u.getHealth()<=0) {
                                    onKilled(u, b);
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

    protected void checkForNewShotings(float dt) {
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

                int fromX = Math.max(0, (int) (newX - u.getRadius() - 1));
                int fromY = Math.max(0, (int) (newY - u.getRadius() - 1));
                int toX = Math.min(SIZE - 1, (int) (newX + u.getRadius() + 1));
                int toY = Math.min(SIZE-1, (int) (newY + u.getRadius() + 1));

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

    protected void updateAlifeUnits() {
            for (int i = 0; i < units.size(); i++) {
                if (units.get(i).getHealth() <= 0) {

                    Unit u = new Human(10, 10);
                    u.setId(units.get(i).getId());
                    units.remove(i);
                    units.add(u);

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

    public StrangeWorld() {
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

        for (int i=10; i<41; i++) {
            blocks[i-1][i-1] = new Floor();
            blocks[i-1][i] = new Floor();
            blocks[i-1][i+1] = new Floor();

            blocks[i][i] = new Floor();

            blocks[i+1][i-1] = new Floor();
            blocks[i+1][i] = new Floor();
            blocks[i+1][i+1] = new Floor();


            blocks[i-1][53-i+1] = new Floor();
            blocks[i-1][53-i] = new Floor();
            blocks[i-1][53-i-1] = new Floor();

            blocks[i][53-i] = new Floor();

            blocks[i+1][53-i+1] = new Floor();
            blocks[i+1][53-i] = new Floor();
            blocks[i+1][53-i-1] = new Floor();
        }

        for (int i=22; i<32; i++) {
            for (int j = 22; j < 32; j++) {
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

        /*Random rnd=new Random();
        for (int i=5; i<50; i++) {
            for (int j=5; j<50; j++) {
                if (rnd.nextInt(100)<96) {
                    blocks[j][i] = new Floor();
                }
            }
        }*/

    }

}