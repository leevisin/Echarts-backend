package com.evan.echartsbackend.controller;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.evan.echartsbackend.pojo.Chart;
import com.evan.echartsbackend.result.Result;
import com.evan.echartsbackend.result.Charts;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @CrossOrigin
    @PostMapping(value = "api/addChart")
    @ResponseBody
    public List addChart(@RequestBody Chart chart) {
        // Write Csv
        String filePath = "src/charts.csv";
        File f = new File(filePath);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f,true));
            CsvWriter cwriter = new CsvWriter(writer,',');
            cwriter.writeRecord(new String[]{chart.getCover(), chart.getTitle(), chart.getType(), chart.getData()},false);
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
}
