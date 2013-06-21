package hubert.differentiation.symbolic.functions;

import hubert.differentiation.symbolic.Function;

/**
 * User: koperek
 * Date: 23.02.13
 * Time: 23:11
 */
public class Exp extends SingleOperandFunction {

    public Exp(Function operand) {
        super(operand);
    }

    @Override
    public double evaluate() {
        return Math.exp(getOperand().evaluate());
    }

    @Override
    public Function clone() {
        return new Exp(getOperand().clone());
    }

    @Override
    public String toString() {
        return "e^" + getOperand();
    }

}
