package org.aims.academics;
import org.aims.dao.UserDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AcademicEmployeeImpl implements UserDAO {

    private String email;
    private String password;

    private Connection con;

    private String connectionString="jdbc:postgresql://localhost:5432/postgres";
    private String username="postgres";
    private String databasePassword="2020csb1068";
    public AcademicEmployeeImpl(String Email, String Password ) throws SQLException {
        this.email = Email;
        this.password = Password;
        con= DriverManager.getConnection(connectionString,username,databasePassword);
    }


    public boolean login() {
        try {
            ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='" + email + "' AND password='" + password + "' AND role='ACAD_STAFF'");
            return rs1.next();
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public String addCourseInCatalog( String courseCode, String courseName,String department,
                                    int lectures,int tutorial, int practicals, int self_study, int credits,
                                    String [] prerequisite) throws SQLException {

        for (String s : prerequisite) {
            if(s.equals(courseCode))
                continue;
            String [] split=s.split(" ");
            ResultSet rs1=con.createStatement().executeQuery("SELECT * FROM courses_catalog WHERE course_code='"+split[0]+"'");
            if(!rs1.next()){
                return "Prerequisite Course Does Not Exist";
            }
        }
        ResultSet rs2=con.createStatement().executeQuery("SELECT dept_id FROM departments WHERE name='"+department+"'");
        if(!rs2.next()){
            return "Department Does Not Exist";
        }
        con.createStatement().executeQuery("SELECT INSERT_COURSE_CATALOG('"+courseName+"','"+courseCode+"','"+rs2.getString("dept_id")+"',"+lectures+","+tutorial+","+practicals+","+self_study+","+credits+")");

        ResultSet rs3=con.createStatement().executeQuery("SELECT MAX(catalog_id) as id FROM courses_catalog;");

        if (!rs3.next()){
            return "Course Not added Successfully";
        }
        for(String s:prerequisite){
            if(s.equals(courseCode))
                continue;
            String [] split=s.split(" ");
            con.createStatement().execute("INSERT INTO courses_pre_req (\"catalog_id\", \"pre_req\",\"grade\") VALUES ('"+rs3.getString("id")+"','"+split[0]+"','"+split[1]+"')");
    }
        return "ADDED";
    }


    public String startSemester(int Year, String Semester) throws SQLException {
        ResultSet rs1=con.createStatement().executeQuery("SELECT  FROM time_semester WHERE status='ONGOING'");
        if(rs1.next()){
            return "Semester Already Ongoing";
        }
        else {
            con.createStatement().executeUpdate("INSERT INTO time_semester VALUES ('" + Semester + "','" + Year + "','ONGOING')");
            return "Semester Started";
        }
    }

    public String endSemester() throws SQLException{
        ResultSet rs1=con.createStatement().executeQuery("SELECT  FROM time_semester WHERE status='ONGOING'");
        if(rs1.next()){
            con.createStatement().executeUpdate("UPDATE time_semester SET status='ENDED' WHERE status='ONGOING'");
            return "Semester Ended";
        }
        else {
            return "No Ongoing Semester";
        }
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

