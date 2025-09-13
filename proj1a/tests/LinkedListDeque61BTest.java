import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

/** Performs some basic linked list tests. */
public class LinkedListDeque61BTest {

     @Test
     /** In this test, we have three different assert statements that verify that addFirst works correctly. */
     public void addFirstTestBasic() {
         Deque61B<String> lld1 = new LinkedListDeque61B<>();

         lld1.addFirst("back"); // after this call we expect: ["back"]
         assertThat(lld1.toList()).containsExactly("back").inOrder();

         lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
         assertThat(lld1.toList()).containsExactly("middle", "back").inOrder();

         lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
         assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();

         /* Note: The first two assertThat statements aren't really necessary. For example, it's hard
            to imagine a bug in your code that would lead to ["front"] and ["front", "middle"] failing,
            but not ["front", "middle", "back"].
          */
     }

     @Test
     /** In this test, we use only one assertThat statement. IMO this test is just as good as addFirstTestBasic.
      *  In other words, the tedious work of adding the extra assertThat statements isn't worth it. */
     public void addLastTestBasic() {
         Deque61B<String> lld1 = new LinkedListDeque61B<>();

         lld1.addLast("front"); // after this call we expect: ["front"]
         lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
         lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
         assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();
     }

     @Test
     /** This test performs interspersed addFirst and addLast calls. */
     public void addFirstAndAddLastTest() {
         Deque61B<Integer> lld1 = new LinkedListDeque61B<>();

         /* I've decided to add in comments the state after each call for the convenience of the
            person reading this test. Some programmers might consider this excessively verbose. */
         lld1.addLast(0);   // [0]
         lld1.addLast(1);   // [0, 1]
         lld1.addFirst(-1); // [-1, 0, 1]
         lld1.addLast(2);   // [-1, 0, 1, 2]
         lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

         assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
     }

    // Below, you'll write your own tests for LinkedListDeque61B.
    @Test
    public void addDouble() {
         Deque61B<Double> d = new LinkedListDeque61B<>();

         d.addFirst(1.32);
         d.addLast(9.28);
         d.addFirst(0.44);
         d.addLast(-0.12);
         d.addFirst(0.00);

         assertThat(d.toList()).containsExactly(0.00, 0.44, 1.32, 9.28, -0.12).inOrder();
    }

    @Test
    /** 测试空队列的情况 */
    public void testEmptyDeque() {
        Deque61B<String> lld = new LinkedListDeque61B<>();

        assertThat(lld.isEmpty()).isTrue();
        assertThat(lld.size()).isEqualTo(0);
        assertThat(lld.toList()).isEmpty();
    }

    @Test
    /** 测试单元素队列的情况 */
    public void testSingleElementDeque() {
        Deque61B<Integer> lld = new LinkedListDeque61B<>();

        lld.addFirst(42);
        assertThat(lld.isEmpty()).isFalse();
        assertThat(lld.size()).isEqualTo(1);
        assertThat(lld.toList()).containsExactly(42);
    }

    @Test
    /** 测试混合操作 */
    public void testMixedOperations() {
        Deque61B<Character> lld = new LinkedListDeque61B<>();

        // 初始添加
        lld.addFirst('b');
        lld.addLast('c');
        lld.addFirst('a');

        assertThat(lld.toList()).containsExactly('a', 'b', 'c').inOrder();

        // 移除操作
        assertThat(lld.removeFirst()).isEqualTo('a');
        assertThat(lld.removeLast()).isEqualTo('c');

        assertThat(lld.toList()).containsExactly('b');

        // 再次添加
        lld.addFirst('x');
        lld.addLast('y');

        assertThat(lld.toList()).containsExactly('x', 'b', 'y').inOrder();
        assertThat(lld.size()).isEqualTo(3);
    }

    @Test
    /** 测试大型队列 */
    public void testLargeDeque() {
        Deque61B<Integer> lld = new LinkedListDeque61B<>();
        int size = 1000;

        for (int i = 0; i < size; i++) {
            if (i % 2 == 0) {
                lld.addFirst(i);
            } else {
                lld.addLast(i);
            }
        }

        assertThat(lld.size()).isEqualTo(size);

        // 验证顺序
        for (int i = 0; i < size; i++) {
            assertThat(lld.get(i)).isNotNull();
        }
    }

    @Test
    /** 测试 null 值处理 */
    public void testNullValues() {
        Deque61B<String> lld = new LinkedListDeque61B<>();

        lld.addFirst(null);
        lld.addLast("not null");
        lld.addFirst(null);

        assertThat(lld.size()).isEqualTo(3);
        assertThat(lld.get(0)).isNull();
        assertThat(lld.get(1)).isNull();
        assertThat(lld.get(2)).isEqualTo("not null");

        assertThat(lld.removeFirst()).isNull();
        assertThat(lld.removeLast()).isEqualTo("not null");
    }
}