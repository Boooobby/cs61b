package deque;

import java.util.*;

public class ArrayDeque61B<T> implements Deque61B<T> {

    private T[] arr;
    private int size, arrSize;
    private int head, tail;

    private class ArrayDequeIterator implements Iterator<T> {

        private int cursor = Math.floorMod(head - 1, arrSize);

        @Override
        public boolean hasNext() {
            return cursor != tail;
        }

        @Override
        public T next() {
            cursor = Math.floorMod(cursor + 1, arrSize);
            return get(cursor);
        }
    }

    public ArrayDeque61B() {
        arr = (T[]) new Object[8];
        arrSize = 8;
        size = 0;
        head = 0;
        tail = arrSize - 1;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof ArrayDeque61B<?> arr2 && size == arr2.size()) {
            Iterator<T> it1 = iterator();
            Iterator<?> it2 = arr2.iterator();

            while (it1.hasNext()) {
                if (!it1.next().equals(it2.next())) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addFirst(T x) {
        if (size == arrSize) {
            arr = resizingUp();
        }
        head = Math.floorMod(head - 1, arrSize);
        arr[head] = x;
        size++;
    }

    @Override
    public void addLast(T x) {
        if (size == arrSize) {
            arr = resizingUp();
        }
        tail = Math.floorMod(tail + 1, arrSize);
        arr[tail] = x;
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        if (this.size == 0) {
            return returnList;
        }

        for (int i = head; i != tail; i = Math.floorMod(i + 1, arrSize)) {
            returnList.add(get(i));
        }
        returnList.add(get(tail));
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
        if (size == 0) {
            return null;
        } else if (arrSize > 16 && arrSize / 4 > size) {
            arr = resizingDown();
        }
        T returnItem = get(head);
        head = Math.floorMod(head + 1, arrSize);
        return returnItem;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        } else if (arrSize > 16 && arrSize / 4 > size) {
            arr = resizingDown();
        }
        T returnItem = get(tail);
        tail = Math.floorMod(tail - 1, arrSize);
        return returnItem;
    }

    @Override
    public T get(int index) {
        if (index >= arrSize) {
            return null;
        } else if (head == 0 && index > tail) {
            return null;
        } else if (tail == arrSize - 1 && index < head) {
            return null;
        } else if (index > tail && index < head) {
            return null;
        } else {
            return arr[index];
        }
    }

    @Override
    public T getRecursive(int index) {
        throw new UnsupportedOperationException("No need to implement getRecursive for proj 1b");
    }

    private T[] resizingUp() {
        T[] newArr = (T[]) new Object[arrSize * 2];
        int j = 0;
        for (int i = head; i != tail; i = Math.floorMod(i + 1, arrSize), j++) {
            newArr[j] = get(i);
        }
        newArr[j] = get(tail);
        arrSize *= 2;
        head = 0;
        tail = j;
        return newArr;
    }

    private T[] resizingDown() {
        T[] newArr = (T[]) new Object[arrSize / 2];
        int j = 0;
        for (int i = head; i != tail; i = Math.floorMod(i + 1, arrSize), j++) {
            newArr[j] = get(i);
        }
        newArr[j] = get(tail);
        arrSize /= 2;
        head = 0;
        tail = j;
        return newArr;
    }
}
