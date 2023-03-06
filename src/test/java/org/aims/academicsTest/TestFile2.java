package org.aims.academicsTest;

import org.aims.dataAccess.academicDAO;
import org.aims.userimpl.AcademicEmployeeImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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

    @Test
    @Order(20)
    public void testStartSemester1() throws Exception{
        when(mockacademicDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(true);
        assertEquals("A Semester Already Ongoing", testacademicEmployeeImpl.startSemester(2020,"1"));
    }

    @Test
    @Order(21)
    public void testStartSemester2() throws Exception{
        when(mockacademicDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(false);
        when(mockacademicDAO.checkSemesterStatus("ONGOING-GS")).thenReturn(false);
        when(mockacademicDAO.checkSemesterValidity("1",2020)).thenReturn(true);
        assertEquals("Semester Already Exists", testacademicEmployeeImpl.startSemester(2020,"1"));
    }

    @Test
    @Order(21)
    public void testStartSemester3() throws Exception{
        when(mockacademicDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(false);
        when(mockacademicDAO.checkSemesterStatus("ONGOING-GS")).thenReturn(false);
        when(mockacademicDAO.checkSemesterValidity("1",2020)).thenReturn(false);
        assertEquals("Semester Started Successfully", testacademicEmployeeImpl.startSemester(2020,"1"));
    }

    @Test
    @Order(22)
    public void testEndSemester1() throws Exception{
        when(mockacademicDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(true);
        assertEquals("Grade Submission Not started", testacademicEmployeeImpl.endSemester());
    }

    @Test
    @Order(23)
    public void testEndSemester2() throws Exception{
        when(mockacademicDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(false);
        when(mockacademicDAO.getStudentids()).thenReturn(new String[]{"1","2"});
        when(mockacademicDAO.checkGradeSubmission("1")).thenReturn(false);
        when(mockacademicDAO.checkGradeSubmission("2")).thenReturn(true);
        assertEquals("Grade Not Submitted for the student", testacademicEmployeeImpl.endSemester());
    }

    @Test
    @Order(24)
    public void testEndSemester3() throws Exception{
        when(mockacademicDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(false);
        when(mockacademicDAO.getStudentids()).thenReturn(new String[]{"1"});
        when(mockacademicDAO.checkGradeSubmission("1")).thenReturn(false);
        when(mockacademicDAO.getfacultyids()).thenReturn(new String[]{"1"});
        assertEquals("Semester Ended", testacademicEmployeeImpl.endSemester());
    }

    @Test
    @Order(25)
    public void testEndSemester4() throws Exception{
        when(mockacademicDAO.checkSemesterStatus("ONGOING-CO")).thenReturn(false);
        when(mockacademicDAO.getStudentids()).thenReturn(null);
        assertEquals("ERROR", testacademicEmployeeImpl.endSemester());
    }

    @Test
    @Order(26)
    public void testGenerateReport1() throws Exception{
        Map<String,String[]> map=new HashMap<>();
        map.put("1",new String[]{"1"});
        when(mockacademicDAO.getStudentEmail()).thenReturn(new String[]{"1"});
        when(mockacademicDAO.viewGrades("1")).thenReturn(new String[]{"1"});
        map.equals(testacademicEmployeeImpl.generateReport());
    }

    @Test
    @Order(27)
    public void testGenerateReport2() throws Exception{
        when(mockacademicDAO.getStudentEmail()).thenReturn(null);
        assertNull(testacademicEmployeeImpl.generateReport());
    }

    @Test
    @Order(28)
    public void testCheckGraduation1() throws Exception{
        when(mockacademicDAO.getCurriculumCourse("2020csb1068@iitrpr.ac.in")).thenReturn(new String []{"1"});
        when(mockacademicDAO.checkCourseTranscript("2020csb1068@iitrpr.ac.in","1")).thenReturn(false);
        assertEquals("Not all Courses Completed", testacademicEmployeeImpl.checkGraduation("2020csb1068@iitrpr.ac.in"));
    }

    @Test
    @Order(29)
    public void testCheckGraduation2() throws Exception{
        when(mockacademicDAO.getCurriculumCourse("2020csb1068@iitrpr.ac.in")).thenReturn(new String []{"1"});
        when(mockacademicDAO.checkCourseTranscript("2020csb1068@iitrpr.ac.in","1")).thenReturn(true);

        Map<String,Double> map=new HashMap<>();
        map.put("PE",4.0);
        when(mockacademicDAO.getEnrolledCreditsType("2020csb1068@iitrpr.ac.in")).thenReturn(map);
        when(mockacademicDAO.getCreditsType("2020csb1068@iitrpr.ac.in","PE")).thenReturn(5);
        assertEquals("Not Enough Credits of PE", testacademicEmployeeImpl.checkGraduation("2020csb1068@iitrpr.ac.in"));
   }

    @Test
    @Order(30)
    public void testCheckGraduation3() throws Exception{
        when(mockacademicDAO.getCurriculumCourse("2020csb1068@iitrpr.ac.in")).thenReturn(new String []{"1"});
        when(mockacademicDAO.checkCourseTranscript("2020csb1068@iitrpr.ac.in","1")).thenReturn(true);

        Map<String,Double> map=new HashMap<>();
        map.put("PE",8.0);
        when(mockacademicDAO.getEnrolledCreditsType("2020csb1068@iitrpr.ac.in")).thenReturn(map);
        when(mockacademicDAO.getCreditsType("2020csb1068@iitrpr.ac.in","PE")).thenReturn(5);
        when(mockacademicDAO.getCreditsType("2020csb1068@iitrpr.ac.in","OE")).thenReturn(5);
        assertEquals("Not Enough Credits of Open Elective", testacademicEmployeeImpl.checkGraduation("2020csb1068@iitrpr.ac.in"));
    }

    @Test
    @Order(31)
    public void testCheckGraduation4() throws Exception{
        when(mockacademicDAO.getCurriculumCourse("2020csb1068@iitrpr.ac.in")).thenReturn(new String []{"1"});
        when(mockacademicDAO.checkCourseTranscript("2020csb1068@iitrpr.ac.in","1")).thenReturn(true);

        Map<String,Double> map=new HashMap<>();
        map.put("PE",8.0);
        when(mockacademicDAO.getEnrolledCreditsType("2020csb1068@iitrpr.ac.in")).thenReturn(map);
        when(mockacademicDAO.getCreditsType("2020csb1068@iitrpr.ac.in","PE")).thenReturn(5);
        when(mockacademicDAO.getCreditsType("2020csb1068@iitrpr.ac.in","OE")).thenReturn(2);
        assertEquals("Student can Graduate", testacademicEmployeeImpl.checkGraduation("2020csb1068@iitrpr.ac.in"));
    }

    @Test
    @Order(32)
    public void testCheckGraduation5() throws Exception{
        when(mockacademicDAO.getCurriculumCourse("2020csb1068@iitrpr.ac.in")).thenReturn(null);
        assertEquals("ERROR", testacademicEmployeeImpl.checkGraduation("2020csb1068@iitrpr.ac.in"));
    }

    @Test
    @Order(33)
    public void testCreateCurriculum1() throws Exception{
        String [] courses=new String[]{"CS201 PE"};
        String [] credits=new String[]{"PE 10"};
        when(mockacademicDAO.getdepartmentid("CSE")).thenReturn(-1);
        assertEquals("Department Does Not Exist", testacademicEmployeeImpl.createCurriculum(2020,courses,credits,"CSE"));
    }

    @Test
    @Order(34)
    public void testCreateCurriculum2() throws Exception{
        String [] courses=new String[]{"CS201 PE"};
        String [] credits=new String[]{"PE -2"};
        when(mockacademicDAO.getdepartmentid("CSE")).thenReturn(1);
        assertEquals("Invalid Credits in PE", testacademicEmployeeImpl.createCurriculum(2020,courses,credits,"CSE"));
    }

    @Test
    @Order(35)
    public void testCreateCurriculum3() throws Exception{
        String [] courses=new String[]{"CS201 PE"};
        String [] credits=new String[]{"PE 2"};
        when(mockacademicDAO.getdepartmentid("CSE")).thenReturn(1);
        when(mockacademicDAO.checkCourseCatalog("CS201")).thenReturn(false);
        assertEquals("Course Does Not Exist in Catalog CS201", testacademicEmployeeImpl.createCurriculum(2020,courses,credits,"CSE"));
    }

    @Test
    @Order(36)
    public void testCreateCurriculum4() throws Exception{
        String [] courses=new String[]{"CS201 PE"};
        String [] credits=new String[]{"PE 2"};
        when(mockacademicDAO.getdepartmentid("CSE")).thenReturn(1);
        when(mockacademicDAO.checkCourseCatalog("CS201")).thenReturn(true);
        when(mockacademicDAO.checkCourseTypes("PE")).thenReturn(false);
        assertEquals("Invalid Course Type PE", testacademicEmployeeImpl.createCurriculum(2020,courses,credits,"CSE"));
    }

    @Test
    @Order(37)
    public void testCreateCurriculum5() throws Exception{
        String [] courses=new String[]{"CS201 PE"};
        String [] credits=new String[]{"PE 2"};
        when(mockacademicDAO.getdepartmentid("CSE")).thenReturn(1);
        when(mockacademicDAO.checkCourseCatalog("CS201")).thenReturn(true);
        when(mockacademicDAO.checkCourseTypes("PE")).thenReturn(true);
        assertEquals("Curriculum Created Successfully", testacademicEmployeeImpl.createCurriculum(2020,courses,credits,"CSE"));
    }
    @Test
    @Order(38)
    public void testCreateCurriculum6() throws Exception{
        assertEquals("ERROR", testacademicEmployeeImpl.createCurriculum(2020,null,null,"CSE"));
    }
}
