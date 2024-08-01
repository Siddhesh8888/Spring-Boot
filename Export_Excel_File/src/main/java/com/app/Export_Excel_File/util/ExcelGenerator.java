package com.app.Export_Excel_File.util;

import com.app.Export_Excel_File.entity.Student;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class ExcelGenerator {

    private List<Student> studentList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private static final List<String> EXCEL_HEADER=List.of("ID","STUDENT_NAME","EMAIL","MOBILE_NO");

    public ExcelGenerator(List<Student> studentList){
        this.studentList=studentList;
        workbook=new XSSFWorkbook();
    }

    public void createExcelSheet(HttpServletResponse httpServletResponse) throws IOException {
        createHeader();
        createRows();
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    public void createExcelSheetInLocalFolder()  {
        createHeader();
        createRows();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String fileName = "student_file_"+timestamp+".xlsx";
        Path filePath = Paths.get("src","main","resources");
        File file = new File(filePath.toFile(),fileName);
        try (FileOutputStream fos = new FileOutputStream(file)){
            workbook.write(fos);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void createHeader(){
        sheet=workbook.createSheet();
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);
        XSSFRow row = sheet.createRow(0);
        int columnNo=0;
        for (String value:EXCEL_HEADER)
        {
            createCell(row,columnNo++,value,cellStyle);
        }
    }

    private void createCell(Row raw, int column, String value, CellStyle cellStyle){
        Cell cell = raw.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }

    private void createRows(){
        int rowCount=1;
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(16);
        cellStyle.setFont(font);

        for (Student student:studentList)
        {
            Row row = sheet.createRow(rowCount++);
            int columnCount=0;
            createCell(row,columnCount++,String.valueOf(student.getId()),cellStyle);
            createCell(row,columnCount++,student.getStudentName(),cellStyle);
            createCell(row,columnCount++,student.getEmail(),cellStyle);
            createCell(row,columnCount++,student.getMobileNo(),cellStyle);
        }
    }
}
