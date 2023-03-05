package org.aims.dataAccess;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class studentDAO {
    private Connection con;
    private final String connectionString = "jdbc:postgresql://localhost:5432/postgres";
    private final String username = "postgres";
    private final String databasePassword = "2020csb1068";
    public studentDAO() {
        try {
            this.con = DriverManager.getConnection(connectionString, username, databasePassword);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public boolean login(String email, String password) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='" + email + "' AND password='" + password + "' AND role='STUDENT'");
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
        ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='" + email + "' AND password='" + oldPassword + "' AND role='STUDENT'");
        return rs1.next();
    }
    public boolean changePassword(String email, String newPassword) throws SQLException {
        con.createStatement().execute("UPDATE passwords SET password='" + newPassword + "' WHERE email='" + email + "'");
        return true;
    }
    public boolean checkCourseOffering(String CourseCode) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM courses_offering WHERE course_code='" + CourseCode + "'");
        return rs.next();
    }
    public String getCatalogid(String CourseCode) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT catalog_id FROM courses_catalog WHERE course_code='" + CourseCode + "'");
        if (rs.next())
            return rs.getString("catalog_id");
        else
            return null;
    }
    public boolean checkCourseEnrollment(String email, String CourseCode) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM courses_enrolled_student_" + getStudentid(email) + " WHERE catalog_id=" + getCatalogid(CourseCode));
        return rs.next();
    }
    public boolean checkSemesterStatus(String Status) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM time_semester WHERE status='" + Status + "'");
        return rs.next();
    }
    public double creditsEnrolled(String email) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("select credits from courses_enrolled_student_" + getStudentid(email) + " P , courses_catalog Q WHERE P.catalog_id=Q.catalog_id;");
        double total_credits = 0;
        while (rs.next()) {
            total_credits += Double.parseDouble(rs.getString("credits"));
        }
        return total_credits;
    }
    public double getCreditsCourse(String courseCode) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT credits FROM courses_catalog WHERE course_code='" + courseCode + "'");
        if (rs.next())
            return rs.getDouble("credits");
        else
            return -1;
    }
    public String getSemester() throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT semester FROM time_semester WHERE status!='ENDED'");
        if (rs.next())
            return rs.getString("semester");
        else
            return null;
    }
    public int getYear() throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT year FROM time_semester WHERE status!='ENDED'");
        if (rs.next())
            return rs.getInt("year");
        else
            return -1;
    }
    public double creditsEarnedSemesterYear(String email, String Semester, int Year) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("Select credits,course_code from transcript_student_" + getStudentid(email) + " P , courses_catalog Q where P.catalog_id=Q.catalog_id AND P.year='" + Year + "' AND P.semester='" + Semester + "'");
        double total_credits = 0;
        while (rs.next()) {
            total_credits += Double.parseDouble(rs.getString("credits"));
        }
        if(total_credits==0)
            return 6;

        return total_credits;
    }
    public double creditsEarned(String email) throws SQLException {
        String query = "select P.credits,Q.grade from courses_catalog P , transcript_student_" + getStudentid(email) + " Q WHERE P.catalog_id=Q.catalog_id";
        ResultSet rs = con.createStatement().executeQuery(query);

        double totalCredits = 0;
        while (rs.next()) {
            if (Double.parseDouble(rs.getString("grade")) > 3.0)
                totalCredits += Double.parseDouble(rs.getString("credits"));

        }
        return totalCredits;
    }
    public double gradePointsEarned(String email) throws SQLException {
        String query = "select P.credits,Q.grade from courses_catalog P , transcript_student_" + getStudentid(email) + " Q WHERE P.catalog_id=Q.catalog_id";
        ResultSet rs = con.createStatement().executeQuery(query);
        double totalGradePoints = 0;
        while (rs.next()) {
            double credits = Double.parseDouble(rs.getString("credits"));
            double grade = Double.parseDouble(rs.getString("grade"));
            if (grade <= 3.0)
                continue;
            totalGradePoints += credits * grade;
        }
        return totalGradePoints;
    }
    public int getStudentid(String email) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT student_id FROM students WHERE email='" + email + "'");
        if (rs.next())
            return rs.getInt("student_id");
        else
            return -1;
    }
    public int getcountCourseOffered() throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT count(*) FROM courses_offering");
        if (rs.next())
            return rs.getInt("count");
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
    public boolean deleteCourseEnrollement(String email, String courseCode) throws SQLException {
        con.createStatement().execute("DELETE FROM courses_enrolled_student_" + getStudentid(email) + " WHERE catalog_id='" + getCatalogid(courseCode) + "'");
        return true;
    }
    public boolean insertCourseEnrollement(String email, String courseCode) throws SQLException {
        con.createStatement().execute("INSERT INTO courses_enrolled_student_" + getStudentid(email) + " VALUES('" + getCatalogid(courseCode) + "')");
        return true;
    }
    public int getcountCourseEnrolled(String email) throws SQLException {
        int id = getStudentid(email);
        if (id == -1)
            return -1;
        ResultSet rs = con.createStatement().executeQuery("SELECT count(*) FROM courses_enrolled_student_" + id);
        if (rs.next())
            return rs.getInt("count");
        else
            return -1;
    }
    public String[] getCourseOffered() throws SQLException {
        int count = getcountCourseOffered();
        if (count == -1)
            return null;
        String[] courses = new String[count];
        ResultSet rs = con.createStatement().executeQuery("SELECT Q.course_code,credits FROM courses_offering as P , courses_catalog as Q WHERE P.catalog_id=Q.catalog_id");
        int i = 0;
        while (rs.next()) {
            courses[i] = "Course Code: " + rs.getString("course_code") + " || Credits: " + rs.getString("credits");
            i++;
        }
        return courses;
    }
    public String[] getCourseEnrolled(String email) throws SQLException {
        int count = getcountCourseEnrolled(email);
        int id = getStudentid(email);
        if (count == -1)
            return null;
        String[] courses = new String[count];
        ResultSet rs = con.createStatement().executeQuery("SELECT Q.course_code,credits FROM courses_enrolled_student_" + id + " as P , courses_catalog as Q WHERE P.catalog_id=Q.catalog_id");
        int i = 0;
        while (rs.next()) {
            courses[i] = "Course Code: " + rs.getString("course_code") + " || Credits: " + rs.getString("credits");
            i++;
        }
        return courses;
    }
    public double getCGPAcriteria(String courseCode) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT \"CGPA\" FROM courses_offering WHERE course_code='" + courseCode + "'");
        if (rs.next())
            return rs.getDouble("CGPA");
        else
            return -1;
    }
    public String[] getPreReqCollege(String courseCode) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("select P.course_code from courses_catalog P , courses_pre_req Q where P.course_code=Q.pre_req AND Q.catalog_id=" + getCatalogid(courseCode));
        int count = 0;
        while (rs.next())
            count++;
        String[] preReq = new String[count];
        rs = con.createStatement().executeQuery("select P.course_code from courses_catalog P , courses_pre_req Q where P.course_code=Q.pre_req AND Q.catalog_id=" + getCatalogid(courseCode));
        int i = 0;
        while (rs.next()) {
            preReq[i] = rs.getString("course_code");
            i++;
        }
        return preReq;
    }
    public String[] getPreReqOffer(String courseCode) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("select Q.pre_req from courses_offering P , courses_pre_req_offering Q where P.offering_id=Q.offering_id AND P.course_code='" + courseCode + "'");
        int count = 0;
        while (rs.next())
            count++;
        String[] preReq = new String[count];
        rs = con.createStatement().executeQuery("select Q.pre_req from courses_offering P , courses_pre_req_offering Q where P.offering_id=Q.offering_id AND P.course_code='" + courseCode + "'");
        int i = 0;
        while (rs.next()) {
            preReq[i] = rs.getString("pre_req");
            i++;
        }
        return preReq;
    }
    public boolean checkCourseTranscript(String email, String courseCode) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM transcript_student_" + getStudentid(email) + " WHERE catalog_id='" + getCatalogid(courseCode) + "'");
        if (rs.next()) {
            return rs.getInt("grade") >= 4;
        } else
            return false;
    }
    public int getOfferingId(String courseCode) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT offering_id FROM courses_offering WHERE course_code='" + courseCode + "'");
        if (rs.next())
            return rs.getInt("offering_id");
        else
            return -1;
    }
    public int getGradeCourse(String email, String courseCode) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT grade FROM transcript_student_" + getStudentid(email) + " WHERE catalog_id='" + getCatalogid(courseCode) + "'");
        if (rs.next())
            return rs.getInt("grade");
        else
            return -1;
    }
    public int getReqGradeOffer(String CourseCode, String PreReqcourseCode) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT grade FROM courses_pre_req_offering WHERE pre_req='" + PreReqcourseCode + "' AND offering_id='" + getOfferingId(CourseCode) + "'");
        if (rs.next())
            return rs.getInt("grade");
        else
            return -1;
    }
}
