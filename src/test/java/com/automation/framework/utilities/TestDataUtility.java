package com.automation.framework.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * TestDataUtility: Handles test data from Excel and JSON files
 */
public class TestDataUtility {

    private static final Logger LOGGER = LogManager.getLogger(TestDataUtility.class);

    /**
     * Read data from Excel file
     */
    public static List<Map<String, String>> readDataFromExcel(String filePath, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                LOGGER.error("Sheet not found: " + sheetName);
                return dataList;
            }

            Row headerRow = sheet.getRow(0);
            int columnCount = headerRow.getLastCellNum();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> dataMap = new HashMap<>();
                for (int j = 0; j < columnCount; j++) {
                    Cell headerCell = headerRow.getCell(j);
                    Cell dataCell = row.getCell(j);

                    String header = headerCell != null ? headerCell.getStringCellValue() : "";
                    String data = dataCell != null ? dataCell.getStringCellValue() : "";

                    dataMap.put(header, data);
                }
                dataList.add(dataMap);
            }

            workbook.close();
            fileInputStream.close();
            LOGGER.info("Data read from Excel file: " + filePath + " - Sheet: " + sheetName + " - Rows: " + dataList.size());

        } catch (IOException e) {
            LOGGER.error("Error reading Excel file: " + e.getMessage(), e);
        }
        return dataList;
    }

    /**
     * Write data to Excel file
     */
    public static void writeDataToExcel(String filePath, String sheetName, List<Map<String, String>> dataList) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(sheetName);

            if (dataList == null || dataList.isEmpty()) {
                LOGGER.warn("No data to write to Excel");
                return;
            }

            // Create header row
            Set<String> headers = dataList.get(0).keySet();
            Row headerRow = sheet.createRow(0);
            int headerIndex = 0;
            for (String header : headers) {
                Cell cell = headerRow.createCell(headerIndex++);
                cell.setCellValue(header);
            }

            // Create data rows
            int rowIndex = 1;
            for (Map<String, String> data : dataList) {
                Row row = sheet.createRow(rowIndex++);
                int cellIndex = 0;
                for (String header : headers) {
                    Cell cell = row.createCell(cellIndex++);
                    cell.setCellValue(data.getOrDefault(header, ""));
                }
            }

            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream);
            workbook.close();
            fileOutputStream.close();
            LOGGER.info("Data written to Excel file: " + filePath);

        } catch (IOException e) {
            LOGGER.error("Error writing to Excel file: " + e.getMessage(), e);
        }
    }

    /**
     * Get cell value from Excel
     */
    public static String getCellValue(String filePath, String sheetName, int rowNumber, int cellNumber) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(rowNumber);
            Cell cell = row.getCell(cellNumber);

            String value = cell.getStringCellValue();
            workbook.close();
            fileInputStream.close();
            return value;

        } catch (IOException e) {
            LOGGER.error("Error reading cell value: " + e.getMessage(), e);
            return "";
        }
    }

    /**
     * Set cell value in Excel
     */
    public static void setCellValue(String filePath, String sheetName, int rowNumber, int cellNumber, String value) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(rowNumber);
            Cell cell = row.createCell(cellNumber);
            cell.setCellValue(value);

            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream);
            workbook.close();
            fileOutputStream.close();
            LOGGER.info("Cell value updated: Row " + rowNumber + ", Cell " + cellNumber);

        } catch (IOException e) {
            LOGGER.error("Error setting cell value: " + e.getMessage(), e);
        }
    }

    /**
     * Get row count from Excel sheet
     */
    public static int getRowCount(String filePath, String sheetName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            int rowCount = sheet.getLastRowNum();
            workbook.close();
            fileInputStream.close();
            return rowCount;
        } catch (IOException e) {
            LOGGER.error("Error getting row count: " + e.getMessage(), e);
            return 0;
        }
    }

    /**
     * Get column count from Excel sheet
     */
    public static int getColumnCount(String filePath, String sheetName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(0);
            int columnCount = row.getLastCellNum();
            workbook.close();
            fileInputStream.close();
            return columnCount;
        } catch (IOException e) {
            LOGGER.error("Error getting column count: " + e.getMessage(), e);
            return 0;
        }
    }
}
