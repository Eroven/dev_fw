package me.zhaotb.spring.springboot;

import org.springframework.stereotype.Service;

@Service
public class HelloAop {

    @Log
    @NotNull
    public void say(String name){
        System.out.println("Hello , " + name);
    }

    @NotNull
    public void say(){
        System.out.println("say what ?");
    }

}
