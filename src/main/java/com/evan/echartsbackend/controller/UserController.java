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
    String filePath = "src/data/charts.csv";
    String fileTmpPath = "src/data/chartsTmp.csv";

    @CrossOrigin
    @PostMapping(value = "api/addChart")
    @ResponseBody
    public List addChart(@RequestBody Chart chart) throws IOException {
        // imgUrl can't be null
        if (imgUrl == "") {
            return updateChart();
        }

        // Write Csv
        File f = new File(filePath);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f,true));
            CsvWriter cwriter = new CsvWriter(writer,',');
            cwriter.writeRecord(new String[]{imgUrl, chart.getTitle(), chart.getType(), chart.getData()},false);
            cwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgUrl = "";
        return updateChart();
    }

    @CrossOrigin
    @PostMapping(value = "api/updateChart")
    @ResponseBody
    public List updateChart() {
        getUsername();
        // New list to return stored charts
        List list = new ArrayList();
        // Read Csv
        try {
            CsvReader csvReader = new CsvReader(filePath);

            // read excel header
            csvReader.readHeaders();

            // read excel content
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
        // Delete Chart and write to chartsTmp.csv
        writeToFile(filePath, fileTmpPath, chart);

        // Copy chartsTmp.csv to charts.csv
        writeToFile(fileTmpPath, filePath, chart);

        return updateChart();
    }

    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy.MM.dd/");

    @Value("${file-save-path}")
    private String fileSavePath;

    @PostMapping("/upload")
    public Map<String, Object> fileupload(MultipartFile file, HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();
        // get file name
        String originName = file.getOriginalFilename();
        // get file type
        if(!originName.endsWith(".png")) {
            result.put("status","error");
            result.put("msg", "文件类型不对");
            return result;
        }
        // new directory for upload data
        String format = sdf.format(new Date());
        String realPath = fileSavePath + format;
        // create new directory if not exist
        File folder = new File(realPath);
        if(! folder.exists()) {
            folder.mkdirs();
        }
        // avoid same file name
        String newName = UUID.randomUUID().toString() + ".png";
        try {
            // generate file, folder is file menu, newName is filename
            file.transferTo(new File(folder,newName));
            // return url to front end
            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() +"/images"+ format + newName;
            imgUrl = url;
            // return url
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
        filePath = "src/data/"+ UserCache.getUsername() + "charts.csv";
        fileTmpPath = "src/data/" + UserCache.getUsername() + "chartsTmp.csv";
        return UserCache.getUsername();
    }

    public void writeToFile(String fromFile, String toFile, Chart chart) {
        try {
            CsvReader csvReader = new CsvReader(fromFile);
            csvReader.readHeaders();
            File f = new File(toFile);
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
    }

}
