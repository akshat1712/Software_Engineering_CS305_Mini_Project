package org.aims.adminTest;


import org.aims.service.AdminService;
import org.aims.userimpl.AdminImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestFile1 {

    private static AdminImpl mockAdminImpl;
    private static AdminService testAdminService;

    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    @BeforeAll()
    public static void setup() {
        mockAdminImpl = mock( AdminImpl.class);
        testAdminService = new AdminService(mockAdminImpl);

        System.setOut(new PrintStream(outContent));
    }

    @AfterAll()
    public static void teardown() {
        System.setOut(originalOut);
    }

    @Test
    public void testLogin() {

        when(mockAdminImpl.login(isA(String.class),isA(String.class))).thenReturn(true);
        assertTrue(testAdminService.login("postgres@iitrpr.ac.in","2020csb1068"));

        when(mockAdminImpl.login(isA(String.class),isA(String.class))).thenReturn(false);
        assertTrue(!testAdminService.login("postgres@gmail.com","2020csb1068"));


    }

    @Test
    public void testAddDepartmentService1() throws SQLException {
        when(mockAdminImpl.AddDepartment(isA(String.class))).thenReturn("ADDED");
        String input="E\nCSE\nA\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        testAdminService.showmenu();
        assertTrue(outContent.toString().contains("ADDED"));
    }

    @Test
    public void testAddDepartmentService2() throws SQLException {
        when(mockAdminImpl.AddDepartment(isA(String.class))).thenThrow(SQLException.class);
        String input="E\nCSE\nA\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        testAdminService.showmenu();
        assertTrue(outContent.toString().contains("Error in adding department"));
    }

    @Test
    public void testAddAcademic1() throws SQLException {
        when(mockAdminImpl.AddAcademicStaff(isA(String.class),isA(String.class),isA(String.class),isA(String.class),isA(String.class))).thenReturn("ADDED");
        String input="D\n1\n2\n3\n4\n5\nA\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        testAdminService.showmenu();
        assertTrue(outContent.toString().contains("ADDED"));
    }


    @Test
    public void testAddAcademic2() throws SQLException {
        when(mockAdminImpl.AddAcademicStaff(isA(String.class),isA(String.class),isA(String.class),isA(String.class),isA(String.class))).thenThrow(SQLException.class);
        String input="D\n1\n2\n3\n4\n5\nA\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        testAdminService.showmenu();
        assertTrue(outContent.toString().contains("Error in adding academic staff"));
    }


    @Test
    public void testAddFaculty1() throws SQLException {
        when(mockAdminImpl.AddFaculty(isA(String.class),isA(String.class),isA(String.class),isA(String.class),isA(String.class),isA(String.class))).thenReturn("ADDED");
        String input="B\n1\n2\n3\n4\n5\n6\nA\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        testAdminService.showmenu();
        assertTrue(outContent.toString().contains("ADDED"));
    }


    @Test
    public void testAddFaculty2() throws SQLException {
        when(mockAdminImpl.AddFaculty(isA(String.class),isA(String.class),isA(String.class),isA(String.class),isA(String.class),isA(String.class))).thenThrow(SQLException.class);
        String input="B\n1\n2\n3\n4\n5\n6\nA\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        testAdminService.showmenu();
        assertTrue(outContent.toString().contains("Error in adding faculty"));
    }

    @Test
    public void testStudent1() throws SQLException {
        when(mockAdminImpl.AddStudent(isA(String.class),isA(String.class),isA(String.class),isA(String.class),isA(String.class),isA(String.class),isA(String.class))).thenReturn("ADDED");
        String input="C\n1\n2\n3\n4\n5\n6\n7\nA\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        testAdminService.showmenu();
        assertTrue(outContent.toString().contains("ADDED"));
    }


    @Test
    public void testStudent2() throws SQLException {
        when(mockAdminImpl.AddStudent(isA(String.class),isA(String.class),isA(String.class),isA(String.class),isA(String.class),isA(String.class),isA(String.class))).thenThrow(SQLException.class);
        String input="C\n1\n2\n3\n4\n5\n6\n7\nA\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        testAdminService.showmenu();
        assertTrue(outContent.toString().contains("Error in adding student"));
    }
}