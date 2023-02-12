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
            System.out.print("Enter your option: ");
            option = sc.nextInt();

            switch (option) {
                case 0 -> System.out.println("Logging out");
                case 1 -> offerCourseService();
                case 2 -> takeBackCourseService();
                case 3 -> viewGradesService();
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

    private void offerCourseService(){

    }
    private void takeBackCourseService(){

    }
    private void viewGradesService(){
        System.out.println("View Grades");
        System.out.println("Enter the email address of the student");
        Scanner sc = new Scanner(System.in);
        String email = sc.nextLine();
        try {
            String response =Faculty.viewGrades(email);
            System.out.println(response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
