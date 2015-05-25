package Shootan.Worlds;

import Shootan.Blocks.Block;
import Shootan.Bullets.Bullet;
import Shootan.Units.Unit;

import java.util.ArrayList;
import java.util.List;

public abstract class World {

    public static final int getPotentialViewDistance=50;

    public abstract Block getBlock(int x, int y);

    public abstract List<Unit> getUnits();

    public abstract List<Bullet> getBullets();

    public abstract void update(float deltaTime);

}