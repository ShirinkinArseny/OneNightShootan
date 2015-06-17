package Shootan.AI.Neurons;

public class NeuronLimitation {

    private int actions;

    public NeuronLimitation(int actions) {
        this.actions = actions;
    }

    public void use() {
        actions--;
    }

    public boolean isOverLimit() {
        return actions<0;
    }

}
