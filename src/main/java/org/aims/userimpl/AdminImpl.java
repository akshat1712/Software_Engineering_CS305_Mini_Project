package org.aims.userimpl;

import org.postgresql.util.PSQLException;


import java.sql.*;
import java.util.ResourceBundle;

public class AdminImpl implements userDAL {

    private String Email;
    private String Password;
    private final Connection con;

    static ResourceBundle rd = ResourceBundle.getBundle("config");
    static String data_base_url = rd.getString("data_base_url");
    static String username = rd.getString("username");
    static String password = rd.getString("password");

    public AdminImpl() throws SQLException {
        con = DriverManager.getConnection(data_base_url, username, password);
    }

    public boolean login(String email,String password) {
        this.Email = email;
        this.Password = password;
        return Email.equals("postgres@iitrpr.ac.in") && Password.equals("2020csb1068");
    }

    public String AddFaculty(String name, String email, String Department, String JoiningDate, String PhoneNumber, String Address) throws PSQLException, SQLException {

        if (!name.matches("^[a-z A-z0-9]+$"))
            return "Name is invalid";
        if (!email.matches("^[a-zA-Z0-9+_.-]+@iitrpr.ac.in$"))
            return "Email is invalid";
        if (!PhoneNumber.matches("^[0-9]{10}$"))
            return "Phone number is invalid";
        if (!JoiningDate.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$"))
            return "Joining date is invalid";
        if (name.matches("^[ ]+$"))
            return "Name is invalid";
        if (Address.matches("^[ ]+$"))
            return "Address is invalid";
        if (!Department.matches("^[a-z A-Z]+"))
            return "Department is invalid";


        ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM FACULTIES WHERE EMAIL='" + email + "' OR PHONE_NUMBER='" + PhoneNumber + "';");
        ResultSet rs2 = con.createStatement().executeQuery("SELECT dept_id FROM DEPARTMENTS WHERE NAME='" + Department + "';");
        if (rs1.next())
            return "Faculty already exists";
        else if (!rs2.next())
            return "Department does not exist";
        else {
            ResultSet rs3 = con.createStatement().executeQuery("SELECT INSERT_FACULTY('" + name + "','" + email + "','" + rs2.getString("dept_id") + "','" + JoiningDate + "','" + PhoneNumber + "','" + Address + "')");
            ResultSet rs4 = con.createStatement().executeQuery("SELECT INSERT_PASSWORD('" + email + "','" + PhoneNumber + "','" + "FACULTY" + "')");
            return "Faculty added successfully";
        }
    }

    public String AddStudent(String name, String email, String EntryNumber, String Department, String Batch, String PhoneNumber, String Address) throws PSQLException, SQLException {

        if (!name.matches("^[a-z A-z0-9]+"))
            return "Name is invalid";
        if (!email.matches("^[a-zA-Z0-9+_.-]+@iitrpr.ac.in"))
            return "Email is invalid";
        if (!EntryNumber.matches("^[0-9]{4}[a-zA-Z]{3}[0-9]{4}$"))
            return "Entry number is invalid";
        if (!Department.matches("^[a-z A-Z]+"))
            return "Department is invalid";
        if (!PhoneNumber.matches("^[0-9]{10}$"))
            return "Phone number is invalid";
        if (!Batch.matches("^[0-9]{4}$"))
            return "Batch is invalid";
        if (name.matches("^[ ]+$"))
            return "Name is invalid";
        if (Address.matches("^[ ]+$"))
            return "Address is invalid";

        ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM STUDENTS WHERE EMAIL='" + email + "' OR PHONE_NUMBER='" + PhoneNumber + "' OR ENTRY_NUMBER='" + EntryNumber + "';");
        ResultSet rs2 = con.createStatement().executeQuery("SELECT dept_id FROM DEPARTMENTS WHERE NAME='" + Department + "';");
        ResultSet rs5 = con.createStatement().executeQuery("SELECT * FROM batch WHERE batch=" + Batch + ";");
        if (rs1.next())
            return "Student already exists";
        else if (!rs2.next())
            return "Department does not exist";
        else if (!rs5.next())
            return "Batch does not exist";
        else {
            ResultSet rs3 = con.createStatement().executeQuery("SELECT INSERT_STUDENT('" + name + "','" + EntryNumber + "','" + email + "','" + rs2.getString("dept_id") + "','" + Batch + "','" + PhoneNumber + "','" + Address + "')");
            ResultSet rs4 = con.createStatement().executeQuery("SELECT INSERT_PASSWORD('" + email + "','" + PhoneNumber + "','" + "STUDENT" + "')");
            return "Student added successfully";
        }
    }

    public String AddAcademicStaff(String name, String email, String JoiningDate, String PhoneNumber, String Address) throws PSQLException, SQLException {

        if (!name.matches("^[a-z A-z0-9]+$"))
            return "Name is invalid";
        if (!email.matches("^[a-zA-Z0-9+_.-]+@iitrpr.ac.in$"))
            return "Email is invalid";
        if (!PhoneNumber.matches("^[0-9]{10}$"))
            return "Phone number is invalid";
        if (!JoiningDate.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$"))
            return "Joining date is invalid";
        if (name.matches("^[ ]+$"))
            return "Name is invalid";
        if (Address.matches("^[ ]+$"))
            return "Address is invalid";

        ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM ACAD_EMPL WHERE EMAIL='" + email + "' OR PHONE_NUMBER='" + PhoneNumber + "';");

        if (rs1.next())
            return "Academic staff already exists";
        else {
            ResultSet rs2 = con.createStatement().executeQuery("SELECT INSERT_ACADEMIC_EMPLOYEE('" + name + "','" + email + "','" + JoiningDate + "','" + PhoneNumber + "','" + Address + "');");
            ResultSet rs3 = con.createStatement().executeQuery("SELECT INSERT_PASSWORD('" + email + "','" + PhoneNumber + "','" + "ACAD_STAFF" + "')");
            return "Academic staff added successfully";
        }
    }

    public String AddDepartment(String Name) throws PSQLException, SQLException {

        if (!Name.matches("^[a-z A-z]+"))
            return "Department name is invalid";
        if (Name.matches("^[ ]+$"))
            return "Department name is Empty";

        ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM DEPARTMENTS WHERE NAME='" + Name + "';");
        if (rs1.next())
            return "Department already exists";
        else {
            ResultSet rs2 = con.createStatement().executeQuery("SELECT INSERT_DEPARTMENT('" + Name + "');");
            return "Department added successfully";
        }
    }


}
