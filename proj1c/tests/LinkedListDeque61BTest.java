import deque.Deque61B;
import deque.LinkedListDeque61B;
import org.junit.jupiter.api.*;

import java.util.Comparator;
import deque.MaxArrayDeque61B;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class LinkedListDeque61BTest {

    @Test
    @DisplayName("Test the method iterator")
    void testIterator() {
        Deque61B<Integer> d = new LinkedListDeque61B<>();
        for (int i = 0; i < 5; i++) {
            d.addLast(i);
        }

        assertThat(d).containsExactly(0, 1, 2, 3, 4).inOrder();
    }

    @Test
    @DisplayName("Test the method equals")
    void testEquals() {
        Deque61B<String> d1 = new LinkedListDeque61B<>();
        Deque61B<String> d2 = new LinkedListDeque61B<>();
        for (int i = 0; i < 20; i++) {
            d1.addLast(Integer.valueOf(i).toString());
            d2.addLast(Integer.valueOf(i).toString());
        }

        assertThat(d1).isEqualTo(d2);
    }

    @Test
    @DisplayName("Test the method toString")
    void testToString() {
        Deque61B<Integer> d = new LinkedListDeque61B<>();
        for (int i = 100; i > 90; i--) {
            d.addFirst(i);
        }

        assertThat(d.toList().toString()).isEqualTo(d.toString());
    }

}
