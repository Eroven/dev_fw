package me.zhaotb.dt.cnode;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cnode")
public class Controller {

    @RequestMapping("server")
    public void doServe(@RequestParam("sn") String serverName, @RequestParam("content") String content) {

    }

}
