package hubert.differentiation.symbolic.functions;

import hubert.differentiation.symbolic.Function;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 20:04
 */
public class Ln extends SingleOperandFunction {

    public Ln(Function operand) {
        super(operand);
    }

    @Override
    public double evaluate() {
        return Math.log(getOperand().evaluate());
    }

    @Override
    public Function clone() {
        return new Ln(getOperand().clone());
    }

    @Override
    public String toString() {
        return "Ln( " + getOperand() + " )";
    }

}
