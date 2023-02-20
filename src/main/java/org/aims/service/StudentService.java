package org.aims.service;


import org.aims.student.StudentImpl;

import java.sql.SQLException;
import java.util.Scanner;

public class StudentService implements UserService {
    private StudentImpl Student;

    public StudentService() {

    }

    public void showmenu() {
        System.out.println("Welcome to Student Menu");

        int option = -1;

        Scanner sc = new Scanner(System.in);
        while (option != 0) {
            System.out.println("\n[0] LOGOUT");
            System.out.println("[1] Register Courses");
            System.out.println("[2] De-Register Courses");
            System.out.println("[3] View Grades");
            System.out.println("[4] Computer CGPA");
            System.out.println("[5] Change Password");
            System.out.println("[6] Courses Offering & Enrolled");
            System.out.print("Enter your option: ");
            try {
                option = sc.nextInt();
            } catch (Exception e) {
                System.out.println("\nInvalid Option");
                sc.nextLine();
                continue;
            }
            System.out.println();

            switch (option) {
                case 0 -> logoutService(); // Checking Done
                case 1 -> registerCourseService(); // Checking Done
                case 2 -> deRegisterCourseService(); // Checking Done
                case 3 -> viewGradesService();  // Checking Done
                case 4 -> computeCGPAService(); // Checking Done
                case 5 -> changePasswordService(); // Checking Done
                case 6 -> CoursesEnrolledService(); // Checking Done

                default -> System.out.println("Invalid option");
            }
        }
    }

    public boolean login(String email, String password) {
        try {
            Student = new StudentImpl(email, password);
            return Student.login();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logoutService() {
        Student.logout();
        System.out.println("Logging Out Successfully\n");

    }

    private void changePasswordService() {
        System.out.println("Change Password");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the old password: ");
        String oldPassword = sc.nextLine();
        System.out.print("Enter the new password: ");
        String newPassword = sc.nextLine();
        try {
            String response = Student.changePassword(oldPassword, newPassword);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerCourseService() {
        System.out.println("Register Course");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the course code: ");
        String courseCode = sc.nextLine();
        try {
            String response = Student.registerCourse(courseCode);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deRegisterCourseService() {
        System.out.println("De-Register Course");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the course code: ");
        String courseCode = sc.nextLine();
        try {
            String response = Student.dropCourse(courseCode);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewGradesService() {
        System.out.println("View Grades");
        try {
            String[] response = Student.viewGrades();
            for (String s : response) {
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void computeCGPAService() {
        System.out.println("Compute CGPA");
        try {
            double response = Student.computeCGPA();
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CoursesEnrolledService(){
        try{
            String[] response = Student.viewCoursesEnrolled();
            System.out.println("Enrolled Courses");
            System.out.println("=====================================");
            for (String s : response){
                System.out.println(s);
            }
            System.out.println("Courses Offering");
            System.out.println("=====================================");
            response = Student.viewCoursesOffered();
            for (String s : response){
                System.out.println(s);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
