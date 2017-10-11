package com.example.demo.controller;


import com.example.demo.domain.Student;
import com.example.demo.repository.IStudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yongsheng.he
 * @describe
 * @date 2017/10/10 10:59
 */
@RestController
public class TestController {

    @Autowired
    private IStudentMapper iStudentMapper;

    @RequestMapping("/findAll")
    public List<Student> findAll(){
        List<Student> students=iStudentMapper.findAll();
        return students;
    }


}
