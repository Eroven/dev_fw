package me.zhaotb.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhaotangbo
 * @since 2020/12/9
 */
@Controller
@RequestMapping("/file")
@Slf4j
public class UploadFileController {

    @Value("${savePath}")
    private String savePath;

    @RequestMapping("view")
    public String view(Model model,@RequestAttribute String userCode){
        model.addAttribute("user", userCode);
        return "uploadFile";
    }

    @RequestMapping("upload")
    public String upload(@RequestParam("file") MultipartFile file){
        log.info("{} - {}", file.getOriginalFilename(), file.getSize());
        return "uploadFile";

    }


}
