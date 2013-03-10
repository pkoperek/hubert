package prototype.differentiation.functions;

import prototype.differentiation.Function;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 19:44
 */
public abstract class SingleOperandFunction extends Function {

    private Function operand;

    protected SingleOperandFunction(Function operand) {
        this.operand = operand;
    }

    public Function getOperand() {
        return operand;
    }

    protected void setOperand(Function operand) {
        this.operand = operand;
    }
}
