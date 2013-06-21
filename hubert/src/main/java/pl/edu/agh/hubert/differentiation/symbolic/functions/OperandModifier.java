package pl.edu.agh.hubert.differentiation.symbolic.functions;

import pl.edu.agh.hubert.differentiation.symbolic.Function;

public class OperandModifier {
    public void changeOperand(SingleOperandFunction function, Function newOperand) {
        function.setOperand(newOperand);
    }

    public void changeLeftOperand(DoubleOperandFunction function, Function newOperand) {
        function.setLeftOperand(newOperand);
    }

    public void changeRightOperand(DoubleOperandFunction function, Function newOperand) {
        function.setRightOperand(newOperand);
    }
}
