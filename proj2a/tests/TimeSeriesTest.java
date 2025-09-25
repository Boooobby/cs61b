import ngrams.TimeSeries;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

/** Unit Tests for the TimeSeries class.
 *  @author Josh Hug
 */
public class TimeSeriesTest {
    @Test
    public void testFromSpec() {
        TimeSeries catPopulation = new TimeSeries();
        catPopulation.put(1991, 0.0);
        catPopulation.put(1992, 100.0);
        catPopulation.put(1994, 200.0);

        TimeSeries dogPopulation = new TimeSeries();
        dogPopulation.put(1994, 400.0);
        dogPopulation.put(1995, 500.0);

        TimeSeries totalPopulation = catPopulation.plus(dogPopulation);
        // expected: 1991: 0,
        //           1992: 100
        //           1994: 600
        //           1995: 500

        List<Integer> expectedYears = new ArrayList<>
                (Arrays.asList(1991, 1992, 1994, 1995));

        assertThat(totalPopulation.years()).isEqualTo(expectedYears);

        List<Double> expectedTotal = new ArrayList<>
                (Arrays.asList(0.0, 100.0, 600.0, 500.0));

        for (int i = 0; i < expectedTotal.size(); i += 1) {
            assertThat(totalPopulation.data().get(i)).isWithin(1E-10).of(expectedTotal.get(i));
        }
    }

    @Test
    public void testEmptyBasic() {
        TimeSeries catPopulation = new TimeSeries();
        TimeSeries dogPopulation = new TimeSeries();

        assertThat(catPopulation.years()).isEmpty();
        assertThat(catPopulation.data()).isEmpty();

        TimeSeries totalPopulation = catPopulation.plus(dogPopulation);

        assertThat(totalPopulation.years()).isEmpty();
        assertThat(totalPopulation.data()).isEmpty();
    }

    @Test
    public void testDividedByBasic() {
        TimeSeries numerator = new TimeSeries();
        numerator.put(1991, 100.0);
        numerator.put(1992, 200.0);
        numerator.put(1993, 300.0);

        TimeSeries denominator = new TimeSeries();
        denominator.put(1991, 50.0);
        denominator.put(1992, 100.0);
        denominator.put(1993, 150.0);

        TimeSeries result = numerator.dividedBy(denominator);

        List<Integer> expectedYears = Arrays.asList(1991, 1992, 1993);
        assertThat(result.years()).isEqualTo(expectedYears);

        assertThat(result.get(1991)).isWithin(1E-10).of(2.0);
        assertThat(result.get(1992)).isWithin(1E-10).of(2.0);
        assertThat(result.get(1993)).isWithin(1E-10).of(2.0);
    }

    @Test
    public void testDividedByExtraYearInDenominator() {
        TimeSeries numerator = new TimeSeries();
        numerator.put(1991, 100.0);
        numerator.put(1992, 200.0);

        TimeSeries denominator = new TimeSeries();
        denominator.put(1991, 50.0);
        denominator.put(1992, 100.0);
        denominator.put(1993, 150.0); // 分子没有1993年，应该被忽略

        // 应该正常执行，分母的1993年被忽略
        TimeSeries result = numerator.dividedBy(denominator);

        List<Integer> expectedYears = Arrays.asList(1991, 1992);
        assertThat(result.years()).isEqualTo(expectedYears);

        assertThat(result.get(1991)).isWithin(1E-10).of(2.0);
        assertThat(result.get(1992)).isWithin(1E-10).of(2.0);
        assertThat(result.containsKey(1993)).isFalse();
    }

    @Test
    public void testDividedByZeroValue() {
        TimeSeries numerator = new TimeSeries();
        numerator.put(1991, 100.0);
        numerator.put(1992, 200.0);

        TimeSeries denominator = new TimeSeries();
        denominator.put(1991, 50.0);
        denominator.put(1992, 0.0); // 除数为零

        // 应该抛出ArithmeticException（实际上会得到Infinity）
        TimeSeries result = numerator.dividedBy(denominator);

        assertThat(result.get(1991)).isWithin(1E-10).of(2.0);
        assertThat(result.get(1992)).isEqualTo(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testDividedByBothEmpty() {
        TimeSeries numerator = new TimeSeries();
        TimeSeries denominator = new TimeSeries();

        // 两个都为空，应该返回空TimeSeries
        TimeSeries result = numerator.dividedBy(denominator);

        assertThat(result.years()).isEmpty();
        assertThat(result.data()).isEmpty();
    }

    @Test
    public void testDividedByFractionalResults() {
        TimeSeries numerator = new TimeSeries();
        numerator.put(1991, 1.0);
        numerator.put(1992, 3.0);
        numerator.put(1993, 5.0);

        TimeSeries denominator = new TimeSeries();
        denominator.put(1991, 2.0);
        denominator.put(1992, 4.0);
        denominator.put(1993, 8.0);

        TimeSeries result = numerator.dividedBy(denominator);

        assertThat(result.get(1991)).isWithin(1E-10).of(0.5);
        assertThat(result.get(1992)).isWithin(1E-10).of(0.75);
        assertThat(result.get(1993)).isWithin(1E-10).of(0.625);
    }

    @Test
    public void testDividedByPreservesOrder() {
        TimeSeries numerator = new TimeSeries();
        numerator.put(1993, 300.0);
        numerator.put(1991, 100.0);
        numerator.put(1992, 200.0);

        TimeSeries denominator = new TimeSeries();
        denominator.put(1991, 50.0);
        denominator.put(1992, 100.0);
        denominator.put(1993, 150.0);

        TimeSeries result = numerator.dividedBy(denominator);

        // 应该保持TreeMap的自然排序（升序）
        List<Integer> expectedYears = Arrays.asList(1991, 1992, 1993);
        assertThat(result.years()).isEqualTo(expectedYears);
    }
} 