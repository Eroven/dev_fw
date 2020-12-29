package me.zhaotb.web.controller;


import lombok.extern.slf4j.Slf4j;
import me.zhaotb.web.dto.CommonResponse;
import me.zhaotb.web.util.CRFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

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
    @ResponseBody
    public CommonResponse upload(@RequestParam("file") MultipartFile file){
        log.info("{} - {}", file.getOriginalFilename(), file.getSize());
        String encode = Base64.getEncoder().encodeToString(file.getOriginalFilename().getBytes());
        File localFile = new File(savePath, encode);
        if (!localFile.getParentFile().isDirectory() && localFile.getParentFile().mkdirs()) {
            return CRFactory.errorMsg("创建目录失败");
        }
        if (localFile.exists()) {
            return CRFactory.errorMsg("文件名重复:" + file.getOriginalFilename());
        }
        try(FileOutputStream fos = new FileOutputStream(localFile)) {
            IOUtils.copy(file.getInputStream(), fos);
        } catch (Exception e) {
            return CRFactory.errorMsg("保存文件失败");
        }
        return CRFactory.okData(encode);

    }




}
