package Shootan.Worlds;

import Shootan.GameEssences.Blocks.Block;
import Shootan.GameEssences.Bullets.Bullet;
import Shootan.GameEssences.Units.Unit;

import java.util.List;

public abstract class AbstractWorld {

    public static final int potentialViewDistance =6;

    public abstract Block getBlock(int x, int y);

    public abstract List<Unit> getUnits();

    public abstract List<Bullet> getBullets();

    public abstract void update(float deltaTime);

}