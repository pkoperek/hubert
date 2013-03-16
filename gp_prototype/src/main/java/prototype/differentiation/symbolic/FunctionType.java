package prototype.differentiation.symbolic;

import prototype.differentiation.symbolic.strategies.*;
import prototype.differentiation.symbolic.strategies.elementary.*;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 19:09
 */
public enum FunctionType {

    ADD(new AddDifferentiationStrategy()),
    SUBTRACT(new SubtractDifferentiationStrategy()),
    MULTIPLY(new MultiplyDifferentiationStrategy()),
    DIVIDE(new DivideDifferentiationStrategy()),
    COS(new CosDifferentiationStrategy()),
    SIN(new SinDifferentiationStrategy()),
    VARIABLE(new VariableDifferentiationStrategy()),
    CONSTANT(new ConstantDifferentiationStrategy()),
    EXP(new ExpDifferentiationStrategy()),
    LN(new LnDifferentiationStrategy()),
    POW(new PowDifferentiationStrategy()),
    TAN(new TanDifferentiationStrategy()),
    ARCSIN(new ArcSinDifferentiationStrategy()),
    ARCCOS(new ArcCosDifferentiationStrategy()),
    ARCTAN(new ArcTanDifferentiationStrategy());

    private DifferentiationStrategy differentiationStrategy;

    private FunctionType(DifferentiationStrategy differentiationStrategy) {
        this.differentiationStrategy = differentiationStrategy;
    }

    public DifferentiationStrategy getDifferentiationStrategy() {
        return differentiationStrategy;
    }

}
