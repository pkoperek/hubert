package prototype.differentiation.symbolic.functions;

import prototype.differentiation.symbolic.Function;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 23:00
 */
public class Constant extends Function {

    private Number number;

    public Constant(Number number) {
        this.number = number;
    }

    public Number getNumber() {
        return number;
    }

    @Override
    public double evaluate() {
        return number.doubleValue();
    }

    @Override
    public Function clone() {
        return new Constant(number.doubleValue());
    }

    @Override
    public String toString() {
        return "" + number;
    }
}
