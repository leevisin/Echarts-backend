package com.evan.echartsbackend.controller;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.evan.echartsbackend.pojo.Chart;
import com.evan.echartsbackend.result.Result;
import com.evan.echartsbackend.result.Charts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Controller
public class UserController {

    String imgUrl = "";

    @CrossOrigin
    @PostMapping(value = "api/addChart")
    @ResponseBody
    public List addChart(@RequestBody Chart chart) throws IOException {
        // Write Csv
        String filePath = "src/charts.csv";
        File f = new File(filePath);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f,true));
            CsvWriter cwriter = new CsvWriter(writer,',');
            cwriter.writeRecord(new String[]{imgUrl, chart.getTitle(), chart.getType(), chart.getData()},false);
            cwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return updateChart();
    }

    @CrossOrigin
    @PostMapping(value = "api/updateChart")
    @ResponseBody
    public List updateChart() {
        String filePath = "src/charts.csv";
        // New list to return stored charts
        List list = new ArrayList();
        // Read Csv
        try {
            CsvReader csvReader = new CsvReader(filePath);

            // 读表头
            csvReader.readHeaders();

            // 读内容
            while (csvReader.readRecord()) {
                // 读一整行
                System.out.println(csvReader.getRawRecord());
                // 读该行的某一列
                System.out.println(csvReader.get("title"));
                Chart aChart = new Chart();
                aChart.setCover(csvReader.get("cover"));
                aChart.setTitle(csvReader.get("title"));
                aChart.setType(csvReader.get("type"));
                aChart.setData(csvReader.get("data"));
                list.add(aChart);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy.MM.dd/");

    @Value("${file-save-path}")
    private String fileSavePath;

    @PostMapping("/upload")
    public Map<String, Object> fileupload(MultipartFile file, HttpServletRequest req){

        System.out.println(fileSavePath);
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
            imgUrl = url;
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
