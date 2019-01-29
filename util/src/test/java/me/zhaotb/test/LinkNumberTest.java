package me.zhaotb.test;

import me.zhaotb.framework.util.math.LinkedNumber;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class LinkNumberTest {

    @Test
    public void value() {
        LinkedNumber a = new LinkedNumber("12345");
        System.out.println(a + " : " + a.getLength());
        LinkedNumber b = new LinkedNumber("-1");
        System.out.println(b + " : " + b.getLength());
        b = new LinkedNumber("+11");
        System.out.println(b + " : " + b.getLength());
        b = new LinkedNumber("+000");
        System.out.println(b + " : " + b.getLength());
        b = new LinkedNumber("-000");
        System.out.println(b + " : " + b.getLength());
    }

    @Test
    public void add() {
        LinkedNumber a = new LinkedNumber("123");
        LinkedNumber b = new LinkedNumber("456");
        System.out.println(a + " + " + b + " = " + a.add(b));
        a = new LinkedNumber("-123");
        b = new LinkedNumber("456");
        System.out.println(a + " + " + b + " = " + a.add(b));
        a = new LinkedNumber("123");
        b = new LinkedNumber("-456");
        System.out.println(a + " + " + b + " = " + a.add(b));
        a = new LinkedNumber("-123");
        b = new LinkedNumber("-456");
        System.out.println(a + " + " + b + " = " + a.add(b));
        a = new LinkedNumber("456");
        b = new LinkedNumber("-456");
        System.out.println(a + " + " + b + " = " + a.add(b));
        a = new LinkedNumber("-456");
        b = new LinkedNumber("456");
        System.out.println(a + " + " + b + " = " + a.add(b));
    }

    @Test
    public void sub(){
        LinkedNumber a = new LinkedNumber("123");
        LinkedNumber b = new LinkedNumber("456");
        System.out.println(a + " - " + b + " = " + a.sub(b));
        a = new LinkedNumber("-123");
        b = new LinkedNumber("456");
        System.out.println(a + " - " + b + " = " + a.sub(b));
        a = new LinkedNumber("123");
        b = new LinkedNumber("-456");
        System.out.println(a + " - " + b + " = " + a.sub(b));
        a = new LinkedNumber("-123");
        b = new LinkedNumber("-456");
        System.out.println(a + " - " + b + " = " + a.sub(b));
        a = new LinkedNumber("456");
        b = new LinkedNumber("-456");
        System.out.println(a + " - " + b + " = " + a.sub(b));
        a = new LinkedNumber("-456");
        b = new LinkedNumber("456");
        System.out.println(a + " - " + b + " = " + a.sub(b));
    }

    @Test
    public void multiply(){
        LinkedNumber a = new LinkedNumber("123");
        LinkedNumber b = new LinkedNumber("456");
        System.out.println(a + " * " + b + " = " + a.multiply(b));
        a = new LinkedNumber("-123");
        b = new LinkedNumber("456");
        System.out.println(a + " * " + b + " = " + a.multiply(b));
        a = new LinkedNumber("123");
        b = new LinkedNumber("-456");
        System.out.println(a + " * " + b + " = " + a.multiply(b));
        a = new LinkedNumber("-123");
        b = new LinkedNumber("-456");
        System.out.println(a + " * " + b + " = " + a.multiply(b));
        a = new LinkedNumber("-0");
        b = new LinkedNumber("456");
        System.out.println(a + " * " + b + " = " + a.multiply(b));
        a = new LinkedNumber("-123");
        b = new LinkedNumber("0");
        System.out.println(a + " * " + b + " = " + a.multiply(b));
        a = new LinkedNumber("-11111111111111111111111111111111111111111111111111111111111111");
        b = new LinkedNumber("11111111111111111111111111111111111111111111111111111111111111");
        System.out.println(a + " * " + b + " = " + a.multiply(b));
        Assert.assertEquals(new BigInteger("-11111111111111111111111111111111111111111111111111111111111111").multiply(new BigInteger("11111111111111111111111111111111111111111111111111111111111111")).toString(),a.multiply(b).toString());
    }


    @Test
    public void divide(){
        BigInteger a = new BigInteger("1111111111111111111111111111111111222222222");
        BigInteger b = new BigInteger("1111111111111111111111111111111111222222221");
        System.out.println(a.divide(b));

        int c = 17;
        int d = 8;
        System.out.println();

    }

    @Test
    public void split(){
//        String str ="270000008612982|QYJM11945269|93000818475882|200047|20190115150641|20190115150641|500001|0|1212990009|93000602745804|0|1|2|20190115150641|755000000|1761713||0|0|0|20181201|2|0|755|0|深圳市宝安区龙华镇同胜村上横朗经济合作社|0|";
        String str = "270000008612982#QYJM11945269#93000818475882#200047#20190115150641#20190115150641#500001#0#1212990009#93000602745804#0#1#2#20190115150641#755000000#1761713##0#0#0#20181201#2#0#755#0#深圳市宝安区龙华镇同胜村上横朗经济合作社#0#";
        String[] split = str.split("#", -1);
        System.out.println(Arrays.toString(split));
    }

    @Test
    public void test() throws ParseException {
//        long l = Long.parseLong("9300652224227000000205534248");
//        System.out.println(l);
        Date yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss").parse("20190206000000");
        System.out.println(yyyyMMddHHmmss);
    }


}