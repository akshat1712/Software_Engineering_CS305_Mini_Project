package org.aims;

import org.aims.service.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        String option = "F";
        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("Select [A] for Student");
            System.out.println("Select [B] for Faculty");
            System.out.println("Select [C] for Academic Staff");
            System.out.println("Select [D] for Admin");
            System.out.println("Select [E] to Exit");
            System.out.println("Please enter your choice ");

            try {
                option = sc.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid option");
                sc.nextLine();
                continue;
            }


            if (option.equals("E")) {
                System.out.println("Thank you for using the system");
                break;
            }

            String email;
            String password;

            System.out.println("Please enter your email: ");
            email = sc.nextLine();
            System.out.println("Please enter your password: ");
            password = sc.nextLine();

            if (option.equals("A")) {
                UserService studentService = new StudentService();
                if (studentService.login(email, password)) {
                    System.out.println("Login Successful");
                    studentService.showmenu();
                } else {
                    System.out.println("Login Failed");
                }
            } else if (option.equals("B")) {
                UserService facultyService = new FacultyService();
                if (facultyService.login(email, password)) {
                    System.out.println("Login Successful");
                    facultyService.showmenu();
                } else {
                    System.out.println("Login Failed");
                }
            } else if (option.equals("C")) {
                UserService academicEmployeeService = new AcademicEmployeeService();
                if (academicEmployeeService.login(email, password)) {
                    System.out.println("Login Successful");
                    academicEmployeeService.showmenu();
                } else {
                    System.out.println("Login Failed");
                }
            } else if( option.equals("D")) {
                UserService adminService = new AdminService();
                if (adminService.login(email, password)) {
                    System.out.println("Login Successful");
                    adminService.showmenu();
                } else {
                    System.out.println("Login Failed");
                }
                adminService=null;
            }
            else {
                System.out.println("Invalid option");
            }
        }

    }
}