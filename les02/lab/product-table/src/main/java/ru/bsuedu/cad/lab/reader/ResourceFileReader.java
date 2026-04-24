package ru.bsuedu.cad.lab.reader;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ResourceFileReader {

    public List<String> readLines(String filePath) throws Exception {
        List<String> lines = new ArrayList<>();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;

            while((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}
