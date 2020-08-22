package com.web.helper.testDataHandling;

import com.web.base.MobileBaseUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.util.*;

public class DataHelper {
	public static List<Map<String, String>> TestSet = new ArrayList<Map<String, String>>();
	static FileInputStream datasheet = null;
	static List<String> Keys = new ArrayList<String>();
	static List<String> Values = new ArrayList<String>();
	static String			    outputExcel	= "";


	public static List<Map<String, String>> readTestDataSet(String excelFilePath) {

		final File f = new File(excelFilePath);

		try {
			DataFormatter formatter = new DataFormatter();
			datasheet = new FileInputStream(f);
			HSSFWorkbook workbook = new HSSFWorkbook(datasheet);
			HSSFSheet worksheet = workbook.getSheet("InputData");
			Iterator<Row> rowIterator = worksheet.iterator();
			while(rowIterator.hasNext()){
				Row row = rowIterator.next();
				if(row.getRowNum()==0){
					for (int k= 0; k< row.getLastCellNum();k++)
						Keys.add(formatter.formatCellValue(row.getCell(k)));
				}
				else{
					for(int k =0; k<row.getLastCellNum();k++)
						Values.add(formatter.formatCellValue(row.getCell(k)));

				}

				if(row.getLastCellNum() == Keys.size() && Values.size() == Keys.size()){
					Map<String,String> TestData = new HashMap<String,String>();
					for(int i = 0; i<Keys.size();i++)
						TestData.put(Keys.get(i), Values.get(i));
					Values.clear();
					TestSet.add(TestData);
					//System.out.println(TestSet);
				}

			}


		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Unable to open the Test Data");
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Unable to read Error!");
		}
        Keys.clear();
		return TestSet;

	}


	public static void setOutputExcel(String excelPath) {
		outputExcel = excelPath;
	}

	public static void writeToOutput(String columnName, String testData) {
		writeTestDataValues("OutputData", MobileBaseUtil.getTestName(), columnName, testData);
	}

	public static void writeTestDataValues(String sheetName, String testCaseName, String columnName, String testData) {

		final String excelPath = outputExcel;
		try {
			final FileInputStream file = new FileInputStream(new File(System.getProperty("user.dir") + excelPath));
			final HSSFWorkbook workbook = new HSSFWorkbook(file);
			final HSSFSheet sheet = workbook.getSheet(sheetName);
			Cell cell = null;
			final int row = findRow(sheet, testCaseName);
			final int column = findColumn(sheet, columnName);
			cell = sheet.getRow(row).getCell(column);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue("");
			cell.setCellValue(testData);
			file.close();
			final FileOutputStream outFile = new FileOutputStream(new File(System.getProperty("user.dir") + excelPath));
			workbook.write(outFile);
			outFile.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static String getFromOutput(String columnName) {

		return getFromOutputValues("OutputData", MobileBaseUtil.getTestName(), columnName);
	}

	public static String getFromInput(String columnName) {

		return getFromOutputValues("InputData", MobileBaseUtil.getTestName(), columnName);
	}

	public static String getFromOutputValues(String sheetName, String testCaseName, String columnName) {

		final String excelPath = outputExcel;
		try {
			final FileInputStream file = new FileInputStream(new File(System.getProperty("user.dir") + excelPath));

			final HSSFWorkbook workbook = new HSSFWorkbook(file);
			final HSSFSheet sheet = workbook.getSheet(sheetName);
			Cell cell = null;
			final int row = findRow(sheet, testCaseName);
			final int column = findColumn(sheet, columnName);

			cell = sheet.getRow(row).getCell(column);

			final String cellValue = cell.toString();

			return cellValue;
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}

	}
	public static int findRow(Sheet sheet, String cellContent) {
		for (final Row row : sheet) {
			for (final Cell cell : row) {
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					if (cell.getRichStringCellValue().getString().trim().equals(cellContent)) {
						return row.getRowNum();
					}
				}
			}
		}
		return 0;
	}

	public static int findColumn(Sheet sheet, String columnValue) {

		int columnIndex = 0;
		for (final Row r : sheet) {
			for (final Cell c : r) {
				if (c.getStringCellValue().equals(columnValue)) {
					columnIndex = c.getColumnIndex();
					return columnIndex;
				}
			}
		}
		return 0;
	}


}
