package org.aims.student;

import org.aims.dao.UserDAO;

import javax.management.Query;
import javax.xml.transform.Result;
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
            return "\nPassword Changed to New";
        }
        else {
            return "\nIncorrect Old Password";
        }
    }

    public String registerCourse(String courseCode) throws SQLException{
        ResultSet rs1=con.createStatement().executeQuery("SELECT catalog_id,offering_id,\"CGPA\" FROM courses_offering WHERE course_code='"+courseCode+"'");
        if (!rs1.next()){
            return "\nCourse Not Being Offered Right Now";
        }
        ResultSet rs2=con.createStatement().executeQuery("SELECT student_id FROM students WHERE email='"+email+"'");
        if (!rs2.next()){
            return "\nInvalid Student Email";
        }
        ResultSet rs3=con.createStatement().executeQuery("SELECT * FROM courses_enrolled_student_"+rs2.getString("student_id")+" WHERE catalog_id="+rs1.getString("catalog_id"));
        if (rs3.next()){
            return "\nCourse Already Registered";
        }
        String CGPA=computeCGPA();

        if( Float.parseFloat(CGPA)<Float.parseFloat(rs1.getString("CGPA"))){
            return "\nCGPA is less than the required CGPA";
        }

        ResultSet rs5=con.createStatement().executeQuery("SELECT * FROM transcript_student_"+rs2.getString("student_id")+" WHERE Grade>'3' AND catalog_id="+rs1.getString("catalog_id"));
        if (rs5.next()){
            return "\nCourse Already Taken in Previous Semester";
        }

        // HAVE TO THINK WHETHER WE HAVE TO DO AND OF COURSE OR NOT

        ResultSet rs4=con.createStatement().executeQuery("select P.catalog_id from courses_catalog P , courses_pre_req Q where P.course_code=Q.pre_req AND Q.catalog_id="+rs1.getString("catalog_id"));
        while(rs4.next()){
            String query="Select * from transcript_student_"+rs2.getString("student_id")+" P where P.catalog_id="+rs4.getString("catalog_id");
            ResultSet rs6=con.createStatement().executeQuery(query);
            if( !rs6.next()){
                return "\nYou Do not satify the prerequisite";
            }
        }

        con.createStatement().execute("INSERT INTO courses_enrolled_student_"+rs2.getString("student_id")+" VALUES("+rs1.getString("catalog_id")+")");
        return "\nCourse Registered";
    }

    public String dropCourse(String courseCode) throws SQLException{
        ResultSet rs1=con.createStatement().executeQuery("SELECT catalog_id FROM courses_offering WHERE course_code='"+courseCode+"'");
        if (!rs1.next()){
            return "\nCourse Not Being Offered Right Now";
        }
        ResultSet rs2=con.createStatement().executeQuery("SELECT student_id FROM students WHERE email='"+email+"'");
        if (!rs2.next()){
            return "\nInvalid Student Email";
        }
        ResultSet rs3=con.createStatement().executeQuery("SELECT * FROM courses_enrolled_student_"+rs2.getString("student_id")+" WHERE catalog_id="+rs1.getString("catalog_id"));
        if (!rs3.next()){
            return "\nCourse Not Registered";
        }
        con.createStatement().execute("DELETE FROM courses_enrolled_student_"+rs2.getString("student_id")+" WHERE catalog_id="+rs1.getString("catalog_id"));
        return "\nCourse Dropped";
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

    public String computeCGPA() throws SQLException{
        ResultSet rs1=con.createStatement().executeQuery("SELECT student_id FROM students WHERE email='"+email+"'");

        if(!rs1.next()){
            return "-1";
        }

        String query="select P.credits,Q.grade from courses_catalog P , transcript_student_"+rs1.getString("student_id")+" Q WHERE P.catalog_id=Q.catalog_id";
        ResultSet rs2=con.createStatement().executeQuery(query);

        double credits_earned=0;
        double points_earned=0;

        while (rs2.next()){
            if (rs2.getDouble("grade")<=3){
                continue;
            }
            credits_earned+=rs2.getDouble("credits");
            points_earned+=rs2.getDouble("grade")*rs2.getDouble("credits");
        }

        if(credits_earned!=0){
            return Double.toString(points_earned/credits_earned);
        }
        else{
            return "0.00";
        }
    }

    public String checkGraduation() throws SQLException{
        return "YES";
    }
}

