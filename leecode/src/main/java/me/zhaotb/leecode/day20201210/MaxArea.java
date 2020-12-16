package me.zhaotb.leecode.day20201210;


/**
 * 在一个由 '0' 和 '1' 组成的二维矩阵内，找到只包含 '1' 的最大正方形，并返回其面积。
 * 示例 1：
 * <p>
 * 输入：matrix = [["1","0","1","0","0"],["1","0","1","1","1"],["1","1","1","1","1"],["1","0","0","1","0"]]
 * 输出：4
 * 提示：
 * <p>
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 300
 * matrix[i][j] 为 '0' 或 '1'
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/maximal-square
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author zhaotangbo
 * @since 2020/12/10
 */
public class MaxArea {

    public int maximalSquare(char[][] matrix) {
        char one = '1';
        int wide = matrix.length;
        int height = matrix[0].length;
        int max = 0;
        int[][] dp = new int[wide][height];
        for (int i = 0; i < wide; i++) {
            if (matrix[i][0] == one) {
                dp[i][0] = 1;
                max = 1;
            }
        }
        for (int i = 0; i < height; i++) {
            if (matrix[0][i] == one) {
                dp[0][i] = 1;
                max = 1;
            }
        }
        for (int i = 1; i < wide; i++) {
            for (int j = 1; j < height; j++) {
                if (matrix[i][j] == one) {
                    dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
                    if (dp[i][j] > max) max = dp[i][j];
                }

            }
        }

        return max * max;
    }


}
