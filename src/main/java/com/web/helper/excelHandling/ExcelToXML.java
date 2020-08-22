package com.helper.excelHandling;

import com.google.common.collect.ListMultimap;
import com.helper.testDataHandling.ControlSheetHandler;
import com.helper.Log;
import org.testng.annotations.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.List;


public class ExcelToXML extends ExcelReader {

    public static String testSuiteFilePath = null;
    public static String xmlSuiteFilePath = null;
    public static ListMultimap<String, String> multimap = null;
    public static String currentWorkBook = null;
    public static List<String> testCaseNames = null;
    String[] allSheets;

    @Test
    public static void Setup() {

        multimap = ControlSheetHandler.loadControlSheet();
        for (final String currentWork : multimap.keySet()) {
            currentWorkBook = currentWork;
            testCaseNames = multimap.get(currentWork);
            System.out.println("SheetName: " + currentWork + " testcase name: " + testCaseNames);
            testSuiteFilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\DataSheet\\" + currentWork + ".xls";
            xmlSuiteFilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\DynamicXML\\" + currentWork + "Updated.xml";
            Testrun();
        }
    }

    public static void Testrun() {

        System.out.println("TestCasePAth :" + testSuiteFilePath);
        final String sheetNameTemp = "TestCase";
        Log.startTestCase("Reading data from Excel");
        buildXML(sheetNameTemp);
    }

	/*
     * public static void setTestSuiteFilePath(String testSuiteFilePath) {
	 * ExcelToXML.testSuiteFilePath = testSuiteFilePath; }
	 *
	 * public static void setXmlSuiteFilePath(String xmlSuiteFilePath) {
	 * ExcelToXML.xmlSuiteFilePath = xmlSuiteFilePath; }
	 */

	public static void buildXML(String sheetName) {

		try {
			final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = null;
			try {
				docBuilder = docFactory.newDocumentBuilder();
			} catch (final ParserConfigurationException e) {
				e.printStackTrace();
			}
			// suite tag starts here
			final Document doc = docBuilder.newDocument();
			final Element suiteElement = doc.createElement("suite");
			doc.appendChild(suiteElement);
			suiteElement.setAttribute("guice-stage", "DEVELOPMENT");
			suiteElement.setAttribute("name", currentWorkBook);
			// suite tag ends here
			// Adding Listeners
			final Element listenersElement = doc.createElement("Listeners");
			suiteElement.appendChild(listenersElement);
			// Adding Listener
			final Element listenerElement = doc.createElement("Listener");
			listenersElement.appendChild(listenerElement);
			// Listeners attributes
			listenerElement.setAttribute("class-name", "TestListeners.class");
			Log.info("Creating dynamic XML");
			for (int rowNumber = 1; rowNumber < rowCount(testSuiteFilePath, sheetName); rowNumber++) {
				try {
					if (!(getExcelCell(testSuiteFilePath, sheetName, rowNumber, 0).equalsIgnoreCase("<EndTestCase>")) && testCaseNames.contains(getExcelCell(testSuiteFilePath, sheetName, rowNumber, 0))) {
						final Element testElement = doc.createElement("test");
						suiteElement.appendChild(testElement);
						testElement.setAttribute("verbose", "2");
						testElement.setAttribute("name", getExcelCell(testSuiteFilePath, sheetName, rowNumber, 0));
						if (testCaseNames.contains(getExcelCell(testSuiteFilePath, sheetName, rowNumber, 0))) {
							final Element classesElement = doc.createElement("classes");
							testElement.appendChild(classesElement);

							for (int i = rowNumber; i < rowCount(testSuiteFilePath, sheetName); i++) {
								if (((getExcelCell(testSuiteFilePath, sheetName, i, 2) != "") && getExcelCell(testSuiteFilePath, sheetName, i, 2) != null) && getExcelCell(testSuiteFilePath, sheetName, i, 1) != "" /*&& i != rowNumber*/) {
									final Element classElementNew = doc.createElement("class");
									classesElement.appendChild(classElementNew);
									classElementNew.setAttribute("name", getExcelCell(testSuiteFilePath, sheetName, i, 1));
									
									Element methodsElement = doc.createElement("methods");
									classElementNew.appendChild(methodsElement);
									
									Element includeElement = doc.createElement("include");
									methodsElement.appendChild(includeElement);
									includeElement.setAttribute("name", getExcelCell(testSuiteFilePath, sheetName, i, 2));
									
									for(int j = i+1; j < rowCount(testSuiteFilePath, sheetName); j++){
										if((getExcelCell(testSuiteFilePath, sheetName, j, 1) == "") && (getExcelCell(testSuiteFilePath, sheetName, j, 2) != "")){
											Element newIncludeElement = doc.createElement("include");
											methodsElement.appendChild(newIncludeElement);
											newIncludeElement.setAttribute("name", getExcelCell(testSuiteFilePath, sheetName, j, 2));
											i++;
										}else
											break;
									}
								} else if (getExcelCell(testSuiteFilePath, sheetName, i, 2).isEmpty()) {
									rowNumber = i;
									break;
								}
							}
						}
					}
					else if(getExcelCell(testSuiteFilePath, sheetName, rowNumber, 0).equalsIgnoreCase("<EndTestCase>")){
						break;
					}
					else {
						for(int k=rowNumber;k < rowCount(testSuiteFilePath, sheetName); k++){
							if(testCaseNames.contains(getExcelCell(testSuiteFilePath, sheetName, k, 0))){
								rowNumber = k-1;
								break;
							}
						}
					}
				} catch (final NullPointerException exception) {
					System.out.println(exception);
					Log.error("Line Number: " + exception.getStackTrace()[1].getLineNumber() + "  : " + exception);
				}
			} // End of outer for loop
			Log.info("XML creation completed");
			// include tag ends here
			try {
				
				TransformerFactory tf = TransformerFactory.newInstance();
				Transformer transformern = tf.newTransformer();
				transformern.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				StringWriter writer = new StringWriter();
				transformern.transform(new DOMSource(doc), new StreamResult(writer));
				String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
				System.out.println(output);
				
				System.out.println(doc.toString());
				final TransformerFactory transformerFactory = TransformerFactory.newInstance();
				final Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				final DOMSource source = new DOMSource(doc);
				FileOutputStream fileOut;
				try {
					fileOut = new FileOutputStream(xmlSuiteFilePath);
					final StreamResult result = new StreamResult(fileOut);
					transformer.transform(source, result);
				} catch (final FileNotFoundException e) {
					e.printStackTrace();
				}
			} catch (final TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (final IllegalArgumentException e) {
				e.printStackTrace();
			} catch (final TransformerFactoryConfigurationError e) {
				e.printStackTrace();
			} catch (final TransformerException e) {
				e.printStackTrace();
			}
			System.out.println("File saved!");
		} catch (final DOMException e) {
			e.printStackTrace();
		}
	}
	/*
	 * @AfterSuite public static void afterSuite(){ deleteFiles(
	 * System.getProperty("user.dir")+"\\src\\main\\resources\\DynamicXML\\"); }
	 */

	/*
	 * public static void deleteFiles(String directoryPath) {
	 *
	 * File directory = new File(directoryPath); try { for (File file :
	 * directory.listFiles()) { Files.delete(file); } } catch (IOException e) {
	 * e.printStackTrace(); Log.info(""+e.getStackTrace()); }
	 * System.out.println("Directory cleared successfully"); }
	 */
}