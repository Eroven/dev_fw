package me.zhaotb.leecode;


import me.zhaotb.leecode.day20201203.LinkedNumber;
import me.zhaotb.leecode.day20201203.MaxLengthOfUnduplicationString;
import me.zhaotb.leecode.day20201203.MidNumber;
import me.zhaotb.leecode.day20201203.PrimeNumber;
import me.zhaotb.leecode.day20201203.LinkedNumber.ListNode;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhaotangbo
 * @since 2020/12/3
 */
public class ExecuteDay20201203Test {

    @Test
    public void testPrimeNumber() {
        PrimeNumber primeNumber = new PrimeNumber();
        System.out.println(primeNumber.countPrimes(2));
        System.out.println(primeNumber.countPrimes(10));
        System.out.println(primeNumber.countPrimes(20));
        System.out.println(primeNumber.countPrimes(100000));
    }

    @Test
    public void testLinkedNumber() {
        ListNode n3 = new ListNode(3);
        ListNode n4 = new ListNode(4, n3);
        ListNode n2 = new ListNode(2, n4);

        ListNode m4 = new ListNode(4);
        ListNode m6 = new ListNode(6, m4);
        ListNode m5 = new ListNode(5, m6);

        LinkedNumber linkedNumber = new LinkedNumber();
        ListNode listNode = linkedNumber.addTwoNumbers(n2, m5);
        System.out.println(listNode);
    }

    @Test
    public void testMaxLengthString() {
        MaxLengthOfUnduplicationString max = new MaxLengthOfUnduplicationString();
//        System.out.println(max.lengthOfLongestSubstring("abcabcbb"));
//        System.out.println(max.lengthOfLongestSubstring("bbbbb"));
//        System.out.println(max.lengthOfLongestSubstring("pwwkew"));
//        System.out.println(max.lengthOfLongestSubstring(""));
//        System.out.println(max.lengthOfLongestSubstring(" "));
        System.out.println(max.lengthOfLongestSubstring("dvdf"));
    }

    @Test
    public void testMidNumber() {
        MidNumber midNumber = new MidNumber();

        int[] nums1 = new int[]{1, 7, 11};
        int[] nums2 = new int[]{17, 25, 33};
        Assert.assertEquals(14D, midNumber.findMedianSortedArrays(nums1, nums2), 0);
//        Assert.assertEquals(14D , midNumber.findMedianSortedArrays(nums2, nums1), 0);

        int[] nums3 = new int[]{1, 7, 11, 15};
        Assert.assertEquals(15D, midNumber.findMedianSortedArrays(nums2, nums3), 0);
        Assert.assertEquals(15D, midNumber.findMedianSortedArrays(nums3, nums2), 0);

        int[] nums4 = new int[]{1, 7, 11, 15, 19};
        Assert.assertEquals(16D, midNumber.findMedianSortedArrays(nums2, nums4), 0);
        Assert.assertEquals(16D, midNumber.findMedianSortedArrays(nums4, nums2), 0);

        int[] nums5 = new int[]{19, 25, 33, 44};
        Assert.assertEquals(17D, midNumber.findMedianSortedArrays(nums3, nums5), 0);
//        Assert.assertEquals(16D , midNumber.findMedianSortedArrays(nums5, nums3), 0);
    }

    @Test
    public void testMidNumber2() {
        MidNumber midNumber = new MidNumber();
        int[] nums1 = new int[]{1, 2};
        int[] nums2 = new int[]{3, 4};
        Assert.assertEquals(2.5, midNumber.findMedianSortedArrays2(nums1, nums2), 0);
        Assert.assertEquals(2.5, midNumber.findMedianSortedArrays2(nums2, nums1), 0);

        int[] nums3 = new int[]{};
        int[] nums4 = new int[]{1};
        Assert.assertEquals(1, midNumber.findMedianSortedArrays2(nums3, nums4), 0);

        int[] nums5 = new int[]{0, 0, 0, 0, 0};
        int[] nums6 = new int[]{-1, 0, 0, 0, 0, 1, 1};
        Assert.assertEquals(0, midNumber.findMedianSortedArrays2(nums5, nums6), 0);

        int[] nums7 = new int[]{-1, 0, 0, 0, 1};
        int[] nums8 = new int[]{0, 0, 0, 0, 0, 0, 0};
        Assert.assertEquals(0, midNumber.findMedianSortedArrays2(nums7, nums8), 0);

        int[] nums9 = new int[]{1, 3};
        int[] nums10 = new int[]{2, 7};
        Assert.assertEquals(2.5, midNumber.findMedianSortedArrays2(nums9, nums10), 0);

        int[] nums11 = new int[]{};
        int[] nums12 = new int[]{2, 3};
        Assert.assertEquals(2.5, midNumber.findMedianSortedArrays2(nums11, nums12), 0);

        int[] nums13 = new int[]{};
        int[] nums14 = new int[]{2, 3, 7, 8};
        Assert.assertEquals(5, midNumber.findMedianSortedArrays2(nums13, nums14), 0);

        int[] nums15 = new int[]{1, 2};
        int[] nums16 = new int[]{-1, 3};
        Assert.assertEquals(1.5D, midNumber.findMedianSortedArrays2(nums15, nums16), 0);

        int[] nums17 = new int[]{1};
        int[] nums18 = new int[]{2, 3, 4};
        Assert.assertEquals(2.5D, midNumber.findMedianSortedArrays2(nums17, nums18), 0);


    }


}
