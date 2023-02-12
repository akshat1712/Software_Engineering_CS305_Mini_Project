package org.aims.faculty;
import java.sql.ResultSet;

import org.aims.dao.UserDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FacultyImpl implements UserDAO {

    private String email;
    private String password;

    private Connection con;

    private String connectionString="jdbc:postgresql://localhost:5432/postgres";
    private String username="postgres";
    private String databasePassword="2020csb1068";


    public FacultyImpl(String Email, String Password) throws SQLException {
        this.email = Email;
        this.password = Password;
        con= DriverManager.getConnection(connectionString,username,databasePassword);
    }


    public boolean login()  {
        try {
            ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='" + email + "' AND password='" + password + "' AND type='FACULTY'");
            return rs1.next();
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public String offerCourse(String courseCode, String section, String semester, int year) throws SQLException {
        ResultSet rs1=con.createStatement().executeQuery("SELECT * FROM courses_catalog WHERE course_code='"+courseCode+"'");
        if(!rs1.next()){
            return "Course Does Not Exist";
        }
        ResultSet rs2=con.createStatement().executeQuery("SELECT * FROM courses_offered WHERE course_code='"+courseCode+"' AND section='"+section+"' AND semester='"+semester+"' AND year="+year);
        if(rs2.next()){
            return "Course Already Offered";
        }
        con.createStatement().executeQuery("SELECT INSERT_COURSE_OFFERED('"+courseCode+"','"+section+"','"+semester+"',"+year+")");
        return "Course Offered Successfully";
    }

    public String takeBackCourse(String courseCode, String section, String semester, int year) throws SQLException {
        ResultSet rs1=con.createStatement().executeQuery("SELECT * FROM courses_catalog WHERE course_code='"+courseCode+"'");
        if(!rs1.next()){
            return "Course Does Not Exist";
        }
        ResultSet rs2=con.createStatement().executeQuery("SELECT * FROM courses_offered WHERE course_code='"+courseCode+"' AND section='"+section+"' AND semester='"+semester+"' AND year="+year);
        if(!rs2.next()){
            return "Course Not Offered";
        }
        con.createStatement().executeQuery("SELECT DELETE_COURSE_OFFERED('"+courseCode+"','"+section+"','"+semester+"',"+year+")");
        return "Course Taken Back Successfully";
    }

    public String viewGrades(String email) throws SQLException {
        ResultSet rs1=con.createStatement().executeQuery("SELECT student_id FROM grades WHERE email='"+email+"'");
        if(rs1.next()){
            String id= rs1.getString("student_id");
            ResultSet rs2=con.createStatement().executeQuery("select course_code,grade,semester,year from transcript_student_"+id+" as T ,courses_catalog C WHERE T.catalog_id=C.catalog_id;");
            return "DONE";
        }
        else {
            return "No student exist with this Email-ID";
        }
    }

}
