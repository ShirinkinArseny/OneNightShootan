package Shootan.Blocks;

public class Floor extends Block {

    @Override
    public long getType() {
        return 1;
    }

    @Override
    public boolean getIsHard() {
        return false;
    }

}
