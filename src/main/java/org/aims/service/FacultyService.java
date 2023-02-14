package org.aims.service;


import org.aims.faculty.FacultyImpl;

import java.util.Scanner;

public class FacultyService  implements UserService{
    private FacultyImpl Faculty;

    public FacultyService( ){

    }

    public void showmenu() {
        System.out.println("Welcome to Faculty Menu");

        int option = -1;

        Scanner sc = new Scanner(System.in);
        while (option!=0){
            System.out.println("[0] LOGOUT");
            System.out.println("[1] Offer Course");
            System.out.println("[2] Take Back course");
            System.out.println("[3] View Grades");
            System.out.println("[4] Change Password");
            System.out.println("[5] Update Grades");
            System.out.print("Enter your option: ");
            option = sc.nextInt();

            switch (option) {
                case 0 -> logoutService();
                case 1 -> offerCourseService();
                case 2 -> takeBackCourseService();
                case 3 -> viewGradesService();
                case 4 -> changePasswordService();
                case 5 -> updateGradesService();
                default -> System.out.println("Invalid option");
            }
        }
    }
    public boolean login(String email,String password) {
        try {
            Faculty = new FacultyImpl(email, password);
            return Faculty.login();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logoutService() {
        try{
              Faculty.logout();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void offerCourseService(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Offer Course");
        System.out.print("Enter the course code: ");
        String courseCode = sc.nextLine();
        System.out.print("Enter the CGPA Cutoff ( Enter -1 if None ): ");
        double cgpaCutoff = sc.nextDouble();
        try {
            String response = Faculty.offerCourse(courseCode, cgpaCutoff);
            System.out.println(response);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to offer course");
        }

    }
    private void takeBackCourseService(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Take Back Course");
        System.out.print("Enter the course code: ");
        String courseCode = sc.nextLine();
        try {
            String response = Faculty.takeBackCourse(courseCode);
            System.out.println(response);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to take back course");
        }
    }
    private void viewGradesService(){
        System.out.println("View Grades");
        System.out.println("Enter the email address of the student");
        Scanner sc = new Scanner(System.in);
        String email = sc.nextLine();
        try {
            String[] response =Faculty.viewGrades(email);
            for (String s : response) {
                System.out.println(s);
            }
        }
        catch (Exception e) {
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
            String response =Faculty.changePassword(oldPassword, newPassword);
            System.out.println(response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateGradesService(){
        System.out.println("Update Grades");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the email address of the student");
        String email = sc.nextLine();
        System.out.println("Enter the course code");
        String courseCode = sc.nextLine();
        System.out.println("Enter the new grade");
        String grade = sc.nextLine();
//        try {
//            String response =Faculty.updateGrades(email, courseCode, grade);
//            System.out.println(response);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
