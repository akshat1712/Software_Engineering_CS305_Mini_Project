package org.aims.studentTest;

import org.aims.dataAccess.studentDAO;
import org.aims.userimpl.StudentImpl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
    public void testLogin1() {
        when(mockstudentDAO.login(isA(String.class), isA(String.class))).thenReturn(true);
        assertTrue(teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123"));
    }

    @Test
    @Order(2)
    public void testLogin2() {
        when(mockstudentDAO.login(isA(String.class), isA(String.class))).thenReturn(false);
        assertTrue(!teststudentImpl.login("2020csb 1068@iitrpr.ac.in", "123"));
    }

    @Test
    @Order(3)
    public void testLogin3() {
        when(mockstudentDAO.login(isA(String.class), isA(String.class))).thenReturn(false);
        assertTrue(!teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123"));
    }

    @Test
    @Order(4)
    public void testLogout() {
        when(mockstudentDAO.logoutLogs(isA(String.class))).thenReturn(true);
        teststudentImpl.logout();
    }

    @Test
    @Order(5)
    public void testChangePassword1() {
        when(mockstudentDAO.checkPassword(isA(String.class), isA(String.class))).thenReturn(true);
        when(mockstudentDAO.changePassword(isA(String.class), isA(String.class))).thenReturn(true);
        assertTrue(teststudentImpl.changePassword("123", "1234").equals("Password Changed Successfully"));
    }

    @Test
    @Order(6)
    public void testChangePassword2() {
        when(mockstudentDAO.checkPassword(isA(String.class), isA(String.class))).thenReturn(false);
        assertTrue(teststudentImpl.changePassword("123", "1234").equals("Incorrect Old Password"));
    }

    @Test
    @Order(7)
    public void testChangePassword3() {
        assertTrue(teststudentImpl.changePassword("123", "123  4").equals("Password Cannot Contain Spaces"));
    }

    @Test
    @Order(8)
    public void testViewGrades(){
        when(mockstudentDAO.viewGrades(isA(String.class))).thenReturn(new String[]{"CS101,1,2020,8"});
        String[] res=teststudentImpl.viewGrades();
        assertTrue(res[0].equals("CS101,1,2020,8"));
    }

    @Test
    @Order(8)
    public void testViewCoursesOffered(){
        when(mockstudentDAO.getCourseOffered()).thenReturn(new String[]{"CS101","CS102"});
        String[] res=teststudentImpl.viewCoursesOffered();
        assertTrue(res[0].equals("CS101"));
        assertTrue(res[1].equals("CS102"));
    }

    @Test
    @Order(9)
    public void testViewCoursesEnrolled(){
        when(mockstudentDAO.getCourseEnrolled(isA(String.class))).thenReturn(new String[]{"CS101","CS102"});
        String[] res=teststudentImpl.viewCoursesEnrolled();
        assertTrue(res[0].equals("CS101"));
        assertTrue(res[1].equals("CS102"));
    }

    @Test
    @Order(10)
    public void testComputerCGPA1(){
        when(mockstudentDAO.creditsEarned(isA(String.class))).thenReturn(50.0);
        when(mockstudentDAO.gradePointsEarned(isA(String.class))).thenReturn(450.0);
        assertTrue(teststudentImpl.computeCGPA()==9.0);
    }

    @Test
    @Order(11)
    public void testComputerCGPA2(){
        when(mockstudentDAO.creditsEarned(isA(String.class))).thenReturn(50.0);
        when(mockstudentDAO.gradePointsEarned(isA(String.class))).thenReturn(0.0);
        assertTrue(teststudentImpl.computeCGPA()==0.0);
    }

    @Test
    @Order(12)
    public void testDropCourse1(){
        when(mockstudentDAO.checkCourseOffering(isA(String.class))).thenReturn(false);
        assertTrue(teststudentImpl.dropCourse("CS101").equals("Course Not Being Offered Right Now"));
        when(mockstudentDAO.checkCourseOffering(isA(String.class))).thenReturn(true);
    }

    @Test
    @Order(13)
    public void testDropCourse2(){
        when(mockstudentDAO.checkCourseEnrollment(isA(String.class),isA(String.class))).thenReturn(false);
        assertTrue(teststudentImpl.dropCourse("CS101").equals("Course Not Registered"));
        when(mockstudentDAO.checkCourseEnrollment(isA(String.class),isA(String.class))).thenReturn(true);
    }

    @Test
    @Order(14)
    public void testDropCourse3(){
        when(mockstudentDAO.checkSemesterStatus(isA(String.class))).thenReturn(false);
        assertTrue(teststudentImpl.dropCourse("CS101").equals("Grade Submission has started, So you cannot drop the course"));
        when(mockstudentDAO.checkSemesterStatus(isA(String.class))).thenReturn(true);
    }

    @Test
    @Order(15)
    public void testDropCourse4(){
        assertTrue(teststudentImpl.dropCourse("CS101").equals("Course Dropped"));
    }

    @Test
    @Order(16)
    public void testDropCourse5(){
        when(mockstudentDAO.deleteCourseEnrollement(isA(String.class),isA(String.class))).thenReturn(false);
        assertTrue(teststudentImpl.dropCourse("CS101").equals("Course Not Dropped"));
        when(mockstudentDAO.deleteCourseEnrollement(isA(String.class),isA(String.class))).thenReturn(true);
    }


    // REGISTER COURSE TESTING PENDING
}
