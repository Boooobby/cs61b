import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root;
    private int size;

    private class Node {
        private final K key;
        private V value;
        private Node left, right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            left = right = null;
        }
    }

    private class BSTMapIterator implements Iterator<K> {

        private final List<K> lst;
        private int cursor;

        public BSTMapIterator() {
            cursor = 0;
            lst = new ArrayList<>();
            traverse(root);
        }

        private void traverse(Node r) {
            if (r == null) {
                return;
            }
            traverse(r.left);
            lst.add(r.key);
            traverse(r.right);
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return cursor < lst.size();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public K next() {
            return lst.get(cursor++);
        }
    }

    public BSTMap() {
        root = null;
        size = 0;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map already contains the specified key, replaces the key's mapping
     * with the value specified.
     *
     * @param key
     * @param value
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(root, key, value);
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     *
     * @param key
     */
    @Override
    public V get(K key) {
        Node n = findPlace(root, key);
        if (n == null) {
            return null;
        } else {
            return n.value;
        }
    }

    /**
     * Returns whether this map contains a mapping for the specified key.
     *
     * @param key
     */
    @Override
    public boolean containsKey(K key) {
        return findPlace(root, key) != null;
    }

    /**
     * Returns the number of key-value mappings in this map.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Removes every mapping from this map.
     */
    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    /**
     * Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException.
     */
    @Override
    public Set<K> keySet() {
        Set<K> set = new TreeSet<>();
        for (K k : this) {
            set.add(k);
        }
        return set;
    }

    /**
     * Removes the mapping for the specified key from this map if present,
     * or null if there is no such mapping.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException.
     *
     * @param key
     */
    @Override
    public V remove(K key) {
        return null;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }

    // private helper methods
    private Node findPlace(Node n, K k) {
        if (n == null) {
            return null;
        }

        int cmp = k.compareTo(n.key);
        if (cmp == 0) {
            return n;
        } else if (cmp < 0) {
            return findPlace(n.left, k);
        } else {
            return findPlace(n.right, k);
        }
    }

    private Node putHelper(Node n, K k, V v) {
        if (n == null) {
            size++;
            return new Node(k, v);
        }

        int cmp = k.compareTo(n.key);
        if (cmp < 0) {
            n.left = putHelper(n.left, k, v);
        } else if (cmp > 0) {
            n.right = putHelper(n.right, k, v);
        } else {
            n.value = v;
        }
        return n;
    }
}
