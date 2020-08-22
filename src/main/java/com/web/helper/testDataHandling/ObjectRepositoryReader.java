package com.helper.testDataHandling;

import com.helper.Log;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class ObjectRepositoryReader {

	static Properties props;
	public static Map<String, String> knownGoodMap = new LinkedHashMap<String, String>();
	private static String key = "";
	private static String value = "";

	public static Map<String, String> getKnownGoodMap(String filePath) throws IOException {
		try {
			FileInputStream fis = new FileInputStream(filePath);
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			HSSFSheet sheet = workbook.getSheet("OR");
			// HSSFRow row = sheet.getRow(0);
			Iterator<Row> rows = sheet.rowIterator();

			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				if (row.getRowNum() != 0) {
					Iterator<Cell> cells = row.cellIterator();

					while (cells.hasNext()) {
						HSSFCell cell = (HSSFCell) cells.next();
						HSSFCell cellElementValue = (HSSFCell) cells.next();
						if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
							key = cell.getStringCellValue();

							value = cellElementValue.getStringCellValue();
						}
						if (key != null && value != null) {
							knownGoodMap.put(key, value);

						}
					}
				} // end of if condition
			}
		} catch (Exception e) {
			Log.warn("No Such Element Exception Occured ..... ");
			e.printStackTrace();
		}

		return knownGoodMap;
	}

	public static String writeToPropertiesFile(String propertiesPath) throws IOException {

		Log.info("Loading Property file");
		props = new Properties();

		File propertiesFile = new File(propertiesPath);
		try {
			FileOutputStream Fos = new FileOutputStream(propertiesFile);
			Iterator mapIterator = knownGoodMap.keySet().iterator();

			while (mapIterator.hasNext()) {

				String key = mapIterator.next().toString();

				String value = knownGoodMap.get(key);
				//Log.info("Writing Property file");
				props.setProperty(key, value);
			}

			props.store(Fos, null);

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return propertiesPath;
	}

	public static By getLocator(final String logicalElementName)  {

		By valueElement = null;
		try {
			String locatorType = "";
			//Log.info("Extracting locators from PropertyFile");
			String locator = props.getProperty(logicalElementName);
			locatorType = locator.split(">")[0];
			final String locatorValue = locator.split(">")[1];

			if (locatorType.toLowerCase().equals("id")) {
                valueElement = By.id(locatorValue);
            } else if (locatorType.toLowerCase().equals("name")) {
				valueElement = By.name(locatorValue);
            } else if (locatorType.toLowerCase().equals("classname") || locatorType.toLowerCase().equals("class")) {
				valueElement = By.className(locatorValue);
            } else if (locatorType.toLowerCase().equals("tagname") || locatorType.toLowerCase().equals("tag")) {
				valueElement = By.className(locatorValue);
            } else if (locatorType.toLowerCase().equals("linktext") || locatorType.toLowerCase().equals("link")) {
				valueElement = By.linkText(locatorValue);
            } else if (locatorType.toLowerCase().equals("partiallinktext")) {
				valueElement = By.partialLinkText(locatorValue);
            } else if (locatorType.toLowerCase().equals("cssselector") || locatorType.toLowerCase().equals("css")) {
				valueElement = By.cssSelector(locatorValue);
            } else if (locatorType.toLowerCase().equals("xpath")) {
				valueElement = By.xpath(locatorValue);
            }
            return valueElement;
		} catch (Exception e) {
			e.printStackTrace();
			return valueElement;
		}
	}
}
