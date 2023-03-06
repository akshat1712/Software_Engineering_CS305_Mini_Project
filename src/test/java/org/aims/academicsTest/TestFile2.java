package org.aims.academicsTest;

import org.aims.dataAccess.academicDAO;
import org.aims.userimpl.AcademicEmployeeImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestFile2 {

    private static academicDAO mockacademicDAO;
    private static AcademicEmployeeImpl testacademicEmployeeImpl;

    @BeforeAll
    public static void setup() throws SQLException {
        mockacademicDAO = mock(academicDAO.class);
        testacademicEmployeeImpl = new AcademicEmployeeImpl(mockacademicDAO);


        // LOGIN
        when(mockacademicDAO.loginLogs(isA(String.class))).thenReturn(true);

        // ADD COURSE
        when(mockacademicDAO.checkCourseCatalog("CS101")).thenReturn(false);
        when(mockacademicDAO.checkCourseCatalog("CS201")).thenReturn(true);
        when(mockacademicDAO.getdepartmentid("CSE")).thenReturn(1);
    }

    @AfterAll
    public static void teardown() throws SQLException {
    }

    @Test
    @Order(1)
    public void testLogin1() throws Exception {
        when(mockacademicDAO.login("ACAD_1@iitrpr.ac.in","123")).thenReturn(true);
        assertTrue(testacademicEmployeeImpl.login("ACAD_1@iitrpr.ac.in","123"));
    }
    @Test
    @Order(2)
    public void testLogin2() throws Exception {
        assertFalse(testacademicEmployeeImpl.login("ACAD_1@gmail.ac.in", "123"));
    }
    @Test
    @Order(3)
    public void testLogin3() throws Exception {
        when(mockacademicDAO.login("ACAD_1@iitrpr.ac.in","123")).thenReturn(false);
        assertFalse(testacademicEmployeeImpl.login("ACAD_1@iitrpr.ac.in", "123"));
    }
    @Test
    @Order(4)
    public void testLogin4() throws Exception {
        when(mockacademicDAO.login("ACAD_1@iitrpr.ac.in","123")).thenThrow(SQLException.class);
        assertFalse(testacademicEmployeeImpl.login("ACAD_1@iitrpr.ac.in", "123"));
    }
    @Test
    @Order(5)
    public void testLogout() throws Exception{
        when(mockacademicDAO.logoutLogs(isA(String.class))).thenReturn(true);
        testacademicEmployeeImpl.logout();
    }

    @Test
    @Order(6)
    public void testViewGrades1() throws Exception{
        when(mockacademicDAO.viewGrades("2020csb1068@iitrpr.ac.in")).thenReturn(new String[]{"CS101,1,2020,8"});
        String[] res=testacademicEmployeeImpl.viewGrades("2020csb1068@iitrpr.ac.in");
        assertEquals("CS101,1,2020,8", res[0]);
    }
    @Test
    @Order(7)
    public void testViewGrades2() throws Exception{
        when(mockacademicDAO.viewGrades("2020csb1068@iitrpr.ac.in")).thenThrow(SQLException.class);
        String[] res=testacademicEmployeeImpl.viewGrades("2020csb1068@iitrpr.ac.in");
        assertNull(res);
    }
    @Test
    @Order(8)
    public void testChangePassword1() throws Exception{
        String res=testacademicEmployeeImpl.changePassword("123","12 34");
        assertEquals("Password Cannot Contain Spaces", res);
    }
    @Test
    @Order(9)
    public void testChangePassword2() throws Exception{
        testacademicEmployeeImpl.login("ACAD_1@iitrpr.ac.in","123");
        when(mockacademicDAO.checkPassword("ACAD_1@iitrpr.ac.in","123")).thenReturn(false);
        String res=testacademicEmployeeImpl.changePassword("123","1234");
        assertEquals("Incorrect Old Password", res);
    }
    @Test
    @Order(10)
    public void testChangePassword3() throws Exception{
        testacademicEmployeeImpl.login("ACAD_1@iitrpr.ac.in","123");
        when(mockacademicDAO.checkPassword("ACAD_1@iitrpr.ac.in","123")).thenReturn(true);
        when(mockacademicDAO.changePassword("ACAD_1@iitrpr.ac.in","123")).thenReturn(true);
        String res=testacademicEmployeeImpl.changePassword("123","1234");
        assertEquals("Password Changed Successfully", res);
    }
    @Test
    @Order(11)
    public void testChangePassword4() throws Exception{
        testacademicEmployeeImpl.login("ACAD_1@iitrpr.ac.in","123");
        when(mockacademicDAO.checkPassword("ACAD_1@iitrpr.ac.in","123")).thenThrow(SQLException.class);
        String res=testacademicEmployeeImpl.changePassword("123","1234");
        assertEquals("ERROR", res);
    }

    @Test
    @Order(12)
    public void testStartGradeSubmission1() throws Exception{
        when(mockacademicDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(false);
        assertEquals("Cannot Start Grade Submission", testacademicEmployeeImpl.startGradeSubmission());
    }

    @Test
    @Order(13)
    public void testStartGradeSubmission2() throws Exception{
        when(mockacademicDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(true);
        assertEquals("Grade Submission Started", testacademicEmployeeImpl.startGradeSubmission());
    }

    @Test
    @Order(14)
    public void testCreateCourseTypes1() throws Exception{
        String res=testacademicEmployeeImpl.createCourseTypes("CS101","PC");
        assertEquals("Course Type Created Successfully", res);
    }
    @Test
    @Order(15)
    public void testCreateCourseTypes2() throws Exception{
        when(mockacademicDAO.CreateCourseTypes("CS101","PC")).thenThrow(SQLException.class);
        String res=testacademicEmployeeImpl.createCourseTypes("CS101","PC");
        assertEquals("ERROR", res);
    }
    @Test
    @Order(16)
    public void testAddCourse1() throws Exception{
        when(mockacademicDAO.checkCourseCatalog("CS101")).thenReturn(true);
        String res=testacademicEmployeeImpl.addCourseInCatalog("CS101","CS101","CSE",1,1,1,1,1,null);
        assertEquals("Course Already Exists", res);
        when(mockacademicDAO.checkCourseCatalog("CS101")).thenReturn(false);
    }
    @Test
    @Order(17)
    public void testAddCourse2() throws Exception{
        when(mockacademicDAO.checkCourseCatalog("CS201")).thenReturn(false);
        String res=testacademicEmployeeImpl.addCourseInCatalog("CS101","CS101","CSE",1,1,1,1,1,new String[] {"CS101","CS201"});
        assertEquals("Prerequisite Course Does Not Exist", res);
        when(mockacademicDAO.checkCourseCatalog("CS201")).thenReturn(true);
    }
    @Test
    @Order(18)
    public void testAddCourse3() throws Exception{
        when(mockacademicDAO.getdepartmentid("CSE")).thenReturn(-1);

        String res=testacademicEmployeeImpl.addCourseInCatalog("CS101","CS101","CSE",1,1,1,1,1,new String[] {"CS101","CS201"});
        assertEquals("Department Does Not Exist", res);
        when(mockacademicDAO.getdepartmentid("CSE")).thenReturn(1);

    }
    @Test
    @Order(19)
    public void testAddCourse4() throws Exception{
        String res=testacademicEmployeeImpl.addCourseInCatalog("CS101","CS101","CSE",1,1,1,1,1,new String[] {"CS101","CS201"});
        assertEquals("COURSE ADDED IN CATALOG SUCCESSFULLY", res);
    }

//    @Test
//    @Order(20)
//    public void testStartSemester1() throws Exception{
//
//    }
}
