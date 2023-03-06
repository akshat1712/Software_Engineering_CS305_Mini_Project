package org.aims.studentTest;


import org.aims.userimpl.StudentImpl;
import org.aims.service.StudentService;

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


    private static StudentImpl mockstudentImpl;
    private static StudentService teststudentService;

    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    @BeforeAll
    public static void setup() {
        mockstudentImpl = mock( StudentImpl.class);
        teststudentService = new StudentService(mockstudentImpl);
        System.setOut(new PrintStream(outContent));
    }

    @AfterAll
    public static void teardown() {
        System.setOut(originalOut);
    }

    @Test
    @Order(1)
    public void testLogin1() {
        when(mockstudentImpl.login("2020csb1068@iitrpr.ac.in","123")).thenReturn(true);
        assertTrue(teststudentService.login("2020csb1068@iitrpr.ac.in","123"));
    }

    @Test
    @Order(2)
    public void testLogin2() {
        when(mockstudentImpl.login("2020csb1068@iitrpr.ac.in","123")).thenReturn(false);
        assertTrue(!teststudentService.login("2020csb1068@iitrpr.ac.in","123"));
    }

    @Test
    @Order(3)
    public void testChangePassword() {
        when(mockstudentImpl.changePassword("123","456")).thenReturn("Password Changed Successfully");
        String input="F\n123\n456\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        teststudentService.showmenu();
        assertTrue(outContent.toString().contains("Password Changed Successfully"));
    }

    @Test
    @Order(4)
    public void testRegisterCourse() {
        when(mockstudentImpl.registerCourse("CS101")).thenReturn("Course Registered");
        String input="B\nCS101\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        teststudentService.showmenu();
        assertTrue(outContent.toString().contains("Course Registered"));
    }

    @Test
    @Order(5)
    public void testDeRegisterCourse() {
        when(mockstudentImpl.dropCourse("CS101")).thenReturn("Course Dropped");
        String input="C\nCS101\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        teststudentService.showmenu();
        assertTrue(outContent.toString().contains("Course Dropped"));
    }

    @Test
    @Order(6)
    public void testComputerCGPA(){
        when(mockstudentImpl.computeCGPA()).thenReturn(9.00);
        String input="E\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        teststudentService.showmenu();
        assertTrue(outContent.toString().contains("9.0"));
    }

    @Test
    @Order(7)
    public void testViewGrades1(){
        when(mockstudentImpl.viewGrades()).thenReturn(new String[]{"CS100 || 1 || 2022 || 8","CS104 || 1 || 2021 || 9"});
        String input="D\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        teststudentService.showmenu();
        assertTrue(outContent.toString().contains("CS100 || 1 || 2022 || 8"));
        assertTrue(outContent.toString().contains("CS104 || 1 || 2021 || 9"));
    }

    @Test
    @Order(8)
    public void testViewGrades2(){
        when(mockstudentImpl.viewGrades()).thenReturn(null);
        String input="Z\nD\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        teststudentService.showmenu();
        assertTrue(outContent.toString().contains("No Grades Available"));
    }

    @Test
    @Order(9)
    public void testCourseEnrolled(){
        when(mockstudentImpl.viewCoursesEnrolled()).thenReturn(new String[]{"CS100"});
        when(mockstudentImpl.viewCoursesOffered()).thenReturn(new String[]{"CS100","CS104"});
        String input="G\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        teststudentService.showmenu();
        assertTrue(outContent.toString().contains("CS100"));
        assertTrue(outContent.toString().contains("====================================="));
        assertTrue(outContent.toString().contains("CS104"));
    }



}
