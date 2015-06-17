package Shootan.AI.Neurons;

import java.util.function.Supplier;

public class SourceNeuron extends AbstractNeuron {

    private Supplier<Float> source;

    public SourceNeuron(Supplier<Float> source) {
        this.source = source;
    }

    @Override
    public float getValue(NeuronLimitation l) {
        l.use();
        if (l.isOverLimit()) {
            return 0;
        }
        return source.get();
    }

}
