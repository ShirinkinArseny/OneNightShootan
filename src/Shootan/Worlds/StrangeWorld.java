package Shootan.Worlds;

import Shootan.Blocks.Block;
import Shootan.Blocks.Brick;
import Shootan.Blocks.Floor;
import Shootan.Blocks.UnitBlock;
import Shootan.Bullets.AbstractBullet;
import Shootan.Units.Unit;

import java.util.ArrayList;

public class StrangeWorld extends World {

    private static final int SIZE = 1000;

    private Unit me;
    private ArrayList<Unit> units = new ArrayList<>();
    private Block[][] blocks = new Block[SIZE][SIZE];

    public Block getBlock(int x, int y) {
        return blocks[y][x];
    }

    public boolean isVisible(int x, int y) {
        return (x >= 0 && x < SIZE && y >= 0 && y < SIZE);
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

            if (alive) {
                for (Unit u : units) {
                    if (u.getId() != b.getAuthor()) {

                        if (false) {//COLLISION DETECTION (Посчитай как расстояние от отрезка новое положение-старое положение до центра юнита)
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
            }

            if (alive) {
                aliveAfterUnitsBullets.add(b);
            }

        }
        bullets.clear();
        for (AbstractBullet b : aliveAfterUnitsBullets) {
            if (b.getX() > 0 && b.getY() > 0) {
                if (getBlock(b.getBlockX(), b.getBlockY()).getIsHard()) {
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

    private void checkForNewShotings() {
        for (Unit u : units) {
            AbstractBullet b = u.getWeapon().getNewBullet();
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


        for (Unit u : units) {
            UnitBlock newBlock = u.tryMove(deltaTime);
            if (newBlock != null && possibleToMoveUnit(newBlock.getBlockX(), newBlock.getBlockY())) {
                isRightToMove(newBlock);
                u.applyMove(newBlock);
            }
        }

        checkForNewShotings();
        updateBulletsCollisions(deltaTime);
        moveBullets(deltaTime);






    }

    public StrangeWorld(Unit me) {
        this.me = me;
        units.add(me);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                blocks[i][j] = new Brick();
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

    }

}
