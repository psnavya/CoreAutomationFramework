package com.helper.testDataHandling;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.helper.excelHandling.ExcelReader;

import java.util.List;

public class ControlSheetHandler extends ExcelReader {

	public static final String controlSheetPath = System.getProperty("user.dir") + "\\ControlSheet.xls";
	public static ListMultimap<String, String> multimap = ArrayListMultimap.create();

	public static ListMultimap loadControlSheet() {
		for (int rowNumber = 7; rowNumber < rowCount(controlSheetPath, "Module"); rowNumber++) {
			if (getExcelCell(controlSheetPath, "Module", rowNumber, 4).equalsIgnoreCase("yes")) {
				multimap.put(getExcelCell(controlSheetPath, "Module", rowNumber, 1), getExcelCell(controlSheetPath, "Module", rowNumber, 2));
			}
		}

		for (final String sheetName : multimap.keySet()) {
			final List<String> testCases = multimap.get(sheetName);
			// System.out.println(sheetName + ": " + testCases );
		}
		return multimap;
	}
}
