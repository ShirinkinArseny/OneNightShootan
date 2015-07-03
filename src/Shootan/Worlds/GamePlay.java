package Shootan.Worlds;

import Shootan.GameEssences.Bullets.Bullet;
import Shootan.GameEssences.Units.Unit;

public interface GamePlay {

    void setGamePlayAPI(GamePlayWorldAPI api);

    void onDeathAction(Unit deathUnit, Bullet killer);

    void update(float sec);

    void onIncomingMessage(String msg);

    void onHitAction(Unit hitted, Bullet hitter);

    void onShot(Unit bullet, Bullet shoter);

    Unit acceptConnection();

}
