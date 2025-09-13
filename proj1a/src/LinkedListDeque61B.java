import java.util.ArrayList;
import java.util.List;

public class LinkedListDeque61B<T> implements Deque61B<T> {

    private Node sentinel;
    private int size;

    public class Node {
        private T item;
        private Node prev, next;

        public Node(T item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    public LinkedListDeque61B() {
        size = 0;
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel.prev = sentinel;
    }

    @Override
    public void addFirst(T x) {
        Node node = new Node(x, sentinel, sentinel.next);
        sentinel.next.prev = node;
        sentinel.next = node;
        size++;
    }

    @Override
    public void addLast(T x) {
        Node node = new Node(x, sentinel.prev, sentinel);
        sentinel.prev.next = node;
        sentinel.prev = node;
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        for (Node n = sentinel.next; n != sentinel; n = n.next) {
            returnList.add(n.item);
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        size--;
        Node returnNode = sentinel.next;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        return returnNode.item;
    }

    @Override
    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        size--;
        Node returnNode = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        return returnNode.item;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        int idx = 0;
        Node n = sentinel.next;
        while (idx < index) {
            n = n.next;
            idx++;
        }
        return n.item;
    }

    @Override
    public T getRecursive(int index) {
        return null;
    }
}
