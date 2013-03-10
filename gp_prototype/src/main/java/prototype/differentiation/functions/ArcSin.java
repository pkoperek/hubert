package prototype.differentiation.functions;

import prototype.differentiation.Function;

/**
 * User: koperek
 * Date: 04.03.13
 * Time: 00:01
 */
public class ArcSin extends SingleOperandFunction {
    public ArcSin(Function operand) {
        super(operand);
    }

    @Override
    public double evaluate() {
        return Math.asin(getOperand().evaluate());
    }

    @Override
    public Function clone() {
        return new ArcSin(getOperand().clone());
    }

    @Override
    public String toString() {
        return "ArcSin( " + getOperand() + " )";
    }
}
