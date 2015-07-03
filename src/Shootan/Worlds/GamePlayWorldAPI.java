package Shootan.Worlds;

import Shootan.GameEssences.Units.Unit;

public interface GamePlayWorldAPI {

    void sendMessage(String message);

    String getUnitName(Unit u);

    int getFrags(Unit player);

    int getDeaths(Unit player);

    void dropStatistic();

    void addUnit(Unit u);

}
