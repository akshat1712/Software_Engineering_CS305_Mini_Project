package org.aims.academicsTest;


import org.aims.dataAccess.academicDAO;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class TestFile3 {
    academicDAO testDAO = new academicDAO();
    String dummyEmail = "dummy@iitrpr.ac.in";
    String dummyPassword = "dummy@123";
    String dummyCourseCode="DUM12";

    String AcademicsEmail = "EACAD@iitrpr.ac.in";
    String FacultyEmail = "FAC@iitrpr.ac.in";
    String StudentEmail = "STU@iitrpr.ac.in";
    String Department="DUMMY";
    String CourseCode="CS000";

    Integer year=2010;
    static ResourceBundle rd = ResourceBundle.getBundle("config");
    static String data_base_url = rd.getString("data_base_url");
    static String username = rd.getString("username");
    static String password = rd.getString("password");
    private static Connection con;

    static {
        try {
            con = DriverManager.getConnection(data_base_url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @BeforeAll
    public void setup() throws Exception{
        con.createStatement().executeQuery("SELECT INSERT_DEPARTMENT('" + Department + "');");
        con.createStatement().executeQuery("SELECT INSERT_STUDENT('" + "STUDENT1" + "','" + "2020CSB9999" + "','" + StudentEmail + "','" + "1" + "','" + year + "','" + "7897897898" + "','" + "IIT ROPAR" + "')");
        con.createStatement().executeQuery("SELECT INSERT_FACULTY('" + "FACULTY1" + "','" + FacultyEmail + "','" + "1" + "','" + "2022-1-1" + "','" + "4564564565" + "','" + "IIT ROPAR" + "')");

        testDAO.insertCourseCatalog(CourseCode, CourseCode, Department, 1, 1, 1, 1, 1);
        testDAO.insertCoursePre(CourseCode,dummyCourseCode);
        testDAO.getYear();
        testDAO.getSemester();
        testDAO.newSemester("1",year);
        testDAO.createbatch(year);
        testDAO.CreateCourseTypes("DEMO","DM");
        con.createStatement().execute("INSERT INTO transcript_student_" + testDAO.getStudentid(StudentEmail) + " VALUES (" + testDAO.getCatalogid(CourseCode) + ",'8','1','2010'" + ")");

    }

    // HELPER FUNCTIONS
    public int  getfacultyidEmail(String email) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT faculty_id FROM faculties WHERE email='" + email + "'");
        if (rs.next())
            return rs.getInt("faculty_id");
        else
            return -1;
    }

    // HELPER FUNCTIONS OVER
    @AfterAll
    public void teardown() throws SQLException {
        String query="DELETE FROM login_logs WHERE email='"+dummyEmail+"'";
        con.createStatement().execute(query);

        query="DROP TABLE transcript_student_"+testDAO.getStudentid(StudentEmail);
        con.createStatement().execute(query);

        query="DROP TABLE courses_enrolled_student_"+testDAO.getStudentid(StudentEmail);
        con.createStatement().execute(query);

        query="DELETE FROM students WHERE email='"+StudentEmail+"'";
        con.createStatement().execute(query);

        query="DROP TABLE courses_teaching_faculty_"+getfacultyidEmail(FacultyEmail);
        con.createStatement().execute(query);

        query="DROP TABLE transcript_faculty_"+getfacultyidEmail(FacultyEmail);
        con.createStatement().execute(query);

        query="DELETE FROM faculties WHERE email='"+FacultyEmail+"'";
        con.createStatement().execute(query);

        query="DELETE FROM courses_pre_req WHERE pre_req='"+dummyCourseCode+"'";
        con.createStatement().execute(query);

        query="DROP TABLE batch_credits_"+year+";";
        con.createStatement().execute(query);

        query="DROP TABLE batch_curriculum_"+year+";";
        con.createStatement().execute(query);

        query="DELETE FROM batch WHERE batch="+year+";";
        con.createStatement().execute(query);

        query="DELETE FROM courses_catalog WHERE course_code='"+CourseCode+"'";
        con.createStatement().execute(query);

        query="DELETE FROM time_semester WHERE year="+year+";";
        con.createStatement().execute(query);

        query="DELETE FROM course_types WHERE type='DEMO';";
        con.createStatement().execute(query);

        query="DELETE FROM departments WHERE name='"+Department+"';";
        con.createStatement().execute(query);

        testDAO.endSemester();
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
    public void testCheckCourseCatalog() throws Exception {
        testDAO.checkCourseCatalog(dummyCourseCode);
    }
    @Test
    @Order(8)
    public void testCheckSemesterStatus() throws Exception {
        testDAO.checkSemesterStatus("ENDED");
    }
    @Test
    @Order(9)
    public void testGetStudentEmail() throws Exception {
        testDAO.getStudentEmail();
    }

    @Test
    @Order(10)
    public void testGetDepartmentID()  throws Exception {
        testDAO.getdepartmentid(Department);
        testDAO.getdepartmentid("12345");
    }

    @Test
    @Order(11)
    public void testSemesterValidity()  throws Exception {
        testDAO.checkSemesterValidity("1",2020);
    }

    @Test
    @Order(12)
    public void testcheckCourseTypes()  throws Exception {
        testDAO.checkCourseTypes("PE");
    }

    @Test
    @Order(13)
    public void testupdateSemesterStatus()  throws Exception {
        testDAO.updateSemesterStatus("A","B");
    }

    @Test
    @Order(14)
    public void testGetStudentID()  throws Exception {
        testDAO.getStudentids();
    }

    @Test
    @Order(15)
    public void testGetFacultyID()  throws Exception {
        testDAO.getfacultyids();
    }

    @Test
    @Order(16)
    public void testGetCatalogID()  throws Exception {
        testDAO.getCatalogid(CourseCode);
        testDAO.getCatalogid("12345");
    }

    @Test
    @Order(17)
    public void testGETStudentID()  throws Exception {
        testDAO.getStudentid(dummyEmail);
    }

    @Test
    @Order(17)
    public void testgetcountCoursetranscript()  throws Exception {
        testDAO.getcountCoursetranscript(StudentEmail);
    }

    @Test
    @Order(18)
    public void testGetTime() throws Exception{
        testDAO.getYear();
        testDAO.getSemester();
    }

    @Test
    @Order(19)
    public void testCreateBatch() throws Exception{
        testDAO.createbatch(year);
    }

    @Test
    @Order(20)
    public void testViewGrades() throws Exception{
        testDAO.viewGrades(StudentEmail);
    }

    @Test
    @Order(21)
    public void testCheckGradeSubmission() throws Exception{
        String res= String.valueOf(testDAO.getStudentid(StudentEmail));
        testDAO.checkGradeSubmission(res);
    }

    @Test
    @Order(22)
    public void testUpdateStudentTranscript() throws Exception{
        String res= String.valueOf(testDAO.getStudentid(StudentEmail));
        testDAO.updateStudentTranscript(res);
    }

    @Test
    @Order(23)
    public void testUpdateFacultyTranscript() throws Exception{
        String res= String.valueOf(getfacultyidEmail(FacultyEmail));
        testDAO.updateFacultyTranscript(res);
    }

    @Test
    @Order(24)
    public void testBatchTables() throws Exception{
        testDAO.createBatchCredits(year,Department,"DM","3");
        testDAO.createBatchCurriculum(year,Department,CourseCode,"DM");
    }

    @Test
    @Order(25)
    public void testGETCurriculumCourse() throws Exception{
        testDAO.getCurriculumCourse(StudentEmail);
    }
    @Test
    @Order(26)
    public void testCheckCourseTranscript() throws Exception{
        testDAO.checkCourseTranscript(StudentEmail,CourseCode);
    }
    @Test
    @Order(27)
    public void testGetStudentType() throws Exception{
        testDAO.getCreditsType(StudentEmail,"PE");
        testDAO.getEnrolledCreditsType(StudentEmail);
    }

}
