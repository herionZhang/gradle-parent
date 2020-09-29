package com.herion.gradle.demo.controller;


import com.herion.biz.HelloBiz;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/helllo")
    public String hello(String name){
        HelloBiz helloBiz=new HelloBiz();
        return helloBiz.hello(name);
    }
}