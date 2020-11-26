package me.zhaotb.jvm.cl;


/**
 * @author zhaotangbo
 * @since 2020/11/19
 */
public class ShowClassLoader {

    public static void main(String[] args) {
        ClassLoader classLoader = MyObject.class.getClassLoader();
        System.out.println(classLoader);
        ClassLoader classLoader2 = MyObject.class.getClassLoader();
        System.out.println(classLoader2);
        ClassLoader classLoader3 = MyObject2.class.getClassLoader();
        System.out.println(classLoader3);

        ClassLoader loader = Integer.class.getClassLoader();
        System.out.println(loader);

        ClassLoader loader2 = Integer.class.getClassLoader();
        System.out.println(loader2);


    }
}
