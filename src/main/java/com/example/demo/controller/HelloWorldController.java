package com.example.demo.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yongsheng.he
 * @describe HelloWorld
 * @date 2017/10/09 16:47
 */
@RestController
public class HelloWorldController {
    @RequestMapping("/hello")
    public String hello() {

        return "Hello World!";
    }
}
