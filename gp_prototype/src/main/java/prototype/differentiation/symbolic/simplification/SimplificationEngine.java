package prototype.differentiation.symbolic.simplification;

import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.functions.DoubleOperandFunction;
import prototype.differentiation.symbolic.functions.OperandModifier;
import prototype.differentiation.symbolic.functions.SingleOperandFunction;

import java.util.Collection;

public class SimplificationEngine {
    private OperandModifier operandModifier;
    private Collection<SimplificationRule> simplificationRules;

    public SimplificationEngine(Collection<SimplificationRule> simplificationRules, OperandModifier operandModifier) {
        this.simplificationRules = simplificationRules;
        this.operandModifier = operandModifier;
    }

    public Function simplify(Function toSimplify) {
        simplifyOperands(toSimplify);

        Function simplified = toSimplify;

        // here we have guarantee that the operands were simplified - attempt to use rules
        for (SimplificationRule simplificationRule : simplificationRules) {
            if (simplificationRule.canSimplify(toSimplify)) {
                simplified = simplificationRule.apply(toSimplify);
            }
        }

        return simplified;
    }

    private void simplifyOperands(Function toSimplify) {
        if (toSimplify instanceof SingleOperandFunction) {
            simplifyOneOperand((SingleOperandFunction) toSimplify);
        }

        if (toSimplify instanceof DoubleOperandFunction) {
            simplifyTwoOperands((DoubleOperandFunction) toSimplify);
        }
    }

    private void simplifyTwoOperands(DoubleOperandFunction toSimplify) {
        Function simplifiedLeftOperand = simplify(toSimplify.getLeftOperand());
        Function simplifiedRightOperand = simplify(toSimplify.getRightOperand());
        operandModifier.changeLeftOperand(toSimplify, simplifiedLeftOperand);
        operandModifier.changeRightOperand(toSimplify, simplifiedRightOperand);
    }

    private void simplifyOneOperand(SingleOperandFunction toSimplify) {
        Function simplified = simplify(toSimplify.getOperand());
        operandModifier.changeOperand(toSimplify, simplified);
    }
}
