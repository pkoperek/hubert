package pl.edu.agh.hubert.differentiation.symbolic.functions;

import pl.edu.agh.hubert.differentiation.symbolic.Function;

/**
 * User: koperek
 * Date: 23.02.13
 * Time: 23:11
 */
public class Multiply extends DoubleOperandFunction {

    public Multiply(Function leftOperand, Function rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public double evaluate() {
        return getLeftOperand().evaluate() * getRightOperand().evaluate();
    }

    @Override
    public Function clone() {
        return new Multiply(getLeftOperand().clone(), getRightOperand().clone());
    }

    @Override
    public String toString() {
        return "(" + getLeftOperand() + " * " + getRightOperand() + ")";
    }

}
