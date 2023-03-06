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
    Integer year=2010;

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
        con.createStatement().execute("INSERT INTO courses_pre_req(\"catalog_id\",\"pre_req\") VALUES('" + testDAO.getCatalogid(CourseCode) + "','" + dummyCourseCode + "')");
        testDAO.getSemester();
        testDAO.getYear();
        con.createStatement().execute("INSERT INTO time_semester VALUES ('" + 1 + "','" + year + "','ONGOING-CO')");
        testDAO.getcountCourseOffered();
        testDAO.getCourseOffered();
        con.createStatement().execute("SELECT INSERT_COURSE_OFFERED('" + testDAO.getCatalogid(CourseCode) + "','" + getfacultyidEmail(FacultyEmail) + "','" + CourseCode + "','" + 8 + "')");
        testDAO.insertCourseEnrollement(StudentEmail, CourseCode);
        testDAO.getCourseEnrolled(StudentEmail);
        con.createStatement().execute("INSERT INTO transcript_student_" + testDAO.getStudentid(StudentEmail) + " VALUES (" + testDAO.getCatalogid(CourseCode) + ",'8','1','2010'" + ")");


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

        query="DELETE FROM courses_offering WHERE course_code='"+CourseCode+"'";
        con.createStatement().execute(query);

        query="DROP TABLE courses_teaching_faculty_"+getfacultyidEmail(FacultyEmail);
        con.createStatement().execute(query);

        query="DROP TABLE transcript_faculty_"+getfacultyidEmail(FacultyEmail);
        con.createStatement().execute(query);

        query="DELETE FROM faculties WHERE email='"+FacultyEmail+"'";
        con.createStatement().execute(query);

        query="DELETE FROM courses_pre_req WHERE pre_req='"+dummyCourseCode+"'";
        con.createStatement().execute(query);

        query="DELETE FROM courses_catalog WHERE course_code='"+CourseCode+"'";
        con.createStatement().execute(query);

        query="DELETE FROM time_semester WHERE year="+year+";";
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

    @Test
    @Order(12)
    public void testGetTime() throws Exception{
        testDAO.getSemester();
        testDAO.getYear();
    }

    @Test
    @Order(13)
    public void testViewGrade() throws Exception{
        testDAO.viewGrades(StudentEmail);
    }

    @Test
    @Order(14)
    public void testEnrollement()  throws Exception{
        testDAO.getcountCourseEnrolled(StudentEmail);
        testDAO.creditsEnrolled(StudentEmail);
        testDAO.deleteCourseEnrollement(StudentEmail, CourseCode);
        testDAO.getcountCourseEnrolled(StudentEmail);
        testDAO.creditsEnrolled(StudentEmail);
    }

    @Test
    @Order(15)
    public void testGetOffering() throws Exception{
        testDAO.getOfferingId(CourseCode);
        testDAO.getOfferingId(dummyCourseCode);
        testDAO.getcountCourseOffered();
        testDAO.getCourseOffered();
    }

    @Test
    @Order(16)
    public void testPreReqOffer() throws Exception{
        testDAO.getPreReqOffer(CourseCode);
        testDAO.getPreReqOffer(dummyCourseCode);
        testDAO.getPreReqCollege(dummyCourseCode);
        testDAO.getPreReqCollege(CourseCode);
    }

    @Test
    @Order(17)
    public void testCGPA() throws Exception{
        testDAO.getCGPAcriteria(dummyCourseCode);
        testDAO.getCGPAcriteria(CourseCode);
    }

    @Test
    @Order(18)
    public void testCourseEnrolled() throws Exception{
        testDAO.getCourseEnrolled(StudentEmail);
    }

    @Test
    @Order(18)
    public void testTranscript() throws Exception{
        testDAO.creditsEarnedSemesterYear(StudentEmail,"1",2010);
        testDAO.creditsEarnedSemesterYear(StudentEmail,"1",2005);
        testDAO.creditsEarned(StudentEmail);
        testDAO.gradePointsEarned(StudentEmail);
        testDAO.checkCourseTranscript(StudentEmail, CourseCode);
        testDAO.getGradeCourse(StudentEmail, CourseCode);
        testDAO.getReqGradeOffer(CourseCode,dummyCourseCode);
    }

    @Test
    @Order(19)
    public void testLeft() throws Exception{
        testDAO.getStudentid(FacultyEmail);
        testDAO.viewGrades(FacultyEmail);
        testDAO.getcountCourseEnrolled(FacultyEmail);
        testDAO.getCourseEnrolled(FacultyEmail);
    }
}
