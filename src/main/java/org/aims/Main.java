package org.aims;

import org.aims.service.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int option; // variable to hold the option chosen

        while(true) {

            System.out.println("Select [1] for Student");
            System.out.println("Select [2] for Faculty");
            System.out.println("Select [3] for Academic Staff");
            System.out.println("Select [4] for Admin");
            System.out.println("Select [5] to Exit");
            System.out.print("Please enter your choice: ");
            Scanner sc= new Scanner(System.in);
            option=sc.nextInt();

            if( option==5){
                System.out.println("Thank you for using the system");
                break;
            }
            else if( option<1 || option>5){
                System.out.println("Invalid option");
                continue;
            }

            String email;
            String password;

            System.out.print("Please enter your email: ");
            email=sc.next();
            System.out.print("Please enter your password: ");
            password=sc.next();

            if( option==1){
                UserService studentService = new StudentService();
                if ( studentService.login(email,password)) {
                    System.out.println("Login Successful");
                    studentService.showmenu();
                }
                else {
                    System.out.println("Login Failed");
                }
            }
            else if( option==2){
                UserService facultyService = new FacultyService();
                if ( facultyService.login(email,password)) {
                    System.out.println("Login Successful");
                    facultyService.showmenu();
                }
                else {
                    System.out.println("Login Failed");
                }
            }
            else if( option==3){
                UserService academicEmployeeService = new AcademicEmployeeService();
                if ( academicEmployeeService.login(email,password)) {
                    System.out.println("Login Successful");
                    academicEmployeeService.showmenu();
                }
                else {
                    System.out.println("Login Failed");
                }
            }
            else{
                UserService adminService = new AdminService();
                if ( adminService.login(email,password)) {
                    System.out.println("Login Successful");
                    adminService.showmenu();
                }
                else {
                    System.out.println("Login Failed");
                }
            }
        }

    }
}