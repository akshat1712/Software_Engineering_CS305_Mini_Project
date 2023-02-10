package org.aims.service;


import org.aims.faculty.FacultyImpl;

import java.util.Scanner;

public class FacultyService  implements UserService{
    private FacultyImpl Faculty;

    public FacultyService( ){

    }

    public void showmenu() {
        System.out.println("Welcome to Faculty Menu");

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
            Faculty = new FacultyImpl(email, password);
            return Faculty.login();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
