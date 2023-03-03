package org.aims.service;

import org.aims.userimpl.AdminImpl;
import java.sql.SQLException;
import java.util.Scanner;


public class AdminService implements UserService {

    private AdminImpl Admin;
    Scanner sc;
    public AdminService() {
        sc= new Scanner(System.in);
    }

    @Override
    public void showmenu() {
        Scanner sc1= new Scanner(System.in);
        System.out.println("Welcome to Admin Menu");
        String option = "F";
        while (!option.equals("A")) {
            System.out.println("[A] LOGOUT");
            System.out.println("[B] Add Faculty"); // CHECKING DONE PROPERLY
            System.out.println("[C] Add Student"); // CHECKING DONE PROPERLY
            System.out.println("[D] Add Academic Staff"); // CHECKING DONE PROPERLY
            System.out.println("[E] Add Department");    // CHECKING DONE PROPERLY
            System.out.println("Enter your option");
            try {
                option = sc1.nextLine();
                System.out.println(option);
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("INVALID OPTION");
                sc1.nextLine();
                continue;
            }
            switch (option) {
                case "A" -> System.out.println("Logging out");
                case "B" -> AddFacultyService(); // Checking Done
                case "C" -> AddStudentService(); // Checking Done
                case "D" -> AddAcademicStaffService(); // Checking Done
                case "E" -> AddDepartmentService(sc1); // Checking Done
                default -> System.out.println("INVALID OPTION");
            }
        }
    }

    @Override
    public boolean login(String email, String password) {
        try {
            Admin = new AdminImpl(email, password);
            return Admin.login();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void AddFacultyService() {
        System.out.println("\nWelcome to Add Faculty\n");

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
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\nError in adding faculty\n");
        }
    }

    private void AddStudentService() {
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
            String response = Admin.AddStudent(name, email, entryNumber, department, batch, phoneNumber, address);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in adding student");
        }
    }

    private void AddAcademicStaffService() {
        System.out.println("\nWelcome to Add Academic Staff\n");

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

    private void AddDepartmentService(Scanner sc1) {
        System.out.println("Welcome to Add Department");
        System.out.print("Enter Department Name: ");
        String name = sc1.nextLine();
        try {
            String response = Admin.AddDepartment(name);
            System.out.println(response);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            System.out.println("Error in adding department");
        }
    }

}
