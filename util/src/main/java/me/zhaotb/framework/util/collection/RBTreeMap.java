package me.zhaotb.framework.util.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 用于查询的集合
 * @author zhaotangbo
 * @date 2019/3/19
 */

public class RBTreeMap<K extends Comparable<? super K>, V> implements Map<K, V> {

    private boolean allowRepeat;

    private long count;

    public RBTreeMap(boolean allowRepeat) {
        this.allowRepeat = allowRepeat;
    }

    /**
     * 当数据超过 {@link Integer#MAX_VALUE} 时返回 {@link Integer#MAX_VALUE}
     * @see RBTreeMap#count()
     * @return 返回数据大小
     */
    @Deprecated
    @Override
    public int size() {
        if (count > Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }
        return (int) count;
    }

    /**
     *
     * @return 返回数据大小
     */
    public long count(){
        return count;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        return null;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }
}
