package com.evan.echartsbackend.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.SysexMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
public class ImgUploadController {

    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy.MM.dd/");

    @Value("${file-save-path}")
    private String fileSavePath;

    @PostMapping("/uploadBack")
    public Map<String, Object> fileupload(MultipartFile file,HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();
        //获取文件的名字
        System.out.println(file);
        String originName = file.getOriginalFilename();
        System.out.println("originName:"+originName);
        //判断文件类型
        if(!originName.endsWith(".png")) {
            result.put("status","error");
            result.put("msg", "文件类型不对");
            return result;
        }
        //给上传的文件新建目录
        String format = sdf.format(new Date());
        String realPath = fileSavePath + format;
        System.out.println("realPath:"+realPath);
        //若目录不存在则创建目录
        File folder = new File(realPath);
        if(! folder.exists()) {
            folder.mkdirs();
        }
        //给上传文件取新的名字，避免重名
        String newName = UUID.randomUUID().toString() + ".png";
        try {
            //生成文件，folder为文件目录，newName为文件名
            file.transferTo(new File(folder,newName));
            //生成返回给前端的url
            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() +"/images"+ format + newName;
            System.out.println("url:"+url);
            //返回URL
            result.put("status", "success");
            result.put("url", url);
        }catch (IOException e) {
            // TODO Auto-generated catch block
            result.put("status", "error");
            result.put("msg",e.getMessage());
        }
        return result;

    }
}