package deque;

import net.sf.saxon.functions.Minimax;

import java.util.*;

public class MaxArrayDeque61B<T> extends ArrayDeque61B<T> {

    private Comparator<T> comparator;

    public MaxArrayDeque61B(Comparator<T> c) {
        super();
        comparator = c;
    }

    public T max() {
        if (this.size() == 0) {
            return null;
        }

        T res = null;
        for (T item : this) {
            if (res == null || comparator.compare(res, item) < 0) {
                res = item;
            }
        }
        return res;
    }

    public T max(Comparator<T> c) {
        if (this.size() == 0) {
            return null;
        }

        T res = null;
        for (T item : this) {
            if (res == null || c.compare(res, item) > 0) {
                res = item;
            }
        }
        return res;
    }
}
