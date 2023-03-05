package org.aims;

import org.aims.dataAccess.academicDAO;
import org.aims.dataAccess.facultyDAO;
import org.aims.dataAccess.studentDAO;
import org.aims.service.*;
import org.aims.userimpl.*;

import java.util.Scanner;

public class Main {

    private static void studentMain(String email, String password){

        StudentImpl student;
        studentDAO studentDAO;

        try{
            studentDAO = new studentDAO();
            student = new StudentImpl(studentDAO);
        }catch (Exception e){return;}

        UserService studentService = new StudentService(student);
        if (studentService.login(email, password)) {
            studentService.showmenu();
        } else {
            System.out.println("Login Failed");
        }
    }

    private static void facultyMain(String email, String password){
        FacultyImpl faculty;
        facultyDAO facultyDAO;

        try {
            facultyDAO = new facultyDAO();
            faculty = new FacultyImpl(facultyDAO);
        }catch (Exception e){return;}

        UserService facultyService = new FacultyService(faculty);

        if (facultyService.login(email, password)) {
            facultyService.showmenu();
        } else {
            System.out.println("Login Failed");
        }
    }

    private static void acadMain(String email, String password){

        AcademicEmployeeImpl academicEmployee;
        academicDAO academicDAO;

        try{
            academicDAO = new academicDAO();
            academicEmployee = new AcademicEmployeeImpl(academicDAO);
        }catch (Exception e){return;}

        UserService academicEmployeeService = new AcademicEmployeeService(academicEmployee);
        if (academicEmployeeService.login(email, password)) {
            academicEmployeeService.showmenu();
        } else {
            System.out.println("Login Failed");
        }
    }

    private static void adminMain(String email, String password){

        AdminImpl adminImpl;
        try{
            adminImpl = new AdminImpl();
        }catch (Exception e){return;}
        UserService adminService = new AdminService(adminImpl);
        if (adminService.login(email, password)) {
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
            option = sc.nextLine();
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