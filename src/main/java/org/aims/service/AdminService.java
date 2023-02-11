package org.aims.service;

import org.aims.admin.AdminImpl;

import java.sql.SQLException;
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
                case 0 -> System.out.println("Logging out...");
                case 1 -> AddFacultyService();
                case 2 -> AddStudentService();
                case 3 -> AddAcademicStaffService();
                case 4 -> AddDepartmentService();
                default -> System.out.println("Invalid option");
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

    private void AddFacultyService() {
        System.out.println("Welcome to Add Faculty");
        Scanner sc = new Scanner(System.in);

        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Department: ");
        String department = sc.nextLine();
        System.out.print("Joining Date: ");
        String joiningDate = sc.nextLine();
        System.out.print("Phone Number: ");
        String phoneNumber = sc.nextLine();
        System.out.print("Address: ");
        String address = sc.nextLine();
        try{
            Admin.AddFaculty(name, email, department, joiningDate, phoneNumber, address);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in adding faculty");
        }
    }

    private void AddStudentService() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Welcome to Add Student");
        System.out.println("Name: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Entry Number: ");
        String entryNumber = sc.nextLine();
        System.out.print("Department: ");
        String department = sc.nextLine();
        System.out.print("Batch: ");
        String batch = sc.nextLine();
        System.out.print("Phone Number: ");
        String phoneNumber = sc.nextLine();
        System.out.print("Address: ");
        String address = sc.nextLine();
        try {
            Admin.AddStudent( name, email, entryNumber, department, batch, phoneNumber, address);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in adding student");
        }
    }

    private void AddAcademicStaffService() {
        System.out.println("Welcome to Add Academic Staff");
        Scanner sc = new Scanner(System.in);

        System.out.println("Name: ");
        String name = sc.nextLine();
        System.out.println("Email: ");
        String email = sc.nextLine();
        System.out.println("Joining Date: ");
        String joiningDate = sc.nextLine();
        System.out.print("Phone Number: ");
        String phoneNumber = sc.nextLine();
        System.out.print("Address: ");
        String address = sc.nextLine();

        try {
            Admin.AddAcademicStaff(name, email, joiningDate, phoneNumber, address);
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in adding academic staff");
        }
    }

    private void AddDepartmentService() {
        System.out.println("Welcome to Add Department");
        System.out.print("Enter Department Name: ");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        try{
            Admin.AddDepartment(name);
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error in adding department");
        }
    }
}
