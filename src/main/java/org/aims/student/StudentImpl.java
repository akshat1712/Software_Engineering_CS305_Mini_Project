package org.aims.student;

import org.aims.dataAccess.userDAL;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentImpl implements userDAL {

    private final String email;
    private final String password;

    private final Connection con;

    private final String connectionString = "jdbc:postgresql://localhost:5432/postgres";
    private final String username = "postgres";
    private final String databasePassword = "2020csb1068";

    public StudentImpl(String Email, String Password) throws SQLException {
        this.email = Email;
        this.password = Password;
        con = DriverManager.getConnection(connectionString, username, databasePassword);
    }

    public boolean login() {

        if( !email.matches("^[a-zA-Z0-9+_.-]+@iitrpr.ac.in"))
            return false;

        try {
            ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='" + email + "' AND password='" + password + "' AND role='STUDENT'");
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

    public void logout() throws PSQLException,SQLException {
        SimpleDateFormat DateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        con.createStatement().execute("UPDATE login_logs SET logout_time='" + DateTime.format(date) + "' WHERE email='" + email + "' AND logout_time='2000-01-01 00:00:00';");
    }

    public String changePassword(String oldPassword, String newPassword) throws PSQLException,SQLException {

        if( newPassword.matches("[\\w]*\\s[\\w]*")){
            return "\nPassword Cannot Contain Spaces";
        }

        ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='" + email + "' AND password='" + oldPassword + "' AND role='STUDENT'");
        if (rs1.next()) {
            con.createStatement().execute("UPDATE passwords SET password='" + newPassword + "' WHERE email='" + email + "'");
            return "\nPassword Changed to New";
        } else {
            return "\nIncorrect Old Password";
        }
    }

    public String registerCourse(String courseCode) throws PSQLException,SQLException {
        ResultSet rs1 = con.createStatement().executeQuery("SELECT catalog_id,offering_id,\"CGPA\" FROM courses_offering WHERE course_code='" + courseCode + "'");
        if (!rs1.next()) {
            return "\nCourse Not Being Offered Right Now";
        }
        ResultSet rs2 = con.createStatement().executeQuery("SELECT student_id FROM students WHERE email='" + email + "'");
        if (!rs2.next()) {
            return "\nInvalid Student Email";
        }
        ResultSet rs3 = con.createStatement().executeQuery("SELECT * FROM courses_enrolled_student_" + rs2.getString("student_id") + " WHERE catalog_id=" + rs1.getString("catalog_id"));
        if (rs3.next()) {
            return "\nCourse Already Registered";
        }
        ResultSet rs10 = con.createStatement().executeQuery("SELECT semester,year FROM time_semester WHERE status='ONGOING-CO'");
        ResultSet rs11 = con.createStatement().executeQuery("select credits from courses_enrolled_student_" + rs2.getString("student_id") + " P , courses_catalog Q WHERE P.catalog_id=Q.catalog_id;");
        ResultSet rs12 = con.createStatement().executeQuery("select credits from courses_catalog where catalog_id=" + rs1.getString("catalog_id"));
        if (!rs10.next()) {
            return "\nSemester Not Started";
        }

        double totalCredits = 0;
        while (rs11.next()) {
            totalCredits += Double.parseDouble(rs11.getString("credits"));
        }


        if (!rs12.next())
            return "\nCourse Not Found";

        totalCredits += Double.parseDouble(rs12.getString("credits"));


        String semester = rs10.getString("semester");
        int year = rs10.getInt("year");

        double prevSemesterCredits = 0;
        if (semester.equals("2")) {
            prevSemesterCredits = credits_earned(year, "1", rs2.getString("student_id")) + credits_earned(year - 1, "2", rs2.getString("student_id"));
        } else {
            prevSemesterCredits = credits_earned(year - 1, "1", rs2.getString("student_id")) + credits_earned(year - 1, "2", rs2.getString("student_id"));
        }

        if (totalCredits > 0.625 * (prevSemesterCredits))
            return "\nCredits Exceeded";

        // here is the end
        String CGPA = computeCGPA();

        if (Float.parseFloat(CGPA) < Float.parseFloat(rs1.getString("CGPA"))) {
            return "\nCGPA is less than the required CGPA";
        }

        ResultSet rs5 = con.createStatement().executeQuery("SELECT * FROM transcript_student_" + rs2.getString("student_id") + " WHERE Grade>'3' AND catalog_id=" + rs1.getString("catalog_id"));
        if (rs5.next()) {
            return "\nCourse Already Taken in Previous Semester";
        }


        ResultSet rs4 = con.createStatement().executeQuery("select P.catalog_id from courses_catalog P , courses_pre_req Q where P.course_code=Q.pre_req AND Q.catalog_id=" + rs1.getString("catalog_id"));
        while (rs4.next()) {
            String query = "Select * from transcript_student_" + rs2.getString("student_id") + " P where P.catalog_id=" + rs4.getString("catalog_id");
            ResultSet rs6 = con.createStatement().executeQuery(query);
            if (!rs6.next()) {
                return "\nYou Do not satify the College prerequisite";
            }
        }

        ResultSet rs7 = con.createStatement().executeQuery("SELECT * FROM courses_pre_req_offering WHERE offering_id=" + rs1.getString("offering_id"));
        while (rs7.next()) {
            String preReq = rs7.getString("pre_req");
            String preReqGrade = rs7.getString("grade");
            ResultSet rs8 = con.createStatement().executeQuery("SELECT * FROM courses_catalog WHERE course_code='" + preReq + "'");
            if (rs8.next()) {
                String query = "Select * from transcript_student_" + rs2.getString("student_id") + " P where P.grade>='" + preReqGrade + "' AND P.catalog_id=" + rs8.getString("catalog_id");
//                System.out.println(query);
                ResultSet rs9 = con.createStatement().executeQuery(query);
                if (!rs9.next()) {
                    return "\nYou Do not satify the Faculty prerequisite";
                }
            }
        }


        con.createStatement().execute("INSERT INTO courses_enrolled_student_" + rs2.getString("student_id") + " VALUES(" + rs1.getString("catalog_id") + ")");
        return "\nCourse Registered";
    }

    public String dropCourse(String courseCode) throws PSQLException,SQLException {
        ResultSet rs1 = con.createStatement().executeQuery("SELECT catalog_id FROM courses_offering WHERE course_code='" + courseCode + "'");
        if (!rs1.next()) {
            return "\nCourse Not Being Offered Right Now";
        }
        ResultSet rs2 = con.createStatement().executeQuery("SELECT student_id FROM students WHERE email='" + email + "'");
        if (!rs2.next()) {
            return "\nInvalid Student Email";
        }
        ResultSet rs3 = con.createStatement().executeQuery("SELECT * FROM courses_enrolled_student_" + rs2.getString("student_id") + " WHERE catalog_id=" + rs1.getString("catalog_id"));
        if (!rs3.next()) {
            return "\nCourse Not Registered";
        }

        ResultSet rs4=con.createStatement().executeQuery("SELECT * FROM time_semester WHERE status='ONGOING-CO'");

        if(!rs4.next())
            return "\nGrade Submission has started, So you cannot drop the course";

        con.createStatement().execute("DELETE FROM courses_enrolled_student_" + rs2.getString("student_id") + " WHERE catalog_id=" + rs1.getString("catalog_id"));
        return "\nCourse Dropped";
    }

    public String[] viewGrades() throws PSQLException,SQLException {
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

    public String computeCGPA() throws PSQLException,SQLException {
        ResultSet rs1 = con.createStatement().executeQuery("SELECT student_id FROM students WHERE email='" + email + "'");

        if (!rs1.next()) {
            return "-1";
        }

        String query = "select P.credits,Q.grade from courses_catalog P , transcript_student_" + rs1.getString("student_id") + " Q WHERE P.catalog_id=Q.catalog_id";
        ResultSet rs2 = con.createStatement().executeQuery(query);

        double credits_earned = 0;
        double points_earned = 0;

        while (rs2.next()) {
            if (rs2.getDouble("grade") <= 3) {
                continue;
            }
            credits_earned += rs2.getDouble("credits");
            points_earned += rs2.getDouble("grade") * rs2.getDouble("credits");
        }

        if (credits_earned != 0) {
            return Double.toString(points_earned / credits_earned);
        } else {
            return "0.00";
        }
    }

    private double credits_earned(int year, String semester, String id) throws PSQLException,SQLException {
        String query = "Select credits from transcript_student_" + id + " P , courses_catalog Q where P.catalog_id=Q.catalog_id AND P.year='" + year + "' AND P.semester='" + semester + "'";
        ResultSet rs1 = con.createStatement().executeQuery(query);

        double credits = 0;

        while (rs1.next()) {
            credits += rs1.getDouble("credits");
        }

        if (credits == 0)
            return 6;
        return credits;
    }

    public String [] viewCoursesEnrolled() throws PSQLException,SQLException{
        ResultSet rs1=con.createStatement().executeQuery("SELECT student_id FROM students WHERE email='"+email+"'");
        if(!rs1.next())
            return null;
        String id=rs1.getString("student_id");
        ResultSet rs2=con.createStatement().executeQuery("SELECT count(*) FROM courses_enrolled_student_"+id);
        int numCourses=0;
        if(rs2.next())
            numCourses=rs2.getInt("count");
        String [] courses=new String[numCourses];
        rs2=con.createStatement().executeQuery("SELECT Q.course_code,credits FROM courses_enrolled_student_"+id+" as P , courses_catalog as Q WHERE P.catalog_id=Q.catalog_id");

        while(rs2.next()){
            courses[rs2.getRow()-1]="Course Code: "+rs2.getString("course_code")+" || Credits: "+rs2.getString("credits");
        }

        return courses;
    }

    public String [] viewCoursesOffered() throws PSQLException,SQLException{
        ResultSet rs1=con.createStatement().executeQuery("SELECT student_id FROM students WHERE email='"+email+"'");
        if(!rs1.next())
            return null;
        String id=rs1.getString("student_id");
        ResultSet rs2=con.createStatement().executeQuery("SELECT count(*) FROM courses_offering");
        int numCourses=0;
        if(rs2.next())
            numCourses=rs2.getInt("count");
        String [] courses=new String[numCourses];
        rs2=con.createStatement().executeQuery("SELECT Q.course_code,credits FROM courses_offering as P , courses_catalog as Q WHERE P.catalog_id=Q.catalog_id");

        while(rs2.next()){
            courses[rs2.getRow()-1]="Course Code: "+rs2.getString("course_code")+" || Credits: "+rs2.getString("credits");
        }

        return courses;
    }
}

