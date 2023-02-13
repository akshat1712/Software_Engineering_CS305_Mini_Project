package org.aims.service;

import org.aims.academics.AcademicEmployeeImpl;


import java.util.Scanner;

public class AcademicEmployeeService implements UserService {
    private AcademicEmployeeImpl AcademicEmployee;

    public AcademicEmployeeService() {

    }

    public void showmenu() {
        System.out.println("Welcome to Academic Staff Menu");

        int option = -1;

        Scanner sc = new Scanner(System.in);

        while (option != 0) {
            System.out.println("[0] LOGOUT");
            System.out.println("[1] Add Course in Catalog");
            System.out.println("[2] Start Semester");
            System.out.println("[3] End Semester");
            System.out.println("[4] View Grades");
            System.out.println("[5] Create Batch");
            System.out.println("[6] Change Password");
            System.out.print("Enter your option: ");
            option = sc.nextInt();

            switch (option) {
                case 0 -> logoutService();
                case 1 -> addCourseInCatalogService();
                case 2 -> startSemesterService();
                case 3 -> endSemesterService();
                case 4 -> viewGradesService();
                case 5 -> createBatchService();
                case 6 -> changePasswordService();
                default -> System.out.println("Invalid option");
            }
        }
    }

    public boolean login(String email, String password) {
        try {
            AcademicEmployee = new AcademicEmployeeImpl(email, password);
            return AcademicEmployee.login();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logoutService() {
        try{
              AcademicEmployee.logout();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addCourseInCatalogService() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Add Course in Catalog");
            System.out.print("Enter Course Code: ");
            String courseCode = sc.nextLine();
            System.out.print("Enter Course Name: ");
            String courseName = sc.nextLine();
            System.out.print("Enter Course Department: ");
            String courseDepartment = sc.nextLine();
            System.out.print("Enter Course Lectures Hours: ");
            int Lectures = sc.nextInt();
            System.out.print("Enter Course Tutorials Hours: ");
            int Tutorials = sc.nextInt();
            System.out.print("Enter Course Practical Hours: ");
            int Practical = sc.nextInt();
            System.out.print("Enter Course Self-Study Hours: ");
            int SelfStudy = sc.nextInt();
            System.out.print("Enter Course Credits: ");
            int Credits = sc.nextInt();
            System.out.print("Enter Number of Pre-Requisite: ");
            int preRequisite = sc.nextInt();
            String[] preRequisiteList = new String[preRequisite+1];
            if (preRequisite > 0)
                System.out.println("Enter Pre-Requisite Course Code with Grade Cutoff, each in new line: ");
            preRequisiteList[0]=courseCode;
            for (int i = 1; i < preRequisite+1; i++) {
                String data = sc.nextLine();
                if(data.equals(""))
                    data = sc.nextLine();
                preRequisiteList[i] = data;
            }

            String response = AcademicEmployee.addCourseInCatalog(courseCode, courseName, courseDepartment, Lectures, Tutorials, Practical, SelfStudy, Credits, preRequisiteList);
            System.out.println(response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startSemesterService() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Semester Number: ");
        String semesterNumber = sc.nextLine();
        System.out.println("Enter the Semester Year: ");
        int semesterYear = sc.nextInt();
        try {
            String response = AcademicEmployee.startSemester(semesterYear, semesterNumber);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void endSemesterService() {
        try {
            String response =AcademicEmployee.endSemester();
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewGradesService(){
        System.out.println("View Grades");
        System.out.println("Enter the email address of the student");
        Scanner sc = new Scanner(System.in);
        String email = sc.nextLine();
        try {
            String response =AcademicEmployee.viewGrades(email);
            System.out.println(response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createBatchService(){
        System.out.println("Create Batch");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the batch number: ");
        int batchNumber = sc.nextInt();
        try {
            String response =AcademicEmployee.CreateBatch(batchNumber);
            System.out.println(response);
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
            String response =AcademicEmployee.changePassword(oldPassword, newPassword);
            System.out.println(response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}