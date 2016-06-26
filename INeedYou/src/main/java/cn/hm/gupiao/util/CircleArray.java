package cn.hm.gupiao.util;

import java.io.Serializable;
import java.util.*;

/**
 * Created by huangming on 2016/6/25.
 */
public class CircleArray<T> implements Collection<T> {

    private Object[] element;
    private int start_index;
    private int capSize;
    private int nowSize;

    public CircleArray(int size) {
        this.capSize = size;
        element = new Object[size];
        this.start_index = 0;
    }

    @Override
    public int size() {
        return nowSize;
    }

    @Override
    public boolean isEmpty() {
        return nowSize == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            for (int i = 0; i < element.length; i++) {
                if (element[i] == null) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < element.length; i++) {
                if (o.equals(element[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new Ltc();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(element, element.length);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < element.length)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(element, element.length, a.getClass());
        System.arraycopy(element, 0, a, 0, element.length);
        if (a.length > element.length)
            a[element.length] = null;
        return a;
    }

    @Override
    public boolean add(T t) {
        element[start_index] = t;
        if (nowSize < capSize) {
            nowSize++;
        }
        start_index++;
        start_index = start_index % capSize;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T t : c) {
            element[start_index] = t;
            start_index++;
            start_index = start_index % capSize;
        }
        return true;
    }

    public T getBefore() {
        return (T) element[(start_index - 1 + capSize) % capSize];
    }

    public T getBefore(int lastNum) {
        return (T) element[(start_index - lastNum + capSize) % capSize];
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (int i = 0; i < element.length; i++) {
            if (element[i] != null) {
                for (Object o : c) {
                    if (element[i].equals(o)) {
                        element[i] = 0;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < element.length; i++) {
            element[i] = null;
        }
    }

    private class Ltc implements Iterator<T> {
        private int innertIndex;

        public Ltc() {
            innertIndex = start_index;
        }

        @Override
        public boolean hasNext() {
            return innertIndex > start_index - capSize;
        }

        @Override
        public T next() {
            return (T) element[(innertIndex-- + capSize) % capSize];
        }
    }

}
