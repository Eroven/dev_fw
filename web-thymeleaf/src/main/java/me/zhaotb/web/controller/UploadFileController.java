package me.zhaotb.web.controller;


import lombok.extern.slf4j.Slf4j;
import me.zhaotb.web.config.FilePathConfig;
import me.zhaotb.web.dto.CommonResponse;
import me.zhaotb.web.dto.account.UserDto;
import me.zhaotb.web.service.UserService;
import me.zhaotb.web.util.CRFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @author zhaotangbo
 * @since 2020/12/9
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
@RequestMapping("/file")
@Slf4j
public class UploadFileController {

    @Autowired
    private FilePathConfig filePathConfig;

    @Autowired
    private UserService userService;

//    @RequestMapping("view")
//    public String view(Model model,@RequestAttribute String userCode){
//        model.addAttribute("user", userCode);
//        return "uploadFile";
//    }

    @RequestMapping("upload")
    public CommonResponse upload(@RequestParam("file") MultipartFile file){
        log.info("{} - {}", file.getOriginalFilename(), file.getSize());
        String encode = Base64.getEncoder().encodeToString(file.getOriginalFilename().getBytes());
        File localFile = new File(filePathConfig.getVideo(), encode);
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


    @RequestMapping("user/photo")
    public CommonResponse getUserPhoto(@RequestAttribute("userDto")UserDto userDto) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        userService.accessUserPhoto(userDto, out);
        return CRFactory.okData(Base64.getEncoder().encodeToString(out.toByteArray()));
    }


}
