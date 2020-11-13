package me.zhaotb.pool.ftp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/ftp")
public class Starter {

    private MyFtp myFtp;

    public Starter(MyFtp myFtp) {
        this.myFtp = myFtp;
    }

    @RequestMapping("/ls")
    public String list() throws Exception {
        List<String> listnames = myFtp.listnames();
        return listnames.toString();
    }

    @RequestMapping("/look")
    public String look() {
        return myFtp.look();
    }

    @RequestMapping("/cat")
    public String cat(@RequestParam("file") String file,@RequestParam(required = false) String charset) throws Exception {
        StringBuilder sb = new StringBuilder();
        InputStream in = myFtp.retrieveFile(file);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) > 0){
            sb.append(new String(buffer, 0, len));
        }
        System.gc();
        Runtime.getRuntime().gc();
        Data bean = S.getBean(Data.class);
        return sb.toString();
    }

    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }
}
