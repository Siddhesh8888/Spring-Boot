package com.app.Export_Excel_File.service;

import com.app.Export_Excel_File.entity.Student;
import com.app.Export_Excel_File.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    public void addStudent(Student student){
        studentRepository.save(student);
        log.info("Added student  : {}",student);
    }
    public List<Student> getAllStudent(){
        return studentRepository.findAll();
    }
}
