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
            System.out.println("[1] Register Courses");
            System.out.println("[2] De-Register Courses");
            System.out.println("[3] View Grades");
            System.out.println("[4] Computer CGPA");
            System.out.println("[5] Change Password");
            System.out.print("Enter your option: ");
            option = sc.nextInt();

            switch (option) {
                case 0 -> logoutService();
                case 1 -> registerCourseService();
                case 2 -> deRegisterCourseService();
//                case 3 -> viewGradesService();
//                case 4 -> computeCGPAService();
                case 5 -> changePasswordService();

                default -> System.out.println("Invalid option");
            }
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

    public void logoutService() {
        try{
            Student.logout();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void changePasswordService(){
        System.out.println("Change Password");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the old password: ");
        String oldPassword = sc.nextLine();
        System.out.println("Enter the new password: ");
        String newPassword = sc.nextLine();
        try {
            String response =Student.changePassword(oldPassword, newPassword);
            System.out.println(response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerCourseService(){
        System.out.println("Register Course");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the course code: ");
        String courseCode = sc.nextLine();
        try {
            String response =Student.registerCourse(courseCode);
            System.out.println(response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deRegisterCourseService(){
        System.out.println("De-Register Course");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the course code: ");
        String courseCode = sc.nextLine();
        try {
            String response =Student.dropCourse(courseCode);
            System.out.println(response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
