package org.aims.service;

import org.aims.academics.AcademicEmployeeImpl;


import java.util.Scanner;

public class AcademicEmployeeService implements UserService {
    private AcademicEmployeeImpl AcademicEmployee;

    public AcademicEmployeeService( ){

    }

    public void showmenu() {
        System.out.println("Welcome to Academic Staff Menu");

        int option = -1;

        Scanner sc = new Scanner(System.in);
        while (option!=0){
            System.out.println("[0] LOGOUT");
            System.out.println("[1] View Profile");
            System.out.println("[2] Update Profile");
            System.out.print("Enter your option: ");
            option = sc.nextInt();
        }
    }

    public boolean login(String email,String password) {
        try {
            AcademicEmployee = new AcademicEmployeeImpl(email, password);
            return AcademicEmployee.login();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
