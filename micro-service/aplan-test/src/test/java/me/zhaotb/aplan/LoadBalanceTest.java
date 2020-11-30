package me.zhaotb.aplan;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author zhaotangbo
 * @since 2020/11/30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class LoadBalanceTest {

    @Autowired
    private RestTemplate restTemplate;



    @Test
    public void testTask(){
        String res = restTemplate.getForObject("http://task/task/test", String.class);
        System.out.println(res);
        res = restTemplate.getForObject("http://task/task/test", String.class);
        System.out.println(res);
        res = restTemplate.getForObject("http://task/task/test", String.class);
        System.out.println(res);
        res = restTemplate.getForObject("http://task/task/test", String.class);
        System.out.println(res);
    }

}
