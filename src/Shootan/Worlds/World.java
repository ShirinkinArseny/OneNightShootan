package Shootan.Worlds;

import Shootan.GameEssences.Blocks.Block;
import Shootan.GameEssences.Bullets.Bullet;
import Shootan.GameEssences.Units.Unit;

import java.util.List;

public abstract class World {

    public static final int potentialViewDistance =10;

    public abstract Block getBlock(int x, int y);

    public abstract List<Unit> getUnits();

    public abstract List<Bullet> getBullets();

    public abstract void update(float deltaTime);

}