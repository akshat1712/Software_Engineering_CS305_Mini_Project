package org.aims.studentTest;

import org.aims.dataAccess.studentDAO;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestFile3 {

    studentDAO testDAO = new studentDAO();
    String dummyEmail = "dummy@iitrpr.ac.in";
    String dummyPassword = "dummy@123";
    String dummyCourseCode="DUM12";

    String FacultyEmail = "FAC@iitrpr.ac.in";
    String StudentEmail = "STU@iitrpr.ac.in";
    String Department="DUMMY";
    String CourseCode="CS000";

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

    }

    public int  getfacultyidEmail(String email) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT faculty_id FROM faculties WHERE email='" + email + "'");
        if (rs.next())
            return rs.getInt("faculty_id");
        else
            return -1;
    }

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

        query="DELETE FROM courses_catalog WHERE course_code='"+CourseCode+"'";
        con.createStatement().execute(query);

        query="DELETE FROM departments WHERE name='"+Department+"';";
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
    public void testCheckSemesterStatus() throws Exception {
        testDAO.checkSemesterStatus("ENDED");
    }
    @Test
    @Order(9)
    public void testGetCatalogID() throws Exception {
        testDAO.getCatalogid(CourseCode);
        testDAO.getCatalogid(dummyCourseCode);
    }
    @Test
    @Order(10)
    public void testCheckCourseEnrollment() throws Exception {
        testDAO.checkCourseEnrollment(StudentEmail, CourseCode);
    }
    @Test
    @Order(11)
    public void testGetCreditsCourse() throws Exception {
        testDAO.getCreditsCourse(CourseCode);
        testDAO.getCreditsCourse(dummyCourseCode);
    }
}
