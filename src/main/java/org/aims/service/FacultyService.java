package org.aims.service;


import org.aims.faculty.FacultyImpl;

import java.util.Scanner;

public class FacultyService implements UserService {
    private FacultyImpl Faculty;

    public FacultyService() {

    }

    public void showmenu() {
        System.out.println("Welcome to Faculty Menu");
        System.out.println();
        int option = -1;

        Scanner sc = new Scanner(System.in);
        while (option != 0) {
            System.out.println("\n[0] LOGOUT");
            System.out.println("[1] Offer Course");  // Checking Done
            System.out.println("[2] Take Back course");  // Checking Done
            System.out.println("[3] View Grades"); // Checking Done
            System.out.println("[4] Change Password"); // Checking Done
            System.out.println("[5] Update Grades"); //Checking Done
            System.out.print("Enter your option: ");
            try {
                option = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid option");
                sc.nextLine();
                continue;
            }
            System.out.println();
            switch (option) {
                case 0 -> logoutService(); // Checking Done
                case 1 -> offerCourseService(); // Checking Done  ( HAVE TO THINK HOW TO MINIMIZE IT )
                case 2 -> takeBackCourseService(); // Checking Done
                case 3 -> viewGradesService(); // Checking Done
                case 4 -> changePasswordService(); // Checking Done
                case 5 -> updateGradesService(); // Checking Done
                default -> System.out.println("Invalid option");
            }
        }
    }

    public boolean login(String email, String password) {
        try {
            Faculty = new FacultyImpl(email, password);
            return Faculty.login();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logoutService() {
        try {
            Faculty.logout();
            System.out.println("\nLogged out successfully\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void offerCourseService() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Offer Course");
        System.out.print("Enter the course code: ");
        String courseCode = sc.nextLine();
        System.out.print("Enter the CGPA Cutoff ( Enter -1 if None ): ");
        double cgpaCutoff = sc.nextDouble();

        int n=-1;
        System.out.println("Enter the prerequisites List");

        while(n<0){
            System.out.print("Enter number of lines you want to enter: ");
            n = sc.nextInt();
            if(n<0){
                System.out.println("Invalid Input");
            }
        }
        if(n>0)
            System.out.println("Enter prerequisites with grades separated from other prerequisites by ,");
        String[] prerequisites = new String[n];

        sc.nextLine();
        for (int i = 0; i < n; i++) {
            prerequisites[i] = sc.nextLine();
        }
        System.out.println();
        try {
            String response = Faculty.offerCourse(courseCode, cgpaCutoff, prerequisites);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to offer course");
        }

    }

    private void takeBackCourseService() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Take Back Course");
        System.out.print("Enter the course code: ");
        String courseCode = sc.nextLine();
        System.out.println();
        try {
            String response = Faculty.takeBackCourse(courseCode);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to take back course");
        }
    }

    private void viewGradesService() {
        System.out.println("View Grades");
        System.out.print("Enter the email address of the student: ");
        Scanner sc = new Scanner(System.in);
        String email = sc.nextLine();
        try {
            String[] response = Faculty.viewGrades(email);
            for (String s : response) {
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changePasswordService() {
        System.out.println("Change Password");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the old password: ");
        String oldPassword = sc.nextLine();
        System.out.print("Enter the new password: ");
        String newPassword = sc.nextLine();
        System.out.println();
        try {
            String response = Faculty.changePassword(oldPassword, newPassword);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateGradesService() {
        System.out.println("Update Grades");
        Scanner sc = new Scanner(System.in);
        String courseCode;
        System.out.print("Enter the course code: ");
        courseCode = sc.nextLine();
        System.out.print("Enter the path of the csv File: ");
        String path = sc.nextLine();
        System.out.println();
        try {
            String response = Faculty.updateGrades(path, courseCode);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to update grades");
        }


    }
}
