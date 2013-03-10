package prototype.differentiation;

import org.jgap.gp.CommandGene;
import org.jgap.gp.function.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: koperek
 * Date: 28.02.13
 * Time: 21:59
 */
public class CommandGeneToFunctionTypeTranslator {

    public static final Map<Class<? extends CommandGene>, FunctionType> MAPPING;

    static {
        Map<Class<? extends CommandGene>, FunctionType> mapping = new HashMap<Class<? extends CommandGene>, FunctionType>();

        mapping.put(Add.class, FunctionType.ADD);
        mapping.put(Subtract.class, FunctionType.SUBTRACT);
        mapping.put(Multiply.class, FunctionType.MULTIPLY);
        mapping.put(Divide.class, FunctionType.DIVIDE);
        mapping.put(Sine.class, FunctionType.SIN);
        mapping.put(Cosine.class, FunctionType.COS);
        mapping.put(Exp.class, FunctionType.EXP);
        mapping.put(Pow.class, FunctionType.POW);
        mapping.put(Log.class, FunctionType.LN);
        mapping.put(Tangent.class, FunctionType.TAN);
        mapping.put(ArcSine.class, FunctionType.ARCSIN);
        mapping.put(ArcCosine.class, FunctionType.ARCCOS);
        mapping.put(ArcTangent.class, FunctionType.ARCTAN);

        // these two are covered as separate tree node classes
//        mapping.put(Variable.class, FunctionType.VARIABLE);
//        mapping.put(Terminal.class, FunctionType.CONSTANT);

        MAPPING = Collections.unmodifiableMap(mapping);
    }

    public FunctionType translate(CommandGene commandGene) {
        if (MAPPING.containsKey(commandGene.getClass())) {
            return MAPPING.get(commandGene.getClass());
        }

        throw new IllegalArgumentException("Don't know how to map: " + commandGene.getClass().getCanonicalName());
    }
}
