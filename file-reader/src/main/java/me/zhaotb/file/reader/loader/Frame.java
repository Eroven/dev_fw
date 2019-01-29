package me.zhaotb.file.reader.loader;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * 将多行字符串保存为一个Frame, 不同Frame可以相互操作
 */
public class Frame implements Serializable,Iterable<Pair<Integer, String>> {

    private Map<Integer, String> data;
    transient private int cursor;
    private volatile int limit;

    public Frame(int initialCapacity) {
        data = new HashMap<>(initialCapacity);
        cursor = 0;
    }

    void setData(int lineNumber, String lineData){
        data.put(lineNumber, lineData);
        limit ++;
    }

    public String getData(int lineNumber){
        return data.get(lineNumber);
    }

    public int size(){
        return data.size();
    }

    /**
     * 将两个Frame 合并成第三个Frame
     * @param other 合并目标
     * @return 合并后的Frame
     */
    public Frame merge(Frame other){
        int newSize = this.data.size() + other.data.size();
        Frame frame = new Frame(newSize);
        frame.data.putAll(this.data);
        frame.data.putAll(other.data);
        frame.limit = frame.data.size();
        return frame;
    }

    /**
     * Returns an iterator over elements of type {@code Pair<Integer, String>}.
     *
     * @return 一个行数据的迭代器, 数据内容是键值对, 键是行号, 值是该行文本
     */
    @Override
    public Iterator<Pair<Integer, String>> iterator() {
        Set<Integer> lineNumbers = data.keySet();
        ArrayList<Integer> list = new ArrayList<>(lineNumbers);
        Collections.sort(list);
        return new LineDataIterator(list);
    }

    private class LineDataIterator implements Iterator<Pair<Integer, String>> {

        private List<Integer> lineNumbers;
        private int itCursor = 0;
        /**
         * 行号
         */
        private int ln = 0;

        private LineDataIterator(List<Integer> lineNumbers) {
            this.lineNumbers = lineNumbers;
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
            return itCursor < limit;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public Pair<Integer, String> next() {
            if (!hasNext()){
                throw new NoSuchElementException();
            }
            Integer lineNumber = lineNumbers.get(cursor++);
            String lineData = data.get(lineNumber);
            return new Pair<>(lineNumber, lineData);
        }
    }
}
