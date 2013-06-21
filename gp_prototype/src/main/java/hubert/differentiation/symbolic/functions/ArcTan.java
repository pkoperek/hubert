package hubert.differentiation.symbolic.functions;

import hubert.differentiation.symbolic.Function;

/**
 * User: koperek
 * Date: 04.03.13
 * Time: 00:01
 */
public class ArcTan extends SingleOperandFunction {
    public ArcTan(Function operand) {
        super(operand);
    }

    @Override
    public double evaluate() {
        return Math.atan(getOperand().evaluate());
    }

    @Override
    public Function clone() {
        return new ArcTan(getOperand().clone());
    }

    @Override
    public String toString() {
        return "ArcTan( " + getOperand() + " )";
    }
}
