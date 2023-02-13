package org.aims.admin;

import org.aims.dao.UserDAO;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminImpl implements UserDAO {

        private final String Email;
        private final String Password;
        private final Connection con;

        private final String connectionString="jdbc:postgresql://localhost:5432/postgres";
        private final String username="postgres";
        private final String databasePassword="2020csb1068";
        public AdminImpl(String email, String password) throws SQLException {
            this.Email= email;
            this.Password= password;
            con= DriverManager.getConnection(connectionString,username,databasePassword);
        }

        public boolean login() {
            return Email.equals("postgres@iitrpr.ac.in") && Password.equals("2020csb1068");
        }

        public void AddFaculty( String name, String email,String Department, String JoiningDate, String PhoneNumber, String Address) throws SQLException {
            ResultSet rs1=con.createStatement().executeQuery("SELECT * FROM FACULTIES WHERE EMAIL='"+email+"' OR PHONE_NUMBER='"+PhoneNumber+"';");
            ResultSet rs2=con.createStatement().executeQuery("SELECT dept_id FROM DEPARTMENTS WHERE NAME='"+Department+"';");
            if(rs1.next())
                System.out.println("Faculty already exists");
            else if(!rs2.next())
                System.out.println("Department does not exist");
            else {
                ResultSet rs3 = con.createStatement().executeQuery("SELECT INSERT_FACULTY('" + name + "','" + email + "','" + rs2.getString("dept_id") + "','" + JoiningDate + "','" + PhoneNumber + "','" + Address + "')");
                ResultSet rs4 = con.createStatement().executeQuery("SELECT INSERT_PASSWORD('" + email + "','" + PhoneNumber + "','" + "FACULTY" + "')");
            }
        }
        public void AddStudent( String name, String email,String EntryNumber,String Department,String Batch, String PhoneNumber,String Address) throws SQLException {
            ResultSet rs1=con.createStatement().executeQuery("SELECT * FROM STUDENTS WHERE EMAIL='"+email+"' OR PHONE_NUMBER='"+PhoneNumber+"' OR ENTRY_NUMBER='"+EntryNumber+"';");
            ResultSet rs2=con.createStatement().executeQuery("SELECT dept_id FROM DEPARTMENTS WHERE NAME='"+Department+"';");

            if(rs1.next())
                System.out.println("Student already exists");
            else if(!rs2.next())
                System.out.println("Department does not exist");
            else {
                ResultSet rs3 = con.createStatement().executeQuery("SELECT INSERT_STUDENT('" + name + "','" + EntryNumber + "','" + email + "','" + rs2.getString("dept_id") + "','" + Batch + "','" + PhoneNumber + "','" + Address + "')");
                ResultSet rs4 = con.createStatement().executeQuery("SELECT INSERT_PASSWORD('" + email + "','" + PhoneNumber + "','" + "STUDENT" + "')");
            }
        }

        public void AddAcademicStaff( String name, String email, String JoiningDate, String PhoneNumber, String Address) throws SQLException{
            ResultSet rs1=con.createStatement().executeQuery("SELECT * FROM ACAD_EMPL WHERE EMAIL='"+email+"' OR PHONE_NUMBER='"+PhoneNumber+"';");
            if (rs1.next())
                System.out.println("Academic Staff already exists");
            else{
                ResultSet rs2=con.createStatement().executeQuery("SELECT INSERT_ACADEMIC_EMPLOYEE('"+name+"','"+email+"','"+JoiningDate+"','"+PhoneNumber+"','"+Address+"');");
                ResultSet rs3 = con.createStatement().executeQuery("SELECT INSERT_PASSWORD('" + email + "','" + PhoneNumber + "','" + "ACAD_STAFF" + "')");
            }
        }

        public void AddDepartment(String Name) throws SQLException{
            ResultSet rs1=con.createStatement().executeQuery("SELECT * FROM DEPARTMENTS WHERE NAME='"+Name+"';");
            if (rs1.next())
                System.out.println("Department already exists");
            else{
                ResultSet rs2=con.createStatement().executeQuery("SELECT INSERT_DEPARTMENT('"+Name+"');");
            }

        }


}
