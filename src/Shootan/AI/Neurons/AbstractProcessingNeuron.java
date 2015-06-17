package Shootan.AI.Neurons;

import java.util.ArrayList;

public abstract class AbstractProcessingNeuron extends AbstractNeuron {


    private ArrayList<AbstractNeuron> sources=new ArrayList<>();

    public void addSource(AbstractNeuron n) {
        sources.add(n);
    }

    protected abstract float processValue(float f);

    @Override
    public float getValue(NeuronLimitation l) {
        l.use();
        if (l.isOverLimit()) return 0;
        float source=0;
        for (AbstractNeuron n: sources) {
            source+=n.getValue(l);
        }
        return processValue(source);
    }
}
