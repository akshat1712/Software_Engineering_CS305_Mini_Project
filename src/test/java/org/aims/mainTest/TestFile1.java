package org.aims.mainTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.aims.Main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestFile1 {


    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    @BeforeAll
    public static void setup() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterAll
    public static void teardown() {
        System.setOut(originalOut);
    }

    @Test
    public void testStudentMain1() {
        String input="A\n123@iitrpr.ac.in\n123\nE\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        org.aims.Main.main(null);
        assertTrue(outContent.toString().contains("Login Failed"));
    }

    @Test
    public void testFacultyMain2() {
        String input="B\n123@iitrpr.ac.in\n123\nE\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        org.aims.Main.main(null);
        assertTrue(outContent.toString().contains("Login Failed"));
    }

    @Test
    public void testAcademicMain3() {
        String input="C\n123@iitrpr.ac.in\n123\nE\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        org.aims.Main.main(null);
        assertTrue(outContent.toString().contains("Login Failed"));
    }

    @Test
    public void testAdminMain4() {
        String input="D\n123@iitrpr.ac.in\n123\nE\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        org.aims.Main.main(null);
        assertTrue(outContent.toString().contains("Login Failed"));
    }

    @Test
    public void testMain5() {
        String input="F\n123@iitrpr.ac.in\n123\nE\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        org.aims.Main.main(null);
        assertTrue(outContent.toString().contains("Invalid option"));
    }
}
