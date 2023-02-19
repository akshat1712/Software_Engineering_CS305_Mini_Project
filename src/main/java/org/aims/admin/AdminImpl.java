package org.aims.admin;

import org.aims.dataAccess.userDAL;
import org.postgresql.util.PSQLException;


import java.sql.*;

public class AdminImpl implements userDAL {

    private final String Email;
    private final String Password;
    private final Connection con;

    private final String connectionString = "jdbc:postgresql://localhost:5432/postgres";
    private final String username = "postgres";
    private final String databasePassword = "2020csb1068";

    public AdminImpl(String email, String password) throws SQLException {
        this.Email = email;
        this.Password = password;
        con = DriverManager.getConnection(connectionString, username, databasePassword);
    }

    public boolean login() {
        return Email.equals("postgres@iitrpr.ac.in") && Password.equals("2020csb1068");
    }

    public String AddFaculty(String name, String email, String Department, String JoiningDate, String PhoneNumber, String Address) throws PSQLException,SQLException {


        if( !name.matches("^[a-z A-z0-9]+$"))
            return "Name is invalid\n";
        if( !email.matches("^[a-zA-Z0-9+_.-]+@iitrpr.ac.in$"))
            return "Email is invalid\n";
        if( !PhoneNumber.matches("^[0-9]{10}$"))
            return "Phone number is invalid\n";
        if( !JoiningDate.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$"))
            return "Joining date is invalid\n";
        if( name.matches("^[ ]+$"))
            return "Name is invalid\n";
        if( Address.matches("^[ ]+$") )
            return "Address is invalid\n";
        if( !Department.matches("^[a-z A-Z]+"))
            return "Department is invalid\n";


        ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM FACULTIES WHERE EMAIL='" + email + "' OR PHONE_NUMBER='" + PhoneNumber + "';");
        ResultSet rs2 = con.createStatement().executeQuery("SELECT dept_id FROM DEPARTMENTS WHERE NAME='" + Department + "';");
        if (rs1.next())
            return "Faculty already exists\n";
        else if (!rs2.next())
            return "Department does not exist\n";
        else {
            ResultSet rs3 = con.createStatement().executeQuery("SELECT INSERT_FACULTY('" + name + "','" + email + "','" + rs2.getString("dept_id") + "','" + JoiningDate + "','" + PhoneNumber + "','" + Address + "')");
            ResultSet rs4 = con.createStatement().executeQuery("SELECT INSERT_PASSWORD('" + email + "','" + PhoneNumber + "','" + "FACULTY" + "')");
            return "Faculty added successfully\n";
        }
    }

    public String AddStudent(String name, String email, String EntryNumber, String Department, String Batch, String PhoneNumber, String Address) throws PSQLException,SQLException {

        if( !name.matches("^[a-z A-z0-9]+"))
            return "Name is invalid\n";
        if( !email.matches("^[a-zA-Z0-9+_.-]+@iitrpr.ac.in"))
            return "Email is invalid\n";
        if( !EntryNumber.matches("^[0-9]{4}[a-zA-Z]{3}[0-9]{4}$"))
            return "Entry number is invalid\n";
        if( !Department.matches("^[a-z A-Z]+"))
            return "Department is invalid\n";
        if( !PhoneNumber.matches("^[0-9]{10}$"))
            return "Phone number is invalid\n";
        if( !Batch.matches("^[0-9]{4}$"))
            return "Batch is invalid\n";
        if( name.matches("^[ ]+$"))
            return "Name is invalid\n";
        if( Address.matches("^[ ]+$"))
            return "Address is invalid\n";

        ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM STUDENTS WHERE EMAIL='" + email + "' OR PHONE_NUMBER='" + PhoneNumber + "' OR ENTRY_NUMBER='" + EntryNumber + "';");
        ResultSet rs2 = con.createStatement().executeQuery("SELECT dept_id FROM DEPARTMENTS WHERE NAME='" + Department + "';");
        ResultSet rs5 = con.createStatement().executeQuery("SELECT * FROM batch WHERE batch=" + Batch + ";");
        if (rs1.next())
            return "Student already exists\n";
        else if (!rs2.next())
            return "Department does not exist\n";
        else if (!rs5.next())
            return "Batch does not exist\n";
        else {
            ResultSet rs3 = con.createStatement().executeQuery("SELECT INSERT_STUDENT('" + name + "','" + EntryNumber + "','" + email + "','" + rs2.getString("dept_id") + "','" + Batch + "','" + PhoneNumber + "','" + Address + "')");
            ResultSet rs4 = con.createStatement().executeQuery("SELECT INSERT_PASSWORD('" + email + "','" + PhoneNumber + "','" + "STUDENT" + "')");
            return "Student added successfully\n";
        }
    }

    public String AddAcademicStaff(String name, String email, String JoiningDate, String PhoneNumber, String Address) throws PSQLException, SQLException {

        if( !name.matches("^[a-z A-z0-9]+$"))
            return "Name is invalid\n";
        if( !email.matches("^[a-zA-Z0-9+_.-]+@iitrpr.ac.in$"))
            return "Email is invalid\n";
        if( !PhoneNumber.matches("^[0-9]{10}$"))
            return "Phone number is invalid\n";
        if( !JoiningDate.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$"))
            return "Joining date is invalid\n";
        if( name.matches("^[ ]+$"))
            return "Name is invalid\n";
        if( Address.matches("^[ ]+$") )
            return "Address is invalid\n";

        ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM ACAD_EMPL WHERE EMAIL='" + email + "' OR PHONE_NUMBER='" + PhoneNumber + "';");

        if (rs1.next())
            return "Academic staff already exists\n";
        else {
            ResultSet rs2 = con.createStatement().executeQuery("SELECT INSERT_ACADEMIC_EMPLOYEE('" + name + "','" + email + "','" + JoiningDate + "','" + PhoneNumber + "','" + Address + "');");
            ResultSet rs3 = con.createStatement().executeQuery("SELECT INSERT_PASSWORD('" + email + "','" + PhoneNumber + "','" + "ACAD_STAFF" + "')");
            return "Academic staff added successfully\n";
        }
    }

    public String AddDepartment(String Name) throws PSQLException,SQLException {

        if( !Name.matches("^[a-z A-z]+"))
            return "Department name is invalid\n";
        if( Name.matches("^[ ]+$"))
            return "Department name is Empty\n";

        ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM DEPARTMENTS WHERE NAME='" + Name + "';");
        if (rs1.next())
            return "Department already exists\n";
        else {
            ResultSet rs2 = con.createStatement().executeQuery("SELECT INSERT_DEPARTMENT('" + Name + "');");
            return "Department added successfully\n";
        }
    }


}
