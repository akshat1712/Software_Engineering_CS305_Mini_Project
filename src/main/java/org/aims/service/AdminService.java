package org.aims.service;

import org.aims.userimpl.AdminImpl;

import java.sql.SQLException;
import java.util.Scanner;

public class AdminService implements UserService {

    private AdminImpl Admin;
    Scanner sc;
    public AdminService( AdminImpl admin) {
        sc= new Scanner(System.in);
        this.Admin= admin;
    }

    @Override
    public void showmenu() {
        System.out.println("Welcome to Admin Menu");
        String option = "F";
        sc= new Scanner(System.in);
        while (!option.equals("A")) {
            System.out.println("[A] LOGOUT");
            System.out.println("[B] Add Faculty"); // CHECKING DONE PROPERLY
            System.out.println("[C] Add Student"); // CHECKING DONE PROPERLY
            System.out.println("[D] Add Academic Staff"); // CHECKING DONE PROPERLY
            System.out.println("[E] Add Department");    // CHECKING DONE PROPERLY
            System.out.println("Enter your option");
            option = sc.nextLine();
            switch (option) {
                case "A" -> System.out.println("Logging out");
                case "B" -> AddFacultyService(); // Checking Done
                case "C" -> AddStudentService(); // Checking Done
                case "D" -> AddAcademicStaffService(); // Checking Done
                case "E" -> AddDepartmentService(); // Checking Done
                default -> System.out.println("INVALID OPTION");
            }
        }
    }

    @Override
    public boolean login(String email, String password) {
        try {
            return Admin.login(email,password);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void AddFacultyService() {
        System.out.println("Welcome to Add Faculty");

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
        try {
            String response = Admin.AddFaculty(name, email, department, joiningDate, phoneNumber, address);
            System.out.println(response);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in adding faculty");
        }
    }

    private void AddStudentService() {
        System.out.println("Welcome to Add Student");
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
            String response = Admin.AddStudent(name, email, entryNumber, department, batch, phoneNumber, address);
            System.out.println(response);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in adding student");
        }
    }

    private void AddAcademicStaffService() {
        System.out.println("Welcome to Add Academic Staff");

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
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in adding academic staff");
        }
    }

    private void AddDepartmentService() {
        System.out.println("Welcome to Add Department");
        System.out.print("Enter Department Name: ");
        String name = sc.nextLine();
        try {
            String response = Admin.AddDepartment(name);
            System.out.println(response);
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
            System.out.println("Error in adding department");
        }
    }

}
