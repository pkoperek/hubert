package prototype.evolution.engine;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.ProgramChromosome;
import org.jgap.util.ICloneable;

/**
 * User: koperek
 * Date: 28.03.13
 * Time: 13:08
 */
public class AgeTrackedProgramChromosome extends ProgramChromosome {

    private int age = 1;

    public AgeTrackedProgramChromosome(GPConfiguration a_conf, int a_size) throws InvalidConfigurationException {
        super(a_conf, a_size);
    }

    public AgeTrackedProgramChromosome(GPConfiguration a_conf, int a_size, IGPProgram a_ind) throws InvalidConfigurationException {
        super(a_conf, a_size, a_ind);
    }

    public AgeTrackedProgramChromosome(GPConfiguration a_conf, int a_size, CommandGene[] a_functionSet, Class[] a_argTypes, IGPProgram a_ind) throws InvalidConfigurationException {
        super(a_conf, a_size, a_functionSet, a_argTypes, a_ind);
    }

    public AgeTrackedProgramChromosome(GPConfiguration a_conf, CommandGene[] a_initialGenes) throws InvalidConfigurationException {
        super(a_conf, a_initialGenes);
    }

    public AgeTrackedProgramChromosome(GPConfiguration a_conf) throws InvalidConfigurationException {
        super(a_conf);
    }

    public AgeTrackedProgramChromosome(ProgramChromosome chromosome) throws InvalidConfigurationException {
        super(chromosome.getGPConfiguration(),
                chromosome.getFunctions().length,
                chromosome.getFunctionSet(),
                chromosome.getArgTypes(),
                chromosome.getIndividual());

        for (int i = 0; i < chromosome.size(); i++) {
            this.setGene(i, chromosome.getGene(i));
        }

        this.redepth();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void incrementAge() {
        this.age++;
    }

    @Override
    public synchronized Object clone() {
        try {
            CommandGene[] genes = cloneGenes(this.getFunctions());
            AgeTrackedProgramChromosome chrom = new AgeTrackedProgramChromosome((GPConfiguration)
                    getGPConfiguration(), (CommandGene[]) genes);

            chrom.setArgTypes((Class[]) getArgTypes().clone());
            chrom.setFunctionSet(cloneFunctionSet(getFunctionSet()));
            chrom.redepth();
            chrom.setAge(getAge());
            chrom.setIndividual(getIndividual());
            return chrom;

        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private CommandGene[] cloneFunctionSet(CommandGene[] functionSet) {
        if (getFunctionSet() != null) {
            return functionSet.clone();
        }

        return null;
    }

    private CommandGene[] cloneGenes(CommandGene[] m_genes) {
        CommandGene[] genes = new CommandGene[m_genes.length];
        for (int i = 0; i < m_genes.length; i++) {
            if (m_genes[i] == null) {
                break;
            }
            // Try deep clone of genes.
            // ------------------------
            if (ICloneable.class.isAssignableFrom(m_genes[i].getClass())) {
                genes[i] = (CommandGene) ((ICloneable) m_genes[i]).clone();
            } else {
                // No deep clone possible.
                // -----------------------
                genes[i] = m_genes[i];
            }
        }
        return genes;
    }
}
