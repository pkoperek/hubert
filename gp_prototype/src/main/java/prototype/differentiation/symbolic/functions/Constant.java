package prototype.differentiation.symbolic.functions;

import prototype.differentiation.symbolic.Function;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 23:00
 */
public class Constant extends Function {

    private double number;

    public Constant(double number) {
        this.number = number;
    }

    public double getNumber() {
        return number;
    }

    @Override
    public double evaluate() {
        return number;
    }

    @Override
    public Function clone() {
        return new Constant(number);
    }

    @Override
    public String toString() {
        return "" + number;
    }
}
