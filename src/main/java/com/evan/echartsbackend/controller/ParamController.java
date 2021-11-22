package com.evan.echartsbackend.controller;

import com.evan.echartsbackend.result.Option;
import com.evan.echartsbackend.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evan.echartsbackend.pojo.Param;

@Controller
public class ParamController {

    @CrossOrigin
    @PostMapping(value = "api/update")
    @ResponseBody
    public Option update(@RequestBody Param requestParam){
        String title = requestParam.getTitle();
        Option returnOption = new Option();
        returnOption.setTitle(title);
        System.out.println("title: " + title);
        return returnOption;
    }
}
