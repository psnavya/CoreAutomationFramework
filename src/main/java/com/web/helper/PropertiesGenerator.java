package com.helper;

/**
 * Created by Navya on 23-08-2020.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.ITestContext;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PropertiesGenerator {

    public static Dictionary dict = new Hashtable();
    public static String TestcasePath = "";
    public static List<Map<String, String>> TestSet = null;
    public static Map<String, String> testData = new HashMap<String, String>();

    public static void loadParameters(String testCase) {

        String XML_Path = System.getProperty("user.dir") + "/src/main/resources/config.xml"; // Environment
        File fXmlFile = new File(XML_Path);
        try {
            FileInputStream is = new FileInputStream(fXmlFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Variable");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    TestcasePath = (eElement.getElementsByTagName("TestcasePath").item(0).getTextContent()).trim();
                }
            }
            Statement stmt = null;
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            stmt = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Excel Driver (*.xls, *.xlsx, *.xlsm, *.xlsb)};DBQ=" + TestcasePath + ";READONLY=false").createStatement();

            ResultSet rs_Query2 = stmt.executeQuery("select * from [InputData$]where TC_Name = " + "'" + testCase + "'");
            ResultSetMetaData rsmd = rs_Query2.getMetaData();
            int ColumnCount = rsmd.getColumnCount();
            String DictVal = "";
            String DictKey = "";
            while (rs_Query2.next()) {
                for (int i = 1; i < ColumnCount - 1; i++) {
                    DictKey = rsmd.getColumnName(i);
                    DictVal = rs_Query2.getString(DictKey);
                    if (null != DictVal) {
                        dict.put(DictKey, DictVal);
                    }
                }
            }
            System.out.println(dict);
            System.out.println(dict.get("Hphone_Stat"));

            rs_Query2.close();
            stmt.close();
            is.close();
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String currentRunningTestCase = "";

    public static String getData(String dataColumn) {
        try {
            if (testData.get(dataColumn) != null || testData.get(dataColumn) == "")
                return testData.get(dataColumn);
            else
                return null;
        } catch (Exception e) {
            System.out.println("Unable to find data value for provided data column");
            e.printStackTrace();
            return null;
        }
    }

    public static void setData(String dataColumn, String dataValue) {

        testData.put(dataColumn, dataValue);
    }

    public static void setCurrentRunningTestCase(String currentRunningTestCaseValue) {
        currentRunningTestCase = currentRunningTestCaseValue;
    }

    public static void deleteFiles(String directoryPath) {

        File directory = new File(directoryPath);
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Directory cleared successfully");
    }

    public static void loadingDataValues(ITestContext ctx) {

        ctx.getCurrentXmlTest().getSuite().getTest();
        String currentRunningTestCase = ctx.getCurrentXmlTest().getName();
        setCurrentRunningTestCase(currentRunningTestCase);
        loadParameters(currentRunningTestCase);
    }

    public static Map<String, String> getTestData(final ITestContext testContext) {

        // Get URL functions
        // read all data from excelHandling
        for (Map<String, String> s : TestSet) {
            if (s.containsValue(testContext.getCurrentXmlTest().getName())) {
                testData.putAll(s);
                break;
            }
        }
        return testData;
    }

    public static String setExcelCell(final String Sheet, final int intRow, final int intCell, final String strSetCell) {

        try {
            final File file = new File(TestcasePath);
            final FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = null;
            try {
                workbook = WorkbookFactory.create(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final Sheet worksheet = workbook.getSheet(Sheet);
            final Row row = worksheet.getRow(intRow);
            final Cell cellA1 = row.getCell(intCell);
            cellA1.setCellValue(strSetCell);
            inputStream.close();

            final File fileTwo = new File(TestcasePath);
            final FileOutputStream outFile = new FileOutputStream(fileTwo);
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



    public static String setExcelCellX(final String Sheet,
			/* , final int intRow, final int intCell, */ final String columnName, final String strSetCell) {

        try {
            final File file = new File(TestcasePath);
            final FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = null;
            try {
                workbook = WorkbookFactory.create(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final Sheet worksheet = workbook.getSheet(Sheet);

            int rowValue = findRow(worksheet, currentRunningTestCase);
            int colValue = findColumn(worksheet, columnName);

            final Row row = worksheet.getRow(rowValue);
            final Cell cellA1 = row.getCell(colValue);
            cellA1.setCellValue(strSetCell);
            inputStream.close();

            final File fileTwo = new File(TestcasePath);
            final FileOutputStream outFile = new FileOutputStream(fileTwo);
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

    public static int findRow(Sheet sheet, String cellContent) {
        for (Row row : sheet) {
            for (Cell cell : row) {
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
        for (Row r : sheet) {
            for (Cell c : r) {
                if (c.getStringCellValue().contains(columnValue)) {
                    columnIndex = c.getColumnIndex();
                    return columnIndex;
                }
            }
        }
        return 0;
    }

    public static String valueMethod(String sampleTestData) {
        System.out.println(sampleTestData);
        return sampleTestData;
    }
}
