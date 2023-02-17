package org.aims.faculty;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

    public String offerCourse(String courseCode, double cgpaCutoff,String[] prerequisites) throws SQLException {
        ResultSet rs1=con.createStatement().executeQuery("SELECT catalog_id FROM courses_catalog WHERE course_code='"+courseCode+"'");
        if(!rs1.next()){
            return "\nCourse Does Not Exist";
        }
        ResultSet rs2=con.createStatement().executeQuery("SELECT * FROM courses_offering WHERE course_code='"+courseCode+"'");
        if(rs2.next()){
            return "\nCourse Already Offered";
        }

        ResultSet rs3=con.createStatement().executeQuery("SELECT * from time_semester WHERE status='ONGOING'");

        if(!rs3.next()){
            return "\nSemester Not Started";
        }

        ResultSet rs4=con.createStatement().executeQuery("SELECT faculty_id FROM faculties WHERE email='"+email+"'");

        if(!rs4.next()){
            return "\nFaculty Does Not Exist";
        }

        for(String s: prerequisites){
            String[] split1 =s.split(",");
            for(String s1:split1){
                String[] split2=s1.split(" ");
                ResultSet rs7=con.createStatement().executeQuery("SELECT * FROM courses_catalog WHERE course_code='"+split2[0]+"'");
                if(!rs7.next()){
                    return "\nPrerequisite Course Does Not Exist";
                }
                if( Integer.parseInt(split2[1]) < 0 || Integer.parseInt(split2[1]) > 10){
                    return "\nInvalid Grade";
                }
            }
        }

        ResultSet rs5=con.createStatement().executeQuery("SELECT INSERT_COURSE_OFFERED('"+rs1.getString("catalog_id")+"','"+rs4.getString("faculty_id")+"','"+courseCode+"',"+cgpaCutoff+")");
        ResultSet rs6=con.createStatement().executeQuery("SELECT * FROM courses_offering WHERE course_code='"+courseCode+"'");

        if(!rs5.next() || !rs6.next()){
            return "Error in Offering the Course";
        }


        int count=0;
        for( String s: prerequisites){
            String[] split1 =s.split(",");
            count+=1;
            for(String s1:split1){
                String[] split2=s1.split(" ");
                String query="INSERT INTO courses_pre_req_offering (\"offering_id\",\"pre_req\",\"grade\",\"type\") VALUES ('"+rs6.getString("offering_id")+"','"+split2[0]+"','"+split2[1]+"','"+count+"')";
                con.createStatement().execute(query);
            }
        }




        con.createStatement().execute("INSERT INTO courses_teaching_faculty_"+rs4.getString("faculty_id")+" VALUES('"+rs1.getString("catalog_id")+"')");

        return "\nCourse Offered Successfully";
    }

    public String takeBackCourse(String courseCode) throws SQLException {
        ResultSet rs1=con.createStatement().executeQuery("SELECT * FROM courses_catalog WHERE course_code='"+courseCode+"'");
        if(!rs1.next()){
            return "\nCourse Does Not Exist";
        }
        ResultSet rs2=con.createStatement().executeQuery("SELECT * FROM courses_offering WHERE course_code='"+courseCode+"'");
        if(!rs2.next()){
            return "\nCourse Not Offered";
        }

        ResultSet rs3=con.createStatement().executeQuery("SELECT faculty_id FROM faculties WHERE email='"+email+"'");

        if(!rs3.next()){
            return "\nFaculty Does Not Exist";
        }

        ResultSet rs5=con.createStatement().executeQuery("SELECT * from time_semester WHERE status='ONGOING'");

        if(!rs5.next()){
            return "\nSemester Has ended";
        }

        if( rs2.getString("faculty_id").equals(rs3.getString("faculty_id"))){
            con.createStatement().execute("DELETE FROM courses_pre_req_offering WHERE offering_id='"+rs2.getString("offering_id")+"'");
            con.createStatement().execute("DELETE FROM courses_offering WHERE offering_id='"+rs2.getString("offering_id")+"'");
            con.createStatement().execute("DELETE FROM courses_teaching_faculty_"+rs3.getString("faculty_id")+" WHERE catalog_id='"+rs1.getString("catalog_id")+"'");

            ResultSet rs6=con.createStatement().executeQuery("SELECT student_id as id FROM students");

            while (rs6.next()){
                con.createStatement().execute("DELETE FROM courses_enrolled_student_"+rs6.getString("id")+" WHERE catalog_id='"+rs1.getString("catalog_id")+"'");
            }

            return "\nCourse Taken Back Successfully";
        }
        else{
            return "\nCourse Not Offered By You";
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
        ResultSet rs1=con.createStatement().executeQuery("SELECT * FROM passwords WHERE email='"+email+"' AND password='"+oldPassword+"' AND role='FACULTY'");
        if (rs1.next()){
            con.createStatement().execute("UPDATE passwords SET password='"+newPassword+"' WHERE email='"+email+"'");
            return "\nPassword Changed to New";
        }
        else {
            return "\nIncorrect Old Password";
        }
    }

    public String updateGrades( String path,String courseCode) throws SQLException {

        ResultSet rs1=con.createStatement().executeQuery(("SELECT catalog_id FROM courses_offering WHERE course_code='"+courseCode+"'"));
        if(!rs1.next()){
            return "\nCourse Not Offered";
        }

        ResultSet rs2=con.createStatement().executeQuery("SELECT faculty_id FROM faculties WHERE email='"+email+"'");
        if(!rs2.next()){
            return "\nFaculty Does Not Exist";
        }

        ResultSet rs3=con.createStatement().executeQuery("SELECT * from courses_teaching_faculty_"+rs2.getString("faculty_id")+" WHERE catalog_id='"+rs1.getString("catalog_id")+"'");
        if(!rs3.next()){
            return "\nCourse Not Offered By You";
        }

        try{
            BufferedReader br;
            br= new BufferedReader(new FileReader(path));
            String line=br.readLine();
            while (line!=null){
                System.out.println(line);
                line=br.readLine();
            }
            br.close();
        }
        catch ( Exception e){
            return "\nInvalid File";
        }



        try{
            BufferedReader br;
            br= new BufferedReader(new FileReader(path));
            String line=br.readLine();

            while(line!=null){
                String[] data=line.split(",");

                if( Integer.parseInt(data[1])>10 || Integer.parseInt(data[1])<0){
                    return "\nInvalid Grade "+ data[1] +" Present";
                }

                ResultSet rs4=con.createStatement().executeQuery("SELECT student_id FROM students WHERE entry_number='"+data[0]+"'");
                if(rs4.next()){
                    String query="SELECT * FROM courses_enrolled_student_"+rs4.getString("student_id")+" WHERE catalog_id="+rs1.getString("catalog_id")+";";
                    ResultSet rs5=con.createStatement().executeQuery(query);
                    if (!rs5.next()){
                        return "\nStudent "+data[0]+" Not Enrolled In Course";
                    }
                }
                else{
                    return "\nInvalid Entry Number Present";
                }
                line=br.readLine();
            }
            br.close();
        }
        catch (Exception e){
            System.out.println(e);
            return "\nFile Does Not Exist1";
        }


        try{
            BufferedReader br;
            br= new BufferedReader(new FileReader(path));
            String line=br.readLine();

            while(line!=null){
                String[] data=line.split(",");
                ResultSet rs4=con.createStatement().executeQuery("SELECT student_id FROM students WHERE entry_number='"+data[0]+"'");
                if(rs4.next()){
                    con.createStatement().execute("UPDATE courses_enrolled_student_"+rs4.getString("student_id")+" SET grade='"+data[1]+"' WHERE catalog_id='"+rs1.getString("catalog_id")+"'");
                }
                line=br.readLine();
            }
            br.close();
        }
        catch (Exception e){
            System.out.println(e);
            return "\nFile Does Not Exist2";
        }

        return "\nGrades Upgraded Successfully";
    }
}
