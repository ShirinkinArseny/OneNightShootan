package Shootan.AI.Neurons;

public class MultiplyerNeuron extends AbstractProcessingNeuron {

    private float coef;

    public MultiplyerNeuron(float coef) {
        this.coef = coef;
    }

    @Override
    protected float processValue(float f) {
        return coef*f;
    }

}
