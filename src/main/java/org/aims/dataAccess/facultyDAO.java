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

    public boolean login(String email, String password) {
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='" + email + "' AND password='" + password + "' AND role='FACULTY'");
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    // Checked
    public boolean loginLogs(String email) {
        try {
            SimpleDateFormat DateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date date = new Date();
            con.createStatement().execute("INSERT INTO login_logs (\"email\",\"login_time\",\"logout_time\") VALUES ('" + email + "','" + DateTime.format(date) + "','2000-01-01 00:00:00');");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // Checked
    public boolean logoutLogs(String email) {
        try {
            SimpleDateFormat DateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            con.createStatement().execute("UPDATE login_logs SET logout_time='" + DateTime.format(date) + "' WHERE email='" + email + "' AND logout_time='2000-01-01 00:00:00';");
            return true;
        } catch (SQLException e) {

            return false;
        }
    }

    // Checked
    public boolean checkPassword(String email, String oldPassword) {
        try {
            ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='" + email + "' AND password='" + oldPassword + "' AND role='FACULTY'");
            return rs1.next();
        } catch (SQLException e) {
            return false;
        }
    }

    // Checked
    public boolean changePassword(String email, String newPassword) {
        try {
            con.createStatement().execute("UPDATE passwords SET password='" + newPassword + "' WHERE email='" + email + "'");
            return true;
        } catch (SQLException e) {

            return false;
        }
    }

    //Checked
    public int getStudentid(String email) {
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT student_id FROM students WHERE email='" + email + "'");
            if (rs.next())
                return rs.getInt("student_id");
            else
                return -1;
        } catch (SQLException e) {

            return -1;
        }
    }

    //Checked
    public int getcountCoursetranscript(String email) {
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT count(*) FROM transcript_student_" + getStudentid(email));
            if (rs.next())
                return rs.getInt("count");
            else
                return -1;
        } catch (SQLException e) {

            return -1;
        }
    }

    //Checked
    public String[] viewGrades(String email) {
        try {
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
        } catch (SQLException e) {

            return null;
        }
    }

    //Checked
    public boolean checkCourseOffering(String CourseCode) {
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM courses_offering WHERE course_code='" + CourseCode + "'");
            return rs.next();
        } catch (SQLException e) {

            return false;
        }
    }

    //Checked
    public boolean checkCourseCatalog(String CourseCode) {
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM courses_catalog WHERE course_code='" + CourseCode + "'");
            return rs.next();
        } catch (SQLException e) {

            return false;
        }
    }

    //Checked
    public boolean checkSemesterStatus(String Status) {
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM time_semester WHERE status='" + Status + "'");
            return rs.next();
        } catch (SQLException e) {

            return false;
        }
    }

    //Changed
    public int getfacultyidEmail(String email) {
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT faculty_id FROM faculties WHERE email='" + email + "'");
            if (rs.next())
                return rs.getInt("faculty_id");
            else
                return -1;
        } catch (SQLException e) {

            return -1;
        }
    }

    // Checked
    public int getfacultyidCourse(String courseCode) {
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT faculty_id FROM courses_offering WHERE course_code='" + courseCode + "'");
            if (rs.next())
                return rs.getInt("faculty_id");
            else
                return -1;
        } catch (SQLException e) {

            return -1;
        }
    }

    // Checked
    public int getOfferingId(String courseCode) {
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT offering_id FROM courses_offering WHERE course_code='" + courseCode + "'");
            if (rs.next())
                return rs.getInt("offering_id");
            else
                return -1;
        } catch (SQLException e) {

            return -2;
        }
    }

    public String getCatalogid(String CourseCode) {
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT catalog_id FROM courses_catalog WHERE course_code='" + CourseCode + "'");
            if (rs.next())
                return rs.getString("catalog_id");
            else
                return null;
        } catch (SQLException e) {

            return null;
        }
    }

    public boolean deleteCourseOffering(String email, String CourseCode) {
        try {
            con.createStatement().execute("DELETE FROM courses_pre_req_offering WHERE offering_id='" + getOfferingId(CourseCode) + "'");
            con.createStatement().execute("DELETE FROM courses_offering WHERE offering_id='" + getOfferingId(CourseCode) + "'");
            con.createStatement().execute("DELETE FROM courses_teaching_faculty_" + getfacultyidEmail(email) + " WHERE catalog_id='" + getCatalogid(CourseCode) + "'");
            return true;
        } catch (SQLException e) {

            return false;
        }
    }

    public String[] getStudentEmail() {
        try {
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
        } catch (SQLException e) {

            return null;
        }
    }

    public boolean deleteCourseEnrollement(String email, String courseCode) {
        try {
            con.createStatement().execute("DELETE FROM courses_enrolled_student_" + getStudentid(email) + " WHERE catalog_id='" + getCatalogid(courseCode) + "'");
            return true;
        } catch (SQLException e) {

            return false;
        }
    }

    public boolean checkCourseTranscript(String email, String courseCode) {
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM transcript_student_" + getStudentid(email) + " WHERE catalog_id='" + getCatalogid(courseCode) + "'");
            if (rs.next()) {
                return rs.getInt("grade") >= 4;
            } else
                return false;
        } catch (SQLException e) {

            return false;
        }
    }

    public boolean checkCourseEnrollment(String email, String CourseCode) {
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM courses_enrolled_student_" + getStudentid(email) + " WHERE catalog_id=" + getCatalogid(CourseCode));
            return rs.next();
        } catch (SQLException e) {

            return false;
        }
    }
    public boolean updateGrade(String email, String courseCode, String grade) {
        try {
            con.createStatement().execute("UPDATE transcript_student_" + getStudentid(email) + " SET grade='" + grade + "' WHERE catalog_id='" + getCatalogid(courseCode) + "'");
            return true;
        } catch (SQLException e) {

            return false;
        }
    }
    public boolean insertCourse(String email, String courseCode, double CGPA) {
        try {
            con.createStatement().execute("SELECT INSERT_COURSE_OFFERED('" + getCatalogid(courseCode) + "','" + getfacultyidEmail(email) + "','" + courseCode + "','" + CGPA + "')");
            return true;
        } catch (SQLException e) {

            return false;
        }
    }
    public boolean insertCoursePreReq(String courseCode, String preReq, String grade, int type) {
        try {
            String query = "INSERT INTO courses_pre_req_offering (\"offering_id\",\"pre_req\",\"grade\",\"type\") VALUES ('" + getOfferingId(courseCode) + "','" + preReq + "','" + grade + "','" + type + "')";
            con.createStatement().execute(query);
            return true;
        } catch (SQLException e) {

            return false;
        }
    }
    public boolean insertCourseFaculty(String email, String courseCode) {
        try {
            con.createStatement().execute("INSERT INTO courses_teaching_faculty_" + getfacultyidEmail(email) + " (\"catalog_id\") VALUES ('" + getCatalogid(courseCode) + "')");
            return true;
        } catch (SQLException e) {

            return false;
        }
    }


}



