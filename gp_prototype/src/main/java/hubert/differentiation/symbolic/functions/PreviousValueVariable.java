package hubert.differentiation.symbolic.functions;

import hubert.data.VariablesValuesContainer;

/**
 * User: koperek
 * Date: 10.03.13
 * Time: 01:25
 */
public class PreviousValueVariable extends Variable {
    public static final String PREVIOUS_VALUE_VARIABLE_SUFFIX = "_previous";

    public PreviousValueVariable(VariablesValuesContainer valuesContainer, String variableName) {
        super(valuesContainer, variableName + PREVIOUS_VALUE_VARIABLE_SUFFIX);
    }
}
