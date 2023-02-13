package org.aims.faculty;
import java.sql.ResultSet;

import org.aims.dao.UserDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FacultyImpl implements UserDAO {

    private final String email;
    private final String password;

    private final Connection con;

    private final String connectionString="jdbc:postgresql://localhost:5432/postgres";
    private final String username="postgres";
    private final String databasePassword="2020csb1068";


    public FacultyImpl(String Email, String Password) throws SQLException {
        this.email = Email;
        this.password = Password;
        con= DriverManager.getConnection(connectionString,username,databasePassword);
    }


    public boolean login()  {
        try {
            ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='" + email + "' AND password='" + password + "' AND role='FACULTY'");
            if( rs1.next()){
                SimpleDateFormat DateTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date=new Date();
                con.createStatement().execute("INSERT INTO login_logs (\"email\",\"login_time\",\"logout_time\") VALUES ('"+email+"','"+DateTime.format(date)+"','2000-01-01 00:00:00');");
                return true;
            }
            return false;
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean logout() throws SQLException{
        SimpleDateFormat DateTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        con.createStatement().execute("UPDATE login_logs SET logout_time='"+DateTime.format(date)+"' WHERE email='"+email+"' AND logout_time='2000-01-01 00:00:00';");
        return true;
    }

    public String offerCourse(String courseCode, double cgpaCutoff) throws SQLException {
        ResultSet rs1=con.createStatement().executeQuery("SELECT catalog_id FROM courses_catalog WHERE course_code='"+courseCode+"'");
        if(!rs1.next()){
            return "Course Does Not Exist";
        }
        ResultSet rs2=con.createStatement().executeQuery("SELECT * FROM courses_offering WHERE course_code='"+courseCode+"'");
        if(rs2.next()){
            return "Course Already Offered";
        }

        ResultSet rs3=con.createStatement().executeQuery("SELECT faculty_id FROM faculties WHERE email='"+email+"'");

        if(!rs3.next()){
            return "Faculty Does Not Exist";
        }

//        String query="SELECT INSERT_COURSE_OFFERED('"+rs1.getString("catalog_id")+"','"+rs3.getString("faculty_id")+"','"+courseCode+"',"+cgpaCutoff+")";
        ResultSet rs4=con.createStatement().executeQuery("SELECT INSERT_COURSE_OFFERED('"+rs1.getString("catalog_id")+"','"+rs3.getString("faculty_id")+"','"+courseCode+"',"+cgpaCutoff+")");
        if(!rs4.next()){
            return "Error";
        }

        con.createStatement().execute("INSERT INTO courses_teaching_faculty_"+rs3.getString("faculty_id")+" VALUES('"+rs1.getString("catalog_id")+"')");
        return "Course Offered Successfully";
    }

    public String takeBackCourse(String courseCode) throws SQLException {
        ResultSet rs1=con.createStatement().executeQuery("SELECT * FROM courses_catalog WHERE course_code='"+courseCode+"'");
        if(!rs1.next()){
            return "Course Does Not Exist";
        }
        ResultSet rs2=con.createStatement().executeQuery("SELECT * FROM courses_offering WHERE course_code='"+courseCode+"'");
        if(!rs2.next()){
            return "Course Not Offered";
        }

        ResultSet rs3=con.createStatement().executeQuery("SELECT faculty_id FROM faculties WHERE email='"+email+"'");

        if(!rs3.next()){
            return "Faculty Does Not Exist";
        }

        if( rs2.getString("faculty_id").equals(rs3.getString("faculty_id"))){
            con.createStatement().execute("DELETE FROM courses_offering WHERE offering_id='"+rs2.getString("offering_id")+"'");
            con.createStatement().execute("DELETE FROM courses_teaching_faculty_"+rs3.getString("faculty_id")+" WHERE catalog_id='"+rs1.getString("catalog_id")+"'");
            return "Course Taken Back Successfully";
        }
        else{
            return "Course Not Offered By You";
        }



    }

    public String viewGrades(String email) throws SQLException {
        ResultSet rs1=con.createStatement().executeQuery("SELECT student_id FROM students WHERE email='"+email+"'");
        if(rs1.next()){
            String id= rs1.getString("student_id");
            ResultSet rs2=con.createStatement().executeQuery("select course_code,grade,semester,year from transcript_student_"+id+" as T ,courses_catalog C WHERE T.catalog_id=C.catalog_id;");
            return "DONE";
        }
        else {
            return "No student exist with this Email-ID";
        }
    }

    public String changePassword( String oldPassword , String newPassword) throws SQLException{
        ResultSet rs1=con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='"+email+"' AND password='"+oldPassword+"' AND role='FACULTY'");
        if (rs1.next()){
            con.createStatement().execute("UPDATE passwords SET password='"+newPassword+"' WHERE email='"+email+"'");
            return "Password Changed to New";
        }
        else {
            return "Incorrect Old Password";
        }
    }

}
