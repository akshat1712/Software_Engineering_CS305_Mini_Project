package org.aims.studentTest;

import org.aims.dataAccess.studentDAO;
import org.aims.userimpl.StudentImpl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TestFile2 {

    private static studentDAO mockstudentDAO;
    private static StudentImpl teststudentImpl;

    @BeforeAll
    public static void setup() throws Exception{
        mockstudentDAO = mock( studentDAO.class);
        teststudentImpl = new StudentImpl(mockstudentDAO);


        when(mockstudentDAO.login(isA(String.class), isA(String.class))).thenReturn(true);
        teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123");

        // DROP COURSE
        when(mockstudentDAO.checkCourseOffering(isA(String.class))).thenReturn(true);
        when(mockstudentDAO.checkCourseEnrollment(isA(String.class),isA(String.class))).thenReturn(true);
        when(mockstudentDAO.checkSemesterStatus(isA(String.class))).thenReturn(true);
        when(mockstudentDAO.deleteCourseEnrollement(isA(String.class),isA(String.class))).thenReturn(true);
    }

    @AfterAll
    public static void teardown() {
    }

    @Test
    @Order(1)
    public void testLogin1() throws Exception {
        when(mockstudentDAO.login("2020csb1068@iitrpr.ac.in", "123")).thenReturn(true);
        assertTrue(teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123"));
    }

    @Test
    @Order(2)
    public void testLogin2() throws Exception {
        when(mockstudentDAO.login(isA(String.class), isA(String.class))).thenReturn(false);
        assertFalse(teststudentImpl.login("2020csb 1068@iitrpr.ac.in", "123"));
    }

    @Test
    @Order(3)
    public void testLogin3() throws Exception {
        when(mockstudentDAO.login("2020csb1068@iitrpr.ac.in", "123")).thenReturn(false);
        assertFalse(teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123"));
    }

    @Test
    @Order(4)
    public void testLogin4() throws Exception {
        when(mockstudentDAO.login("2020csb1068@iitrpr.ac.in", "123")).thenThrow(SQLException.class);
        assertFalse(teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123"));
    }

    @Test
    @Order(5)
    public void testLogout() throws Exception {
        when(mockstudentDAO.logoutLogs(isA(String.class))).thenReturn(true);
        teststudentImpl.logout();
    }

    @Test
    @Order(6)
    public void testChangePassword1() throws Exception {
        when(mockstudentDAO.checkPassword("2020csb1068@iitrpr.ac.in", "1234")).thenReturn(true);
        when(mockstudentDAO.changePassword("2020csb1068@iitrpr.ac.in", "1234")).thenReturn(true);
        assertEquals("Password Changed Successfully", teststudentImpl.changePassword("1234", "1234"));
    }

    @Test
    @Order(7)
    public void testChangePassword2() throws Exception {
        when(mockstudentDAO.checkPassword("2020csb1068@iitrpr.ac.in", "1234")).thenReturn(false);
        assertEquals("Incorrect Old Password", teststudentImpl.changePassword("123", "1234"));
    }

    @Test
    @Order(8)
    public void testChangePassword3() throws Exception {
        assertEquals("Password Cannot Contain Spaces", teststudentImpl.changePassword("123", "123  4"));
    }

    @Test
    @Order(9)
    public void testChangePassword4() throws Exception {
        when(mockstudentDAO.checkPassword("2020csb1068@iitrpr.ac.in", "1234")).thenThrow(SQLException.class);
        assertEquals("Error", teststudentImpl.changePassword("1234", "1234"));
    }
    @Test
    @Order(10)
    public void testViewGrades1() throws Exception{
        when(mockstudentDAO.viewGrades(isA(String.class))).thenReturn(new String[]{"CS101,1,2020,8"});
        String[] res=teststudentImpl.viewGrades();
        assertEquals("CS101,1,2020,8", res[0]);
    }

    @Test
    @Order(11)
    public void testViewGrades2() throws Exception{
        when(mockstudentDAO.viewGrades(isA(String.class))).thenThrow(SQLException.class);
        String[] res=teststudentImpl.viewGrades();
        assertNull(res);
    }

    @Test
    @Order(12)
    public void testViewCoursesOffered1() throws Exception{
        when(mockstudentDAO.getCourseOffered()).thenReturn(new String[]{"CS101","CS102"});
        String[] res=teststudentImpl.viewCoursesOffered();
        assertEquals("CS101", res[0]);
        assertEquals("CS102", res[1]);
    }

    @Test
    @Order(13)
    public void testViewCoursesOffered2() throws Exception{
        when(mockstudentDAO.getCourseOffered()).thenThrow(SQLException.class);
        String[] res=teststudentImpl.viewCoursesOffered();
        assertNull(res);
    }

    @Test
    @Order(15)
    public void testViewCoursesEnrolled1() throws Exception{
        when(mockstudentDAO.getCourseEnrolled(isA(String.class))).thenReturn(new String[]{"CS101","CS102"});
        String[] res=teststudentImpl.viewCoursesEnrolled();
        assertEquals("CS101", res[0]);
        assertEquals("CS102", res[1]);
    }

    @Test
    @Order(16)
    public void testViewCoursesEnrolled2() throws Exception{
        when(mockstudentDAO.getCourseEnrolled(isA(String.class))).thenThrow(SQLException.class);
        String[] res=teststudentImpl.viewCoursesEnrolled();
        assertNull(res);
    }

    @Test
    @Order(17)
    public void testComputerCGPA1() throws Exception{
        when(mockstudentDAO.creditsEarned(isA(String.class))).thenReturn(50.0);
        when(mockstudentDAO.gradePointsEarned(isA(String.class))).thenReturn(450.0);
        assertEquals(9.0, teststudentImpl.computeCGPA());
    }

    @Test
    @Order(18)
    public void testComputerCGPA2() throws Exception{
        when(mockstudentDAO.creditsEarned(isA(String.class))).thenReturn(50.0);
        when(mockstudentDAO.gradePointsEarned(isA(String.class))).thenReturn(0.0);
        assertEquals(0.0, teststudentImpl.computeCGPA());
    }

    @Test
    @Order(19)
    public void testComputerCGPA3() throws Exception{
        when(mockstudentDAO.creditsEarned(isA(String.class))).thenThrow(SQLException.class);
        when(mockstudentDAO.gradePointsEarned(isA(String.class))).thenReturn(0.0);
        assertEquals(0.0, teststudentImpl.computeCGPA());
    }

    @Test
    @Order(20)
    public void testDropCourse1() throws Exception{
        when(mockstudentDAO.checkCourseOffering(isA(String.class))).thenReturn(false);
        assertEquals("Course Not Being Offered Right Now", teststudentImpl.dropCourse("CS101"));
        when(mockstudentDAO.checkCourseOffering(isA(String.class))).thenReturn(true);
    }

    @Test
    @Order(21)
    public void testDropCourse2() throws Exception{
        when(mockstudentDAO.checkCourseEnrollment(isA(String.class),isA(String.class))).thenReturn(false);
        assertEquals("Course Not Registered", teststudentImpl.dropCourse("CS101"));
        when(mockstudentDAO.checkCourseEnrollment(isA(String.class),isA(String.class))).thenReturn(true);
    }

    @Test
    @Order(22)
    public void testDropCourse3() throws Exception{
        when(mockstudentDAO.checkSemesterStatus(isA(String.class))).thenReturn(false);
        assertEquals("Grade Submission has started, So you cannot drop the course", teststudentImpl.dropCourse("CS101"));
        when(mockstudentDAO.checkSemesterStatus(isA(String.class))).thenReturn(true);
    }

    @Test
    @Order(23)
    public void testDropCourse4() throws Exception{
        assertEquals("Course Dropped", teststudentImpl.dropCourse("CS101"));
    }

    @Test
    @Order(24)
    public void testDropCourse5() throws Exception{
        when(mockstudentDAO.deleteCourseEnrollement(isA(String.class),isA(String.class))).thenReturn(false);
        assertEquals("Course Not Dropped", teststudentImpl.dropCourse("CS101"));
        when(mockstudentDAO.deleteCourseEnrollement(isA(String.class),isA(String.class))).thenReturn(true);
    }

    @Test
    @Order(25)
    public void testDropCourse6() throws Exception{
        when(mockstudentDAO.checkCourseOffering(isA(String.class))).thenThrow(SQLException.class);
        assertEquals("Error", teststudentImpl.dropCourse("CS101"));
        when(mockstudentDAO.deleteCourseEnrollement(isA(String.class),isA(String.class))).thenReturn(true);
    }


}
