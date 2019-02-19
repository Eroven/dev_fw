package me.zhaotb.jvm.options;

import me.zhaotb.myagent.AgentInst;

import java.lang.instrument.Instrumentation;
import java.util.LinkedList;

/**
 * @author zhaotangbo
 * @date 2019/1/30
 */
public class Main {

    public static void main(String[] args) {
//        Instrumentation inst = AgentInst.getInstrumentation();
        printMem();
        LinkedList<String> list = new LinkedList<String>();
        for (int i = 0; i < 100000; i++) {
            list.add("Ac2");
        }
        System.out.println(list.size());
        printMem();
        System.out.println(list.size());
    }

    private static void printMem() {
        Runtime runtime = Runtime.getRuntime();
        int processors = runtime.availableProcessors();
        double freeMemory = runtime.freeMemory();
        double totalMemory = runtime.totalMemory();
        System.out.println("processors : " + processors);
        System.out.println("freeMemory : " + freeMemory + " B ; " + (freeMemory / 1024) + " KB ; " +
                (freeMemory / 1024 / 1024) + " MB ; " +
                (freeMemory / 1024 / 1024 / 1024) + " GB .");
        System.out.println("totalMemory : " + totalMemory + " B ; " + (totalMemory / 1024) + " KB ; " +
                (totalMemory / 1024 / 1024) + " MB ; " +
                (totalMemory / 1024 / 1024 / 1024) + " GB .");
    }
}
