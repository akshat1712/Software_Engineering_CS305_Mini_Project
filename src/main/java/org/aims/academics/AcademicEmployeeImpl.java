package org.aims.academics;

import org.aims.dataAccess.userDAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AcademicEmployeeImpl implements userDAL {

    private final String email;
    private final String password;

    private final Connection con;

    private final String connectionString = "jdbc:postgresql://localhost:5432/postgres";
    private final String username = "postgres";
    private final String databasePassword = "2020csb1068";

    public AcademicEmployeeImpl(String Email, String Password) throws SQLException {
        this.email = Email;
        this.password = Password;
        con = DriverManager.getConnection(connectionString, username, databasePassword);
    }


    public boolean login() {
        try {
            ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='" + email + "' AND password='" + password + "' AND role='ACAD_STAFF'");
            if (rs1.next()) {
                SimpleDateFormat DateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                con.createStatement().execute("INSERT INTO login_logs (\"email\",\"login_time\",\"logout_time\") VALUES ('" + email + "','" + DateTime.format(date) + "','2000-01-01 00:00:00');");
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String logout() throws SQLException {
        SimpleDateFormat DateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        con.createStatement().execute("UPDATE login_logs SET logout_time='" + DateTime.format(date) + "' WHERE email='" + email + "' AND logout_time='2000-01-01 00:00:00';");
        return "Logged Out Successfully\n";
    }

    public String addCourseInCatalog(String courseCode, String courseName, String department,
                                     int lectures, int tutorial, int practicals, int self_study, double credits,
                                     String[] prerequisite) throws SQLException {

        for (String s : prerequisite) {
            if (s.equals(courseCode))
                continue;

            String query = "SELECT * FROM courses_catalog WHERE course_code='" + s + "'";
            System.out.println(query);
            ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM courses_catalog WHERE course_code='" + s + "'");
            if (!rs1.next()) {
                return "Prerequisite Course Does Not Exist";
            }
        }
        ResultSet rs2 = con.createStatement().executeQuery("SELECT dept_id FROM departments WHERE name='" + department + "'");
        if (!rs2.next()) {
            return "Department Does Not Exist";
        }


        con.createStatement().executeQuery("SELECT INSERT_COURSE_CATALOG('" + courseName + "','" + courseCode + "','" + rs2.getString("dept_id") + "'," + lectures + "," + tutorial + "," + practicals + "," + self_study + "," + credits + ")");

        ResultSet rs3 = con.createStatement().executeQuery("SELECT MAX(catalog_id) as id FROM courses_catalog;");

        if (!rs3.next()) {
            return "Course Not added Successfully";
        }

        for (String s : prerequisite) {
            if (s.equals(courseCode))
                continue;
            con.createStatement().execute("INSERT INTO courses_pre_req (\"catalog_id\", \"pre_req\") VALUES ('" + rs3.getString("id") + "','" + s + "')");
        }
        return "COURSE ADDED IN CATALOG SUCCESSFULLY\n";
    }

    public String createCurriculum(int batch, String[] courses, String[] credits, String Department) throws SQLException {

        ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM batch WHERE batch=" + batch + "");
        if (!rs1.next()) {
            ResultSet rs2 = con.createStatement().executeQuery("SELECT BATCH_TABLE_CREATION(" + batch + ")");
            if (!rs2.next()) {
                return "Batch Not Created";
            }
        }

        ResultSet rs3 = con.createStatement().executeQuery("SELECT dept_id FROM departments WHERE name='" + Department + "'");
        if (!rs3.next()) {
            return "Department Does Not Exist";
        }

        for (String s : credits) {
            String[] split = s.split(" ");

            if (Double.parseDouble(split[1]) < 0) {
                return "Invalid Credits in " + split[0];
            }
        }

        for (String s : courses) {
            String[] split = s.split(" ");
            ResultSet rs4 = con.createStatement().executeQuery("SELECT catalog_id FROM courses_catalog WHERE course_code='" + split[0] + "'");
            if (!rs4.next()) {
                return "Course Does Not Exist " + split[0];
            }

            ResultSet rs5 = con.createStatement().executeQuery("SELECT * FROM course_types WHERE type_alias='" + split[1] + "'");
            if (!rs5.next()) {
                return "Invalid Course Type " + split[1];
            }

            String query = "INSERT INTO batch_curriculum_" + batch + " (\"department_id\",\"catalog_id\",\"type\") VALUES('" + rs3.getString("dept_id") + "','" + rs4.getString("catalog_id") + "','" + split[1] + "')";
//            System.out.println(query);
            con.createStatement().execute(query);

        }

        for (String s : credits) {
            String[] split = s.split(" ");
            ResultSet rs4 = con.createStatement().executeQuery("SELECT * FROM course_types WHERE type_alias='" + split[0] + "'");
            if (!rs4.next()) {
                return "Invalid Course Type " + split[0];
            }

            String query = "INSERT INTO batch_credits_" + batch + " (\"department_id\",\"type\",\"credits\") VALUES('" + rs3.getString("dept_id") + "','" + split[0] + "','" + split[1] + "')";
//            System.out.println(query);
            con.createStatement().execute(query);
        }


        return "Curriculum Created Successfully\n";

    }

    public String startSemester(int Year, String Semester) throws SQLException {
        ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM time_semester WHERE status!='ENDED'");
        ResultSet rs2 = con.createStatement().executeQuery("SELECT * FROM time_semester WHERE year=" + Year + " AND semester='" + Semester + "'");
        if (rs1.next()) {
            return "A Semester Already Ongoing\n";
        } else if (rs2.next()) {
            return "This is not valid semester\n";
        } else {
            con.createStatement().executeUpdate("INSERT INTO time_semester VALUES ('" + Semester + "','" + Year + "','ONGOING')");
            return "Semester Started\n";
        }
    }

    public String endSemester() throws SQLException {
        ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM time_semester WHERE status='ONGOING'");
        if (!rs1.next()) {
            return "\nNo Semester Ongoing\n";
        }

        ResultSet rs2 = con.createStatement().executeQuery("SELECT student_id,entry_number FROM students");

        while (rs2.next()) {
            String id = rs2.getString("student_id");
            ResultSet rs3 = con.createStatement().executeQuery("SELECT * FROM courses_enrolled_student_" + id);
            while (rs3.next()) {
                String grade = rs3.getString("grade");
                if (grade == null) {
                    return "\n Grade Not submitted for the student " + rs2.getString("entry_number");
                }
            }
        }


        ResultSet rs4 = con.createStatement().executeQuery("SELECT student_id FROM students");
        while (rs4.next()) {
            String id = rs4.getString("student_id");
            String query = "INSERT INTO transcript_student_" + id + " (\"catalog_id\",\"grade\",\"semester\",\"year\") SELECT catalog_id,grade," + rs1.getString("semester") + "," + rs1.getString("year") + " FROM courses_enrolled_student_" + id;
//            System.out.println(query);
            con.createStatement().execute(query);
            con.createStatement().execute("TRUNCATE TABLE courses_enrolled_student_" + id);
        }

        ResultSet rs5 = con.createStatement().executeQuery("SELECT faculty_id FROM faculties");
        while (rs5.next()) {
            String id = rs5.getString("faculty_id");
            String query = "INSERT INTO transcript_faculty_" + id + " (\"catalog_id\",\"semester\",\"year\") SELECT catalog_id," + rs1.getString("semester") + "," + rs1.getString("year") + " FROM courses_teaching_faculty_" + id;
//            System.out.println(query);
            con.createStatement().execute(query);
            con.createStatement().execute("TRUNCATE TABLE courses_teaching_faculty_" + id);
        }
        con.createStatement().executeUpdate("UPDATE time_semester SET status='ENDED' WHERE status='ONGOING'");
        con.createStatement().executeQuery("TRUNCATE TABLE courses_offering");
        con.createStatement().executeQuery("TRUNCATE TABLE courses_pre_req_offering");

        return "Semester Ended\n";
    }

    public String[] viewGrades(String email) throws SQLException {
        ResultSet rs1 = con.createStatement().executeQuery("SELECT student_id FROM students WHERE email='" + email + "'");
        if (rs1.next()) {
            String id = rs1.getString("student_id");
            ResultSet rs2 = con.createStatement().executeQuery("select count(*) from transcript_student_" + id + " as T ,courses_catalog C WHERE T.catalog_id=C.catalog_id;");
            int numGrades = 0;

            if (rs2.next()) {
                numGrades = rs2.getInt("count");
            }

            String[] grades = new String[numGrades];

            rs2 = con.createStatement().executeQuery("select course_code,grade,semester,year from transcript_student_" + id + " as T ,courses_catalog C WHERE T.catalog_id=C.catalog_id;");

            while (rs2.next()) {
                grades[rs2.getRow() - 1] = "Course Code: " + rs2.getString("course_code") + " || Grade: " + rs2.getString("grade") + " || Semester: " + rs2.getString("semester") + " || Year: " + rs2.getString("year");
            }

            return grades;
        } else {
            return null;
        }
    }

    public String changePassword(String oldPassword, String newPassword) throws SQLException {
        ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='" + email + "' AND password='" + oldPassword + "' AND role='ACAD_STAFF'");
        if (rs1.next()) {
            con.createStatement().execute("UPDATE passwords SET password='" + newPassword + "' WHERE email='" + email + "'");
            return "Password Changed to New\n";
        } else {
            return "Incorrect Old Password\n";
        }
    }

    public Map<String, String[]> generateReport() throws Exception {

        ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM students");

        Map<String, String[]> transcriptAllStudents = new HashMap<String, String[]>();

        while (rs1.next()) {
            String[] grades = viewGrades(rs1.getString("email"));
            transcriptAllStudents.put(rs1.getString("entry_number"), grades);
        }
        return transcriptAllStudents;
    }

    public String createCourseTypes(String courseType, String alias) {
        try {
            String query = "INSERT INTO course_types VALUES ('" + courseType + "','" + alias + "')";
            System.out.println(query);
            con.createStatement().execute("INSERT INTO course_types VALUES ('" + courseType + "','" + alias + "')");
            return "Course Type Created Successfully\n";
        } catch (SQLException e) {
            return "Course Type Already Exists\n";
        }
    }


    public String checkGraduation(String email) throws SQLException {
        ResultSet rs1 = con.createStatement().executeQuery("SELECT student_id,batch,dept_id FROM students WHERE email='" + email + "'");
        if (!rs1.next()) {
            return "\nStudent Not Found";
        }
        String query1 = "select course_code,Q.credits,type from transcript_student_" + rs1.getString("student_id") + " P, courses_catalog Q, batch_curriculum_" + rs1.getString("batch") + " R WHERE P.catalog_id=Q.catalog_id AND Q.catalog_id=R.catalog_id AND R.department_id=" + rs1.getString("dept_id");
        String query2 = "SELECT * from transcript_student_" + rs1.getString("student_id") + ";";

        System.out.println(query1);

        ResultSet rs2 = con.createStatement().executeQuery(query1);
        ResultSet rs3 = con.createStatement().executeQuery(query2);

        int size1 = 0;
        int size2 = 0;

        while (rs2.next()) {
            size1++;
        }
        while (rs3.next()) {
            size2++;
        }

        if (size1 != size2) {
            return "\nNot all courses of this students has been defined a type";
        }

        Map<String, Double> creditsTypeCount = new HashMap<String, Double>();

        ResultSet rs4 = con.createStatement().executeQuery(query1);

        while (rs4.next()) {
            if (creditsTypeCount.containsKey(rs4.getString("type"))) {
                creditsTypeCount.put(rs4.getString("type"), creditsTypeCount.get(rs4.getString("type")) + rs4.getDouble("credits"));
            } else {
                creditsTypeCount.put(rs4.getString("type"), rs4.getDouble("credits"));
            }
        }


        ResultSet rs5 = con.createStatement().executeQuery("SELECT * FROM batch_credits_" + rs1.getString("batch"));

        Double total_credits = 0.0;
        Double credits_without_open_elective = 0.0;
        Double open_elective_required = 0.0;
        while (rs5.next()) {
            total_credits += rs5.getDouble("credits");
            if (Objects.equals(rs5.getString("type"), "OE")) {
                open_elective_required = rs5.getDouble("credits");
                continue;
            }
            credits_without_open_elective += rs5.getDouble("credits");

            if (creditsTypeCount.containsKey(rs5.getString("type"))) {
                if (creditsTypeCount.get(rs5.getString("type")) < rs5.getDouble("credits")) {
                    return "\nNot enough credits of type " + rs5.getString("type");
                }
            } else {
                return "\nNot enough credits of type " + rs5.getString("type");
            }
        }

        if (total_credits - credits_without_open_elective < open_elective_required) {
            return "\nNot enough credits of type OE";
        }

        return "YES";
    }
}

