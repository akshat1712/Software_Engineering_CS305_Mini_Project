package org.aims.academics;
import org.aims.dao.UserDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AcademicEmployeeImpl implements UserDAO {

    private final String email;
    private final String password;

    private final Connection con;

    private final String connectionString="jdbc:postgresql://localhost:5432/postgres";
    private final String username="postgres";
    private final String databasePassword="2020csb1068";
    public AcademicEmployeeImpl(String Email, String Password ) throws SQLException {
        this.email = Email;
        this.password = Password;
        con= DriverManager.getConnection(connectionString,username,databasePassword);
    }


    public boolean login() {
        try {
            ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='" + email + "' AND password='" + password + "' AND role='ACAD_STAFF'");
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

    public String logout() throws SQLException{
            SimpleDateFormat DateTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date=new Date();
            con.createStatement().execute("UPDATE login_logs SET logout_time='"+DateTime.format(date)+"' WHERE email='"+email+"' AND logout_time='2000-01-01 00:00:00';");
            return "Logged Out Successfully\n";
    }

    public String addCourseInCatalog( String courseCode, String courseName,String department,
                                    int lectures,int tutorial, int practicals, int self_study, double credits,
                                    String [] prerequisite) throws SQLException {

        for (String s : prerequisite) {
            if(s.equals(courseCode))
                continue;
            String [] split=s.split(" ");
            if( Integer.parseInt(split[1]) < 0 || Integer.parseInt(split[1]) > 10){
                return "Invalid Grade in "+split[0];
            }

            ResultSet rs1=con.createStatement().executeQuery("SELECT * FROM courses_catalog WHERE course_code='"+split[0]+"'");
            if(!rs1.next()){
                return "Prerequisite Course Does Not Exist";
            }
        }
        ResultSet rs2=con.createStatement().executeQuery("SELECT dept_id FROM departments WHERE name='"+department+"'");
        if(!rs2.next()){
            return "Department Does Not Exist";
        }

//        String query="SELECT INSERT_COURSE_CATALOG('"+courseName+"','"+courseCode+"','"+rs2.getString("dept_id")+"',"+lectures+","+tutorial+","+practicals+","+self_study+","+credits+")";
//        System.out.println(query);

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
        return "COURSE ADDED IN CATALOG SUCCESSFULLY\n";
    }

    public String createCurriculum(int batch,String [] courses,String[] credits,String Department) throws SQLException{

        ResultSet rs1=con.createStatement().executeQuery("SELECT * FROM batch WHERE batch="+batch+"");
        if(!rs1.next()){
            ResultSet rs2=con.createStatement().executeQuery("SELECT BATCH_TABLE_CREATION("+batch+")");
            if (!rs2.next()){
                return "Batch Not Created";
            }
        }

        ResultSet rs3=con.createStatement().executeQuery("SELECT dept_id FROM departments WHERE name='"+Department+"'");
        if(!rs3.next()){
            return "Department Does Not Exist";
        }

        for(String s:credits){
            String[] split =s.split(" ");

            if( Double.parseDouble(split[1]) < 0){
                return "Invalid Credits in "+split[0];
            }
        }

        for (String s:courses){
            String[] split =s.split(" ");
            ResultSet rs4=con.createStatement().executeQuery("SELECT catalog_id FROM courses_catalog WHERE course_code='"+split[0]+"'");
            if(!rs4.next()){
                return "Course Does Not Exist "+split[0];
            }

            ResultSet rs5=con.createStatement().executeQuery("SELECT * FROM course_types WHERE type_alias='"+split[1]+"'");
            if(!rs5.next()){
                return "Invalid Course Type "+split[1];
            }

            String query="INSERT INTO batch_curriculum_"+batch+" (\"department_id\",\"catalog_id\",\"type\") VALUES('"+rs3.getString("dept_id")+"','"+rs4.getString("catalog_id")+"','"+split[1]+"')";
//            System.out.println(query);
            con.createStatement().execute(query);

        }

        for (String s:credits){
            String[] split =s.split(" ");
            ResultSet rs4=con.createStatement().executeQuery("SELECT * FROM course_types WHERE type_alias='"+split[0]+"'");
            if(!rs4.next()){
                return "Invalid Course Type "+split[0];
            }

            String query="INSERT INTO batch_credits_"+batch+" (\"department_id\",\"type\",\"credits\") VALUES('"+rs3.getString("dept_id")+"','"+split[0]+"','"+split[1]+"')";
//            System.out.println(query);
            con.createStatement().execute(query);
        }


        return "Curriculum Created Successfully\n";

    }
    public String startSemester(int Year, String Semester) throws SQLException {
        ResultSet rs1=con.createStatement().executeQuery("SELECT  FROM time_semester WHERE status='ONGOING'");
        if(rs1.next()){
            return "A Semester Already Ongoing\n";
        }
        else {
            con.createStatement().executeUpdate("INSERT INTO time_semester VALUES ('" + Semester + "','" + Year + "','ONGOING')");
            return "Semester Started\n";
        }
    }

    public String endSemester() throws SQLException{
        ResultSet rs1=con.createStatement().executeQuery("SELECT  FROM time_semester WHERE status='ONGOING'");
        if(rs1.next()){
            con.createStatement().executeUpdate("UPDATE time_semester SET status='ENDED' WHERE status='ONGOING'");
            return "Semester Ended\n";
        }
        else {
            return "No Ongoing Semester\n";
        }
    }

    public String[] viewGrades(String email) throws SQLException {
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

    public String changePassword( String oldPassword , String newPassword) throws SQLException{
        ResultSet rs1=con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='"+email+"' AND password='"+oldPassword+"' AND role='ACAD_STAFF'");
        if (rs1.next()){
            con.createStatement().execute("UPDATE passwords SET password='"+newPassword+"' WHERE email='"+email+"'");
            return "Password Changed to New\n";
        }
        else {
            return "Incorrect Old Password\n";
        }
    }

    public String generateReport() throws SQLException{

        ResultSet rs1=con.createStatement().executeQuery("SELECT email FROM students");

        while(rs1.next()){
            String email=rs1.getString("email");

        }

        return "DONE";
    }

    public String createCourseTypes(String courseType,String alias){
        try {
            String query="INSERT INTO course_types VALUES ('"+courseType+"','"+alias+"')";
            System.out.println(query);
            con.createStatement().execute("INSERT INTO course_types VALUES ('"+courseType+"','"+alias+"')");
            return "Course Type Created Successfully\n";
        } catch (SQLException e) {
            return "Course Type Already Exists\n";
        }
    }
}

