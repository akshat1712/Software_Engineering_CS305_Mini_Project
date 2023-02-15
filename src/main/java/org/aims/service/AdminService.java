package org.aims.service;

import org.aims.admin.AdminImpl;

import java.sql.SQLException;
import java.util.Scanner;

public class AdminService implements UserService{

    private AdminImpl Admin;

    public AdminService( ){

    }
    public void showmenu() {
        System.out.println("\nWelcome to Admin Menu\n");

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
                case 0 -> System.out.println("\nLogging out...\n");
                case 1 -> AddFacultyService(); // Checking Done
                case 2 -> AddStudentService(); // Checking Done
                case 3 -> AddAcademicStaffService(); // Checking Done
                case 4 -> AddDepartmentService(); // Checking Done
                default -> System.out.println("\nINVALID OPTION\n");
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
        System.out.println("\nWelcome to Add Faculty\n");
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
        System.out.println();
        try{
            String response = Admin.AddFaculty(name, email, department, joiningDate, phoneNumber, address);
            System.out.println(response);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("\nError in adding faculty\n");
        }
    }

    private void AddStudentService() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nWelcome to Add Student\n");
        System.out.print("Name: ");
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
        System.out.println();
        try {
            String response = Admin.AddStudent( name, email, entryNumber, department, batch, phoneNumber, address);
            System.out.println(response);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in adding student");
        }
    }

    private void AddAcademicStaffService() {
        System.out.println("\nWelcome to Add Academic Staff\n");
        Scanner sc = new Scanner(System.in);

        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Joining Date: ");
        String joiningDate = sc.nextLine();
        System.out.print("Phone Number: ");
        String phoneNumber = sc.nextLine();
        System.out.print("Address: ");
        String address = sc.nextLine();
        System.out.println();
        try {
            String response = Admin.AddAcademicStaff(name, email, joiningDate, phoneNumber, address);
            System.out.println(response);
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in adding academic staff");
        }
    }

    private void AddDepartmentService() {
        System.out.println("\nWelcome to Add Department\n");
        System.out.print("Enter Department Name: ");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        System.out.println();
        try{
            String response=Admin.AddDepartment(name);
            System.out.println(response);
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error in adding department");
        }
    }
}
