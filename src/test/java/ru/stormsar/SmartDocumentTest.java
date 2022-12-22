package ru.stormsar;

import junit.framework.TestCase;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class SmartDocumentTest extends TestCase {

    TestsTool tool = new TestsTool(Paths.get("src/test/resources/data/in.json"));


    @Test
    public void get() throws Exception {
        System.out.println("\u001B[33m" + "\n\nGET TESTS\n" + "\u001B[0m");
        tool.resetTestCount();

        tool.getValue(105, "entry.age");

        tool.getValue(Paths.get("src/test/resources/get/entry.json"), "entry");

        tool.getValue(Paths.get("src/test/resources/get/entry.address.json"), "entry.address");

        tool.getValue("first", "array.0.name");

        List<Object> out = new ArrayList<>();
        out.add("first");
        out.add(null);
        out.add("array");
        tool.getValue(out, "array.name");

        out = new ArrayList<>();
        out.add(null);
        out.add(null);

        List<Object> entry = new ArrayList<>();
        entry.add("1");
        entry.add("2");
        out.add(entry);
        tool.getValue(out, "array.value.data");

        tool.getValue("2", "array.2.value.1.data");

        Document entryDoc = new Document("name", "first").append("value", "test");
        tool.getValue(entryDoc, "array.0");
    }

    @Test
    public void put() throws Exception {
        System.out.println("\u001B[33m" + "\n\nPUT TESTS\n" + "\u001B[0m");
        tool.resetTestCount();


        tool.putValue("new value", "entry.name", "src/test/resources/put/entry.name_new value.json");

        tool.putValue("new value", "entry.value", "src/test/resources/put/entry.value_new value.json");

        Document newDoc = new Document("doc", "doc");
        tool.putValue(newDoc, "entry.address", "src/test/resources/put/entry.address_doc.json");

        newDoc = new Document();
        tool.putValue(newDoc, "entry", "src/test/resources/put/entry_clear.json");

        newDoc = new Document("newValue", "new");
        tool.putValue(newDoc, "entry.newEntry", "src/test/resources/put/entry.newEntry.newValue_new.json");

        tool.putValue("add", "array.0.name", "src/test/resources/put/array.0.name_add.json");

        tool.putValue("add", "array.5.name", "src/test/resources/put/array.5.name_add.json");

        tool.putValue("add", "array.name", "src/test/resources/put/array.5.name_add.json");

        tool.putValue("", "array", "src/test/resources/put/array.json");

        tool.putValue("new", "array.2.value.[].name", "src/test/resources/put/array.2.value.[].name_new.json");
    }

    @Test
    public void remove() throws Exception {
        System.out.println("\u001B[33m" + "\n\nREMOVE TESTS\n" + "\u001B[0m");
        tool.resetTestCount();


        tool.removeValue("number", "src/test/resources/remove/number.json");

        tool.removeValue("entry.address", "src/test/resources/remove/entry.address.json");

        tool.removeValue("array.name", "src/test/resources/remove/array.name.json");

        tool.removeValue("array.2.value", "src/test/resources/remove/array.2.value.json");

        tool.removeValue("array.2", "src/test/resources/remove/array.2.json");
    }
}