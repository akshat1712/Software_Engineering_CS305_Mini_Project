package org.aims.dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class facultyDAO {
    private Connection con;
    private final String connectionString = "jdbc:postgresql://localhost:5432/postgres";
    private final String username = "postgres";
    private final String databasePassword = "2020csb1068";
    public facultyDAO() {
        try {
            this.con = DriverManager.getConnection(connectionString, username, databasePassword);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public boolean login(String email, String password) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='" + email + "' AND password='" + password + "' AND role='FACULTY'");
        return rs.next();
    }
    public boolean loginLogs(String email) throws SQLException {
        SimpleDateFormat DateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = new Date();
        con.createStatement().execute("INSERT INTO login_logs (\"email\",\"login_time\",\"logout_time\") VALUES ('" + email + "','" + DateTime.format(date) + "','2000-01-01 00:00:00');");
        return true;
    }
    public boolean logoutLogs(String email) throws SQLException {
        SimpleDateFormat DateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        con.createStatement().execute("UPDATE login_logs SET logout_time='" + DateTime.format(date) + "' WHERE email='" + email + "' AND logout_time='2000-01-01 00:00:00';");
        return true;
    }
    public boolean checkPassword(String email, String oldPassword) throws SQLException {
        ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='" + email + "' AND password='" + oldPassword + "' AND role='FACULTY'");
        return rs1.next();
    }
    public boolean changePassword(String email, String newPassword) throws SQLException {
        con.createStatement().execute("UPDATE passwords SET password='" + newPassword + "' WHERE email='" + email + "'");
        return true;
    }
    public int getStudentid(String email) throws SQLException{
        ResultSet rs = con.createStatement().executeQuery("SELECT student_id FROM students WHERE email='" + email + "'");
        if (rs.next())
            return rs.getInt("student_id");
        else
            return -1;
    }
    public int getcountCoursetranscript(String email) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT count(*) FROM transcript_student_" + getStudentid(email));
        if (rs.next())
            return rs.getInt("count");
        else
            return -1;
    }
    public String[] viewGrades(String email) throws SQLException {
        if (getStudentid(email) == -1)
            return null;

        String query = "select P.course_code,Q.grade,semester,year from courses_catalog P , transcript_student_" + getStudentid(email) + " Q WHERE P.catalog_id=Q.catalog_id";
        ResultSet rs = con.createStatement().executeQuery(query);
        String[] grades = new String[getcountCoursetranscript(email)];
        int i = 0;
        while (rs.next()) {
            grades[i] = rs.getString("course_code") + " || " + rs.getString("grade") + " || " + rs.getString("semester") + " || " + rs.getString("year");
            i++;
        }
        return grades;
    }
    public boolean checkCourseOffering(String CourseCode) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM courses_offering WHERE course_code='" + CourseCode + "'");
        return rs.next();
    }
    public boolean checkCourseCatalog(String CourseCode) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM courses_catalog WHERE course_code='" + CourseCode + "'");
        return rs.next();
    }
    public boolean checkSemesterStatus(String Status) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM time_semester WHERE status='" + Status + "'");
        return rs.next();
    }
    public int getfacultyidEmail(String email) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT faculty_id FROM faculties WHERE email='" + email + "'");
        if (rs.next())
            return rs.getInt("faculty_id");
        else
            return -1;
    }
    public int getfacultyidCourse(String courseCode) throws SQLException{
        ResultSet rs = con.createStatement().executeQuery("SELECT faculty_id FROM courses_offering WHERE course_code='" + courseCode + "'");
        if (rs.next())
            return rs.getInt("faculty_id");
        else
            return -1;
    }
    public int getOfferingId(String courseCode) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT offering_id FROM courses_offering WHERE course_code='" + courseCode + "'");
        if (rs.next())
            return rs.getInt("offering_id");
        else
            return -1;
    }
    public String getCatalogid(String CourseCode) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT catalog_id FROM courses_catalog WHERE course_code='" + CourseCode + "'");
        if (rs.next())
            return rs.getString("catalog_id");
        else
            return null;
    }
    public boolean deleteCourseOffering(String email, String CourseCode) throws SQLException{
        con.createStatement().execute("DELETE FROM courses_pre_req_offering WHERE offering_id='" + getOfferingId(CourseCode) + "'");
        con.createStatement().execute("DELETE FROM courses_offering WHERE offering_id='" + getOfferingId(CourseCode) + "'");
        con.createStatement().execute("DELETE FROM courses_teaching_faculty_" + getfacultyidEmail(email) + " WHERE catalog_id='" + getCatalogid(CourseCode) + "'");
        return true;
    }
    public String[] getStudentEmail() throws SQLException{
        ResultSet rs = con.createStatement().executeQuery("SELECT email FROM students");
        int count = 0;
        while (rs.next()) {
            count++;
        }
        String[] studentid = new String[count];

        rs = con.createStatement().executeQuery("SELECT email FROM students");
        int i = 0;
        while (rs.next()) {
            studentid[i] = rs.getString("email");
            i++;
        }
        return studentid;
    }
    public boolean deleteCourseEnrollement(String email, String courseCode) throws SQLException {
        con.createStatement().execute("DELETE FROM courses_enrolled_student_" + getStudentid(email) + " WHERE catalog_id='" + getCatalogid(courseCode) + "'");
        return true;
    }
    public boolean checkCourseEnrollment(String email, String CourseCode) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM courses_enrolled_student_" + getStudentid(email) + " WHERE catalog_id=" + getCatalogid(CourseCode));
        return rs.next();
    }
    public boolean updateGrade(String email, String courseCode, String grade) throws SQLException{
        con.createStatement().execute("UPDATE transcript_student_" + getStudentid(email) + " SET grade='" + grade + "' WHERE catalog_id='" + getCatalogid(courseCode) + "'");
        return true;
    }
    public boolean insertCourse(String email, String courseCode, double CGPA) throws SQLException {
        con.createStatement().execute("SELECT INSERT_COURSE_OFFERED('" + getCatalogid(courseCode) + "','" + getfacultyidEmail(email) + "','" + courseCode + "','" + CGPA + "')");
        return true;
    }
    public boolean insertCoursePreReq(String courseCode, String preReq, String grade, int type) throws SQLException {
        String query = "INSERT INTO courses_pre_req_offering (\"offering_id\",\"pre_req\",\"grade\",\"type\") VALUES ('" + getOfferingId(courseCode) + "','" + preReq + "','" + grade + "','" + type + "')";
        con.createStatement().execute(query);
        return true;
    }
    public boolean insertCourseFaculty(String email, String courseCode) throws SQLException {
        con.createStatement().execute("INSERT INTO courses_teaching_faculty_" + getfacultyidEmail(email) + " (\"catalog_id\") VALUES ('" + getCatalogid(courseCode) + "')");
        return true;
    }
}



