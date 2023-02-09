package org.example;

public class Student implements user {
    public void showmenu() {
        System.out.println("Welcome to Student Menu");
        showprofile();
        changeprofile();
    }
    public void showprofile() {
        System.out.println("Welcome to Student Profile");
    }
    public void changeprofile() {
        System.out.println("Welcome to Student Change Profile");
    }
}

