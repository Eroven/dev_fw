package me.zhaotb.app.program.hello;

import me.zhaotb.app.api.AbstractMicroAppService;
import me.zhaotb.app.api.MicroApplication;

/**
 * @author zhaotangbo
 * @date 2019/3/5
 */
public class HelloWorld extends AbstractMicroAppService implements MicroApplication {


    @Override
    public void execute() {
        while (true){
            System.out.println("Hello World");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new HelloWorld().startAsync();
    }
}
