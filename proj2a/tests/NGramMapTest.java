import ngrams.NGramMap;
import ngrams.TimeSeries;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utils.Utils.*;
import static com.google.common.truth.Truth.assertThat;

/** Unit Tests for the NGramMap class.
 *  @author Josh Hug
 */
public class NGramMapTest {
    @Test
    public void testCountHistory() {
        NGramMap ngm = new NGramMap(SHORT_WORDS_FILE, TOTAL_COUNTS_FILE);
        List<Integer> expectedYears = new ArrayList<>
                (Arrays.asList(2005, 2006, 2007, 2008));
        List<Double> expectedCounts = new ArrayList<>
                (Arrays.asList(646179.0, 677820.0, 697645.0, 795265.0));

        TimeSeries request2005to2008 = ngm.countHistory("request");
        assertThat(request2005to2008.years()).isEqualTo(expectedYears);

        for (int i = 0; i < expectedCounts.size(); i += 1) {
            assertThat(request2005to2008.data().get(i)).isWithin(1E-10).of(expectedCounts.get(i));
        }

        expectedYears = new ArrayList<>
                (Arrays.asList(2006, 2007));
        expectedCounts = new ArrayList<>
                (Arrays.asList(677820.0, 697645.0));

        TimeSeries request2006to2007 = ngm.countHistory("request", 2006, 2007);

        assertThat(request2006to2007.years()).isEqualTo(expectedYears);

        for (int i = 0; i < expectedCounts.size(); i += 1) {
            assertThat(request2006to2007.data().get(i)).isWithin(1E-10).of(expectedCounts.get(i));
        }
    }

    @Test
    public void testOnLargeFile() {
        // creates an NGramMap from a large dataset
        NGramMap ngm = new NGramMap(TOP_14337_WORDS_FILE,
                TOTAL_COUNTS_FILE);

        // returns the count of the number of occurrences of fish per year between 1850 and 1933.
        TimeSeries fishCount = ngm.countHistory("fish", 1850, 1933);
        assertThat(fishCount.get(1865)).isWithin(1E-10).of(136497.0);
        assertThat(fishCount.get(1922)).isWithin(1E-10).of(444924.0);

        TimeSeries totalCounts = ngm.totalCountHistory();
        assertThat(totalCounts.get(1865)).isWithin(1E-10).of(2563919231.0);

        // returns the relative weight of the word fish in each year between 1850 and 1933.
        TimeSeries fishWeight = ngm.weightHistory("fish", 1850, 1933);
        assertThat(fishWeight.get(1865)).isWithin(1E-7).of(136497.0/2563919231.0);

        TimeSeries dogCount = ngm.countHistory("dog", 1850, 1876);
        assertThat(dogCount.get(1865)).isWithin(1E-10).of(75819.0);

        List<String> fishAndDog = new ArrayList<>();
        fishAndDog.add("fish");
        fishAndDog.add("dog");
        TimeSeries fishPlusDogWeight = ngm.summedWeightHistory(fishAndDog, 1865, 1866);

        double expectedFishPlusDogWeight1865 = (136497.0 + 75819.0) / 2563919231.0;
        assertThat(fishPlusDogWeight.get(1865)).isWithin(1E-10).of(expectedFishPlusDogWeight1865);
    }

    @Test
    public void testWeightHistoryBasic() {
        NGramMap ngm = new NGramMap(SHORT_WORDS_FILE, TOTAL_COUNTS_FILE);

        // 测试单个词的相对频率
        TimeSeries requestWeight = ngm.weightHistory("request", 2005, 2008);
        TimeSeries requestCount = ngm.countHistory("request", 2005, 2008);
        TimeSeries totalCounts = ngm.totalCountHistory();

        // 验证相对频率计算正确：count / total
        for (int year : requestWeight.years()) {
            double expectedWeight = requestCount.get(year) / totalCounts.get(year);
            assertThat(requestWeight.get(year)).isWithin(1E-10).of(expectedWeight);
        }
    }

    @Test
    public void testSummedWeightHistoryMultipleWords() {
        NGramMap ngm = new NGramMap(TOP_14337_WORDS_FILE, TOTAL_COUNTS_FILE);

        List<String> words = Arrays.asList("fish", "dog", "cat");
        TimeSeries summedWeight = ngm.summedWeightHistory(words, 1865, 1865);

        // 计算期望值：三个词在1865年的相对频率之和
        TimeSeries fishWeight = ngm.weightHistory("fish", 1865, 1865);
        TimeSeries dogWeight = ngm.weightHistory("dog", 1865, 1865);
        TimeSeries catWeight = ngm.weightHistory("cat", 1865, 1865);

        double expectedSum = 0.0;
        if (!fishWeight.isEmpty()) expectedSum += fishWeight.get(1865);
        if (!dogWeight.isEmpty()) expectedSum += dogWeight.get(1865);
        if (!catWeight.isEmpty()) expectedSum += catWeight.get(1865);

        assertThat(summedWeight.get(1865)).isWithin(1E-10).of(expectedSum);
    }

    @Test
    public void testTotalCountHistoryDefensiveCopy() {
        NGramMap ngm = new NGramMap(SHORT_WORDS_FILE, TOTAL_COUNTS_FILE);

        TimeSeries original = ngm.totalCountHistory();
        TimeSeries copy = ngm.totalCountHistory();

        // 修改拷贝不应该影响原始数据
        if (!copy.isEmpty()) {
            int firstYear = copy.years().get(0);
            double originalValue = copy.get(firstYear);
            copy.put(firstYear, 999999.0); // 修改拷贝

            // 原始对象应该保持不变
            assertThat(original.get(firstYear)).isWithin(1E-10).of(originalValue);
        }
    }

    @Test
    public void testNonExistentWord() {
        NGramMap ngm = new NGramMap(SHORT_WORDS_FILE, TOTAL_COUNTS_FILE);

        // 测试不存在的单词
        TimeSeries nonExistentCount = ngm.countHistory("nonexistentword");
        TimeSeries nonExistentWeight = ngm.weightHistory("nonexistentword");

        // 应该返回空TimeSeries
        assertThat(nonExistentCount.years()).isEmpty();
        assertThat(nonExistentWeight.years()).isEmpty();
    }

    @Test
    public void testYearRangeFiltering() {
        NGramMap ngm = new NGramMap(SHORT_WORDS_FILE, TOTAL_COUNTS_FILE);

        // 测试年份范围过滤
        TimeSeries fullRange = ngm.countHistory("request");
        TimeSeries partialRange = ngm.countHistory("request", 2006, 2007);

        // 部分范围应该只包含指定年份
        assertThat(partialRange.years()).containsExactly(2006, 2007);

        // 验证数据正确性
        for (int year : partialRange.years()) {
            assertThat(partialRange.get(year)).isWithin(1E-10).of(fullRange.get(year));
        }
    }

    @Test
    public void testWeightHistoryConsistency() {
        NGramMap ngm = new NGramMap(TOP_14337_WORDS_FILE, TOTAL_COUNTS_FILE);

        // 测试weightHistory两种重载方法的一致性
        TimeSeries fishWeightFull = ngm.weightHistory("fish");
        TimeSeries fishWeightPartial = ngm.weightHistory("fish", 1900, 1910);

        // 部分范围应该是全范围的子集
        for (int year : fishWeightPartial.years()) {
            assertThat(fishWeightPartial.get(year)).isWithin(1E-10).of(fishWeightFull.get(year));
            assertThat(year).isAtLeast(1900);
            assertThat(year).isAtMost(1910);
        }
    }
}  