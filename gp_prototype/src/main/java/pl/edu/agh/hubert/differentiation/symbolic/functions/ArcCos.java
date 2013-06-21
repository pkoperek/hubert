package pl.edu.agh.hubert.differentiation.symbolic.functions;

import pl.edu.agh.hubert.differentiation.symbolic.Function;

/**
 * User: koperek
 * Date: 04.03.13
 * Time: 00:01
 */
public class ArcCos extends SingleOperandFunction {
    public ArcCos(Function operand) {
        super(operand);
    }

    @Override
    public double evaluate() {
        return Math.acos(getOperand().evaluate());
    }

    @Override
    public Function clone() {
        return new ArcCos(getOperand().clone());
    }

    @Override
    public String toString() {
        return "ArcCos( " + getOperand() + " )";
    }
}
