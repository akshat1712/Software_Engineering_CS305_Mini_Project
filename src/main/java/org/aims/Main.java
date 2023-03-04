package org.aims;

import org.aims.dataAccess.facultyDAO;
import org.aims.service.*;
import org.aims.userimpl.AdminImpl;
import org.aims.userimpl.FacultyImpl;
import org.aims.userimpl.userDAL;

import java.util.Scanner;

public class Main {

    private static void studentMain(String email, String password){
        UserService studentService = new StudentService();
        if (studentService.login(email, password)) {
            System.out.println("Login Successful");
            studentService.showmenu();
        } else {
            System.out.println("Login Failed");
        }
    }

    private static void facultyMain(String email, String password){
        // MY CODE
        FacultyImpl faculty;
        facultyDAO facultyDAO;

        try {
            facultyDAO = new facultyDAO();
            faculty = new FacultyImpl(facultyDAO);
        } catch (Exception e) {
            System.out.println("Error in connecting to database");
            return;
        }
        UserService facultyService = new FacultyService(faculty);
        // MY CODE
//        UserService facultyService = new FacultyService();

        if (facultyService.login(email, password)) {
            System.out.println("Login Successful");
            facultyService.showmenu();
        } else {
            System.out.println("Login Failed");
        }
    }

    private static void acadMain(String email, String password){
        UserService academicEmployeeService = new AcademicEmployeeService();
        if (academicEmployeeService.login(email, password)) {
            System.out.println("Login Successful");
            academicEmployeeService.showmenu();
        } else {
            System.out.println("Login Failed");
        }
    }

    private static void adminMain(String email, String password){

        AdminImpl adminImpl;
        try{
            adminImpl = new AdminImpl();
        }catch (Exception e){
            System.out.println("Error in connecting to database");
            return;
        }
        UserService adminService = new AdminService(adminImpl);

        if (adminService.login(email, password)) {
            System.out.println("Login Successful");
            adminService.showmenu();
        } else {
            System.out.println("Login Failed");
        }
        adminService=null;
    }

    public static void main(String[] args) {
        String option = "F";
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Select [A] for Student");
            System.out.println("Select [B] for Faculty");
            System.out.println("Select [C] for Academic Staff");
            System.out.println("Select [D] for Admin");
            System.out.println("Select [E] to Exit");
            System.out.println("Please enter your choice");
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
            System.out.println("Please enter your email");
            String email = sc.nextLine();
            System.out.println("Please enter your password");
            String password = sc.nextLine();
            if (option.equals("A")) {
                studentMain(email,password);
            } else if (option.equals("B")) {
                facultyMain(email,password);
            } else if (option.equals("C")) {
                acadMain(email,password);
            } else if( option.equals("D")) {
                adminMain(email,password);
            } else {
                System.out.println("Invalid option");
            }
        }
    }
}