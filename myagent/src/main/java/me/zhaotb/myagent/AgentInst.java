package me.zhaotb.myagent;

import org.apache.commons.lang3.StringUtils;

import java.lang.instrument.Instrumentation;
import java.util.HashMap;

/**
 * @author zhaotangbo
 * @date 2018/12/5
 */
public class AgentInst {

    private static Instrumentation inst;

    /**
     *
     * @param agentOps  package1.class1.method1|package1.class1.method2|...
     * @param instrumentation {@link Instrumentation}
     */
    public static void premain(String agentOps, Instrumentation instrumentation){
        inst = instrumentation;
        System.out.print("premain :");
        System.out.println(agentOps);

        if (StringUtils.isBlank(agentOps)){
            return;
        }
        MyTransformer transformer = new MyTransformer();
        String[] split = agentOps.split("\\|");
        for (String str : split) {
            str = str.trim();
            if (StringUtils.isEmpty(str)){
                continue;
            }
            String[] pair = str.split("#");
            if (pair.length != 2){
                continue;
            }
            transformer.add(pair[0], pair[1]);
        }
        System.out.println("代理方法: " + transformer.getMap());
        instrumentation.addTransformer(transformer);
    }

    public static Instrumentation getInstrumentation(){
        if (inst == null){
            throw new IllegalStateException("无法访问Instrumentation.请检查启动命令是否将me.zhaotb.myagent.AgentInst类的jar包指定在 -javaagent: 后面");
        }
        return inst;
    }



}
