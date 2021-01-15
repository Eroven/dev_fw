package me.zhaotb.leecode.day20210115;


import java.util.Stack;

/**
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 *
 *
 *
 * 示例 1：
 *
 * 输入：height = [0,1,0,2,1,0,1,3,2,1,2,1]
 * 输出：6
 * 解释：上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。
 *
 * 示例 2：
 *
 * 输入：height = [4,2,0,3,2,5]
 * 输出：9
 *
 *
 *
 * 提示：
 *
 *     n == height.length
 *     0 <= n <= 3 * 104
 *     0 <= height[i] <= 105
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/trapping-rain-water
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @author zhaotangbo
 * @date 2021/1/15
 */
public class Trap {

    /**
     * 雨水面积 = 矩形面积 - 空白面积 - 方块面积
     */
    public int trap(int[] height) {
        int blocks = 0;
        int empty = 0;
        int max = 0;
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < height.length; i++) {
            blocks += height[i];

            stack.push(height[i]);

            if (height[i] >= max) {
                stack.clear();
                empty += (height[i] - max) * i;
                max = height[i];
                stack.push(max);
            }
        }

        int min = 0;
        int width = 0;
        while (!stack.empty()) {
            Integer pop = stack.pop();
            if (pop > min) {
                empty += (pop - min) * width;
                min = pop;
            }
            width ++;
        }

        return (max * height.length) - empty - blocks;
    }

}
