package com.helper;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

/**
 * Created by Navya on 23-08-2020.
 */
public class FileOperations {
    public static void afterSuite() {

        deleteFiles(System.getProperty("user.dir") + "\\src\\test\\resources\\DynamicXML\\");
    }

    public static void deleteFiles(String directoryPath) {

        final File directory = new File(directoryPath);
        try {
            FileUtils.cleanDirectory(directory);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        System.out.println("Directory cleared successfully");
    }

    public static String encodeFileToBase64Binary(String path) {

        File file = new File(path);
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.getEncoder().encode(bytes), "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "data:image/png;base64, " + encodedfile;
    }

}
