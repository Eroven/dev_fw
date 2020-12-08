package me.zhaotb.leecode.day20201203;


/**
 * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
 * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
 * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/add-two-numbers
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * 示例：
 * <p>
 * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
 * 输出：7 -> 0 -> 8
 * 原因：342 + 465 = 807
 *
 * @author zhaotangbo
 * @since 2020/12/3
 */
public class LinkedNumber {

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode res = new ListNode();
        ListNode tmp = res;
        int t = 0;//进位
        while (l1 != null) {
            tmp.next = new ListNode();
            tmp = tmp.next;
            if (l2 != null) {//一样长
                int val = l1.val + l2.val + t;
                tmp.val = val % 10;
                t = val / 10;
                l1 = l1.next;
                l2 = l2.next;
            } else {//l1比较长
                int val = l1.val + t;
                tmp.val = val % 10;
                t = val / 10;
                l1 = l1.next;
            }
        }
        while (l2 != null) {//l2比较长
            tmp.next = new ListNode();
            tmp = tmp.next;
            int val = l2.val + t;
            tmp.val = val % 10;
            t = val / 10;
            l2 = l2.next;
        }
        if (t > 0) {
            tmp.next = new ListNode();
            tmp.next.val = t;
        }

        return res.next;
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        public ListNode(int val) {
            this.val = val;
        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        @Override
        public String toString() {
            ListNode tmp = this;
            StringBuilder sb = new StringBuilder("[");
            sb.append(val);
            while (tmp.next != null) {
                tmp = tmp.next;
                sb.append(" ,").append(tmp.val);
            }
            sb.append("]");
            return sb.toString();
        }
    }
}
