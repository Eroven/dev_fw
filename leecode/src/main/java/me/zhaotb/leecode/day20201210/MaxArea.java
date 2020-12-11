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

    private static final char ZERO = '0';

    public int maximalSquare(char[][] matrix) {

        int max = 0;

        int x = 0;
        int y = 0;
        while (possibleMax(matrix, x, y) > max){
            if (matrix[x][y] == ZERO){

            }

        }


        return 0;
    }

    private int max(char[][] matrix, int x, int y) {
        if (matrix[x][y] == ZERO){
            int max1 = max(matrix, x + 1, y);
            int max2 = max(matrix, x, y + 1);
            return Math.max(max1, max2);
        } else {
            int area = 1;
            int possibleMax = possibleMax(matrix, x, y);
            for (int i = 0; i < possibleMax; i++) {
                if (matrix[x + 1][y] != ZERO && matrix[x][y + 1] != ZERO && matrix[x+1][y+1] != ZERO){
                    area = 4;
                }

            }
        }
        return 0;
    }


    private int possibleMax(char[][] matrix, int x, int y) {
        int xLeft = matrix.length - x;
        int yLeft = matrix[0].length - y;
        return Math.min(xLeft, yLeft);
    }


}
