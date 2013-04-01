package prototype.evolution.engine;

import org.jgap.gp.impl.GPGenotype;

/**
 * User: koperek
 * Date: 16.03.13
 * Time: 12:58
 */
public class EvolutionEngineEvent {
    private final EvolutionEngineEventType type;
    private final GPGenotype genotype;
    private final EvolutionEngine source;

    public EvolutionEngineEvent(EvolutionEngineEventType type, GPGenotype genotype, EvolutionEngine source) {
        this.type = type;
        this.genotype = genotype;
        this.source = source;
    }

    public GPGenotype getGenotype() {
        return genotype;
    }

    public EvolutionEngineEventType getType() {
        return type;
    }

    public EvolutionEngine getSource() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EvolutionEngineEvent that = (EvolutionEngineEvent) o;

        if (genotype != null ? !genotype.equals(that.genotype) : that.genotype != null) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (genotype != null ? genotype.hashCode() : 0);
        return result;
    }
}
