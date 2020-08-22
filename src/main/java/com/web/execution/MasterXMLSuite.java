package com.execution;

import com.base.BaseUtil;
import com.helper.FileOperations;
import com.helper.PropertiesGenerator;
import com.helper.excelHandling.ExcelToXML;
import com.relevantcodes.extentreports.ExtentReports;
import org.testng.TestNG;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Navya on 23-08-2020.
 */

public class MasterXMLSuite extends PropertiesGenerator {


    private static String xmlSuiteFilePath = null;
    public static String EXTENTREPORTPATH = System.getProperty("user.dir") + "\\test-output\\extentReport\\ExtentReport"+ System.currentTimeMillis()+".html";
    public static AppDriver app = new AppDriver();
    public static Map<String,String> tempMap = new HashMap<>();
    public static ExtentReports extent = null;

    public static void main(String[] args) throws IOException {

		/*extent = new ExtentReports(EXTENTREPORTPATH, false, NetworkMode.OFFLINE);
        BaseUtil.setExtent(extent);
		extent.addSystemInfo("Release# ", Appdriver.readConfigFile("BUILD_NUMBER"));
		extent.addSystemInfo("Environment: ", Appdriver.readConfigFile("ENVIRONMENT"));*/

        FileOperations.afterSuite();
        ExcelToXML.Setup();
        ArrayList<String> suitePath = new ArrayList<String>();
        final List<String> suites = new ArrayList<String>();
        suitePath = generatePaths();
        final TestNG testng = new TestNG();
        for (int i = 0; i < suitePath.size(); i++) {
            xmlSuiteFilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\DynamicXML\\" + suitePath.get(i);
            suites.add(xmlSuiteFilePath);
        }
        tempMap=app.ExecutionPlatform();
        System.out.println(suites);
        testng.setTestSuites(suites);
        testng.run();
        BaseUtil.clearXMLFiles();
    }

    public static ArrayList<String> generatePaths() {
        final File folder = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\DynamicXML");
        final File[] listOfFiles = folder.listFiles();
        final ArrayList<String> listOfValues = new ArrayList<String>();
        for (final File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
                listOfValues.add(file.getName());
            }
        }
        return listOfValues;
    }

    public static void suiteXMLs(ArrayList<String> arrayList) {
        final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
        }
        final Document doc = docBuilder.newDocument();
        final Element suiteElementTop = doc.createElement("suite");
        suiteElementTop.setAttribute("guice-stage", "DEVELOPMENT");
        suiteElementTop.setAttribute("name", "Default suite");

        doc.appendChild(suiteElementTop);

        final Element suiteElement = doc.createElement("suite-files");
        suiteElementTop.appendChild(suiteElement);
        for (final String suiteName : arrayList) {

            final Element suiteElementTwo = doc.createElement("suite-file");

            suiteElement.appendChild(suiteElementTwo);
            suiteElementTwo.setAttribute("path", System.getProperty("user.dir") + "\\src\\test\\resources\\DynamicXML\\" + suiteName);
        }

        try {
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
    }
}
