package com.Lee.bookstore_backend.utils.fileProcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
public class CsvFileProcessor {
    public boolean isCsvFile(MultipartFile file) {
        String pattern = ".+\\.csv";
        return Pattern.matches(pattern, file.getOriginalFilename());
    }

    public String readContent(MultipartFile multipartFile) throws IOException {
        StringBuilder content = new StringBuilder();
        File file = File.createTempFile("tmp", ".tmp", new File(".\\"));
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8));
//        else reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("/n");
        }
        System.out.println("content: " + content);
        return content.toString();
    }

    public JSONArray parseContent(String content) {
        JSONArray parseResult = new JSONArray();
        String[] rows = content.split("/n");
        int rowNumber = rows.length;
        String[] columnNames = rows[0].split(",");
        int colNumber = columnNames.length;
        for (int i = 1; i < rowNumber; ++i) {
            String[] cols = rows[i].split(",");
            JSONObject object = new JSONObject();
            int colRealSize = cols.length;
            for (int j = 0; j < colRealSize; ++j) {
                String col = cols[j];
                if (col.startsWith("\"") && col.startsWith("\""))
                    col = col.substring(1, col.length() - 2);
                object.put(columnNames[j], col);
            }
            for (int j = colRealSize; j < colNumber; ++j) {
                object.put(columnNames[j], null);
            }
            parseResult.add(object);
        }
        System.out.println("parseResult: " + parseResult.toString());
        return parseResult;
    }

}
