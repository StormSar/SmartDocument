package ru.stormsar;

import org.bson.Document;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertEquals;

public class TestsTool {

    private Document inDoc;

    private final Path docPath;

    private int testCount = 0;

    public TestsTool(Path docPath) {
        this.docPath = docPath;
        init();
    }

    private void init(){
        try {
            inDoc = loadDoc(docPath);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void resetTestCount() {
        testCount = 0;
    }

    public void getValue(Path docPath, String getPath) throws Exception {
        System.out.printf("Test_%s - ", ++testCount);

        Document out = loadDoc(docPath);

        Document doc = SmartDocument.get(inDoc, getPath, Document.class);
        assertEquals(doc, out);

        System.out.println("\u001B[32m" + "Complete" + "\u001B[0m");
    }

    public <T> void getOptionalValue(T value, String getPath, T newValue) {
        System.out.printf("Test_%s - ", ++testCount);

        T out = (T) SmartDocument.getOptional(inDoc, getPath).orElse(newValue);
        assertEquals(out, value);

        System.out.println("\u001B[32m" + "Complete" + "\u001B[0m");
    }

    public <T> void getValue(T value, String getPath) {
        System.out.printf("Test_%s - ", ++testCount);

        T out = (T) SmartDocument.get(inDoc, getPath);
        assertEquals(out, value);

        System.out.println("\u001B[32m" + "Complete" + "\u001B[0m");
    }

    public <T> void getOptionalValue(Object value, String getPath, Class<T> clazz, T newValue) {
        System.out.printf("Test_%s - ", ++testCount);

        T out = SmartDocument.getOptional(inDoc, getPath, clazz).orElse(newValue);
        assertEquals(out, value);

        System.out.println("\u001B[32m" + "Complete" + "\u001B[0m");
    }

    public <T> void getValue(Object value, String getPath, Class<T> clazz) {
        System.out.printf("Test_%s - ", ++testCount);

        T out = SmartDocument.get(inDoc, getPath, clazz);
        assertEquals(out, value);

        System.out.println("\u001B[32m" + "Complete" + "\u001B[0m");
    }

    public Document loadDoc(Path path) throws Exception {
        byte[] content = Files.readAllBytes(path);
        return Document.parse(new String(content, StandardCharsets.UTF_8));
    }

    public <T> void putValue(T value, String putPath, String testDocPath) throws Exception {
        System.out.printf("Test_%s - ", ++testCount);

        init();

        Document testDoc = loadDoc(Paths.get(testDocPath));

        Document doc = SmartDocument.put(inDoc, putPath, value);
        assertEquals(doc, testDoc);

        System.out.println("\u001B[32m" + "Complete" + "\u001B[0m");
    }

    public void removeValue(String removePath, String testDocPath) throws Exception {
        System.out.printf("Test_%s - ", ++testCount);

        init();

        Document testDoc = loadDoc(Paths.get(testDocPath));

        Document doc = SmartDocument.remove(inDoc, removePath);
        assertEquals(doc, testDoc);

        System.out.println("\u001B[32m" + "Complete" + "\u001B[0m");
    }
}
