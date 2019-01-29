package me.zhaotb.framework.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author zhaotangbo
 * @date 2019/1/18
 */
public class FilterUtil {

    /**
     * 过滤结果，包含： 过滤后的数据，挑选结果，最大值
     *
     * @param <T>
     */
    public static class FilterResult<T> {
        private List<T> data;
        /**
         * 长度与数据源一样， 对应的下标的值表示是否保留。true：保留
         */
        private boolean[] answer;
        private int maxValue;
        private int useCapacity;

        private FilterResult() {
        }

        public List<T> getData() {
            return data;
        }

        public boolean[] getAnswers() {
            return answer;
        }

        public int getMaxValue() {
            return maxValue;
        }

        public int getUseCapacity() {
            return useCapacity;
        }

        @Override
        public String toString() {
            return "FilterResult{" +
                    "maxValue=" + maxValue +
                    ", useCapacity=" + useCapacity +
                    '}';
        }
    }

    /**
     * 递归，将可能的全部列举出来
     * @param src 数据源
     * @param vol 容量
     * @param <T> 泛型
     * @return 返回过滤结果
     */
    public static <T extends PickAble> List<FilterResult<T>> filterNormal(List<T> src, int vol){
        Node head = new Node(0, 0);

        ArrayList<Node> leafNodes = new ArrayList<>();
        recursiveFilter(src, head, 0, vol, leafNodes);

        ArrayList<Node> maxNodes = new ArrayList<>();
        int max = Integer.MIN_VALUE;
        for (Node node: leafNodes) {
            if (node.curVal > max){
                max = node.curVal;
                maxNodes.clear();
                maxNodes.add(node);
            } else if (node.curVal == max){
                maxNodes.add(node);
            }
        }

        ArrayList<FilterResult<T>> list = new ArrayList<>();
        FilterResult<T> result;
        int i;
        for (Node node: maxNodes) {
            result = new FilterResult<>();
            i = src.size();
            result.maxValue = max;
            result.data = new ArrayList<>();
            result.answer = new boolean[i];
            while (i-- > 0){
                if (node.obj != null){
                    result.answer[i] = true;
                    result.data.add((T) node.obj);
                    result.useCapacity += node.obj.weight();
                }
                node = node.pre;
            }
            list.add(result);
        }

        return list;
    }

    /**
     * 在不超过指定容量的情况下挑选出最佳结果，使其价值最高
     * 动态规划： 一个最优解；最高价值
     * @param src 数据源
     * @param vol 容量
     * @param <T> 数据对象必须继承 {@link PickAble}
     * @return 返回过滤结果
     */
    public static <T extends PickAble> FilterResult<T> filterDynamicPlanning(List<T> src, int vol) {
        int size = src.size();
        int[][] buffer = new int[size + 1][vol + 1];
        //初始化
        for (int i = 0; i < buffer.length; i++) {
            buffer[i][0] = 0;
        }
        for (int i = 1; i < buffer[0].length; i++) {
            buffer[0][i] = 0;
        }

        //动态规划计算结果（0-1背包模型）
        T obj;
        for (int i = 1; i <= size; i++) {
            obj = src.get(i - 1);
            for (int j = 1; j <= vol; j++) {
                if (obj.weight() <= j) {
                    buffer[i][j] = Math.max(buffer[i - 1][j], buffer[i - 1][j - obj.weight()] + obj.value());
                } else {
                    buffer[i][j] = buffer[i - 1][j];
                }
            }
        }

        //解释结果并返回
        boolean[] answer = new boolean[size];
        ArrayList<T> res = new ArrayList<>();
        int tVol = 0;
        for (int i = size; i > 0; i--) {
            if (buffer[i][tVol] > buffer[i - 1][tVol]) {
                answer[i - 1] = true;
                obj = src.get(i - 1);
                tVol += obj.weight();
                res.add(obj);
            }
        }

        FilterResult<T> result = new FilterResult<>();
        result.answer = answer;
        result.data = res;
        result.maxValue = buffer[size][vol];
        result.useCapacity = tVol;
        return result;
    }

    /**
     * 在不超过指定容量的情况下挑选出最佳结果，使其价值最高
     * 动态规划，优化空间复杂度 ： 只能计算出最高价值
     * @param src 数据源
     * @param vol 容量
     * @param <T> 数据对象必须继承 {@link PickAble}
     * @return 返回过滤结果
     */
    public static <T extends PickAble> FilterResult<T> filterDynamicPlanningOpt(List<T> src, int vol) {
        int size = src.size();
        int[] buffer = new int[vol + 1];
        //初始化
        T obj = src.get(0);
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = obj.weight() <= i ? obj.value() : 0;
        }

        //动态规划计算结果（0-1背包模型）
        for (int i = 1; i < size; i++) {
            obj = src.get(i);
            for (int j = vol; j >= obj.weight(); j--) {
                buffer[j] = Math.max(buffer[j], obj.value() + buffer[j - obj.weight()]);
            }
        }

        FilterResult<T> result = new FilterResult<>();
        result.maxValue = buffer[vol];
        return result;
    }

    /**
     * 贪心算法优化递归所有情况 {@link #filterNormal(List, int)}
     * @param src 数据源
     * @param vol 容量
     * @param <T> 泛型
     * @return 返回过滤结果
     */
    public static <T extends PickAble> List<FilterResult<T>> filterGreedy(List<T> src, int vol) {
        src.sort((t1, t2) -> Double.compare(1.0 * t2.value() / t2.weight(), 1.0 * t1.value() / t1.weight()));

        GreedyFilter filter = new GreedyFilter();
        Node head = new Node(0, 0);
        filter.recursiveFilterGreedy(src, head, 0, vol);
        ArrayList<FilterResult<T>> list = new ArrayList<>();
        FilterResult<T> res;
        for (Node node : filter.leafNodes) {
            res = new FilterResult<>();
            res.maxValue = filter.maxVal;
            res.data = new ArrayList<>();
            res.answer = new boolean[src.size()];
            int i = src.size();
            while (i-- > 0){
                if (node.obj != null){
                    res.data.add((T) node.obj);
                    res.useCapacity += node.obj.weight();
                    res.answer[i] = true;
                }
                node = node.pre;
            }
            list.add(res);
        }

        return list;
    }

    private static class GreedyFilter {
        private int maxVal = Integer.MIN_VALUE;
        private ArrayList<Node> leafNodes = new ArrayList<>();

        private <T extends PickAble> void recursiveFilterGreedy(List<T> src, Node last, int index, int vol) {
            if (index >= src.size()) {
                if (maxVal < last.curVal){
                    maxVal = last.curVal;
                    leafNodes.clear();//清掉不是最大值的叶子节点
                    leafNodes.add(last);
                }else if (maxVal == last.curVal){
                    leafNodes.add(last);
                }
                return;
            }
            double max = mostlyMax(src, index, vol - last.curCap);
            if (max < maxVal){//不可能有解节点，不算叶子节点
                return;
            }
            T t = src.get(index);
            if (vol >= t.weight()) {
                last.right = new Node(last.curVal + t.value(), last.curCap + t.weight());
                last.right.obj = t;
                last.right.pre = last;
                recursiveFilterGreedy(src, last.right, index + 1, vol - t.weight());
            }
            last.left = new Node(last.curVal, last.curCap);
            last.left.pre = last;
            recursiveFilterGreedy(src, last.left, index + 1, vol);
        }

        private <T extends PickAble> double mostlyMax(List<T> src, int idx, int cap){
            T t;
            double res = 0.0;
            for (int i = idx; i < src.size(); i++) {
                t = src.get(i);
                if (cap >= t.weight()){
                    cap -= t.weight();
                    res += t.value();
                }else if (cap > 0){
                    res += 1.0 * t.value() / t.weight() * cap;
                    return res;
                }
            }
            return res;
        }

    }

    private static <T extends PickAble> void loopFilter(List<T> src, int vol, Node last, ArrayList<Node> leafNodes) {
        Stack<Node> stack = new Stack<>();
        stack.push(last);
        int idx = src.size() - 1;
        while (!stack.empty()) {
            last = stack.pop();
            if (last.left != null || last.right != null) {
                idx ++;
                continue;
            }
            if (idx < 0){
                leafNodes.add(last);
                idx ++;
                continue;
            }
            if (last.left == null) {
                last.left = new Node(last.curVal, last.curCap);
                last.left.pre = last;
                stack.push(last.left);
            }

            if (last.right == null) {
                T t = src.get(idx);
                if (vol - last.curCap >= t.weight()){
                    last.right = new Node(last.curVal + t.value(), last.curCap + t.weight());
                    last.right.pre = last;
                    stack.push(last.right);
                }
            }

            idx --;
        }
    }

    /**
     *
     * @param src 数据源
     * @param last 上一个节点
     * @param index 数据源的索引
     * @param vol 当前剩余容量
     * @param leafNodes 保存的所有叶子节点
     * @param <T> 泛型
     */
    private static <T extends PickAble> void recursiveFilter(List<T> src, Node last, int index, int vol, List<Node> leafNodes) {
        if (index >= src.size()) {
            leafNodes.add(last);
            return;
        }
        T t = src.get(index);
        if (vol >= t.weight()) {
            last.right = new Node(last.curVal + t.value(), last.curCap + t.weight());
            last.right.obj = t;
            last.right.pre = last;
            recursiveFilter(src, last.right, index + 1, vol - t.weight(), leafNodes);
        }
        last.left = new Node(last.curVal, last.curCap);
        last.left.pre = last;
        recursiveFilter(src, last.left, index + 1, vol, leafNodes);
    }

    private static class Node {
        private Node(int curVal, int curCap) {
            this.curVal = curVal;
            this.curCap = curCap;
        }

        PickAble obj;
        int curVal;
        int curCap;
        Node pre;
        Node left;
        Node right;

        @Override
        public String toString() {
            return "[" + curVal + ", " + curCap + "]";
        }
    }

    public static void print(int[][] arr) {
        System.out.println("\n===============");
        int iMax = arr.length;
        int jMax = arr[0].length;
        for (int j = 0; j < jMax; j++) {
            for (int i = 0; i < iMax; i++) {
                System.out.printf("%2d  ", arr[i][j]);
            }
            System.out.println();
        }
        System.out.println("===============\n");
    }



}
