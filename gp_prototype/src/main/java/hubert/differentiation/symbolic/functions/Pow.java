package hubert.differentiation.symbolic.functions;

import hubert.differentiation.symbolic.Function;

/**
 * User: koperek
 * Date: 07.03.13
 * Time: 19:46
 */
public class Pow extends DoubleOperandFunction {
    public Pow(Function leftOperand, Function rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public double evaluate() {
        return Math.pow(getLeftOperand().evaluate(), getRightOperand().evaluate());
    }

    @Override
    public Function clone() {
        return new Pow(getLeftOperand().clone(), getRightOperand().clone());
    }
}
