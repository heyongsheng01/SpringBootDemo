package com.example.demo.repository;

import com.example.demo.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yongsheng.he
 * @date 2017/09/30 15:57
 */
public interface IStudentMapper extends JpaRepository<Student,Integer> {

}
