package me.zhaotb.framework.util.math;

/**
 * @author zhaotangbo
 * @date 2019/1/14
 */
public final class LinkedNumber implements Comparable<LinkedNumber> {

    private byte signed;//1标识负数，0标识正数
    private ValNode head;
    private int length;

    public LinkedNumber(String val) {
        char[] temp = val.toCharArray();
        if (temp.length < 1) {
            throw new IllegalArgumentException(val + " is not a number.");
        }
        int first = -1;
        if (temp[0] == '-') {
            signed = 1;
            first = 0;
        } else if (temp[0] == '+') {
            signed = 0;
            first = 0;
        }

        boolean zero = true;
        char[] chars = new char[temp.length];
        for (int i = first + 1; i < temp.length; i++) {
            if (!Character.isDigit(temp[i])) {
                throw new IllegalArgumentException(val + " is not a number for index : " + i);
            }
            if (zero && '0' == temp[i]) {
                continue;
            }
            zero = false;
            chars[length++] = temp[i];
        }

        if (length == 0) {
            chars[length++] = '0';
        }

        head = new ValNode(Byte.parseByte(String.valueOf(chars[length - 1])));
        ValNode tmp = head;
        for (int i = length - 2; i > -1; i--) {
            tmp.next = new ValNode(Byte.parseByte(String.valueOf(chars[i])));
            tmp = tmp.next;
        }

    }

    private LinkedNumber(ValNode head) {
        this(head, (byte) 0);
    }

    private LinkedNumber(ValNode head, byte signed) {
        this.signed = signed;
        this.head = head;
        ValNode tmp = head;
        while (tmp != null) {
            length++;
            tmp = tmp.next;
        }
    }

    private String value(boolean signed) {
        StringBuilder sb = new StringBuilder();
        ValNode tmp = head;
        while (tmp != null) {
            sb.append(tmp.val);
            tmp = tmp.next;
        }
        if (signed) {
            sb.append(this.signed == 1 ? "-" : "");
        }
        return sb.reverse().toString();
    }

    @Override
    public String toString() {
        return value(true);
    }

    public int getLength() {
        return length;
    }

    @Override
    public int compareTo(LinkedNumber o) {
        if (o == null) {
            return -1;
        }
        if (this == o) {
            return 1;
        }
        if (this.signed == 1) {
            if (o.signed == 0) return -1;
            else return -compareToUnsigned(o);
        } else {
            if (o.signed == 1) return 1;
            else return compareToUnsigned(o);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof LinkedNumber) {
            return this.compareTo((LinkedNumber) obj) == 0;
        } else return false;
    }

    /**
     * 无符号比较，即绝对值比较大小
     *
     * @param o 另一个数值
     * @return 返回值大于0 标识大于， 等于0 标识等于， 小于0 标识小于
     */
    private int compareToUnsigned(LinkedNumber o) {
        return this.length > o.length ? 1 : this.length < o.length ? -1 : this.value(false).compareTo(o.value(false));
    }

    public LinkedNumber add(LinkedNumber another) {
        if (this.signed == 0) {
            if (another.signed == 0) return new LinkedNumber(addUnsigned(another.head));
            else {
                if (compareToUnsigned(another) >= 0) return new LinkedNumber(subUnsigned(another.head));
                else return new LinkedNumber(another.subUnsigned(this.head), (byte) 1);
            }
        } else {
            if (another.signed == 0) {
                if (compareToUnsigned(another) >= 0) return new LinkedNumber(subUnsigned(another.head), (byte) 1);
                else return new LinkedNumber(another.subUnsigned(this.head));
            } else return new LinkedNumber(addUnsigned(another.head), (byte) 1);
        }
    }

    public LinkedNumber sub(LinkedNumber another) {
        if (this.signed == 0) {
            if (another.signed == 0) {
                if (this.compareToUnsigned(another) >= 0) return new LinkedNumber(subUnsigned(another.head));
                else return new LinkedNumber(another.subUnsigned(this.head), (byte) 1);
            } else return this.add(new LinkedNumber(another.head));
        } else {
            if (another.signed == 0) return new LinkedNumber(addUnsigned(another.head), (byte) 1);
            else {
                if (this.compareToUnsigned(another) >= 0) return new LinkedNumber(subUnsigned(another.head), (byte) 1);
                else return new LinkedNumber(another.subUnsigned(this.head));
            }
        }
    }

    public LinkedNumber multiply(LinkedNumber another) {
        if (this.signed + another.signed == 1)
            return new LinkedNumber(multiplyUnsigned(another.head), (byte) 1);
        else
            return new LinkedNumber(multiplyUnsigned(another.head));
    }

    public LinkedNumber divide(LinkedNumber another){
        if (this.signed + another.signed == 1)
            return new LinkedNumber(divideUnsigned(another.head), (byte) 1);
        else
            return new LinkedNumber(divideUnsigned(another.head));

    }

    private ValNode divideUnsigned(ValNode node){
        return new NodeDecorator(this.head).divide(node);
    }

    private ValNode multiplyUnsigned(ValNode node) {
        return new NodeDecorator(this.head).multiply(node);
    }

    private ValNode addUnsigned(ValNode another) {
        return new NodeDecorator(this.head).add(another);
    }

    //总是较大的数减去较小的数
    private ValNode subUnsigned(ValNode another) {
        return new NodeDecorator(this.head).sub(another);
    }


}
