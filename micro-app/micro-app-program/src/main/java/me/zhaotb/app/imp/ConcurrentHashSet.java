package me.zhaotb.app.imp;

import com.sun.istack.internal.NotNull;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhaotangbo
 * @date 2018/12/18
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> {

    private static final Object PRESENT = new Object();
    private ConcurrentHashMap<E, Object> hashMap = new ConcurrentHashMap<>();

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return hashMap.keySet().iterator();
    }

    @Override
    public boolean add(E e) {
        return hashMap.put(e, PRESENT) == null;
    }

    @Override
    public boolean contains(Object o) {
        return hashMap.get(o) != null;
    }

    @Override
    public int size() {
        return hashMap.size();
    }
}
