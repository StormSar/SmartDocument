package ru.stormsar;

import junit.framework.TestCase;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class SmartDocumentTest extends TestCase {
    private Document inDoc;

    @Before
    public void init() throws Exception {
        inDoc = loadDoc(Paths.get("src/test/resources/data/in.json"));
    }

    @Test
    public void get() throws Exception {
        System.out.println("\u001B[33m" + "\n\nGET TESTS\n" + "\u001B[0m");


        System.out.print("Test_1 - ");
        getValue(105, "entry.age");

        System.out.print("Test_2 - ");
        getValue(Paths.get("src/test/resources/get/entry.json"), "entry");

        System.out.print("Test_3 - ");
        getValue(Paths.get("src/test/resources/get/entry.address.json"), "entry.address");

        System.out.print("Test_4 - ");
        getValue("first", "array.0.name");

        System.out.print("Test_5 - ");
        List<Object> out = new ArrayList<>();
        out.add("first");
        out.add(null);
        out.add("array");
        getValue(out, "array.name");

        out = new ArrayList<>();
        out.add(null);
        out.add(null);

        System.out.print("Test_6 - ");
        List<Object> entry = new ArrayList<>();
        entry.add("1");
        entry.add("2");
        out.add(entry);
        getValue(out, "array.value.data");

        System.out.print("Test_7 - ");
        getValue("2", "array.2.value.1.data");

        System.out.print("Test_8 - ");
        Document entryDoc = new Document("name", "first").append("value", "test");
        getValue(entryDoc, "array.0");
    }

    @Test
    public void put() throws Exception {
        System.out.println("\u001B[33m" + "\n\nPUT TESTS\n" + "\u001B[0m");


        System.out.print("Test_1 - ");
        putValue("new value", "entry.name", "src/test/resources/put/entry.name_new value.json");

        System.out.print("Test_2 - ");
        putValue("new value", "entry.value", "src/test/resources/put/entry.value_new value.json");

        System.out.print("Test_3 - ");
        Document newDoc = new Document("doc", "doc");
        putValue(newDoc, "entry.address", "src/test/resources/put/entry.address_doc.json");

        System.out.print("Test_4 - ");
        newDoc = new Document();
        putValue(newDoc, "entry", "src/test/resources/put/entry_clear.json");

        System.out.print("Test_5 - ");
        newDoc = new Document("newValue", "new");
        putValue(newDoc, "entry.newEntry", "src/test/resources/put/entry.newEntry.newValue_new.json");

        System.out.print("Test_6 - ");
        putValue("add", "array.0.name", "src/test/resources/put/array.0.name_add.json");

        System.out.print("Test_7 - ");
        putValue("add", "array.5.name", "src/test/resources/put/array.5.name_add.json");

        System.out.print("Test_8 - ");
        putValue("add", "array.name", "src/test/resources/put/array.5.name_add.json");

        System.out.print("Test_9 - ");
        putValue("", "array", "src/test/resources/put/array.json");

        System.out.print("Test_10 - ");
        putValue("new", "array.2.value.[].name", "src/test/resources/put/array.2.value.[].name_new.json");
    }

    @Test
    public void remove() throws Exception {
        System.out.println("\u001B[33m" + "\n\nREMOVE TESTS\n" + "\u001B[0m");


        System.out.print("Test_1 - ");
        removeValue("number", "src/test/resources/remove/number.json");

        System.out.print("Test_2 - ");
        removeValue("entry.address", "src/test/resources/remove/entry.address.json");

        System.out.print("Test_3 - ");
        removeValue("array.name", "src/test/resources/remove/array.name.json");

        System.out.print("Test_4 - ");
        removeValue("array.2.value", "src/test/resources/remove/array.2.value.json");

        System.out.print("Test_5 - ");
        removeValue("array.2", "src/test/resources/remove/array.2.json");
    }

    private void getValue(Path docPath, String getPath) throws Exception {
        Document out = loadDoc(docPath);

        Document doc = (Document) SmartDocument.get(inDoc, getPath);
        assertEquals(doc, out);

        System.out.println("\u001B[32m" + "Complete" + "\u001B[0m");
    }

    private <T> void getValue(T value, String getPath) throws Exception {
        T out = (T) SmartDocument.get(inDoc, getPath);
        assertEquals(out, value);

        System.out.println("\u001B[32m" + "Complete" + "\u001B[0m");
    }

    private Document loadDoc(Path path) throws Exception {
        byte[] content = Files.readAllBytes(path);
        return Document.parse(new String(content, StandardCharsets.UTF_8));
    }

    private <T> void putValue(T value, String putPath, String testDocPath) throws Exception {
        init();

        Document testDoc = loadDoc(Paths.get(testDocPath));

        Document doc = SmartDocument.put(inDoc, putPath, value);
        assertEquals(doc, testDoc);

        System.out.println("\u001B[32m" + "Complete" + "\u001B[0m");
    }

    private <T> void removeValue(String putPath, String testDocPath) throws Exception {
        init();

        Document testDoc = loadDoc(Paths.get(testDocPath));

        Document doc = SmartDocument.remove(inDoc, putPath);
        assertEquals(doc, testDoc);

        System.out.println("\u001B[32m" + "Complete" + "\u001B[0m");
    }
}