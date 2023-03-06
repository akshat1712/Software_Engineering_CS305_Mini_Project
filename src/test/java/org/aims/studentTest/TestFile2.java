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
    public static void setup() throws Exception {
        mockstudentDAO = mock(studentDAO.class);
        teststudentImpl = new StudentImpl(mockstudentDAO);


        when(mockstudentDAO.login(isA(String.class), isA(String.class))).thenReturn(true);
        teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123");

        // DROP COURSE
        when(mockstudentDAO.checkCourseOffering("CS101")).thenReturn(true);
        when(mockstudentDAO.checkCourseEnrollment("2020csb1068@iitrpr.ac.in", "CS101")).thenReturn(true);
        when(mockstudentDAO.checkSemesterStatus(isA(String.class))).thenReturn(true);
        when(mockstudentDAO.deleteCourseEnrollement("2020csb1068@iitrpr.ac.in", "CS101")).thenReturn(true);

        // REGISTER COURSE
        when(mockstudentDAO.checkCourseOffering("CS104")).thenReturn(true);
        when(mockstudentDAO.checkCourseEnrollment("2020csb1068@iitrpr.ac.in", "CS104")).thenReturn(false);
        when(mockstudentDAO.creditsEnrolled("2020csb1068@iitrpr.ac.in")).thenReturn(20.00);
        when(mockstudentDAO.getCreditsCourse("CS101")).thenReturn(3.00);
        when(mockstudentDAO.getSemester()).thenReturn("1");
        when(mockstudentDAO.getYear()).thenReturn(2022);
        when(mockstudentDAO.creditsEarnedSemesterYear("2020csb1068@iitrpr.ac.in", "1", 2021)).thenReturn(20.0);
        when(mockstudentDAO.creditsEarnedSemesterYear("2020csb1068@iitrpr.ac.in", "2", 2021)).thenReturn(20.0);
        when(mockstudentDAO.getCGPAcriteria("CS101")).thenReturn(-1.00);
        when(mockstudentDAO.checkCourseTranscript("2020csb1068@iitrpr.ac.in", "CS104")).thenReturn(false);
        when(mockstudentDAO.checkSemesterStatus("ONGOING-GR")).thenReturn(false);
        when(mockstudentDAO.getPreReqCollege("CS104")).thenReturn(new String[] {"CS201"});
        when(mockstudentDAO.checkCourseTranscript("2020csb1068@iitrpr.ac.in","CS201")).thenReturn(true);
        when(mockstudentDAO.getPreReqOffer("CS104")).thenReturn(new String[] {"CS202"});
        when(mockstudentDAO.checkCourseTranscript("2020csb1068@iitrpr.ac.in","CS202")).thenReturn(true);
        when(mockstudentDAO.getGradeCourse("2020csb1068@iitrpr.ac.in","CS202")).thenReturn(10);
        when(mockstudentDAO.getReqGradeOffer("CS104","CS202")).thenReturn(9);
        when(mockstudentDAO.insertCourseEnrollement("2020csb1068@iitrpr.ac.in","CS104")).thenReturn(true);

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
    public void testViewGrades1() throws Exception {
        when(mockstudentDAO.viewGrades(isA(String.class))).thenReturn(new String[]{"CS101,1,2020,8"});
        String[] res = teststudentImpl.viewGrades();
        assertEquals("CS101,1,2020,8", res[0]);
    }

    @Test
    @Order(11)
    public void testViewGrades2() throws Exception {
        when(mockstudentDAO.viewGrades(isA(String.class))).thenThrow(SQLException.class);
        String[] res = teststudentImpl.viewGrades();
        assertNull(res);
    }

    @Test
    @Order(12)
    public void testViewCoursesOffered1() throws Exception {
        when(mockstudentDAO.getCourseOffered()).thenReturn(new String[]{"CS101", "CS102"});
        String[] res = teststudentImpl.viewCoursesOffered();
        assertEquals("CS101", res[0]);
        assertEquals("CS102", res[1]);
    }

    @Test
    @Order(13)
    public void testViewCoursesOffered2() throws Exception {
        when(mockstudentDAO.getCourseOffered()).thenThrow(SQLException.class);
        String[] res = teststudentImpl.viewCoursesOffered();
        assertNull(res);
    }

    @Test
    @Order(15)
    public void testViewCoursesEnrolled1() throws Exception {
        when(mockstudentDAO.getCourseEnrolled(isA(String.class))).thenReturn(new String[]{"CS101", "CS102"});
        String[] res = teststudentImpl.viewCoursesEnrolled();
        assertEquals("CS101", res[0]);
        assertEquals("CS102", res[1]);
    }

    @Test
    @Order(16)
    public void testViewCoursesEnrolled2() throws Exception {
        when(mockstudentDAO.getCourseEnrolled(isA(String.class))).thenThrow(SQLException.class);
        String[] res = teststudentImpl.viewCoursesEnrolled();
        assertNull(res);
    }

    @Test
    @Order(17)
    public void testComputerCGPA1() throws Exception {
        when(mockstudentDAO.creditsEarned(isA(String.class))).thenReturn(50.0);
        when(mockstudentDAO.gradePointsEarned(isA(String.class))).thenReturn(450.0);
        assertEquals(9.0, teststudentImpl.computeCGPA());
    }

    @Test
    @Order(18)
    public void testComputerCGPA2() throws Exception {
        when(mockstudentDAO.creditsEarned(isA(String.class))).thenReturn(50.0);
        when(mockstudentDAO.gradePointsEarned(isA(String.class))).thenReturn(0.0);
        assertEquals(0.0, teststudentImpl.computeCGPA());
    }

    @Test
    @Order(19)
    public void testComputerCGPA3() throws Exception {
        when(mockstudentDAO.creditsEarned(isA(String.class))).thenThrow(SQLException.class);
        when(mockstudentDAO.gradePointsEarned(isA(String.class))).thenReturn(0.0);
        assertEquals(0.0, teststudentImpl.computeCGPA());
    }

    @Test
    @Order(20)
    public void testDropCourse1() throws Exception {
        when(mockstudentDAO.checkCourseOffering("CS101")).thenReturn(false);
        assertEquals("Course Not Being Offered Right Now", teststudentImpl.dropCourse("CS101"));
        when(mockstudentDAO.checkCourseOffering("CS101")).thenReturn(true);
    }

    @Test
    @Order(21)
    public void testDropCourse2() throws Exception {
        when(mockstudentDAO.checkCourseEnrollment("2020csb1068@iitrpr.ac.in", "CS101")).thenReturn(false);
        teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123");
        assertEquals("Course Not Registered", teststudentImpl.dropCourse("CS101"));
        when(mockstudentDAO.checkCourseEnrollment("2020csb1068@iitrpr.ac.in", "CS101")).thenReturn(true);
    }

    @Test
    @Order(22)
    public void testDropCourse3() throws Exception {
        when(mockstudentDAO.checkSemesterStatus(isA(String.class))).thenReturn(false);
        assertEquals("Grade Submission has started, So you cannot drop the course", teststudentImpl.dropCourse("CS101"));
        when(mockstudentDAO.checkSemesterStatus(isA(String.class))).thenReturn(true);
    }

    @Test
    @Order(23)
    public void testDropCourse4() throws Exception {
        assertEquals("Course Dropped", teststudentImpl.dropCourse("CS101"));
    }

    @Test
    @Order(24)
    public void testDropCourse5() throws Exception {
        when(mockstudentDAO.deleteCourseEnrollement("2020csb1068@iitrpr.ac.in", "CS101")).thenReturn(false);
        teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123");
        assertEquals("Course Not Dropped", teststudentImpl.dropCourse("CS101"));
        when(mockstudentDAO.deleteCourseEnrollement("2020csb1068@iitrpr.ac.in", "CS101")).thenReturn(true);
    }


    @Test
    @Order(26)
    public void testRegisterCourse1() throws Exception {
        when(mockstudentDAO.checkCourseOffering("CS104")).thenReturn(false);
        assertEquals("Course Not Offered", teststudentImpl.registerCourse("CS104"));
        when(mockstudentDAO.checkCourseOffering("CS104")).thenReturn(true);
    }

    @Test
    @Order(27)
    public void testRegisterCourse2() throws Exception {
        teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123");
        when(mockstudentDAO.checkCourseEnrollment("2020csb1068@iitrpr.ac.in", "CS104")).thenReturn(true);
        assertEquals("Course Already Enrolled", teststudentImpl.registerCourse("CS104"));
        when(mockstudentDAO.checkCourseEnrollment("2020csb1068@iitrpr.ac.in", "CS104")).thenReturn(false);
    }

    @Test
    @Order(28)
    public void testRegisterCourse3() throws Exception {
        teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123");
        when(mockstudentDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(false);
        assertEquals("Semester Not Started", teststudentImpl.registerCourse("CS104"));
        when(mockstudentDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(true);
    }

    @Test
    @Order(29)
    public void testRegisterCourse4() throws Exception {
        teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123");

        when(mockstudentDAO.creditsEarnedSemesterYear("2020csb1068@iitrpr.ac.in", "1", 2021)).thenReturn(10.0);
        when(mockstudentDAO.creditsEarnedSemesterYear("2020csb1068@iitrpr.ac.in", "2", 2021)).thenReturn(10.0);
        assertEquals("Credits Exceeded", teststudentImpl.registerCourse("CS104"));
        when(mockstudentDAO.creditsEarnedSemesterYear("2020csb1068@iitrpr.ac.in", "1", 2021)).thenReturn(20.0);
        when(mockstudentDAO.creditsEarnedSemesterYear("2020csb1068@iitrpr.ac.in", "2", 2021)).thenReturn(20.0);
    }

    @Test
    @Order(30)
    public void testRegisterCourse5() throws Exception {
        teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123");
        when(mockstudentDAO.getCGPAcriteria("CS104")).thenReturn(1.00);
        assertEquals("CGPA Criteria Not Satisfied", teststudentImpl.registerCourse("CS104"));
        when(mockstudentDAO.getCGPAcriteria("CS104")).thenReturn(-1.00);


    }

    @Test
    @Order(31)
    public void testRegisterCourse6() throws Exception {
        teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123");
        when(mockstudentDAO.checkSemesterStatus("ONGOING-GR")).thenReturn(true);
        assertEquals("Grade Submission has started, So you cannot register for new courses", teststudentImpl.registerCourse("CS104"));
        when(mockstudentDAO.checkSemesterStatus("ONGOING-GR")).thenReturn(false);
    }

    @Test
    @Order(32)
    public void testRegsiterCourse7() throws Exception {
        teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123");
        when(mockstudentDAO.checkCourseTranscript("2020csb1068@iitrpr.ac.in", "CS104")).thenReturn(true);
        assertEquals("Course Already Taken", teststudentImpl.registerCourse("CS104"));
        when(mockstudentDAO.checkCourseTranscript("2020csb1068@iitrpr.ac.in", "CS104")).thenReturn(false);
    }

    @Test
    @Order(33)
    public void testRegsiterCourse8() throws Exception {
        teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123");
        when(mockstudentDAO.checkCourseTranscript("2020csb1068@iitrpr.ac.in","CS201")).thenReturn(false);
        assertEquals("You Do not satify the College prerequisite", teststudentImpl.registerCourse("CS104"));
        when(mockstudentDAO.checkCourseTranscript("2020csb1068@iitrpr.ac.in","CS201")).thenReturn(true);
    }

    @Test
    @Order(34)
    public void testRegsiterCourse9() throws Exception {
        teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123");
        when(mockstudentDAO.checkCourseTranscript("2020csb1068@iitrpr.ac.in","CS202")).thenReturn(false);
        assertEquals("You Do not satify the Offer prerequisite", teststudentImpl.registerCourse("CS104"));
        when(mockstudentDAO.checkCourseTranscript("2020csb1068@iitrpr.ac.in","CS202")).thenReturn(true);
    }

    @Test
    @Order(35)
    public void testRegsiterCourse10() throws Exception {
        teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123");
        when(mockstudentDAO.getGradeCourse("2020csb1068@iitrpr.ac.in","CS202")).thenReturn(7);
        when(mockstudentDAO.getReqGradeOffer("CS104","CS202")).thenReturn(8);
        assertEquals("You Do not satify the Offer prerequisite", teststudentImpl.registerCourse("CS104"));
        when(mockstudentDAO.getGradeCourse("2020csb1068@iitrpr.ac.in","CS202")).thenReturn(10);
        when(mockstudentDAO.getReqGradeOffer("CS104","CS202")).thenReturn(9);
    }

    @Test
    @Order(36)
    public void testRegsiterCourse11() throws Exception {
        teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123");
        assertEquals("Course Registered", teststudentImpl.registerCourse("CS104"));
    }

    @Test
    @Order(37)
    public void testRegsiterCourse12() throws Exception {
        teststudentImpl.login("2020csb1068@iitrpr.ac.in", "123");
        when(mockstudentDAO.insertCourseEnrollement("2020csb1068@iitrpr.ac.in","CS104")).thenReturn(false);
        assertEquals("Course Not Registered", teststudentImpl.registerCourse("CS104"));
    }
}