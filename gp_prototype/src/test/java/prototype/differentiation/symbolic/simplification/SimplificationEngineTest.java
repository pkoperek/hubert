package prototype.differentiation.symbolic.simplification;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.functions.*;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static prototype.differentiation.symbolic.TestTools.aVariable;

@RunWith(MockitoJUnitRunner.class)
public class SimplificationEngineTest {
    private SimplificationEngine engine;

    @Mock
    private OperandModifier operandModifier;

    @Test
    public void shouldNotSimplifyVariablesAndConstants() throws Exception {
        // Given
        Function variableToSimplify = aVariable("x");
        Function constantToSimplify = new Constant(10.0);

        // When
        engine = new SimplificationEngine(Collections.<SimplificationRule>emptyList(), operandModifier);
        Function simplifiedVariable = engine.simplify(variableToSimplify);
        Function simplifiedConstant = engine.simplify(constantToSimplify);

        // Then
        assertThat(simplifiedVariable).isSameAs(variableToSimplify);
        assertThat(simplifiedConstant).isSameAs(constantToSimplify);
    }

    @Test
    public void shouldNotSimplifyIfNoRules() throws Exception {

        // Given
        Function toSimplify = new Multiply(aVariable("x"), new Constant(10.0));

        // When
        engine = new SimplificationEngine(Collections.<SimplificationRule>emptyList(), operandModifier);
        Function simplified = engine.simplify(toSimplify);

        // Then
        assertThat(simplified).isSameAs(toSimplify);
        assertThat(((DoubleOperandFunction) simplified).getLeftOperand()).isSameAs(((DoubleOperandFunction) toSimplify).getLeftOperand());
        assertThat(((DoubleOperandFunction) simplified).getRightOperand()).isSameAs(((DoubleOperandFunction) toSimplify).getRightOperand());
    }

    @Test
    public void shouldSimplifyOperandsFirstForDoubleOperandFunction() throws Exception {
        // Given
        Function doubleOperandsFunctionToSimplify = new Add(aVariable("x"), new Constant(10.0));

        SimplificationRule mockVariableSimplificationRule = mock(SimplificationRule.class);
        given(mockVariableSimplificationRule.canSimplify(any(Variable.class))).willReturn(true);

        SimplificationRule mockAddSimplificationRule = mock(SimplificationRule.class);
        given(mockAddSimplificationRule.canSimplify(any(Add.class))).willReturn(true);

        // When
        engine = new SimplificationEngine(Arrays.asList(mockAddSimplificationRule, mockVariableSimplificationRule), operandModifier);
        Function simplified = engine.simplify(doubleOperandsFunctionToSimplify);

        // Then
        InOrder order = inOrder(mockVariableSimplificationRule, mockAddSimplificationRule);
        order.verify(mockVariableSimplificationRule).canSimplify(any(Variable.class));
        order.verify(mockVariableSimplificationRule).apply(any(Variable.class));
        order.verify(mockAddSimplificationRule).canSimplify(any(Add.class));
        order.verify(mockAddSimplificationRule).apply(any(Add.class));
    }

    @Test
    public void shouldSimplifyOperandsFirstForSingleOperandFunction() throws Exception {
        // Given
        Function singleOperandFunctionToSimplify = new Sin(aVariable("x"));

        SimplificationRule mockVariableSimplificationRule = mock(SimplificationRule.class);
        given(mockVariableSimplificationRule.canSimplify(any(Variable.class))).willReturn(true);

        SimplificationRule mockSinSimplificationRule = mock(SimplificationRule.class);
        given(mockSinSimplificationRule.canSimplify(any(Add.class))).willReturn(true);

        // When
        engine = new SimplificationEngine(Arrays.asList(mockSinSimplificationRule, mockVariableSimplificationRule), operandModifier);
        Function simplified = engine.simplify(singleOperandFunctionToSimplify);

        // Then
        InOrder order = inOrder(mockVariableSimplificationRule, mockSinSimplificationRule);
        order.verify(mockVariableSimplificationRule).canSimplify(any(Variable.class));
        order.verify(mockVariableSimplificationRule).apply(any(Variable.class));
        order.verify(mockSinSimplificationRule).canSimplify(any(Add.class));
        order.verify(mockSinSimplificationRule).apply(any(Add.class));
    }

}
