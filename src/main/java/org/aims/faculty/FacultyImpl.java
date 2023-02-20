package org.aims.faculty;

import org.aims.dataAccess.facultyDAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.ResultSet;

import org.aims.dataAccess.userDAL;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FacultyImpl implements userDAL {

    private final String email;
    private final String password;

    private final Connection con;

    private final String connectionString = "jdbc:postgresql://localhost:5432/postgres";
    private final String username = "postgres";
    private final String databasePassword = "2020csb1068";

    private final facultyDAO facultyDAO = new facultyDAO();

    public FacultyImpl(String Email, String Password) throws PSQLException,SQLException {
        this.email = Email;
        this.password = Password;
        con = DriverManager.getConnection(connectionString, username, databasePassword);
    }


    public boolean login() {
        if( !email.matches("^[a-zA-Z0-9+_.-]+@iitrpr.ac.in"))
            return false;

        if(facultyDAO.login(email,password)){
            facultyDAO.loginLogs(email);
            return true;
        }
        else
            return false;
    }

    public void logout() throws PSQLException,SQLException {
        facultyDAO.logoutLogs(email);
    }

    public String offerCourse(String courseCode, double cgpaCutoff, String[] prerequisites) throws PSQLException,SQLException {
        ResultSet rs1 = con.createStatement().executeQuery("SELECT catalog_id FROM courses_catalog WHERE course_code='" + courseCode + "'");
        if (!rs1.next()) {
            return "\nCourse Does Not Exist";
        }
        ResultSet rs2 = con.createStatement().executeQuery("SELECT * FROM courses_offering WHERE course_code='" + courseCode + "'");
        if (rs2.next()) {
            return "\nCourse Already Offered";
        }

        ResultSet rs3 = con.createStatement().executeQuery("SELECT * from time_semester WHERE status='ONGOING-CO'");

        if (!rs3.next()) {
            return "\nSemester Not Started";
        }

        ResultSet rs4 = con.createStatement().executeQuery("SELECT faculty_id FROM faculties WHERE email='" + email + "'");

        if (!rs4.next()) {
            return "\nFaculty Does Not Exist";
        }

        for (String s : prerequisites) {
            String[] split1 = s.split(",");
            for (String s1 : split1) {
                String[] split2 = s1.split(" ");
                ResultSet rs7 = con.createStatement().executeQuery("SELECT * FROM courses_catalog WHERE course_code='" + split2[0] + "'");
                if (!rs7.next()) {
                    return "\nPrerequisite Course Does Not Exist";
                }
                if (Integer.parseInt(split2[1]) < 0 || Integer.parseInt(split2[1]) > 10) {
                    return "\nInvalid Grade";
                }
            }
        }

        ResultSet rs5 = con.createStatement().executeQuery("SELECT INSERT_COURSE_OFFERED('" + rs1.getString("catalog_id") + "','" + rs4.getString("faculty_id") + "','" + courseCode + "'," + cgpaCutoff + ")");
        ResultSet rs6 = con.createStatement().executeQuery("SELECT * FROM courses_offering WHERE course_code='" + courseCode + "'");

        if (!rs5.next() || !rs6.next()) {
            return "Error in Offering the Course";
        }


        int count = 0;
        for (String s : prerequisites) {
            String[] split1 = s.split(",");
            count += 1;
            for (String s1 : split1) {
                String[] split2 = s1.split(" ");
                String query = "INSERT INTO courses_pre_req_offering (\"offering_id\",\"pre_req\",\"grade\",\"type\") VALUES ('" + rs6.getString("offering_id") + "','" + split2[0] + "','" + split2[1] + "','" + count + "')";
                con.createStatement().execute(query);
            }
        }


        con.createStatement().execute("INSERT INTO courses_teaching_faculty_" + rs4.getString("faculty_id") + " VALUES('" + rs1.getString("catalog_id") + "')");

        return "\nCourse Offered Successfully";
    }

    public String takeBackCourse(String courseCode) throws PSQLException,SQLException {

        if( facultyDAO.checkCourseCatalog(courseCode)){
            return "\nCourse Does Not Exist";
        }

        if( facultyDAO.checkCourseOffering(courseCode)){
            return "\nCourse Not Offered";
        }

        if(!facultyDAO.checkSemesterStatus("ONGOING-CO")){
            return "\nCourse Offering is not Open";
        }

        if( facultyDAO.getfacultyidEmail(email)!=facultyDAO.getfacultyidCourse(courseCode))
            return "\nYou are not the Faculty of this Course";


        facultyDAO.deleteCourseOffering(email,courseCode);


            return "\nCourse Taken Back Successfully";
    }

    public String[] viewGrades(String email) throws PSQLException,SQLException {
        return facultyDAO.viewGrades(email);
    }

    public String changePassword(String oldPassword, String newPassword) throws PSQLException,SQLException {
        if( newPassword.matches("[\\w]*\\s[\\w]*")){
            return "\nPassword Cannot Contain Spaces";
        }

        if(facultyDAO.checkPassword(email,oldPassword)){
            facultyDAO.changePassword(email,newPassword);
            return "\nPassword Changed Successfully";
        }
        else
            return "\nIncorrect Old Password";
    }

    public String updateGrades(String path, String courseCode) throws PSQLException,SQLException {

        ResultSet rs1 = con.createStatement().executeQuery(("SELECT catalog_id FROM courses_offering WHERE course_code='" + courseCode + "'"));
        if (!rs1.next()) {
            return "\nCourse Not Offered";
        }

        ResultSet rs2 = con.createStatement().executeQuery("SELECT faculty_id FROM faculties WHERE email='" + email + "'");
        if (!rs2.next()) {
            return "\nFaculty Does Not Exist";
        }

        ResultSet rs3 = con.createStatement().executeQuery("SELECT * from courses_teaching_faculty_" + rs2.getString("faculty_id") + " WHERE catalog_id='" + rs1.getString("catalog_id") + "'");
        if (!rs3.next()) {
            return "\nCourse Not Offered By You";
        }

        ResultSet rs6= con.createStatement().executeQuery("SELECT * from time_semester WHERE status='ONGOING-GS'");

        if( !rs6.next()){
            return "Grade Submission Not Ongoing";
        }


        try {
            BufferedReader br;
            br = new BufferedReader(new FileReader(path));
            String line = br.readLine();

            while (line != null) {
                String[] data = line.split(",");

                if (Integer.parseInt(data[1]) > 10 || Integer.parseInt(data[1]) < 0) {
                    return "\nInvalid Grade " + data[1] + " Present";
                }

                ResultSet rs4 = con.createStatement().executeQuery("SELECT student_id FROM students WHERE entry_number='" + data[0] + "'");
                if (rs4.next()) {
                    String query = "SELECT * FROM courses_enrolled_student_" + rs4.getString("student_id") + " WHERE catalog_id=" + rs1.getString("catalog_id") + ";";
                    ResultSet rs5 = con.createStatement().executeQuery(query);
                    if (!rs5.next()) {
                        return "\nStudent " + data[0] + " Not Enrolled In Course";
                    }
                } else {
                    return "\nInvalid Entry Number Present";
                }
                line = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e);
            return "\nFile Does Not Exist1";
        }


        try {
            BufferedReader br;
            br = new BufferedReader(new FileReader(path));
            String line = br.readLine();

            while (line != null) {
                String[] data = line.split(",");
                ResultSet rs4 = con.createStatement().executeQuery("SELECT student_id FROM students WHERE entry_number='" + data[0] + "'");
                if (rs4.next()) {
                    con.createStatement().execute("UPDATE courses_enrolled_student_" + rs4.getString("student_id") + " SET grade='" + data[1] + "' WHERE catalog_id='" + rs1.getString("catalog_id") + "'");
                }
                line = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e);
            return "\nFile Does Not Exist2";
        }

        return "\nGrades Upgraded Successfully";
    }
}
