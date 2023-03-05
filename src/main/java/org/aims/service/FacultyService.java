package org.aims.service;
import org.aims.userimpl.FacultyImpl;
import java.sql.SQLException;
import java.util.Scanner;

public class FacultyService implements UserService {
    private FacultyImpl Faculty;
    Scanner sc;
    public FacultyService( FacultyImpl faculty) {
        this.Faculty= faculty;
    }

    public void showmenu() {
        System.out.println("Welcome to Faculty Menu");
        String option = "E";

        this.sc = new Scanner(System.in);
        while (!option.equals("A")) {
            System.out.println("[A] LOGOUT");
            System.out.println("[B] Offer Course");  // Checking Done
            System.out.println("[C] Take Back course");  // Checking Done
            System.out.println("[D] View Grades"); // Checking Done
            System.out.println("[E] Change Password"); // Checking Done
            System.out.println("[F] Update Grades"); //Checking Done
            System.out.print("Enter your option: ");

            option = this.sc.nextLine();

            switch (option) {
                case "A" -> logoutService(); // Checking Done
                case "B" -> offerCourseService(); // Checking Done  ( HAVE TO THINK HOW TO MINIMIZE IT )
                case "C" -> takeBackCourseService(); // Checking Done
                case "D" -> viewGradesService(); // Checking Done
                case "E" -> changePasswordService(); // Checking Done
                case "F" -> updateGradesService(); // Checking Done
                default -> System.out.println("Invalid option");
            }
        }
    }

    public boolean login(String email, String password) {
        return Faculty.login(email, password);
    }

    public void logoutService() {
        Faculty.logout();
        System.out.println("Logged out successfully");
    }

    private void offerCourseService() {
        System.out.println("Offer Course");
        System.out.println("Enter the course code");
        String courseCode = this.sc.nextLine();
        double cgpaCutoff;
        while( true ) {
            System.out.println("Enter the CGPA Cutoff ( Enter -1 if None )");
            try {
                cgpaCutoff = this.sc.nextDouble();
                break;
            } catch (Exception e) {
                System.out.println("Invalid Input");
                this.sc.nextLine();
            }
        }

        int n = -1;
        System.out.println("Enter the prerequisites List");

        while (n < 0) {
            System.out.println("Enter number of lines you want to enter");
            try {
                n = this.sc.nextInt();
            }
            catch (Exception e) {
                System.out.println("Invalid Input");
                n=-1;
                this.sc.nextLine();
                continue;
            }
            if (n < 0) {
                System.out.println("Enter a valid number");
            }
        }

        if (n > 0)
            System.out.println("Enter prerequisites with grades separated by ,");
        String[] prerequisites = new String[n];

        this.sc.nextLine();
        for (int i = 0; i < n; i++) {
            prerequisites[i] = this.sc.nextLine();
        }
        String response = Faculty.offerCourse(courseCode, cgpaCutoff, prerequisites);
        System.out.println(response);

    }

    private void takeBackCourseService() {
        System.out.println("Take Back Course");
        System.out.println("Enter the course code");
        String courseCode = this.sc.nextLine();
        String response = Faculty.takeBackCourse(courseCode);
        System.out.println(response);
    }

    private void viewGradesService() {
        System.out.println("View Grades");
        System.out.println("Enter the email address of the student");
        String email = this.sc.nextLine();
        String[] response = Faculty.viewGrades(email);
        if (response == null) {
            System.out.println("No grades available");
            return;
        }
        for (String s : response) {
            System.out.println(s);
        }
    }

    private void changePasswordService() {
        System.out.println("Change Password");
        System.out.println("Enter the old password");
        String oldPassword = this.sc.nextLine();
        System.out.println("Enter the new password");
        String newPassword = this.sc.nextLine();
        String response = Faculty.changePassword(oldPassword, newPassword);
        System.out.println(response);
    }

    private void updateGradesService() {
        System.out.println("Update Grades");
        String courseCode;
        System.out.println("Enter the course code");
        courseCode = this.sc.nextLine();
        System.out.println("Enter the path of the csv File");
        String path = this.sc.nextLine();
        String response = Faculty.updateGrades(path, courseCode);
        System.out.println(response);
    }
}
