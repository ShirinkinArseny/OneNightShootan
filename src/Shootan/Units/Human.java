package Shootan.Units;

public class Human extends Unit {

    public Human(float x, float y) {
        super(x, y, 0.5f, 10f, 1);
    }

    @Override
    public int getType() {
        return 0;
    }
}
