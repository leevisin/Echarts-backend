package com.evan.echartsbackend.controller;

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
    public Result update(@RequestBody Param requestParam){
        String title = requestParam.getTitle();
        System.out.println("title: " + title);
        return new Result(200);
    }
}
