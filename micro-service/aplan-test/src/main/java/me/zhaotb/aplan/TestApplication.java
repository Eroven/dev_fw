package me.zhaotb.aplan;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhaotangbo
 * @since 2020/11/30
 */
@SpringBootApplication
@RestController
@RequestMapping("test")
public class TestApplication {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("task/test")
    public String taskTest(){
        return restTemplate.getForObject("http://task/task/test", String.class);
    }

    @RequestMapping
    public String readName(){
        return restTemplate.getForObject("http://config-client/config/readName", String.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
