package Shootan.Units;

public class UnitFactory {

    public static Unit create(int type) {
        switch (type) {
            case 0: return new Human(0, 0);
        }
        return null;
    }

}
