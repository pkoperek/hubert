package prototype.evolution.engine.common;

import org.jgap.gp.IGPProgram;
import prototype.evolution.engine.Mutator;

public class StaticMutator implements Mutator {

    @Override
    public IGPProgram mutate(IGPProgram toMutate) {


        return toMutate;
    }

}
