package org.aims.facultyTest;

import org.aims.service.FacultyService;
import org.aims.userimpl.FacultyImpl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestFile1 {

    private static FacultyImpl mockfacultyImpl;
    private static FacultyService testfacultyService;

    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    @BeforeAll
    public static void setup() {
        mockfacultyImpl = mock( FacultyImpl.class);
        testfacultyService = new FacultyService(mockfacultyImpl);
//        System.setOut(new PrintStream(outContent));

    }

    @AfterAll
    public static void teardown() {
        System.setOut(originalOut);
    }


    @Test
    public void testLogin1(){
        when(mockfacultyImpl.login(isA(String.class),isA(String.class))).thenReturn(true);
        assertTrue(testfacultyService.login("2020csb1068@iitrpr.ac.in","2020csb1068"));
    }

    @Test
    public void testLogin2(){
        when(mockfacultyImpl.login(isA(String.class),isA(String.class))).thenReturn(false);
        assertTrue(!testfacultyService.login("2020csb1068@iitrpr.ac.in","2020csb1068"));
    }


    @Test
    public void testViewGrades(){

        when(mockfacultyImpl.viewGrades(isA(String.class))).thenReturn(new String[]{"2020csb1068","2020csb1069","2020csb1070"});
        String input="D\n2020csb1068@iitrpr.ac.in\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        testfacultyService.showmenu();
    }
}
