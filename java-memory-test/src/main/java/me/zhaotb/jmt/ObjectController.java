package me.zhaotb.jmt;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author zhaotangbo
 * @since 2020/11/19
 */
@RestController
@RequestMapping("obj")
public class ObjectController {

    private volatile ConcurrentLinkedQueue<MyObject> data = new ConcurrentLinkedQueue<>();

    @RequestMapping("string/create")
    public Object createString(@RequestParam(defaultValue = "1") int num
            ,@RequestParam(defaultValue = "5") int len
            ,@RequestParam(defaultValue = "true") boolean keep) {
        RandomObject randomString = new RandomObject();
        for (int i = 0; i < num; i++) {
            MyObject string = randomString.anyStringObj(len);
            if (keep){
                data.offer(string);
            }
        }
        return "OK";
    }

    @RequestMapping("string/remove")
    public Object cleanString(int num) {
        if (data.size() > num) {
            while (num > 0){
                data.poll();
                num --;
            }
        } else {
            data.clear();
        }
        return "OK";
    }

}
