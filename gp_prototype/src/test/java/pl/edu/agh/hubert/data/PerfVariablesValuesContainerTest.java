package pl.edu.agh.hubert.data;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 12:19
 */
public class PerfVariablesValuesContainerTest {

    private final static long REPETITIONS = 100000;
    private final static String[] VARIABLES = {"a", "b", "c", "d", "e", "f", "g"};
    private ArrayVariablesValuesContainer arrayVariablesValuesContainer;
    private MapVariablesValuesContainer mapVariablesValuesContainer;

    @Before
    public void setUp() throws Exception {
        arrayVariablesValuesContainer = new ArrayVariablesValuesContainer(VARIABLES.length);
        mapVariablesValuesContainer = new MapVariablesValuesContainer();
    }

    @Ignore
    @Test
    public void shouldShowTheFastestValuesContainerReading() throws Exception {

        // Given
        fillWithData(arrayVariablesValuesContainer);
        fillWithData(mapVariablesValuesContainer);

        // When
        long arrayTime = measureReadTime(arrayVariablesValuesContainer);
        long mapTime = measureReadTime(mapVariablesValuesContainer);

        System.out.println("Reads");
        System.out.println("Array: " + arrayTime + "ns" + " Map: " + mapTime + "ns");
        System.out.println("Array: " + arrayTime / 1000000 + "ms" + " Map: " + mapTime / 1000000 + "ms");

        // Then

    }

    @Ignore
    @Test
    public void shouldShowTheFastestValuesContainerWriting() throws Exception {

        // Given

        // When
        long arrayTime = measureWriteTime(arrayVariablesValuesContainer);
        long mapTime = measureWriteTime(mapVariablesValuesContainer);

        System.out.println("Writes");
        System.out.println("Array: " + arrayTime + "ns" + " Map: " + mapTime + "ns");
        System.out.println("Array: " + arrayTime / 1000000 + "ms" + " Map: " + mapTime / 1000000 + "ms");

        // Then

    }

    private long measureWriteTime(VariablesValuesContainer variablesValuesContainer) {
        long start = System.nanoTime();
        for (long i = 0; i < REPETITIONS; i++) {
            variablesValuesContainer.clear();
            for (String variable : VARIABLES) {
                variablesValuesContainer.setVariableValue(variable, 20.0);
            }
        }
        long stop = System.nanoTime();
        return stop - start;
    }

    private long measureReadTime(VariablesValuesContainer valuesContainer) {
        long start = System.nanoTime();
        for (long i = 0; i < REPETITIONS; i++) {
            for (String variable : VARIABLES) {
                valuesContainer.getVariableValue(variable);
            }
        }
        long stop = System.nanoTime();
        return stop - start;
    }

    private void fillWithData(VariablesValuesContainer variablesValuesContainer) {
        for (int i = 0; i < VARIABLES.length; i++) {
            variablesValuesContainer.setVariableValue(VARIABLES[i], 1.0);
        }
    }
}
