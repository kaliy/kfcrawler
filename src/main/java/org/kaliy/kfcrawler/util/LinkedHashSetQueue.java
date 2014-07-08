package org.kaliy.kfcrawler.util;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Queue;

public class LinkedHashSetQueue<T> extends LinkedHashSet<T> implements Queue<T> {

    @Override
    public boolean offer(T t) {
        return add(t);
    }

    @Override
    public T remove() {
        Iterator<T> iterator = iterator();
        T element = iterator.next();
        if (null == element) {
            throw new NoSuchElementException();
        }
        iterator.remove();
        return element;
    }

    @Override
    public T poll() {
        if (isEmpty()) {
            return null;
        }
        Iterator<T> iterator = iterator();
        T element = iterator.next();
        iterator.remove();
        return element;
    }

    @Override
    public T element() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return iterator().next();
    }

    @Override
    public T peek() {
        return iterator().next();
    }
}
