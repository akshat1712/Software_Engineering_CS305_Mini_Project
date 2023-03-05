package org.aims.service;


import org.aims.userimpl.StudentImpl;

import java.sql.SQLException;
import java.util.Scanner;

public class StudentService implements UserService {
    private StudentImpl Student;

    Scanner sc;
    public StudentService(StudentImpl student) {
        this.Student = student;
    }

    public void showmenu() {
        System.out.println("Welcome to Student Menu");

        String option = "Z";

        this.sc = new Scanner(System.in);
        while (!option.equals("A")) {
            System.out.println("[A] LOGOUT");
            System.out.println("[B] Register Courses");
            System.out.println("[C] De-Register Courses");
            System.out.println("[D] View Grades");
            System.out.println("[E] Computer CGPA");
            System.out.println("[F] Change Password");
            System.out.println("[G] Courses Offering & Enrolled");
            System.out.print("Enter your option: ");
            option = this.sc.nextLine();

            switch (option) {
                case "A" -> logoutService(); // Checking Done
                case "B" -> registerCourseService(); // Checking Done
                case "C" -> deRegisterCourseService(); // Checking Done
                case "D" -> viewGradesService();  // Checking Done
                case "E" -> computeCGPAService(); // Checking Done
                case "F" -> changePasswordService(); // Checking Done
                case "G" -> CoursesEnrolledService(); // Checking Done
                default -> System.out.println("Invalid option");
            }
        }
    }

    public boolean login(String email, String password) {
        return Student.login(email, password);
    }

    public void logoutService() {
        Student.logout();
        System.out.println("Logging Out Successfully");
    }

    private void changePasswordService() {
        System.out.println("Change Password");
        System.out.print("Enter the old password: ");
        String oldPassword = this.sc.nextLine();
        System.out.print("Enter the new password: ");
        String newPassword = this.sc.nextLine();
        String response = Student.changePassword(oldPassword, newPassword);
        System.out.println(response);
    }

    private void registerCourseService() {
        System.out.println("Register Course");
        System.out.print("Enter the course code: ");
        String courseCode = this.sc.nextLine();
        String response = Student.registerCourse(courseCode);
        System.out.println(response);
    }

    private void deRegisterCourseService() {
        System.out.println("De-Register Course");
        System.out.print("Enter the course code: ");
        String courseCode = this.sc.nextLine();
        String response = Student.dropCourse(courseCode);
        System.out.println(response);
    }

    private void viewGradesService() {
        System.out.println("View Grades");
        String[] response = Student.viewGrades();
        if(response == null) {
            System.out.println("No Grades Available");
            return;
        }
        for (String s : response) {
            System.out.println(s);
        }
    }

    private void computeCGPAService() {
        System.out.println("Compute CGPA");
        double response = Student.computeCGPA();
        System.out.println(response);
    }

    private void CoursesEnrolledService() {
        String[] response = Student.viewCoursesEnrolled();
        System.out.println("Enrolled Courses");
        for (String s : response) {
            System.out.println(s);
        }
        System.out.println("=====================================");
        System.out.println("Courses Offering");
        response = Student.viewCoursesOffered();
        for (String s : response) {
            System.out.println(s);
        }
    }
}
