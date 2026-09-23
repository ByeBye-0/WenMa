package com.smy.WenMa.Controller;

import com.smy.WenMa.Tool.AliyunOss;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/Upload")
public class UploadController {
    @Autowired
    private AliyunOss aliyunOss;

    @PostMapping("/avtor")
    public String load(@RequestParam("file") MultipartFile file) throws Exception {
        log.info("上传{}", file.getOriginalFilename());
        String res = aliyunOss.upload(file.getBytes(), file.getOriginalFilename());
        log.info("{}",res);
        return res;
    }
}
