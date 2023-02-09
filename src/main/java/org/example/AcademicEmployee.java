package org.example;

import java.util.Scanner;

public class AcademicEmployee implements user {
    public void showmenu() {
        System.out.println("Welcome to Academic Employee Menu");


        int option=-1; // variable to hold the option chosen
        while(option!=3) {
            System.out.println("Select [1] for View Profile");
            System.out.println("Select [2] for Change Profile");
            System.out.println("Select [3] for Exit");
            System.out.print("Please enter your choice: ");
            Scanner sc= new Scanner(System.in);
            option=sc.nextInt();
            if( option==1){
                showprofile();
            }
            else if( option==2){
                changeprofile();
            }
            else if( option==3){
                System.out.println("Thank You , Have a Nice Day");
            }
            else{
                System.out.println("Please Enter a Correct number");
            }
        }

    }
    public void showprofile() {
        System.out.println("Welcome to Academic Employee Profile");
    }
    public void changeprofile() {
        System.out.println("Welcome to Academic Employee Change Profile");
    }
}

