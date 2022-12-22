package ru.stormsar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.file.Paths;

@RunWith(JUnit4.class)
public class SmartDocumentEmtyTest {
    TestsTool tool = new TestsTool(Paths.get("src/test/resources/data/in.json"));

    @Test
    public void emptyGet() {
        System.out.println("\u001B[33m" + "\n\nEMPTY GET TESTS\n" + "\u001B[0m");
        tool.resetTestCount();


        tool.getValue(null, "entry.array", int.class);

        tool.getValue(null, "array.2.value.data1", String.class);
    }

    @Test
    public void emptyRemove() throws Exception {
        System.out.println("\u001B[33m" + "\n\nEMPTY REMOVE TESTS\n" + "\u001B[0m");
        tool.resetTestCount();


        tool.removeValue("value", "src/test/resources/data/in.json");

        tool.removeValue("array.3", "src/test/resources/data/in.json");

        tool.removeValue("array.2.value.0.name", "src/test/resources/data/in.json");
    }

}
