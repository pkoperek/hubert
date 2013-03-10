package prototype;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: koperek
 * Date: 04.03.13
 * Time: 23:11
 */
public class PairGeneratorTest {

    private PairGenerator generator;

    @Before
    public void setUp() throws Exception {
        generator = new PairGenerator();
    }

    @Test
    public void shouldGenerateSpecificNumberOfPairs() throws Exception {

        // Given
        List<String> elements = new ArrayList<String>();
        elements.add("1");
        elements.add("2");
        elements.add("3");
        elements.add("4");
        elements.add("5");
        elements.add("6");
        elements.add("7");
        elements.add("8");

        // When
        List<Pair<String>> pairs = generator.generatePairs(elements);

        // Then
        assertThat(pairs.size()).isEqualTo( 8*7/2 ); // k(k-1) / 2
    }

    @Test
    public void shouldGeneratePairsFromVariablesList() throws Exception {

        // Given
        List<String> variables = new ArrayList<String>();
        variables.add("1");
        variables.add("2");
        variables.add("3");

        // When
        List<Pair<String>> pairs = generator.generatePairs(variables);

        // Then
        assertThat(pairs).containsExactly(
                new Pair("1","2"),
                new Pair("1","3"),
                new Pair("2","3")
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenListHasOneElement() throws Exception {

        // Given
        List<String> variables = new ArrayList<String>();
        variables.add("1");

        // When
        generator.generatePairs(variables);

        // Then

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenListEmpty() throws Exception {

        // Given

        // When
        generator.generatePairs(new ArrayList<String>());

        // Then

    }
}
