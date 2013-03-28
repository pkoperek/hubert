package prototype.differentiation.symbolic.functions;

import prototype.differentiation.symbolic.Function;

/**
 * User: koperek
 * Date: 23.02.13
 * Time: 23:11
 */
public class Divide extends DoubleOperandFunction {

    public Divide(Function leftOperand, Function rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public double evaluate() {
        return getLeftOperand().evaluate() / getRightOperand().evaluate();
    }

    @Override
    public Function clone() {
        return new Divide(getLeftOperand().clone(), getRightOperand().clone());
    }

    @Override
    public String toString() {
        return "(" + getLeftOperand() + " / " + getRightOperand() + ")";
    }

}
