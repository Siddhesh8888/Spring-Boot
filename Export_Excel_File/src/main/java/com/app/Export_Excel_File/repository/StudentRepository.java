package com.app.Export_Excel_File.repository;

import com.app.Export_Excel_File.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
