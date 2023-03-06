package org.aims.facultyTest;

import org.aims.service.FacultyService;
import org.aims.userimpl.FacultyImpl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
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
        System.setOut(new PrintStream(outContent));

    }

    @AfterAll
    public static void teardown() {
        System.setOut(originalOut);
    }


    @Test
    @Order(1)
    public void testLogin1(){
        when(mockfacultyImpl.login("mudgal@iitrpr.ac.in","1234")).thenReturn(true);
        assertTrue(testfacultyService.login("mudgal@iitrpr.ac.in","1234"));
    }

    @Test
    @Order(2)
    public void testLogin2(){
        when(mockfacultyImpl.login("mudgal@iitrpr.ac.in","1234")).thenReturn(false);
        assertTrue(!testfacultyService.login("mudgal@iitrpr.ac.in","1234"));
    }


    @Test
    @Order(4)
    public void testViewGrades1(){
        when(mockfacultyImpl.viewGrades("2020csb1068@iitrpr.ac.in")).thenReturn(new String[]{"CS100 || 1 || 2022 || 8","CS104 || 1 || 2021 || 9"});
        String input="Z\nD\n2020csb1068@iitrpr.ac.in\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        testfacultyService.showmenu();
        assertTrue(outContent.toString().contains("CS100 || 1 || 2022 || 8"));
        assertTrue(outContent.toString().contains("CS104 || 1 || 2021 || 9"));
    }

    @Test
    @Order(5)
    public void testViewGrades2(){
        when(mockfacultyImpl.viewGrades("2020csb1068@iitrpr.ac.in")).thenReturn(null);
        String input="D\n2020csb1068@iitrpr.ac.in\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        testfacultyService.showmenu();
        assertTrue(outContent.toString().contains("No grades available"));
    }

    @Test
    @Order(6)
    public void testChangePassword(){
        when(mockfacultyImpl.changePassword("123","456")).thenReturn("Password Changed Successfully");
        String input="E\n123\n456\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        testfacultyService.showmenu();
        assertTrue(outContent.toString().contains("Password Changed Successfully"));
    }

    @Test
    @Order(9)
    public void testUpdateGrade(){
        when(mockfacultyImpl.updateGrades("D:\\CS305\\MiniProject\\Grades\\CS101.txt","CS101")).thenReturn("Grade Updated Successfully");
        String input="F\nCS101\nD:\\CS305\\MiniProject\\Grades\\CS101.txt\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        testfacultyService.showmenu();
        assertTrue(outContent.toString().contains("Grade Updated Successfully"));
    }

    @Test
    @Order(10)
    public void testTakeBackCourse(){
        when(mockfacultyImpl.takeBackCourse("CS101")).thenReturn("Course Taken Back Successfully");
        String input="C\nCS101\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        testfacultyService.showmenu();
        assertTrue(outContent.toString().contains("Course Taken Back Successfully"));
    }


    @Test
    @Order(11)
    public void testOfferCourse(){
        when(mockfacultyImpl.offerCourse("CS101",8.00,new String[]{"CS100,8"})).thenReturn("Course Offered Successfully");

        String input="B\nCS101\nABC\n8.00\nBCD\n-1\n1\nCS100,8\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        testfacultyService.showmenu();
        assertTrue(outContent.toString().contains("Course Offered Successfully"));
    }

}
