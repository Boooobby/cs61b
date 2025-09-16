import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Deque;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDeque61BTest {

    @Test
    @DisplayName("ArrayDeque61B has no fields besides backing array and primitives")
    void noNonTrivialFields() {
        List<Field> badFields = Reflection.getFields(ArrayDeque61B.class)
                .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(Object[].class) || f.isSynthetic()))
                .toList();

        assertWithMessage("Found fields that are not array or primitives").that(badFields).isEmpty();
    }

    @Test
    @DisplayName("Test the methods add")
    void testAdd() {
        Deque61B<Character> d = new ArrayDeque61B<>();
        d.addFirst('b');
        d.addFirst('a');
        d.addLast('c');
        d.addLast('d');
        d.addLast('e');

        assertThat(d.toList()).containsExactly('a', 'b', 'c', 'd', 'e').inOrder();
    }

    @Test
    @DisplayName("Test the method get")
    void testGet() {
        Deque61B<Integer> d = new ArrayDeque61B<>();
        d.addFirst(5);
        d.addFirst(4);
        d.addFirst(3);
        d.addLast(6);
        d.addLast(7);

        assertThat(d.get(0)).isEqualTo(6);
        assertThat(d.get(1)).isEqualTo(7);
        assertThat(d.get(7)).isEqualTo(5);
        assertThat(d.get(6)).isEqualTo(4);
        assertThat(d.get(5)).isEqualTo(3);
    }

    @Test
    @DisplayName("Test the method isEmpty")
    void testIsEmpty() {
        Deque61B<String> d = new ArrayDeque61B<>();

        assertThat(d.isEmpty()).isTrue();

        d.addLast("123");

        assertThat(d.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("Test the method size")
    void testSize() {
        Deque61B<Double> d = new ArrayDeque61B<>();
        d.addLast(6.75);
        d.addLast(6.33);

        assertThat(d.size()).isEqualTo(2);

        d.addFirst(2.09);
        d.addFirst(0.00);

        assertThat(d.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("Test the method toList")
    void testToList() {
        Deque61B<Integer> d = new ArrayDeque61B<>();
        d.addFirst(5);
        d.addFirst(4);
        d.addFirst(3);
        d.addLast(6);
        d.addLast(7);

        assertThat(d.toList()).containsExactly(3, 4, 5, 6, 7).inOrder();
    }

    @Test
    @DisplayName("Test the methods remove")
    void testRemove() {
        Deque61B<String> d = new ArrayDeque61B<>();
        d.addFirst("world");
        d.addFirst("hello");
        d.addLast("foo");
        String a = d.removeLast();

        assertThat(d.toList()).containsExactly("hello", "world").inOrder();

        d.addFirst("baz");
        d.addFirst("bar");
        String b = d.removeFirst();
        String c = d.removeFirst();

        assertThat(d.toList()).containsExactly("hello", "world").inOrder();
        assertThat(List.of(a, b, c)).containsExactly("foo", "bar", "baz").inOrder();
    }

    @Test
    @DisplayName("Test the method resizingUp")
    void testResizingUp() {
        Deque61B<Integer> d = new ArrayDeque61B<>();
        for (int i = 0; i < 10; i++) {
            d.addLast(i);
        }

        assertThat(d.toList()).containsExactly(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).inOrder();
    }

    @Test
    @DisplayName("Test the method resizingDown")
    void testResizingDown() {
        Deque61B<Integer> d = new ArrayDeque61B<>();
        for (int i = 0; i < 100; i++) {
            d.addLast(i);
        }
        for (int i = 0; i < 95; i++) {
            d.removeLast();
        }

        assertThat(d.toList()).containsExactly(0, 1, 2, 3, 4).inOrder();
    }

}
