package me.zhaotb.spring.springboot;


import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author zhaotangbo
 * @since 2020/11/16
 */
@RestController
@RequestMapping("file")
public class FileDownloadController {

    private static int max = 1000000;
    private static long len = 0;
    private static String content = "你好ABCDEFAAAAAAAAAAAAAAAAAAAOOOOOOOOOOOOOOOOOOOOOOOOOOOPPPPP(**********2222222222222222222288888888888888888888888888888";
    static {
        for (int i = 0; i < max; i++) {
            len += (content + "：" + i + "\n").getBytes().length;
        }
    }

    @RequestMapping("download")
    public void download(@RequestParam("fn") String fileName, HttpServletResponse response) throws InterruptedException, IOException {


        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setHeader("Content-Length", String.valueOf(len));

        Thread.sleep(10000);
        ServletOutputStream outputStream = response.getOutputStream();
        for (int i = 0; i < max; i++) {
            outputStream.write((content + "：" + i + "\n").getBytes());
        }


    }

    @RequestMapping("big")
    public void downloadBigFile(HttpServletResponse response) throws IOException, InterruptedException {
        File file = new File("D:\\utils\\hive\\apache-hive-2.3.6-bin.tar.gz");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=bigfile.tar.gz");
        response.setHeader("Content-Length", String.valueOf(file.length()));
        try (FileInputStream input = new FileInputStream(file)){
            ServletOutputStream output = response.getOutputStream();
            byte[] buffer = new byte[1024 * 4];
            long count = 0;
            int n;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
                count += n;
                Thread.sleep(100);
            }
            System.out.println("size=" + count);
        }
    }

}
