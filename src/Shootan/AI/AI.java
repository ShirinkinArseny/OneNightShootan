package Shootan.AI;

import Shootan.GameEssences.Units.ControlledUnit;
import Shootan.GameEssences.Units.VisibleUnit;
import Shootan.Worlds.VisibleWorld;

import java.util.ArrayList;

public interface AI {

    void applyIntelligence(
            ControlledUnit me,
            ArrayList<VisibleUnit> visibleUnits,
            VisibleWorld world
    );

}
