package prototype.evolution.engine.dc;

import org.jgap.gp.CommandGene;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.ProgramChromosome;

// hamming distance
// http://ti.arc.nasa.gov/m/pub-archive/1451/1451%20(Mengshoel).pdf p.3
public class HammingDistance implements IndividualDistance {

    public double distance(IGPProgram a_leftIndividual, IGPProgram a_rightIndividual) {
        // assuming individuals have the same size
        if (a_leftIndividual.size() != a_rightIndividual.size()) {
            throw new IllegalArgumentException("Individuals have different number of chromosomes!");
        }

        // iterate over all chromosomes
        long distance = 0;

        for (int i = 0; i < a_leftIndividual.size(); i++) {
            distance += chromosomeDistance(
                    a_leftIndividual.getChromosome(i),
                    a_rightIndividual.getChromosome(i)
            );
        }

        return distance;
    }

    private int chromosomeDistance(ProgramChromosome a_leftChromosome, ProgramChromosome a_rightChromosome) {
        CommandGene[] leftGenes = a_leftChromosome.getFunctions();
        CommandGene[] rightGenes = a_rightChromosome.getFunctions();

        int distance = 0;
        int i = 0;
        while (i < leftGenes.length && i < rightGenes.length) {
            if (!equal(leftGenes[i], rightGenes[i])) {
                distance++;
            }
        }

        return distance;
    }

    private boolean equal(CommandGene leftGene, CommandGene rightGene) {
        return leftGene.equals(rightGene);
    }
}
