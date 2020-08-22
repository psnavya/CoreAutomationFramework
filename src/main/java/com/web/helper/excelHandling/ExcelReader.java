package com.helper.excelHandling;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Calendar;

public class ExcelReader {

	public static String testDataPath = System.getProperty("user.dir") + "\\TestCase.xls";
	public static String sheet = "Verification";
	public static String filename = System.getProperty("user.dir") + "\\src\\config\\testcases\\TestData.xlsx";
	public String path;
	public FileInputStream fis = null;
	public FileOutputStream fileOut = null;
	private final XSSFWorkbook workbook = null;
	// private XSSFSheet sheet = null;
	private XSSFRow row = null;
	private XSSFCell cell = null;

	public void setSheet(String sheet) {
		ExcelReader.sheet = sheet;
	}

	/**
	 * Read Excel Data
	 *
	 * @param sheet
	 * @param intRow
	 * @param intCol
	 * @return Cell Data
	 */
	public static String getExcelCell(final String sheet, final int intRow, final int intCol) {

		return getExcelCell(testDataPath, sheet, intRow, intCol);
	}

	/**
	 * Read Excel Data
	 *
	 * @param intRow
	 * @param intCol
	 * @return Cell Data
	 */
	public static String getExcelCell(final int intRow, final int intCol) {

		return getExcelCell(sheet, intRow, intCol);
	}

	/**
	 * @param excelPath
	 * @param sheet
	 * @param intRow
	 * @param intCol
	 * @return
	 */
	public static String getExcelCell(final String excelPath, final String sheet, final int intRow, final int intCol) {

		try {
			final File file = new File(excelPath);
			final FileInputStream inputStream = new FileInputStream(file);
			final HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			final HSSFSheet worksheet = workbook.getSheet(sheet);
			final HSSFRow row = worksheet.getRow(intRow);
			Row.MissingCellPolicy policy = Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;
			final HSSFCell cellA1 = row.getCell(intCol, policy);
			return cellA1.toString();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param sheet
	 * @return
	 */
	public static int rowCount(final String sheet) {

		return rowCount(testDataPath, sheet);
	}

	/**
	 * @param sheet
	 * @return
	 */
	public static int rowCount(final String excelPath, final String sheet) {

		try {
			final File file = new File(excelPath);
			final FileInputStream inputStream = new FileInputStream(file);
			final HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			final HSSFSheet worksheet = workbook.getSheet(sheet);
			final int rows = worksheet.getPhysicalNumberOfRows();
			return rows;
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * @param path
	 */
	public static void setDataSheet(String path) {

		testDataPath = path;
	}

	/**
	 * Write to Excel's specified cell
	 *
	 * @param Sheet
	 * @param intRow
	 * @param intCell
	 * @param strSetCell
	 * @return Entered string
	 */
	public static String setExcelCell(final String Sheet, final int intRow, final int intCell, final String strSetCell) {

		try {
			final File file = new File(testDataPath);
			final FileInputStream inputStream = new FileInputStream(file);
			final HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			final HSSFSheet worksheet = workbook.getSheet(Sheet);
			final HSSFRow row = worksheet.getRow(intRow);
			final HSSFCell cellA1 = row.getCell(intCell);
			cellA1.setCellValue(strSetCell);
			inputStream.close();
			final FileOutputStream outFile = new FileOutputStream(new File(testDataPath));
			workbook.write(outFile);
			outFile.close();
			return cellA1.toString();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getCellData(String sheetName, String colName, int rowNum) {
		XSSFSheet sheet = null;
		try {
			if (rowNum <= 0)
				return "";

			final int index = workbook.getSheetIndex(sheetName);
			int col_Num = -1;
			if (index == -1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				// System.out.println(row.getCell(i).getStringCellValue().trim());
				if (row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
					col_Num = i;
			}
			if (col_Num == -1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				return "";
			cell = row.getCell(col_Num);

			if (cell == null)
				return "";
			// System.out.println(cell.getCellType());
			if (cell.getCellType() == Cell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

				String cellText = String.valueOf(cell.getNumericCellValue());
				if (DateUtil.isCellDateFormatted(cell)) {
					// format in form of M/D/YY
					final double d = cell.getNumericCellValue();

					final Calendar cal = Calendar.getInstance();
					cal.setTime(DateUtil.getJavaDate(d));
					cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
					cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" + cellText;

					// System.out.println(cellText);

				}

				return cellText;
			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());

		} catch (final Exception e) {

			e.printStackTrace();
			return "row " + rowNum + " or column " + colName + " does not exist in xls";
		}
	}

}
