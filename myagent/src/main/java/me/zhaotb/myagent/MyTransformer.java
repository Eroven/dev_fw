package me.zhaotb.myagent;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author zhaotangbo
 * @date 2018/12/5
 */
class MyTransformer implements ClassFileTransformer {

    private Map<String, List<String>> map;

    MyTransformer() {
        map = new HashMap<>();
    }

    MyTransformer(Map<String, List<String>> map) {
        this.map = map;
    }

    void add(String className, String method){
        List<String> list = map.computeIfAbsent(className, k -> new LinkedList<>());
        list.add(method);
    }

    /**
     * 获取代理类 -> 方法名集合的映射
     * @return 返回一个只读map,所有非读取操作均会抛出异常
     */
    public Map<String, List<String>> getMap() {
        return Collections.unmodifiableMap(map);
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        String pakClassName = className.replaceAll("/", ".");
        List<String> methods = map.get(pakClassName);
        if (methods == null || methods.isEmpty()){
            return null;
        }
        try {
            CtClass ctClass = ClassPool.getDefault().get(pakClassName);
            for (String method : methods) {

                try {
                    //更改原方法名
                    CtMethod ctMethod = ctClass.getDeclaredMethod(method);
                    String newMethodName = method + "$myAgent";
                    ctMethod.setName(newMethodName);

                    //创建新方法 复制原方法的方法体,用原方法名命名
                    CtMethod newMethod = CtNewMethod.copy(ctMethod, method, ctClass, null);

                    CtClass returnType = ctMethod.getReturnType();
                    //构建新的方法体
                    StringBuilder bodyStr = new StringBuilder();
                    bodyStr.append("{")
                            .append("\nlong start = System.currentTimeMillis();\n");
                    if (CtClass.voidType != returnType) {
                        bodyStr.append(returnType.getName())
                                .append(" obj = ");
                    }
                    bodyStr.append(newMethodName)
                            .append("($$);\n")
                            .append("\nlong end = System.currentTimeMillis();\n")
                            .append("System.out.println(\"")
                            .append(pakClassName)
                            .append("#")
                            .append(method)
                            .append("耗时:\" + (end - start));\n");
                    if (CtClass.voidType != returnType) {
                        bodyStr.append("return obj;\n");
                    }
                    bodyStr.append("}");
                    newMethod.setBody(bodyStr.toString());
                    ctClass.addMethod(newMethod);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return ctClass.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
