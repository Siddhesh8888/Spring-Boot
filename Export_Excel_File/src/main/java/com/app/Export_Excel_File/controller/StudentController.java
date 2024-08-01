package com.app.Export_Excel_File.controller;

import com.app.Export_Excel_File.entity.Student;
import com.app.Export_Excel_File.service.StudentService;
import com.app.Export_Excel_File.util.ExcelGenerator;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/student")
public class StudentController {
    @Autowired
    StudentService studentService;

    @GetMapping("/create-Excel")
    public void exportDataIntoExcelFile(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=student" + currentDateTime + ".xlsx";
        httpServletResponse.setHeader(headerKey, headerValue);

        List<Student> listOfStudents = studentService.getAllStudent();
        ExcelGenerator generator = new ExcelGenerator(listOfStudents);
        generator.createExcelSheet(httpServletResponse);
    }

    @GetMapping("/create-excel-locally")
    public String createExcelLocally(){
        List<Student> listOfStudents = studentService.getAllStudent();
        ExcelGenerator generator = new ExcelGenerator(listOfStudents);
        generator.createExcelSheetInLocalFolder();
        return "File created successfully!!";
    }

}
