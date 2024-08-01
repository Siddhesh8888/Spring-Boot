package com.app.Export_Excel_File;

import com.app.Export_Excel_File.entity.Student;
import com.app.Export_Excel_File.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;

@SpringBootApplication
public class ExportExcelFileApplication implements CommandLineRunner {

	@Autowired
	StudentService studentService;

	public static void main(String[] args) {
		SpringApplication.run(ExportExcelFileApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		for(int i=1;i<=10;i++)
		{
			Student student=new Student();
			student.setStudentName("student"+i);
			student.setEmail("student"+i+"@gmail.com");
			student.setMobileNo(String.valueOf(Math.abs(new Random(10).nextInt())));
			studentService.addStudent(student);
		}
	}
}
