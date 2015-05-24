package Shootan.Worlds;

import Shootan.Blocks.Block;
import Shootan.Bullets.AbstractBullet;
import Shootan.Units.Unit;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public abstract class World {

    public static final int getPotentialViewDistance=50;

    public abstract Block getBlock(int x, int y);

    public abstract ArrayList<Unit> getUnits();

    public abstract ArrayList<AbstractBullet> getBullets();

    public abstract Unit getMe();

    public abstract void update(float deltaTime);

}