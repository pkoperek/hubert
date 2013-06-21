package pl.edu.agh.hubert.data;

/**
 * Should be faster - assumes that the variables are read in the same order as they are written
 * <p/>
 * User: koperek
 * Date: 27.03.13
 * Time: 11:34
 */
public class ArrayVariablesValuesContainer implements VariablesValuesContainer {

    private final String[] variableNames;
    private final double[] values;
    private int currentWriteIdx;

    public ArrayVariablesValuesContainer(int size) {
        this.values = new double[size];
        this.variableNames = new String[size];
        this.currentWriteIdx = 0;
    }

    @Override
    public void setVariableValue(String variable, double value) {
        this.variableNames[currentWriteIdx] = variable;
        this.values[currentWriteIdx] = value;
        this.currentWriteIdx++;
    }

    @Override
    public double getVariableValue(String variable) {
        for (int i = 0; i < variableNames.length; i++) {
            if (variableNames[i].equals(variable)) {
                return values[i];
            }
        }

        throw new IllegalArgumentException("Variable not set yet! " + variable);
    }

    @Override
    public void clear() {
        this.currentWriteIdx = 0;
    }
}
