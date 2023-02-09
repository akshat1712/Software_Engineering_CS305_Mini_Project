package org.example;

public class Faculty implements user {
    public void showmenu() {
        System.out.println("Welcome to Faculty Menu");
        showprofile();
        changeprofile();
    }
    public void showprofile() {
        System.out.println("Welcome to Faculty Profile");
    }
    public void changeprofile() {
        System.out.println("Welcome to Faculty Change Profile");
    }
}
