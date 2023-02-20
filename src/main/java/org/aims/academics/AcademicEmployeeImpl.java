package org.aims.academics;

import org.aims.dataAccess.academicDAO;

import org.aims.dataAccess.userDAL;
import org.postgresql.util.PSQLException;

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

    private final academicDAO academicDAO = new academicDAO();

    public AcademicEmployeeImpl(String Email, String Password) throws SQLException {
        this.email = Email;
        this.password = Password;
        con = DriverManager.getConnection(connectionString, username, databasePassword);
    }


    public boolean login() {
        if (!email.matches("^[a-zA-Z0-9+_.-]+@iitrpr.ac.in"))
            return false;

        if (academicDAO.login(email, password)) {
            academicDAO.loginLogs(email);
            return true;
        } else
            return false;
    } //DONE

    public String logout() {
        academicDAO.logoutLogs(email);
        return "LOGGED OUT SUCCESSFULLY";
    } //DONE


    public String addCourseInCatalog(String courseCode, String courseName, String department,
                                     int lectures, int tutorial, int practicals, int self_study, double credits,
                                     String[] prerequisite) {

        if (academicDAO.checkCourseCatalog(courseCode))
            return "Course Already Exists";

        for (String s : prerequisite) {
            if (s.equals(courseCode))
                continue;
            if (!academicDAO.checkCourseCatalog(s))
                return "Prerequisite Course Does Not Exist";
        }

        if (academicDAO.getdepartmentid(department) == -1)
            return "Department Does Not Exist";


        academicDAO.insertCourseCatalog(courseCode, courseName, department, lectures, tutorial, practicals, self_study, credits);

        for (String s : prerequisite) {
            if (s.equals(courseCode))
                continue;
            academicDAO.insertCoursePre(courseCode, s);
        }
        return "COURSE ADDED IN CATALOG SUCCESSFULLY\n";
    } //DONE

    public String createCurriculum(int batch, String[] courses, String[] credits, String Department) throws PSQLException, SQLException {

        academicDAO.createbatch(batch);

        if (academicDAO.getdepartmentid(Department) == -1)
            return "Department Does Not Exist";

        for (String s : credits) {
            String[] split = s.split(" ");

            if (Double.parseDouble(split[1]) < 0) {
                return "Invalid Credits in " + split[0];
            }
        }

        for (String s : courses) {
            String[] split = s.split(" ");

            if (!academicDAO.checkCourseCatalog(split[0]))
                return "Course Does Not Exist in Catalog " + split[0];

            if (!academicDAO.checkCourseTypes(split[1]))
                return "Invalid Course Type " + split[1];
        }

        for (String s : courses) {
            String[] split = s.split(" ");
            String query = "INSERT INTO batch_curriculum_" + batch + " (\"department_id\",\"catalog_id\",\"type\") VALUES('" + academicDAO.getdepartmentid(Department) + "','" + academicDAO.getCatalogid(split[0]) + "','" + split[1] + "')";

            con.createStatement().execute(query);
        }
        for (String s : credits) {
            String[] split = s.split(" ");
            String query = "INSERT INTO batch_credits_" + batch + " (\"department_id\",\"type\",\"credits\") VALUES('" + academicDAO.getdepartmentid(Department) + "','" + split[0] + "','" + split[1] + "')";
            con.createStatement().execute(query);
        }
        return "Curriculum Created Successfully\n";

    } // DONE

    public String startSemester(int Year, String Semester) {

        if (academicDAO.checkSemesterStatus("ONGOING-GS") || academicDAO.checkSemesterStatus("ONGOING-CO"))
            return "A Semester Already Ongoing\n";

        if (academicDAO.checkSemesterValidity(Semester, Year))
            return "Semester Already Exists\n";

        academicDAO.newSemester(Semester, Year);
        return "Semester Started Successfully\n";
    } // DONE

    public String endSemester() throws PSQLException, SQLException {

        if( academicDAO.checkSemesterStatus("ONGOING-CO"))
            return "Grade Submission Not started\n";

        String[] students=academicDAO.getStudentids();

        for( String s: students){
            if( academicDAO.checkGradeSubmission(s))
                return "Grade Not Submitted for the student "+s;
        }

        for( String s: students){
            academicDAO.updateStudentTranscript(s);
        }

        String[] faculties=academicDAO.getfacultyids();

        for( String s: faculties){
            academicDAO.updateFacultyTranscript(s);
        }

        con.createStatement().executeUpdate("UPDATE time_semester SET status='ENDED' WHERE status!='ENDED'");
        con.createStatement().execute("TRUNCATE TABLE courses_offering CASCADE");

        return "Semester Ended\n";
    } //DONE

    public String[] viewGrades(String email) {
        return academicDAO.viewGrades(email);
    } // DONE

    public String changePassword(String oldPassword, String newPassword) {
        if (newPassword.matches("[\\w]*\\s[\\w]*")) {
            return "\nPassword Cannot Contain Spaces";
        }

        if (academicDAO.checkPassword(email, oldPassword)) {
            academicDAO.changePassword(email, newPassword);
            return "\nPassword Changed Successfully";
        } else
            return "\nIncorrect Old Password";
    } // DONE

    public Map<String, String[]> generateReport() throws PSQLException, SQLException {

        ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM students");

        Map<String, String[]> transcriptAllStudents = new HashMap<String, String[]>();

        while (rs1.next()) {
            String[] grades = viewGrades(rs1.getString("email"));
            transcriptAllStudents.put(rs1.getString("entry_number"), grades);
        }
        return transcriptAllStudents;
    } // No Need for DAO

    public String createCourseTypes(String courseType, String alias) {
        try {
            String query = "INSERT INTO course_types VALUES ('" + courseType + "','" + alias + "')";
            con.createStatement().execute("INSERT INTO course_types VALUES ('" + courseType + "','" + alias + "')");
            return "Course Type Created Successfully\n";
        } catch (SQLException e) {
            return "Course Type Already Exists\n";
        }
    } // No Need for DAO


    public String checkGraduation(String email) throws PSQLException, SQLException {
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
    }  // HAVE TO BE CONVERTED TO DAO


    public String startGradeSubmission() {
        if (!academicDAO.checkSemesterStatus("ONGOING-CO"))
            return "Cannot Start Grade Submission";
        academicDAO.updateSemesterStatus("ONGOING-CO", "ONGOING-GS");
        return "Grade Submission Started";
    } //DONE
}

