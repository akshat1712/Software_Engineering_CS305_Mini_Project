package org.aims.facultyTest;

import org.aims.dataAccess.facultyDAO;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class TestFile3 {
    facultyDAO testDAO = new facultyDAO();
    static String dummyEmail = "dummy@iitrpr.ac.in";
    static String dummyPassword = "dummy@123";
    static String dummyCourseCode="DUM12";

    static String FacultyEmail = "FAC@iitrpr.ac.in";
    static String StudentEmail = "STU@iitrpr.ac.in";
    static String Department="DUMMY";
    static String CourseCode="CS000";

    private static final String connectionString = "jdbc:postgresql://localhost:5432/postgres";
    private static final String username = "postgres";
    private static final String databasePassword = "2020csb1068";
    private static Connection con;

    static {
        try {
            con = DriverManager.getConnection(connectionString, username, databasePassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @BeforeAll
    public void setup() throws Exception{
        con.createStatement().executeQuery("SELECT INSERT_DEPARTMENT('" + Department + "');");
        con.createStatement().executeQuery("SELECT INSERT_STUDENT('" + "STUDENT1" + "','" + "2020CSB9999" + "','" + StudentEmail + "','" + "1" + "','" + "2020" + "','" + "7897897898" + "','" + "IIT ROPAR" + "')");
        con.createStatement().executeQuery("SELECT INSERT_FACULTY('" + "FACULTY1" + "','" + FacultyEmail + "','" + "1" + "','" + "2022-1-1" + "','" + "4564564565" + "','" + "IIT ROPAR" + "')");
        con.createStatement().executeQuery("SELECT INSERT_COURSE_CATALOG('" + CourseCode + "','" + CourseCode + "','" + "1" + "'," + 1 + "," + 1 + "," + 1 + "," + 1 + "," + 1 + ")");
        con.createStatement().executeQuery("SELECT INSERT_COURSE_OFFERED('" + testDAO.getCatalogid(CourseCode) + "','" + testDAO.getfacultyidEmail(FacultyEmail) + "','" + CourseCode + "','" + 8 + "')");
        testDAO.insertCoursePreReq(CourseCode, dummyCourseCode,"9",1);
    }




    @AfterAll
    public void teardown() throws SQLException {
        String query="DELETE FROM login_logs WHERE email='"+dummyEmail+"'";
        con.createStatement().execute(query);

        query="DELETE FROM courses_offering WHERE faculty_id="+testDAO.getfacultyidEmail(FacultyEmail);
        con.createStatement().execute(query);

        query="DROP TABLE transcript_student_"+testDAO.getStudentid(StudentEmail);
        con.createStatement().execute(query);

        query="DROP TABLE courses_enrolled_student_"+testDAO.getStudentid(StudentEmail);
        con.createStatement().execute(query);

        query="DELETE FROM students WHERE email='"+StudentEmail+"'";
        con.createStatement().execute(query);

        query="DELETE FROM courses_pre_req_offering WHERE pre_req='"+dummyCourseCode+"'";
        con.createStatement().execute(query);

        query="DELETE FROM courses_offering WHERE course_code='"+CourseCode+"'";
        con.createStatement().execute(query);

        query="DROP TABLE courses_teaching_faculty_"+testDAO.getfacultyidEmail(FacultyEmail);
        con.createStatement().execute(query);

        query="DROP TABLE transcript_faculty_"+testDAO.getfacultyidEmail(FacultyEmail);
        con.createStatement().execute(query);

        query="DELETE FROM faculties WHERE email='"+FacultyEmail+"'";
        con.createStatement().execute(query);

        query="DELETE FROM courses_catalog WHERE course_code='"+CourseCode+"'";
        con.createStatement().execute(query);

        query="DELETE FROM departments WHERE name='"+Department+"'";
        con.createStatement().execute(query);
    }
    @Test
    @Order(2)
    public void testLogin() throws Exception {
        testDAO.login(dummyEmail, dummyPassword);
    }
    @Test
    @Order(3)
    public void testLoginLogs() throws Exception {
        testDAO.loginLogs(dummyEmail);
    }
    @Test
    @Order(4)
    public void testLogoutLogs() throws Exception {
        testDAO.logoutLogs(dummyEmail);
    }
    @Test
    @Order(5)
    public void testCheckPassword() throws Exception {
        testDAO.checkPassword(dummyEmail, dummyPassword);
    }
    @Test
    @Order(6)
    public void testChangePassword() throws Exception {
        testDAO.changePassword(dummyEmail, dummyPassword);
    }
    @Test
    @Order(7)
    public void testCheckCourseOffering() throws Exception {
        testDAO.checkCourseOffering(dummyCourseCode);
    }
    @Test
    @Order(8)
    public void testCheckCourseCatalog() throws Exception {
        testDAO.checkCourseCatalog(dummyCourseCode);
    }
    @Test
    @Order(9)
    public void testCheckSemesterStatus() throws Exception {
        testDAO.checkSemesterStatus("ENDED");
    }
    @Test
    @Order(10)
    public void testGetStudentEmail() throws Exception {
        testDAO.getStudentEmail();
    }
    @Test
    @Order(11)
    public void testGetStudentid1() throws Exception {
        testDAO.getStudentid(StudentEmail);
    }
    @Test
    @Order(12)
    public void testGetStudentid2() throws Exception {
        testDAO.getStudentid(FacultyEmail);
    }
    @Test
    @Order(13)
    public void testUpdateGrade() throws Exception {
        testDAO.updateGrade(StudentEmail, CourseCode, "8");
    }
    @Test
    @Order(14)
    public void testInsertCourseFaculty() throws Exception {
        testDAO.insertCourseFaculty(FacultyEmail, CourseCode);
    }
    @Test
    @Order(15)
    public void testInsertCourse() throws Exception {
        testDAO.insertCourse(FacultyEmail, CourseCode,8.20);
    }
    @Test
    @Order(17)
    public void testGetfacultyidCourse() throws Exception {
        testDAO.getfacultyidCourse(CourseCode);
    }
    @Test
    @Order(18)
    public void getCountCourseTranscript() throws Exception {
        testDAO.getcountCoursetranscript(StudentEmail);
    }
    @Test
    @Order(19)
    public void testDeleteCourseEnrollement() throws Exception {
        testDAO.deleteCourseEnrollement(StudentEmail, CourseCode);
    }
    @Test
    @Order(20)
    public void testCheckCourseEnrollement() throws Exception {
        testDAO.checkCourseEnrollment(StudentEmail, CourseCode);
    }

    @Test
    @Order(21)
    public void testViewGrade() throws Exception {
        testDAO.viewGrades(StudentEmail);
    }

    @Test
    @Order(22)
    public void testGetOfferingCourse() throws Exception {
        int res=testDAO.getOfferingId(CourseCode);
    }
    @Test
    @Order(21)
    public void testDeleteCourse() throws Exception {
        testDAO.deleteCourseOffering(FacultyEmail, CourseCode);
    }

    @Test
    @Order(22)
    public void testExtra() throws Exception {
        testDAO.getfacultyidEmail(StudentEmail);
        testDAO.getCatalogid(dummyCourseCode);
        testDAO.getOfferingId(CourseCode);
        testDAO.getfacultyidCourse(CourseCode);
        testDAO.viewGrades(FacultyEmail);
    }
}
