package me.zhaotb.framework.util;

import me.zhaotb.myagent.AgentInst;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author zhaotangbo
 * @date 2018/12/5
 */
public class MemoryUtil {

    private Instrumentation inst;

    public MemoryUtil() {
        this.inst = AgentInst.getInstrumentation();
    }

    private void show(String arg){
        System.out.println("show : " + arg);
    }

    private void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Instrumentation getInst() {
        return inst;
    }

    public long sizeOf(Object obj){
        return inst.getObjectSize(obj);
    }

    public static void main(String[] args) {
        System.out.println("程序参数: " + Arrays.toString(args));
        MemoryUtil util = new MemoryUtil();
        Method[] declaredMethods = MemoryUtil.class.getDeclaredMethods();
        util.show("ztb");
        Instrumentation inst = util.getInst();
        System.out.println("inst: " + inst);
        System.out.println("New MemoryUtil Object size is " + util.sizeOf(util) + " B");

        System.out.println("byte size : " + util.sizeOf((byte)1));
        System.out.println("byte size : " + util.sizeOf('a'));
        System.out.println("int size : " + util.sizeOf(1));
        System.out.println("long size : " + util.sizeOf(1L));
        System.out.println("long size : " + util.sizeOf(1L));
        System.out.println("long size : " + util.sizeOf(1L));
        System.out.println("long size : " + util.sizeOf(1L));

    }

}
