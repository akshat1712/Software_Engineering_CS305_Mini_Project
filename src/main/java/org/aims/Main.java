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
            try{
                option=sc.nextInt();
            }
            catch (Exception e){
                System.out.println("\nInvalid option\n");
                continue;
            }


            if( option==5){
                System.out.println("\nThank you for using the system");
                break;
            }
            else if( option<1 || option>5){
                System.out.println("\nInvalid option");
                continue;
            }

            String email;
            String password;

            System.out.print("\nPlease enter your email: ");
            email=sc.next();
            System.out.print("\nPlease enter your password: ");
            password=sc.next();

            if( option==1){
                UserService studentService = new StudentService();
                if ( studentService.login(email,password)) {
                    System.out.println("\nLogin Successful\n");
                    studentService.showmenu();
                }
                else {
                    System.out.println("\nLogin Failed\n");
                }
            }
            else if( option==2){
                UserService facultyService = new FacultyService();
                if ( facultyService.login(email,password)) {
                    System.out.println("\nLogin Successful\n");
                    facultyService.showmenu();
                }
                else {
                    System.out.println("\nLogin Failed\n");
                }
            }
            else if( option==3){
                UserService academicEmployeeService = new AcademicEmployeeService();
                if ( academicEmployeeService.login(email,password)) {
                    System.out.println("\nLogin Successful\n");
                    academicEmployeeService.showmenu();
                }
                else {
                    System.out.println("\nLogin Failed\n");
                }
            }
            else{
                UserService adminService = new AdminService();
                if ( adminService.login(email,password)) {
                    System.out.println("\nLogin Successful");
                    adminService.showmenu();
                }
                else {
                    System.out.println("\nLogin Failed\n");
                }
            }
        }

    }
}