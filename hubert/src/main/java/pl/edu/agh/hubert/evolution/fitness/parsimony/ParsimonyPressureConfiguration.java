package pl.edu.agh.hubert.evolution.fitness.parsimony;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 17:31
 */
public class ParsimonyPressureConfiguration {
    private ParsimonyPressureType type;
    private double constantParsimony;

    public ParsimonyPressureType getType() {
        return type;
    }

    public void setType(ParsimonyPressureType type) {
        this.type = type;
    }

    public double getConstantParsimony() {
        return constantParsimony;
    }

    public void setConstantParsimony(double constantParsimony) {
        this.constantParsimony = constantParsimony;
    }
}
