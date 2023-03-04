package org.aims.facultyTest;


import org.aims.dataAccess.facultyDAO;
import org.aims.userimpl.FacultyImpl;
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

    private static facultyDAO mockfacultyDAO;
    private static FacultyImpl testFacultyImpl;

    @BeforeAll()
    public static void setup() throws Exception{
        mockfacultyDAO = mock( facultyDAO.class);
        testFacultyImpl = new FacultyImpl(mockfacultyDAO);


        // LOGIN TEST
        when(mockfacultyDAO.loginLogs(isA(String.class))).thenReturn(true);

        // OFFER COURSE
        when(mockfacultyDAO.checkCourseCatalog(isA(String.class))).thenReturn(true);
        when(mockfacultyDAO.checkCourseOffering(isA(String.class))).thenReturn(false);
        when(mockfacultyDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(true);
        when(mockfacultyDAO.insertCourse(isA(String.class),isA(String.class),isA(Double.class))).thenReturn(true);
        when(mockfacultyDAO.insertCoursePreReq(isA(String.class),isA(String.class),isA(String.class),isA(Integer.class))).thenReturn(true);
        when(mockfacultyDAO.insertCourseFaculty(isA(String.class),isA(String.class))).thenReturn(true);

        // TAKE BACK COURSE
        when(mockfacultyDAO.getfacultyidCourse(isA(String.class))).thenReturn(1);
        when(mockfacultyDAO.getfacultyidEmail(isA(String.class))).thenReturn(1);
        when(mockfacultyDAO.deleteCourseOffering(isA(String.class),isA(String.class))).thenReturn(true);
        when(mockfacultyDAO.getStudentEmail()).thenReturn(new String[]{"2020csb1068@iitrpr.ac.in,2020csb1114@iitrpr.ac.in"});
        when(mockfacultyDAO.deleteCourseEnrollement(isA(String.class),isA(String.class))).thenReturn(true);

    }

    @AfterAll()
    public static void teardown() {
    }

    @Test
    @Order(1)
    public void testLogin1() {
        when(mockfacultyDAO.login(isA(String.class),isA(String.class))).thenReturn(true);
        assertTrue(testFacultyImpl.login("mudgal@iitrpr.ac.in","123"));
    }

    @Test
    @Order(2)
    public void testLogin2() {
        when(mockfacultyDAO.login(isA(String.class),isA(String.class))).thenReturn(true);
        assertTrue(!testFacultyImpl.login("mudgal@gmail.ac.in","123"));
    }

    @Test
    @Order(3)
    public void testLogin3() {
        when(mockfacultyDAO.login(isA(String.class),isA(String.class))).thenReturn(false);
        assertTrue(!testFacultyImpl.login("mudgal@iitrpr.ac.in","123"));
    }

    @Test
    @Order(4)
    public void testLogout(){
        when(mockfacultyDAO.logoutLogs(isA(String.class))).thenReturn(true);
        testFacultyImpl.logout();
    }


    @Test
    @Order(5)
    public void testOfferCourse1(){
        when(mockfacultyDAO.checkCourseCatalog(isA(String.class))).thenReturn(false);
        String res=testFacultyImpl.offerCourse("CS101",8.0,new String[]{"CS100,9"});
        assertTrue(res.equals("Course Does Not Exist"));
        when(mockfacultyDAO.checkCourseCatalog(isA(String.class))).thenReturn(true);
    }

    @Test
    @Order(6)
    public void testOfferCourse2(){
        when(mockfacultyDAO.checkCourseOffering(isA(String.class))).thenReturn(true);
        String res=testFacultyImpl.offerCourse("CS101",8.0,new String[]{"CS100,9"});
        assertTrue(res.equals("Course Already Offered"));
        when(mockfacultyDAO.checkCourseOffering(isA(String.class))).thenReturn(false);
    }

    @Test
    @Order(7)
    public void testOfferCourse3(){
        when(mockfacultyDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(false);
        String res=testFacultyImpl.offerCourse("CS101",8.0,new String[]{"CS100,9"});
        assertTrue(res.equals("Course Offering is not Available"));
        when(mockfacultyDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(true);
    }

    @Test
    @Order(8)
    public void testOfferCourse4(){
        String res=testFacultyImpl.offerCourse("CS101",11.0,new String[]{"CS100,9"});
        assertTrue(res.equals("Invalid CGPA Cutoff"));
    }

    @Test
    @Order(9)
    public void testOfferCourse5(){
        when(mockfacultyDAO.checkCourseCatalog("CS100")).thenReturn(false);
        String res=testFacultyImpl.offerCourse("CS101",9.0,new String[]{"CS100,9"});
        assertTrue(res.equals("Prerequisite Course Does Not Exist"));
        when(mockfacultyDAO.checkCourseCatalog("CS100")).thenReturn(true);
    }

    @Test
    @Order(10)
    public void testOfferCourse6(){
        String res=testFacultyImpl.offerCourse("CS101",9.0,new String[]{"CS100,11"});
        assertTrue(res.equals("Invalid Grade"));
    }

    @Test
    @Order(11)
    public void testOfferCourse7(){
        String res=testFacultyImpl.offerCourse("CS101",9.0,new String[]{"CS100,9"});
        assertTrue(res.equals("Course Offered Successfully"));
    }


    @Test
    @Order(12)
    public void testTakeBackCourse1(){
        when(mockfacultyDAO.checkCourseCatalog(isA(String.class))).thenReturn(false);
        String res=testFacultyImpl.takeBackCourse("CS101");
        assertTrue(res.equals("Course Does Not Exist"));
        when(mockfacultyDAO.checkCourseCatalog(isA(String.class))).thenReturn(true);
    }

    @Test
    @Order(13)
    public void testTakeBackCourse2(){
        String res=testFacultyImpl.takeBackCourse("CS101");
        assertTrue(res.equals("Course Not Offered"));
    }


    @Test
    @Order(14)
    public void testTakeBackCourse3(){
        when(mockfacultyDAO.checkCourseOffering(isA(String.class))).thenReturn(true);
        when(mockfacultyDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(false);
        String res=testFacultyImpl.takeBackCourse("CS101");
        assertTrue(res.equals("Course Offering is not Open"));
        when(mockfacultyDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(true);
    }

    @Test
    @Order(15)
    public void testTakeBackCourse4(){
        when(mockfacultyDAO.checkCourseOffering(isA(String.class))).thenReturn(true);
        when(mockfacultyDAO.getfacultyidEmail(isA(String.class))).thenReturn(2);
        String res=testFacultyImpl.takeBackCourse("CS101");
        assertTrue(res.equals("You are not the Faculty of this Course"));
        when(mockfacultyDAO.getfacultyidEmail(isA(String.class))).thenReturn(1);
    }

    @Test
    @Order(16)
    public void testTakeBackCourse5(){
        when(mockfacultyDAO.checkCourseOffering(isA(String.class))).thenReturn(true);

        when(mockfacultyDAO.getfacultyidEmail(isA(String.class))).thenReturn(1);
        when(mockfacultyDAO.getfacultyidCourse(isA(String.class))).thenReturn(1);

        testFacultyImpl.login("mudgal@iitrpr.ac.in","123");
        String res=testFacultyImpl.takeBackCourse("CS101");
        assertTrue(res.equals("Course Taken Back Successfully"));
    }

    @Test
    @Order(17)
    public void testViewGrades(){
        when(mockfacultyDAO.viewGrades(isA(String.class))).thenReturn(new String[]{"CS101,1,2020,8"});
        String[] res=testFacultyImpl.viewGrades("2020csb1068@iitrpr.ac.in");
        assertTrue(res[0].equals("CS101,1,2020,8"));
    }

    @Test
    @Order(18)
    public void testChangePassword1(){
        String res=testFacultyImpl.changePassword("123","12 34");
        assertTrue(res.equals("Password Cannot Contain Spaces"));
    }

    @Test
    @Order(19)
    public void testChangePassword2(){

        testFacultyImpl.login("mudgal@iitrpr.ac.in","123");
        when(mockfacultyDAO.checkPassword(isA(String.class),isA(String.class))).thenReturn(false);
        String res=testFacultyImpl.changePassword("123","1234");
        assertTrue(res.equals("Incorrect Old Password"));
    }


    @Test
    @Order(20)
    public void testChangePassword3(){

        testFacultyImpl.login("mudgal@iitrpr.ac.in","123");
        when(mockfacultyDAO.checkPassword(isA(String.class),isA(String.class))).thenReturn(true);
        when(mockfacultyDAO.changePassword(isA(String.class),isA(String.class))).thenReturn(true);
        String res=testFacultyImpl.changePassword("123","1234");
        assertTrue(res.equals("Password Changed Successfully"));
    }


    // TESTING OF UPDATE GRADES IS STILL LEFT, HAVE TO THINK HOW TO DO WITH FILES
}
