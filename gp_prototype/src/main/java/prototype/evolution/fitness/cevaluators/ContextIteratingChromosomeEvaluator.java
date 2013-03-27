package prototype.evolution.fitness.cevaluators;

import org.jgap.gp.impl.ProgramChromosome;

import java.util.Collection;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 16:29
 */
public class ContextIteratingChromosomeEvaluator<T> implements ChromosomeEvaluator {

    private final Collection<T> items;
    private final ContextChromosomeEvaluator<T> delegate;

    public ContextIteratingChromosomeEvaluator(Collection<T> items, ContextChromosomeEvaluator<T> delegate) {
        this.items = items;
        this.delegate = delegate;
    }

    @Override
    public double evaluate(ProgramChromosome chromosome) {
        double error = 0.0d;

        for (T item : items) {
            error += delegate.evaluateChromosomeInContext(chromosome, item);
        }

        return error;
    }
}
