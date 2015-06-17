package Shootan.AI.Neurons;

public class FilterNeuron extends AbstractProcessingNeuron {

    private float coef;

    public FilterNeuron(float coef) {
        this.coef = coef;
    }

    @Override
    protected float processValue(float f) {
        return f>coef?1:0;
    }

}
