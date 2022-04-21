package com.evan.echartsbackend.controller;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.evan.echartsbackend.pojo.Chart;
import com.evan.echartsbackend.result.Result;
import com.evan.echartsbackend.result.Charts;
import com.evan.echartsbackend.util.UserCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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


@CrossOrigin
@RestController
public class UserController {

    String imgUrl = "";

    @CrossOrigin
    @PostMapping(value = "api/addChart")
    @ResponseBody
    public List addChart(@RequestBody Chart chart) throws IOException {
        // imgUrl can't be null
        if (imgUrl == "") {
            return updateChart();
        }

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
        System.out.println(UserCache.getUsername());
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

    @CrossOrigin
    @PostMapping(value = "api/deleteChart")
    @ResponseBody
    public List deleteChart(@RequestBody Chart chart) throws IOException {
        String filePath = "src/charts.csv";
        String fileTmpPath = "src/chartsTmp.csv";

        // Delete Chart and write to chartsTmp.csv
        try {
            CsvReader csvReader = new CsvReader(filePath);
            csvReader.readHeaders();
            File f = new File(fileTmpPath);
            BufferedWriter writer = new BufferedWriter(new FileWriter(f,false));
            CsvWriter cwriter = new CsvWriter(writer,',');
            cwriter.writeRecord(new String[]{"cover", "title", "type", "data"},false);
            while (csvReader.readRecord()) {
                if (chart.getCover().equals(csvReader.get("cover"))) {
                    continue;
                }
                cwriter.writeRecord(new String[]{csvReader.get("cover"), csvReader.get("title"), csvReader.get("type"), csvReader.get("data")},false);
            }
            cwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Copy chartsTmp.csv to charts.csv
        try {
            CsvReader csvReader = new CsvReader(fileTmpPath);
            csvReader.readHeaders();
            File f = new File(filePath);
            BufferedWriter writer = new BufferedWriter(new FileWriter(f,false));
            CsvWriter cwriter = new CsvWriter(writer,',');
            cwriter.writeRecord(new String[]{"cover", "title", "type", "data"},false);
            while (csvReader.readRecord()) {
                cwriter.writeRecord(new String[]{csvReader.get("cover"), csvReader.get("title"), csvReader.get("type"), csvReader.get("data")},false);
            }
            cwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return updateChart();
    }

    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy.MM.dd/");

    @Value("${file-save-path}")
    private String fileSavePath;

    @PostMapping("/upload")
    public Map<String, Object> fileupload(MultipartFile file, HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();
        //获取文件的名字
        String originName = file.getOriginalFilename();
        //判断文件类型
        if(!originName.endsWith(".png")) {
            result.put("status","error");
            result.put("msg", "文件类型不对");
            return result;
        }
        //给上传的文件新建目录
        String format = sdf.format(new Date());
        String realPath = fileSavePath + format;
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

    @CrossOrigin
    @PostMapping(value = "api/getUsername")
    @ResponseBody
    public String getUsername() {
        System.out.println(UserCache.getUsername());
        return UserCache.getUsername();
    }

}
