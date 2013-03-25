package prototype.differentiation.symbolic.functions;

import prototype.data.VariablesValues;
import prototype.differentiation.symbolic.Function;

/**
 * User: koperek
 * Date: 23.02.13
 * Time: 23:29
 */
public class Variable extends Function {

    private final VariablesValues valuesContainer;
    private String variableName;

    public Variable(VariablesValues valuesContainer, String variableName) {
        this.valuesContainer = valuesContainer;
        this.variableName = variableName;
    }

    @Override
    public double evaluate() {
        return valuesContainer.getVariableValue(variableName);
    }

    @Override
    public Function clone() {
        return new Variable(valuesContainer, new String(variableName));
    }

    @Override
    public String toString() {
        return variableName;
    }

    public String getVariableName() {
        return variableName;
    }
}
