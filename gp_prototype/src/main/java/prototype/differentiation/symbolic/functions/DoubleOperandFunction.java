package prototype.differentiation.symbolic.functions;

import prototype.differentiation.symbolic.Function;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 19:44
 */
public abstract class DoubleOperandFunction extends Function {

    private Function leftOperand;
    private Function rightOperand;

    protected DoubleOperandFunction(Function leftOperand, Function rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public Function getLeftOperand() {
        return leftOperand;
    }

    public Function getRightOperand() {
        return rightOperand;
    }

    protected void setLeftOperand(Function leftOperand) {
        this.leftOperand = leftOperand;
    }

    protected void setRightOperand(Function rightOperand) {
        this.rightOperand = rightOperand;
    }
}
