package com.evan.echartsbackend.controller;

import com.evan.echartsbackend.pojo.Chart;
import com.evan.echartsbackend.result.Result;
import com.evan.echartsbackend.result.Charts;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @CrossOrigin
    @PostMapping(value = "api/user")
    @ResponseBody
    public List addChart(@RequestBody Chart chart) {
        List list = new ArrayList();
        Chart aChart = new Chart();
        aChart.setData("option = {\n" +
                "        title: {\n" +
                "          text: 'Title Test',\n" +
                "        },\n" +
                "        tooltip: {},\n" +
                "        xAxis: {\n" +
                "          type: 'category',\n" +
                "          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']\n" +
                "        },\n" +
                "        yAxis: {\n" +
                "          type: 'value'\n" +
                "        },\n" +
                "        series: [\n" +
                "          {\n" +
                "            data: [150, 230, 224, 218, 135, 147, 260],\n" +
                "            type: 'line'\n" +
                "          }\n" +
                "        ]\n" +
                "      };");
        aChart.setCover("https://static01.imgkr.com/temp/566c928d2ce14157963f828ba166e9aa.JPG");
        aChart.setTitle(chart.getTitle());
        Chart bChart = new Chart();
        bChart.setData("456");
        bChart.setCover("https://static01.imgkr.com/temp/566c928d2ce14157963f828ba166e9aa.JPG");
        list.add(aChart);
        list.add(bChart);
        return list;
    }

//    public Charts addChart(@RequestBody Chart chart) {
//        System.out.println(chart.getData());
//        Chart aChart = new Chart();
//        aChart.setData(chart.getData());
//        return new Charts(aChart);
//    }
}
