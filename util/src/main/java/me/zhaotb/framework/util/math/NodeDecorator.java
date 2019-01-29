package me.zhaotb.framework.util.math;

/**
 * @author zhaotangbo
 * @date 2019/1/15
 */
class NodeDecorator {
    private ValNode head;

    NodeDecorator(ValNode head) {
        this.head = head;
    }

    ValNode add(ValNode node) {
        ValNode tmpL = this.head;
        ValNode tmpR = node;
        byte over = 0;
        ValNode newHead = new ValNode((byte) 0);
        ValNode tmp = newHead;
        while (tmpL != null || tmpR != null) {
            byte valL = tmpL == null ? 0 : tmpL.val;
            byte valR = tmpR == null ? 0 : tmpR.val;
            byte and = (byte) (valL + valR + over);
            over = (byte) (and / 10);
            byte left = (byte) (and % 10);
            tmp.next = new ValNode(left);
            tmp = tmp.next;
            if (tmpL != null) tmpL = tmpL.next;
            if (tmpR != null) tmpR = tmpR.next;
        }
        return newHead.next;
    }

    ValNode sub(ValNode node) {
        ValNode tmpL = this.head;
        ValNode tmpR = node;

        ValNode head = new ValNode((byte) 0);
        ValNode temp = head;
        byte borrow = 0;
        while (tmpR != null) {
            byte a = (byte) (tmpL.val - borrow);
            borrow = 0;
            byte b = tmpR.val;
            byte val = (byte) (a - b);
            if (val < 0) {
                val += 10;
                borrow += 1;
            }
            temp.next = new ValNode(val);
            temp = temp.next;
            tmpL = tmpL.next;
            tmpR = tmpR.next;
        }

        while (tmpL != null) {
            temp.next = new ValNode((byte) (tmpL.val - borrow));
            borrow = 0;
            temp = temp.next;
            tmpL = tmpL.next;
        }
        trimZero(head.next);
        return head.next;
    }

    //去高位0
    private boolean trimZero(ValNode node) {
        if (node.next == null) {
            return node.val == 0;
        } else {
            boolean delete = trimZero(node.next);
            if (delete) {
                node.next = null;
                return node.val == 0;
            }
        }
        return false;
    }

    ValNode multiply(ValNode node) {
        NodeDecorator all = new NodeDecorator(new ValNode((byte) 0));
        ValNode valNode;
        long zeroNum = 0;
        while (node != null){
            valNode = multiplyUnsignedSingle(node.val);
            valNode = insertZero(valNode, zeroNum++);
            all = new NodeDecorator(all.add((valNode)));
            node = node.next;
        }
        trimZero(all.head);
        return all.head;
    }

    private ValNode insertZero(ValNode node, long zeroNum){
        ValNode temp;
        for (long i = 0; i < zeroNum; i++) {
            temp = new ValNode((byte) 0);
            temp.next = node;
            node = temp;
        }
        return node;
    }

    private ValNode multiplyUnsignedSingle(byte b){
        ValNode tmp = head;
        ValNode newHead = new ValNode((byte) 0);
        ValNode newTmp = newHead;
        byte carry = 0;
        while (tmp != null) {
            byte i = (byte) (tmp.val * b + carry);
            carry = (byte) (i / 10);
            newTmp.next = new ValNode((byte) (i % 10));
            tmp = tmp.next;
            newTmp = newTmp.next;
        }
        if (carry != 0){
            newTmp.next = new ValNode(carry);
        }
        return newHead.next;
    }

    ValNode divide(ValNode node){
        ValNode tmp = head;
        ValNode newHead = new ValNode((byte) 0);
        ValNode newTmp = newHead;
        return null;
    }
}
