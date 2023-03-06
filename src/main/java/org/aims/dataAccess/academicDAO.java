package org.aims.dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class academicDAO {
    private Connection con;
    private final String connectionString = "jdbc:postgresql://localhost:5432/postgres";
    private final String username = "postgres";
    private final String databasePassword = "2020csb1068";
    public academicDAO() {
        try {
            this.con = DriverManager.getConnection(connectionString, username, databasePassword);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public boolean login(String email, String password) throws SQLException  {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='" + email + "' AND password='" + password + "' AND role='ACAD_STAFF'");
        return rs.next();
    }
    public boolean loginLogs(String email) throws SQLException  {
        SimpleDateFormat DateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = new Date();
        con.createStatement().execute("INSERT INTO login_logs (\"email\",\"login_time\",\"logout_time\") VALUES ('" + email + "','" + DateTime.format(date) + "','2000-01-01 00:00:00');");
        return true;
    }
    public boolean logoutLogs(String email) throws SQLException  {
        SimpleDateFormat DateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        con.createStatement().execute("UPDATE login_logs SET logout_time='" + DateTime.format(date) + "' WHERE email='" + email + "' AND logout_time='2000-01-01 00:00:00';");
        return true;
    }
    public boolean checkPassword(String email, String oldPassword) throws SQLException  {
        ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='" + email + "' AND password='" + oldPassword + "' AND role='ACAD_STAFF'");
        return rs1.next();
    }
    public boolean changePassword(String email, String newPassword) throws SQLException  {
        con.createStatement().execute("UPDATE passwords SET password='" + newPassword + "' WHERE email='" + email + "'");
        return true;
    }
    public boolean checkCourseCatalog(String CourseCode) throws SQLException  {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM courses_catalog WHERE course_code='" + CourseCode + "'");
        return rs.next();
    }
    public int getdepartmentid(String dept) throws SQLException  {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM departments WHERE name='" + dept + "'");
        if (rs.next()) return rs.getInt("dept_id");
        else return -1;
    }
    public boolean insertCourseCatalog(String courseName, String courseCode, String dept, int l, int t, int p, int s, double c) throws SQLException  {
        ResultSet rs = con.createStatement().executeQuery("SELECT INSERT_COURSE_CATALOG('" + courseName + "','" + courseCode + "','" + getdepartmentid(dept) + "'," + l + "," + t + "," + p + "," + s + "," + c + ")");
        return rs.next();
    }
    public boolean insertCoursePre(String courseCode, String preCourseCode) throws SQLException  {
        con.createStatement().execute("INSERT INTO courses_pre_req(\"catalog_id\",\"pre_req\") VALUES('" + getCatalogid(courseCode) + "','" + preCourseCode + "')");
        return true;
    }
    public String getCatalogid(String CourseCode) throws SQLException  {
        ResultSet rs = con.createStatement().executeQuery("SELECT catalog_id FROM courses_catalog WHERE course_code='" + CourseCode + "'");
        if (rs.next()) return rs.getString("catalog_id");
        else return null;
    }
    public boolean checkSemesterValidity(String semester, int year) throws SQLException  {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM time_semester WHERE semester='" + semester + "' AND year=" + year);
        return rs.next();
    }
    public boolean newSemester(String semester, int Year) throws SQLException  {
        con.createStatement().execute("INSERT INTO time_semester VALUES ('" + semester + "','" + Year + "','ONGOING-CO')");
        return true;
    }
    public boolean createbatch(int batch) throws SQLException  {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM batch WHERE batch=" + batch);
        if (rs.next()) return true;
        else {
            ResultSet rs1 = con.createStatement().executeQuery("SELECT BATCH_TABLE_CREATION(" + batch + ")");
            return rs1.next();
        }
    }
    public boolean checkCourseTypes(String courseType) throws SQLException  {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM course_types WHERE type_alias='" + courseType + "'");
        return rs.next();
    }
    public boolean checkSemesterStatus(String Status) throws SQLException  {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM time_semester WHERE status='" + Status + "'");
        return rs.next();
    }
    public boolean updateSemesterStatus(String oldStatus, String newStatus) throws SQLException  {
        con.createStatement().execute("UPDATE time_semester SET status='" + newStatus + "' WHERE status='" + oldStatus + "'");
        return true;
    }
    public int getStudentid(String email) throws SQLException  {
        ResultSet rs = con.createStatement().executeQuery("SELECT student_id FROM students WHERE email='" + email + "'");
        if (rs.next()) return rs.getInt("student_id");
        else return -1;
    }
    public int getcountCoursetranscript(String email) throws SQLException  {
        ResultSet rs = con.createStatement().executeQuery("SELECT count(*) FROM transcript_student_" + getStudentid(email));
        if (rs.next()) return rs.getInt("count");
        else return -1;
    }
    public String[] viewGrades(String email) throws SQLException  {
        if (getStudentid(email) == -1) return null;
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
    public String[] getStudentids() throws SQLException  {
        ResultSet rs = con.createStatement().executeQuery("SELECT student_id FROM students");
        int count = 0;
        while (rs.next()) {
            count++;
        }
        String[] studentid = new String[count];

        rs = con.createStatement().executeQuery("SELECT student_id FROM students");

        int i = 0;
        while (rs.next()) {
            studentid[i] = rs.getString("student_id");
            i++;
        }
        return studentid;
    }
    public String[] getfacultyids() throws SQLException  {
        ResultSet rs = con.createStatement().executeQuery("SELECT faculty_id FROM faculties");
        int count = 0;
        while (rs.next()) {
            count++;
        }
        String[] facultyid = new String[count];

        rs = con.createStatement().executeQuery("SELECT faculty_id FROM faculties");

        int i = 0;
        while (rs.next()) {
            facultyid[i] = rs.getString("faculty_id");
            i++;
        }
        return facultyid;
    }
    public boolean checkGradeSubmission(String id) throws SQLException  {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM courses_enrolled_student_" + id + " WHERE grade is NULL");
        return rs.next();
    }
    public boolean updateStudentTranscript(String id) throws SQLException  {
        String query = "INSERT INTO transcript_student_" + id + " (\"catalog_id\",\"grade\",\"semester\",\"year\") SELECT catalog_id,grade," + getSemester() + "," + getYear() + " FROM courses_enrolled_student_" + id;
        con.createStatement().execute(query);
        con.createStatement().execute("TRUNCATE TABLE courses_enrolled_student_" + id);
        return true;
    }
    public boolean updateFacultyTranscript(String id) throws SQLException  {
        String query = "INSERT INTO transcript_faculty_" + id + " (\"catalog_id\",\"semester\",\"year\") SELECT catalog_id," + getSemester() + "," + getYear() + " FROM courses_teaching_faculty_" + id;
        con.createStatement().execute(query);
        con.createStatement().execute("TRUNCATE courses_teaching_faculty_" + id);
        return true;
    }
    public String getSemester() throws SQLException  {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM time_semester WHERE status!='ENDED'");
        if (rs.next()) return rs.getString("semester");
        else return "0";
    }
    public int getYear() throws SQLException  {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM time_semester WHERE status!='ENDED'");
        if (rs.next()) return rs.getInt("year");
        else return 0;
    }
    public String[] getCurriculumCourse(String email) throws SQLException  {
        ResultSet rs0 = con.createStatement().executeQuery("SELECT * FROM students WHERE email='" + email + "'");
        if (!rs0.next()) return null;
        ResultSet rs = con.createStatement().executeQuery("Select course_code from batch_curriculum_" + rs0.getInt("batch") + " P , courses_catalog Q WHERE P.catalog_id=Q.catalog_id AND P.department_id=+" + rs0.getInt("dept_id"));
        int count = 0;
        while (rs.next()) {
            count++;
        }
        String[] courses = new String[count];
        rs = con.createStatement().executeQuery("Select course_code from batch_curriculum_" + rs0.getInt("batch") + " P , courses_catalog Q WHERE P.catalog_id=Q.catalog_id AND P.department_id=+" + rs0.getInt("dept_id"));

        int i = 0;
        while (rs.next()) {
            courses[i] = rs.getString("course_code");
            i++;
        }
        return courses;
    }
    public boolean checkCourseTranscript(String email, String courseCode) throws SQLException  {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM transcript_student_" + getStudentid(email) + " WHERE catalog_id='" + getCatalogid(courseCode) + "'");
        if (rs.next()) {
            return rs.getInt("grade") >= 4;
        } else return false;
    }
    public int getCreditsType(String email, String type) throws SQLException  {
        ResultSet rs0 = con.createStatement().executeQuery("SELECT * FROM students WHERE email='" + email + "'");

        if (!rs0.next()) return -1;

        String query = "select credits from batch_credits_" + rs0.getInt("batch") + " WHERE type='" + type + "' AND department_id=" + rs0.getInt("dept_id");

        ResultSet rs = con.createStatement().executeQuery(query);

        int credits = 0;
        while (rs.next()) {
            credits += rs.getInt("credits");
        }
        return credits;
    }
    public Map<String, Double> getEnrolledCreditsType(String email) throws SQLException  {

        ResultSet rs0 = con.createStatement().executeQuery("SELECT * FROM students WHERE email='" + email + "'");
        if (!rs0.next()) return null;

        Map<String, Double> res = new HashMap<>();
        String query = "select P.grade,credits,type from transcript_student_" + getStudentid(email) + " P, batch_curriculum_" + rs0.getInt("batch") + " Q, courses_catalog R WHERE P.catalog_id=Q.catalog_id AND Q.catalog_id=R.catalog_id AND Q.department_id=" + rs0.getInt("dept_id");

        ResultSet rs = con.createStatement().executeQuery(query);
        while (rs.next()) {
            if (rs.getInt("grade") <= 3) continue;
            if (res.containsKey(rs.getString("type"))) {
                res.put(rs.getString("type"), res.get(rs.getString("type")) + rs.getDouble("credits"));
            } else {
                res.put(rs.getString("type"), rs.getDouble("credits"));
            }
        }
        return res;
    }
    public int createBatchCurriculum(int batch,String dept,String courseCode,String type) throws SQLException {
        String query = "INSERT INTO batch_curriculum_" + batch + " (\"department_id\",\"catalog_id\",\"type\") VALUES('" + getdepartmentid(dept) + "','" + getCatalogid(courseCode) + "','" + type + "')";
        con.createStatement().execute(query);
        return 1;
    }
    public int createBatchCredits(int batch,String dept,String courseCode,String type) throws SQLException {
        String query = "INSERT INTO batch_credits_" + batch + " (\"department_id\",\"type\",\"credits\") VALUES('" + getdepartmentid(dept) + "','" + courseCode + "','" + type + "')";
        con.createStatement().execute(query);
        return 1;
    }
    public int endSemester() throws SQLException {
        con.createStatement().execute("UPDATE time_semester SET status='ENDED' WHERE status!='ENDED'");
        con.createStatement().execute("TRUNCATE TABLE courses_offering CASCADE");
        return 1;
    }
    public int CreateCourseTypes(String courseType, String alias) throws SQLException {
        con.createStatement().execute("INSERT INTO course_types VALUES ('" + courseType + "','" + alias + "')");
        return 1;
    }
    public String[] getStudentEmail() throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT email FROM students");
        int count = 0;
        while(rs.next()){
            count++;
        }
        String[] emails = new String[count];
        rs = con.createStatement().executeQuery("SELECT email FROM students");
        int i = 0;
        while(rs.next()){
            emails[i] = rs.getString("email");
            i++;
        }
        return emails;
    }
}
