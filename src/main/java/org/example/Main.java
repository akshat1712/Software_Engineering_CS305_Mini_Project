package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int option=-1; // variable to hold the option chosen

        while(option!=2) {

            System.out.println("Select [1] for Login");
            System.out.println("Select [2] for exit");
            System.out.print("Please enter your choice: ");
            Scanner sc= new Scanner(System.in);
            option=sc.nextInt();

            if( option==1){
                Login Login = new Login(); // making a new instance of login
                Login.loginForm(); // calling the loginForm function
            }
            else if( option==2){
                System.out.println("Thank You , Have a Nice Day");
            }
            else{
                System.out.println("Please Enter a Correct number");

            }
        }

    }
}