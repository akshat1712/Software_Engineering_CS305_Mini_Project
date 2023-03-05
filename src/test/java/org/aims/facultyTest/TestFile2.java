package org.aims.facultyTest;


import org.aims.dataAccess.facultyDAO;
import org.aims.userimpl.FacultyImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
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
    public void testLogin1() throws Exception {
        when(mockfacultyDAO.login("mudgal@iitrpr.ac.in","123")).thenReturn(true);
        assertTrue(testFacultyImpl.login("mudgal@iitrpr.ac.in","123"));
    }
    @Test
    @Order(2)
    public void testLogin2() throws Exception {
        assertFalse(testFacultyImpl.login("mudgal@gmail.ac.in", "123"));
    }
    @Test
    @Order(3)
    public void testLogin3() throws Exception {
        when(mockfacultyDAO.login("mudgal@iitrpr.ac.in","123")).thenReturn(false);
        assertFalse(testFacultyImpl.login("mudgal@iitrpr.ac.in", "123"));
    }
    @Test
    @Order(4)
    public void testLogin4() throws Exception {
        when(mockfacultyDAO.login("mudgal@iitrpr.ac.in","123")).thenThrow(SQLException.class);
        assertFalse(testFacultyImpl.login("mudgal@iitrpr.ac.in", "123"));
    }
    @Test
    @Order(5)
    public void testLogout() throws Exception{
        when(mockfacultyDAO.logoutLogs(isA(String.class))).thenReturn(true);
        testFacultyImpl.logout();
    }
    @Test
    @Order(6)
    public void testOfferCourse1() throws Exception{
        when(mockfacultyDAO.checkCourseCatalog("CS101")).thenReturn(false);
        String res=testFacultyImpl.offerCourse("CS101",8.0,new String[]{"CS100,9"});
        assertEquals("Course Does Not Exist", res);
        when(mockfacultyDAO.checkCourseCatalog(isA(String.class))).thenReturn(true);
    }
    @Test
    @Order(7)
    public void testOfferCourse2() throws Exception{
        when(mockfacultyDAO.checkCourseOffering("CS101")).thenReturn(true);
        String res=testFacultyImpl.offerCourse("CS101",8.0,new String[]{"CS100,9"});
        assertEquals("Course Already Offered", res);
        when(mockfacultyDAO.checkCourseOffering(isA(String.class))).thenReturn(false);
    }
    @Test
    @Order(8)
    public void testOfferCourse3() throws Exception{
        when(mockfacultyDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(false);
        String res=testFacultyImpl.offerCourse("CS101",8.0,new String[]{"CS100,9"});
        assertEquals("Course Offering is not Available", res);
        when(mockfacultyDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(true);
    }
    @Test
    @Order(9)
    public void testOfferCourse4() throws Exception{
        String res=testFacultyImpl.offerCourse("CS101",11.0,new String[]{"CS100,9"});
        assertEquals("Invalid CGPA Cutoff", res);
    }
    @Test
    @Order(10)
    public void testOfferCourse5() throws Exception{
        when(mockfacultyDAO.checkCourseCatalog("CS100")).thenReturn(false);
        String res=testFacultyImpl.offerCourse("CS101",9.0,new String[]{"CS100,9"});
        assertEquals("Prerequisite Course Does Not Exist", res);
        when(mockfacultyDAO.checkCourseCatalog("CS100")).thenReturn(true);
    }
    @Test
    @Order(11)
    public void testOfferCourse6() throws Exception{
        String res=testFacultyImpl.offerCourse("CS101",9.0,new String[]{"CS100,11"});
        assertEquals("Invalid Grade", res);
    }
    @Test
    @Order(12)
    public void testOfferCourse7() throws Exception{
        String res=testFacultyImpl.offerCourse("CS101",9.0,new String[]{"CS100,9"});
        assertEquals("Course Offered Successfully", res);
    }
    @Test
    @Order(13)
    public void testOfferCourse8() throws Exception{
        when(mockfacultyDAO.checkCourseOffering("CS101")).thenThrow(SQLException.class);
        String res=testFacultyImpl.offerCourse("CS101",9.0,new String[]{"CS100,9"});
        assertEquals("Error", res);
        when(mockfacultyDAO.checkCourseCatalog("CS101")).thenReturn(true);
    }
    @Test
    @Order(14)
    public void testTakeBackCourse1() throws Exception{
        when(mockfacultyDAO.checkCourseCatalog("CS102")).thenReturn(false);
        String res=testFacultyImpl.takeBackCourse("CS102");
        assertEquals("Course Does Not Exist", res);
        when(mockfacultyDAO.checkCourseCatalog(isA(String.class))).thenReturn(true);
    }
    @Test
    @Order(15)
    public void testTakeBackCourse2() throws Exception{
        String res=testFacultyImpl.takeBackCourse("CS102");
        assertEquals("Course Not Offered", res);
    }
    @Test
    @Order(16)
    public void testTakeBackCourse3() throws Exception{
        when(mockfacultyDAO.checkCourseOffering("CS102")).thenReturn(true);
        when(mockfacultyDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(false);
        String res=testFacultyImpl.takeBackCourse("CS102");
        assertEquals("Course Offering is not Open", res);
        when(mockfacultyDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(true);
    }
    @Test
    @Order(17)
    public void testTakeBackCourse4() throws Exception{
        when(mockfacultyDAO.checkCourseOffering("CS102")).thenReturn(true);
        when(mockfacultyDAO.getfacultyidEmail("mudgal@iitrpr.ac.in")).thenReturn(2);
        testFacultyImpl.login("mudgal@iitrpr.ac.in","123");
        String res=testFacultyImpl.takeBackCourse("CS102");
        assertEquals("You are not the Faculty of this Course", res);
        when(mockfacultyDAO.getfacultyidEmail("mudgal@iitrpr.ac.in")).thenReturn(1);
    }
    @Test
    @Order(18)
    public void testTakeBackCourse5() throws Exception{
        when(mockfacultyDAO.checkCourseOffering("CS102")).thenReturn(true);

        when(mockfacultyDAO.getfacultyidEmail("mudgal@iitrpr.ac.in")).thenReturn(1);
        when(mockfacultyDAO.getfacultyidCourse("mudgal@iitrpr.ac.in")).thenReturn(1);

        testFacultyImpl.login("mudgal@iitrpr.ac.in","123");
        String res=testFacultyImpl.takeBackCourse("CS102");
        assertEquals("Course Taken Back Successfully", res);
    }
    @Test
    @Order(19)
    public void testTakeBackCourse6() throws Exception{
        when(mockfacultyDAO.checkCourseCatalog("CS102")).thenThrow(SQLException.class);
        String res=testFacultyImpl.takeBackCourse("CS102");
        assertEquals("Error", res);
    }
    @Test
    @Order(20)
    public void testViewGrades1() throws Exception{
        when(mockfacultyDAO.viewGrades("2020csb1068@iitrpr.ac.in")).thenReturn(new String[]{"CS101,1,2020,8"});
        String[] res=testFacultyImpl.viewGrades("2020csb1068@iitrpr.ac.in");
        assertEquals("CS101,1,2020,8", res[0]);
    }
    @Test
    @Order(21)
    public void testViewGrades2() throws Exception{
        when(mockfacultyDAO.viewGrades("2020csb1068@iitrpr.ac.in")).thenThrow(SQLException.class);
        String[] res=testFacultyImpl.viewGrades("2020csb1068@iitrpr.ac.in");
        assertNull(res);
    }
    @Test
    @Order(18)
    public void testChangePassword1() throws Exception{
        String res=testFacultyImpl.changePassword("123","12 34");
        assertEquals("Password Cannot Contain Spaces", res);
    }
    @Test
    @Order(19)
    public void testChangePassword2() throws Exception{
        testFacultyImpl.login("mudgal@iitrpr.ac.in","123");
        when(mockfacultyDAO.checkPassword("mudgal@iitrpr.ac.in","123")).thenReturn(false);
        String res=testFacultyImpl.changePassword("123","1234");
        assertEquals("Incorrect Old Password", res);
    }
    @Test
    @Order(20)
    public void testChangePassword3() throws Exception{
        testFacultyImpl.login("mudgal@iitrpr.ac.in","123");
        when(mockfacultyDAO.checkPassword("mudgal@iitrpr.ac.in","123")).thenReturn(true);
        when(mockfacultyDAO.changePassword("mudgal@iitrpr.ac.in","123")).thenReturn(true);
        String res=testFacultyImpl.changePassword("123","1234");
        assertEquals("Password Changed Successfully", res);
    }
    @Test
    @Order(21)
    public void testChangePassword4() throws Exception{
        testFacultyImpl.login("mudgal@iitrpr.ac.in","123");
        when(mockfacultyDAO.checkPassword("mudgal@iitrpr.ac.in","123")).thenThrow(SQLException.class);
        String res=testFacultyImpl.changePassword("123","1234");
        assertEquals("Error", res);
    }

    @Test
    @Order(21)
    public void testUpdateGrades1() throws Exception{
        String res=testFacultyImpl.updateGrades("D:\\CS305\\Mini_Project\\Grades1","CS103");
        assertEquals("Course Not Offered", res);
    }
    @Test
    @Order(21)
    public void testUpdateGrades2() throws Exception{
        when(mockfacultyDAO.checkCourseOffering("CS103")).thenReturn(true);
        testFacultyImpl.login("nitin@iitrpr.ac.in","123");

        when(mockfacultyDAO.getfacultyidCourse("CS103")).thenReturn(1);
        when(mockfacultyDAO.getfacultyidEmail("nitin@iitrpr.ac.in")).thenReturn(2);

        String res=testFacultyImpl.updateGrades("D:\\CS305\\Mini_Project\\Grades1","CS103");
        assertEquals("You are not the Faculty of this Course", res);

        when(mockfacultyDAO.getfacultyidCourse("CS103")).thenReturn(1);
        when(mockfacultyDAO.getfacultyidEmail("nitin@iitrpr.ac.in")).thenReturn(1);
    }
    @Test
    @Order(22)
    public void testUpdateGrades3() throws Exception{
        testFacultyImpl.login("nitin@iitrpr.ac.in","123");
        when(mockfacultyDAO.checkSemesterStatus("ONGOING-GS")).thenReturn(false);
        String res=testFacultyImpl.updateGrades("D:\\CS305\\Mini_Project\\Grades1","CS103");
        assertEquals("Course Offering is not Open for Grading", res);
        when(mockfacultyDAO.checkSemesterStatus("ONGOING-GS")).thenReturn(true);
    }

    @Test
    @Order(23)
    public void testUpdateGrades4() throws Exception{
        testFacultyImpl.login("nitin@iitrpr.ac.in","123");
        String res=testFacultyImpl.updateGrades("D:\\CS305\\Mini_Project\\Grades\\Grades1","CS103");
        assertEquals("Invalid Grade 11 Present", res);
    }
    @Test
    @Order(24)
    public void testUpdateGrades5() throws Exception{
        testFacultyImpl.login("nitin@iitrpr.ac.in","123");
        when(mockfacultyDAO.checkCourseEnrollment("2020csb1068@iitrpr.ac.in","CS103")).thenReturn(false);
        String res=testFacultyImpl.updateGrades("D:\\CS305\\Mini_Project\\Grades\\Grades2","CS103");
        assertEquals("Student Not Enrolled in the Course", res);
        when(mockfacultyDAO.checkCourseEnrollment("2020csb1068@iitrpr.ac.in","CS103")).thenReturn(true);
    }
    @Test
    @Order(25)
    public void testUpdateGrades6() throws Exception{
        testFacultyImpl.login("nitin@iitrpr.ac.in","123");
        String res=testFacultyImpl.updateGrades("D:\\CS305\\Mini_Project\\Grades\\Grades2","CS103");
        assertEquals("Grades Upgraded Successfully", res);
    }
    @Test
    @Order(26)
    public void testUpdateGrades8() throws Exception{
        when(mockfacultyDAO.checkCourseOffering("CS103")).thenReturn(true);
        String res=testFacultyImpl.updateGrades("D:\\CS305\\Mini_Project\\Grades\\Grades3","CS103");
        assertEquals("File Does Not Exist1", res);
    }
}
