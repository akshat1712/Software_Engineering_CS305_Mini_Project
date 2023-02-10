package org.aims.service;

import org.aims.admin.AdminImpl;

import java.util.Scanner;

public class AdminService implements UserService{

    private AdminImpl Admin;

    public AdminService( ){

    }
    public void showmenu() {
        System.out.println("Welcome to Admin Menu");

        int option = -1;

        Scanner sc = new Scanner(System.in);
        while (option!=0){
            System.out.println("[0] LOGOUT");
            System.out.println("[1] Add Faculty");
            System.out.println("[2] Add Student");
            System.out.println("[3] Add Academic Staff");
            System.out.println("[4] Add Department");
            System.out.print("Enter your option: ");
            option = sc.nextInt();

            switch (option) {
                case 0:
                    System.out.println("Logging out...");
                    break;
                case 1:
                    AddFaculty();
                    break;
                case 2:
                    AddStudent();
                    break;
                case 3:
                    AddAcademicStaff();
                    break;
                case 4:
                    AddDepartment();
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    public boolean login(String email,String password) {
        try {
            Admin = new AdminImpl(email, password);
            return Admin.login();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean AddFaculty() {
        System.out.println("Welcome to Add Faculty");
        return true;
    }

    private boolean AddStudent() {
        System.out.println("Welcome to Add Student");
        return true;
    }

    private boolean AddAcademicStaff() {
        System.out.println("Welcome to Add Academic Staff");
        return true;
    }

    private boolean AddDepartment() {
        System.out.println("Welcome to Add Department");
        return true;
    }
}
