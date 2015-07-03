package Shootan.Worlds;

import Shootan.GameEssences.Blocks.Block;
import Shootan.GameEssences.Blocks.Brick;
import Shootan.GameEssences.Blocks.Floor;
import Shootan.GameEssences.Bullets.Bullet;
import Shootan.GameEssences.Units.Human;
import Shootan.GameEssences.Units.Unit;

import java.util.ArrayList;
import java.util.List;

import static Shootan.Utils.GeometryUtils.getQuadDistFromPointToLine;
import static Shootan.Utils.GeometryUtils.getQuadIntersectsCircle;

public abstract class StrangeWorld extends AbstractWorld implements VisibleWorld{

    public static final int SIZE = 100;

    protected final ArrayList<Unit> units = new ArrayList<>();
    protected Block[][] blocks = new Block[SIZE][SIZE];
    protected boolean[][] visibility = new boolean[SIZE][SIZE];
    protected final ArrayList<Bullet> bullets = new ArrayList<>();

    protected void onShot(Unit bullet, Bullet shoter) {}

    protected void onHit(Unit killedUnit, Bullet killer) {}

    protected void onKilled(Unit killedUnit, Bullet killer) {}

    public Block getVisibleBlock(int x, int y) {
        if (!isVisible(x, y)) return null;
        return blocks[y][x];
    }

    public boolean isVisible(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE && visibility[y][x];
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

    private boolean updateBlockVisibility(float cameraBlockX, float cameraBlockY, int blockX, int blockY) {


        boolean isVisible1=!getHasHardBlocksIgnoringStart(blockX, blockY, cameraBlockX, cameraBlockY);
        if (isVisible1) return true;
        boolean isVisible2=!getHasHardBlocksIgnoringStart(blockX+1, blockY, cameraBlockX, cameraBlockY);
        if (isVisible2) return true;
        boolean isVisible3=!getHasHardBlocksIgnoringStart(blockX, blockY+1, cameraBlockX, cameraBlockY);
        if (isVisible3) return true;
        boolean isVisible4=!getHasHardBlocksIgnoringStart(blockX+1, blockY+1, cameraBlockX, cameraBlockY);
        return isVisible4;

        /*if (res!=0) {

            float myAngle = getMe().getViewAngle();
            if (myAngle < 0) myAngle += 2 * Math.PI;

            float blockAngle = (float) Math.atan2(blockY - cameraBlockY, blockX - cameraBlockX);
            if (blockAngle < 0) blockAngle += 2 * Math.PI;

            float angle = Math.abs(myAngle - blockAngle);
            angle = (float) Math.min(angle, 2 * Math.PI - angle);

            int res2;
            if (angle < Math.PI / 3) res2 = 4;
            else if (angle < Math.PI * 2 / 5) res2 = 3;
            else if (angle < Math.PI * 3 / 7) res2 = 2;
            else if (angle < Math.PI * 1 / 2) res2 = 1;
            else
                res2 = 0;

            if (res2 == 0) {
                res = 0;
            } else {
                res=res2*res/4;
            }
        }*/
    }

    protected void updateVisibilityMap(float cameraX, float cameraY) {

        for (int i=0; i<SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                visibility[i][j] = false;
            }
        }

        int cameraXI= (int) cameraX;
        int cameraYI= (int) cameraY;


            for (int i=Math.max(0, cameraXI- potentialViewDistance); i<Math.min(SIZE, cameraXI + potentialViewDistance); i++) {
                for (int j=Math.max(0, cameraYI- potentialViewDistance); j < Math.min(SIZE, cameraYI + potentialViewDistance); j++) {
                    visibility[j][i]=(updateBlockVisibility(cameraX, cameraY, i, j));
                }
            }


    }

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
                                } else {
                                    onHit(u, b);
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
                onShot(u, b);
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

    protected abstract void additionalUpdate(float deltaTime);

    @Override
    public void update(float deltaTime) {
        moveUnits(deltaTime);
        checkForNewShotings(deltaTime);
        updateBulletsCollisions(deltaTime);
        moveBullets(deltaTime);
        additionalUpdate(deltaTime);
    }

    public StrangeWorld() {

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                blocks[i][j] = new Brick();
                visibility[i][j]=false;
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