package pl.edu.agh.hubert.differentiation.symbolic.functions;

import pl.edu.agh.hubert.differentiation.symbolic.Function;

/**
 * User: koperek
 * Date: 04.03.13
 * Time: 00:01
 */
public class Tan extends SingleOperandFunction {

    public Tan(Function operand) {
        super(operand);
    }

    @Override
    public double evaluate() {
        return Math.tan(getOperand().evaluate());
    }

    @Override
    public Function clone() {
        return new Tan(getOperand().clone());
    }

    @Override
    public String toString() {
        return "Tan( " + getOperand() + " )";
    }
}
