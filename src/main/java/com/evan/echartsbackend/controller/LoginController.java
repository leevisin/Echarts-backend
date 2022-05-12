package com.evan.echartsbackend.controller;

import com.evan.echartsbackend.result.Result;
import com.evan.echartsbackend.util.UserCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import com.evan.echartsbackend.pojo.User;

import java.util.Objects;

@Controller
public class LoginController {

    @CrossOrigin
    @PostMapping(value = "api/login")
    @ResponseBody
    public Result login(@RequestBody User requestUser) {
        // prevent XSS attack
        String username = requestUser.getUsername();
        username = HtmlUtils.htmlEscape(username);

        System.out.println("Username: " + username);
        System.out.println("Password: " + requestUser.getPassword());

        if (Objects.equals("admin", username) && Objects.equals("123456", requestUser.getPassword())) {
            UserCache.setUsername(username);
            return new Result(200);
        } else if (Objects.equals("lds", username) && Objects.equals("0533", requestUser.getPassword())) {
            UserCache.setUsername(username);
            return new Result(200);
        } else {
            String message = "Username or Password is incorrect";
            UserCache.setUsername("");
            return new Result(400);
        }

    }
}