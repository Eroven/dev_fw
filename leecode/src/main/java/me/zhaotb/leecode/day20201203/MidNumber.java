package me.zhaotb.leecode.day20201203;


/**
 * 给定两个大小为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。请你找出并返回这两个正序数组的中位数。
 * <p>
 * 进阶：你能设计一个时间复杂度为 O(log (m+n)) 的算法解决此问题吗？
 * <p>
 * 示例 1：
 * <p>
 * 输入：nums1 = [1,3], nums2 = [2]
 * 输出：2.00000
 * 解释：合并数组 = [1,2,3] ，中位数 2
 * <p>
 * 示例 2：
 * <p>
 * 输入：nums1 = [1,2], nums2 = [3,4]
 * 输出：2.50000
 * 解释：合并数组 = [1,2,3,4] ，中位数 (2 + 3) / 2 = 2.5
 * <p>
 * 示例 3：
 * <p>
 * 输入：nums1 = [0,0], nums2 = [0,0]
 * 输出：0.00000
 * <p>
 * 示例 4：
 * <p>
 * 输入：nums1 = [], nums2 = [1]
 * 输出：1.00000
 * <p>
 * 示例 5：
 * <p>
 * 输入：nums1 = [2], nums2 = []
 * 输出：2.00000
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * nums1.length == m
 * nums2.length == n
 * 0 <= m <= 1000
 * 0 <= n <= 1000
 * 1 <= m + n <= 2000
 * -106 <= nums1[i], nums2[i] <= 106
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/median-of-two-sorted-arrays
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/median-of-two-sorted-arrays
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author zhaotangbo
 * @since 2020/12/3
 */
public class MidNumber {

    //解题思路：从左边开始从小到打逐个数数，知道取到中位数。时间复杂度为O((m+n)/2)，即 O(m+n)
    public double findMedianSortedArrays2(int[] nums1, int[] nums2) {
        if (nums1 == null || nums2 == null) throw new IllegalArgumentException("传入数组不能为null");
        if (nums1.length < 1 && nums2.length < 1) throw new IllegalArgumentException("两个数组都为空数组，无法判定中位数");
        if (nums1.length < 1 || nums2.length < 1) {//单个为空的情况，直接取另外一个数组的中位数
            int[] tmp = nums1.length > 0 ? nums1 : nums2;
            if (tmp.length % 2 > 0) {
                return tmp[tmp.length / 2];
            } else {
                return (tmp[tmp.length / 2 - 1] + tmp[tmp.length / 2]) / 2D;
            }
        }

        int length = nums1.length + nums2.length;
        boolean isOdd = length % 2 > 0;//是否为奇数
        int half = length / 2 + 1;//往后数这么多个数字
        int[] val = new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE};
        int idx1 = 0;
        int idx2 = 0;
        for (int i = 0; i < half; i++) {
            if (idx2 >= nums2.length) {
                val[i % 2] = nums1[idx1 ++];
            } else if (idx1 >= nums1.length) {
                val[i % 2] = nums2[idx2 ++];
            } else if (nums1[idx1] < nums2[idx2]) {
                val[i % 2] = nums1[idx1 ++];
            } else {
                val[i % 2] = nums2[idx2 ++];
            }
        }

        if (isOdd) {
            return Math.max(val[0], val[1]);
        } else {
            return (val[0] + val[1]) / 2D;
        }
    }

    //O(log n+m)
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1 == null || nums2 == null) throw new IllegalArgumentException("传入数组不能为null");
        if (nums1.length < 1 && nums2.length < 1) throw new IllegalArgumentException("两个数组都为空数组，无法判定中位数");
        if (nums1.length < 1 || nums2.length < 1) {//单个为空的情况，直接取另外一个数组的中位数
            int[] tmp = nums1.length > 0 ? nums1 : nums2;
            if (tmp.length % 2 > 0) {
                return tmp[tmp.length / 2];
            } else {
                return (tmp[tmp.length / 2 - 1] + tmp[tmp.length / 2]) / 2D;
            }
        }
        if (nums1.length < nums2.length) {//保证nums1的长度大于等于nums2,减少移动次数
           return findMedianSortedArrays(nums2, nums1);
        }

        //找到nums1的中位数
        boolean isOdd = (nums1.length + nums2.length) % 2 > 0;//组合后判断奇偶
        int idx1 = (nums1.length) / 2;
        int idx2 = binarySearch(nums2, nums1[idx1]);
        int right = nums2.length - idx2;//右边的个数
        int left = idx2;//左边的个数
        int offset = Math.abs(right - left);
        int coe = right > left ? 1 : -1;
        int[] val = new int[]{nums1[idx1], nums1[idx1]};
        for (int j = 0; j < offset; j++) {
            if (nums1[idx1] > nums2[idx2]){
                if (coe > 0){
                    val[j % 2] = nums2[idx2 ++];
                } else {
                    val[j % 2] = nums1[idx1 --];
                }
            }
            val [j % 2] = nums1[idx1];
            if (idx1 == -1) {
                idx2 += coe;
            } else {
                if (nums1[idx1] < nums2[idx2]) {
                    if (idx1 + 1 < nums1.length) {
                        idx1 += 1;
                    } else {
                        idx1 = -1;//中位数已经跟第一个数组没关系了
                    }
                } else {
                    idx2 += 1;
                }
            }
        }
        if (idx1 == -1) {
            if (isOdd) {
                return nums2[idx2];
            } else {
                return (1.0D * nums2[idx2] + nums2[idx2 + 1]) / 2;
            }
        }
        if (isOdd) {
            if (nums1[idx1] < nums2[idx2]) {
                return nums1[idx1];
            } else {
                return nums2[idx2];
            }
        } else {
            return (1.0D * nums1[idx1] + nums2[idx2]) / 2;
        }

    }

    //O(log m)
    public int binarySearch(int[] a, int key) {
        int low = 0;
        int high = a.length - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = a[mid];

            if (midVal < key)
                low = mid + 1;
            else if (midVal > key)
                high = mid - 1;
            else
                return mid;
        }
        // 找不到返回低位近似值
        if (high < 0) return low;
        if (low >= a.length) return high;
        return low;
    }


}
