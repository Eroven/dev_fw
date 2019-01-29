package me.zhaotb.spring.springboot;

/**
 * @author zhaotangbo
 * @date 2019/1/8
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        MyObj myObj = new MyObj();
        MyObj1 myObj1 = new MyObj1();
        MyObj2 myObj2 = new MyObj2();
        MyObj3 myObj3 = new MyObj3();
        MyObj4 myObj4 = new MyObj4();
        MyObj5 myObj5 = new MyObj5();
        synchronized (Main.class){
            Main.class.wait();
        }
    }
}
