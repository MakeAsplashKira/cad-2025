package ru.bsuedu.cad.lab.reader;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ResourceFileReader implements Reader, InitializingBean {
    @Value("${products.file.name}")
    private String fileName;

    @Override
    public void afterPropertiesSet() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String initTime = LocalDateTime.now().format(formatter);
        System.out.println("[ResourceFileReader] Бин инициализирован в: " + initTime) ;
    }

    @Override
    public List<String> read() throws Exception {
        List<String> lines = new ArrayList<>();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;

            while((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}
