package com.devtop.discountcalculator.service;

import com.devtop.discountcalculator.DiscountCalculator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileLineReaderService {
    private final String fileDir;
    private final String defaultFile;

    public FileLineReaderService(String fileDir, String defaultFile) {
        this.fileDir = fileDir;
        this.defaultFile = defaultFile;
    }

    public List<String> readLines() {
        List<String> lines = new ArrayList<>();
        Scanner scanner = new Scanner(getFileInputStream());
        scanner.useDelimiter("\r?\n");
        while (scanner.hasNext()) {
            lines.add(scanner.next());
        }
        return lines;
    }

    private InputStream getFileInputStream() {
        if (fileDir == null) {
            ClassLoader classLoader = DiscountCalculator.class.getClassLoader();
            return classLoader.getResourceAsStream(defaultFile);
        }

        try {
            return new DataInputStream(new FileInputStream(new File(fileDir)));
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file: " + fileDir);
        }
        return null;
    }
}
