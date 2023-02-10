package org.aims.service;


import org.aims.student.StudentImpl;

import java.sql.SQLException;
import java.util.Scanner;

public class StudentService implements UserService {
    private StudentImpl Student;

    public StudentService(){

    }

    public void showmenu() {
        System.out.println("Welcome to Student Menu");

        int option = -1;

        Scanner sc = new Scanner(System.in);
        while (option!=0){
            System.out.println("[0] LOGOUT");
            System.out.println("[1] View Profile");
            System.out.println("[2] Update Profile");
            System.out.println("[3] View Courses");
            System.out.println("[4] View Grades");
            System.out.println("[5] Register Courses");
            System.out.print("Enter your option: ");
            option = sc.nextInt();
        }
    }
    public boolean login(String email,String password) {
        try {
            Student = new StudentImpl(email, password);
            return Student.login();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
