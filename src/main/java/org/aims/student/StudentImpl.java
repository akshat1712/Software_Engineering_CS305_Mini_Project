package org.aims.student;

import org.aims.dao.UserDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentImpl implements UserDAO {

    private final String email;
    private final String password;

    private final Connection con;

    private final String connectionString="jdbc:postgresql://localhost:5432/postgres";
    private final String username="postgres";
    private final String databasePassword="2020csb1068";

    public StudentImpl(String Email, String Password) throws SQLException {
        this.email = Email;
        this.password = Password;
        con= DriverManager.getConnection(connectionString,username,databasePassword);
    }

    public boolean login() {
        try {
            ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='" + email + "' AND password='" + password + "' AND role='STUDENT'");
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

    public void logout() throws SQLException{
        SimpleDateFormat DateTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        con.createStatement().execute("UPDATE login_logs SET logout_time='"+DateTime.format(date)+"' WHERE email='"+email+"' AND logout_time='2000-01-01 00:00:00';");
    }

    public String changePassword( String oldPassword , String newPassword) throws SQLException{
        ResultSet rs1=con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='"+email+"' AND password='"+oldPassword+"' AND role='STUDENT'");
        if (rs1.next()){
            con.createStatement().execute("UPDATE passwords SET password='"+newPassword+"' WHERE email='"+email+"'");
            return "Password Changed to New";
        }
        else {
            return "Incorrect Old Password";
        }
    }

    public String registerCourse(String courseCode) throws SQLException{
        ResultSet rs1=con.createStatement().executeQuery("SELECT catalog_id FROM courses_offering WHERE course_code='"+courseCode+"'");
        if (!rs1.next()){
            return "Course Not Being Offered Right Now";
        }
        ResultSet rs2=con.createStatement().executeQuery("SELECT student_id FROM students WHERE email='"+email+"'");
        if (!rs2.next()){
            return "Invalid Student Email";
        }
        ResultSet rs3=con.createStatement().executeQuery("SELECT * FROM courses_enrolled_student_"+rs2.getString("student_id")+" WHERE catalog_id="+rs1.getString("catalog_id"));
        if (rs3.next()){
            return "Course Already Registered";
        }
        con.createStatement().execute("INSERT INTO courses_enrolled_student_"+rs2.getString("student_id")+" VALUES("+rs1.getString("catalog_id")+")");
        return "Course Registered";
    }

    public String dropCourse(String courseCode) throws SQLException{
        ResultSet rs1=con.createStatement().executeQuery("SELECT catalog_id FROM courses_offering WHERE course_code='"+courseCode+"'");
        if (!rs1.next()){
            return "Course Not Being Offered Right Now";
        }
        ResultSet rs2=con.createStatement().executeQuery("SELECT student_id FROM students WHERE email='"+email+"'");
        if (!rs2.next()){
            return "Invalid Student Email";
        }
        ResultSet rs3=con.createStatement().executeQuery("SELECT * FROM courses_enrolled_student_"+rs2.getString("student_id")+" WHERE catalog_id="+rs1.getString("catalog_id"));
        if (!rs3.next()){
            return "Course Not Registered";
        }
        con.createStatement().execute("DELETE FROM courses_enrolled_student_"+rs2.getString("student_id")+" WHERE catalog_id="+rs1.getString("catalog_id"));
        return "Course Dropped";
    }

    public String[] viewGrades() throws SQLException {
        ResultSet rs1=con.createStatement().executeQuery("SELECT student_id FROM students WHERE email='"+email+"'");
        if(rs1.next()){
            String id= rs1.getString("student_id");
            ResultSet rs2=con.createStatement().executeQuery("select count(*) from transcript_student_"+id+" as T ,courses_catalog C WHERE T.catalog_id=C.catalog_id;");
            int numGrades=0;

            if(rs2.next()){
                numGrades=rs2.getInt("count");
            }

            String [] grades=new String[numGrades];

            rs2=con.createStatement().executeQuery("select course_code,grade,semester,year from transcript_student_"+id+" as T ,courses_catalog C WHERE T.catalog_id=C.catalog_id;");

            while(rs2.next()){
                grades[rs2.getRow()-1]="Course Code: "+rs2.getString("course_code")+" || Grade: "+rs2.getString("grade")+" || Semester: "+rs2.getString("semester")+" || Year: "+rs2.getString("year");
            }

            return grades;
        }
        else {
            return null;
        }
    }
}

