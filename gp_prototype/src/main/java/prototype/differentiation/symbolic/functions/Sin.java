package prototype.differentiation.symbolic.functions;

import prototype.differentiation.symbolic.Function;

/**
 * User: koperek
 * Date: 23.02.13
 * Time: 23:11
 */
public class Sin extends SingleOperandFunction {

    public Sin(Function operand) {
        super(operand);
    }

    @Override
    public double evaluate() {
        return Math.sin(getOperand().evaluate());
    }

    @Override
    public Function clone() {
        return new Sin(getOperand().clone());
    }

    @Override
    public String toString() {
        return "Sin( " + getOperand() + " )";
    }

}
