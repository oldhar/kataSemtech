package com.hca.semtech;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class TestFileUtils {
    private List<String> lines = new ArrayList<>();
    private File file;
    public TestFileUtils createFile() {
        try {
            file = File.createTempFile("testDataFile", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public TestFileUtils withLine(String line) {
        lines.add(line);
        return this;
    }

    public String build() {
        try {
            Files.write(file.toPath(), lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file.getPath();
    }
}
