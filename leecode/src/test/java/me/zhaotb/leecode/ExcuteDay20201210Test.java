package me.zhaotb.leecode;


import me.zhaotb.leecode.day20201210.ArithmeticExpression;
import me.zhaotb.leecode.day20201210.Dota2;
import me.zhaotb.leecode.day20201210.MaxArea;
import org.junit.Test;

import java.util.List;

/**
 * @author zhaotangbo
 * @since 2020/12/10
 */
public class ExcuteDay20201210Test {

    @Test
    public void testMaxArea(){
        MaxArea maxArea = new MaxArea();

    }

    @Test
    public void testArithmeticExpression(){
        ArithmeticExpression ae = new ArithmeticExpression();
        List<String> list = ae.parseInfixExpression("1 + 2 * 3 / ( 2 * 1 + 1) - 1");
        System.out.println(list);
        List<String> postfix = ae.infix2Postfix(list);
        System.out.println(postfix);
        System.out.println(ae.calculate(postfix));

    }

    @Test
    public void testDota2(){
        Dota2 dota2 = new Dota2();
        System.out.println(dota2.predictPartyVictory("RD"));
        System.out.println(dota2.predictPartyVictory("RDD"));
        System.out.println(dota2.predictPartyVictory("RDDR"));
        System.out.println(dota2.predictPartyVictory("RDDDDDRRRRR"));
        System.out.println(dota2.predictPartyVictory("DDDDRRDDDRDRDRRDDRDDDRDRRRRDRRRRRDRDDRDDRRDDRRRDDRRRDDDDRRRRRRRDDRRRDDRDDDRRRDRDDRDDDRRDRRDRRRDRDRDR"));
//DDDDRRDDDRDRDRRDDRDDDRDRRRRDRRRRRDRDDRDDRRDDRRRDDRRRDDDDRRRRRRRDDRRRDDRDDDRRRDRDDRDDDRRDRRDRRRDRDRDR
    }
}
