package org.aims.student;

import org.aims.dao.UserDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class StudentImpl implements UserDAO {

    private String email;
    private String password;

    private Connection con;

    private String connectionString="jdbc:postgresql://localhost:5432/postgres";
    private String username="postgres";
    private String databasePassword="2020csb1068";

    public StudentImpl(String Email, String Password) throws SQLException {
        this.email = Email;
        this.password = Password;
        con= DriverManager.getConnection(connectionString,username,databasePassword);
    }
    public void showmenu() {
        System.out.println("Welcome to Student Menu");
        showprofile();
        changeprofile();
    }
    public void showprofile() {
        System.out.println("Welcome to Student Profile");
    }
    public void changeprofile() {
        System.out.println("Welcome to Student Change Profile");
    }

    public boolean login() {
        System.out.println("Welcome to Student Login");
        return true;
    }
}

