package ru.stormsar;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


class SmartDocumentTest {

    private final String path = "subservices.0.statusHistory";
    private final String fileName = "src/test/resources/testDocument.json";

    @Test
    void getValue() {
        try {
            byte[] content = Files.readAllBytes(Paths.get(fileName));
            Document doc = Document.parse(new String(content, StandardCharsets.UTF_8));
            System.out.println(SmartDocument.getValue(doc, path, String.class));
        } catch (Throwable t){
            t.printStackTrace();
        }
    }
}