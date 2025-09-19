import deque.Deque61B;
import org.junit.jupiter.api.*;

import java.util.Comparator;
import deque.MaxArrayDeque61B;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class MaxArrayDeque61BTest {
    private static class StringLengthComparator implements Comparator<String> {
        public int compare(String a, String b) {
            return a.length() - b.length();
        }
    }

    @Test
    public void basicTest() {
        MaxArrayDeque61B<String> mad = new MaxArrayDeque61B<>(new StringLengthComparator());
        mad.addFirst("");
        mad.addFirst("2");
        mad.addFirst("fury road");
        assertThat(mad.max()).isEqualTo("fury road");
    }

    @Test
    void testMax() {
        MaxArrayDeque61B<Integer> d = new MaxArrayDeque61B<Integer>(Comparator.naturalOrder());
        d.addFirst(1232);
        d.addFirst(100);
        d.addLast(90);

        assertThat(d.max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return len(o2) - len(o1);
            }

            private int len(Integer i) {
                return i.toString().length();
            }
        })).isEqualTo(1232);
    }
}
