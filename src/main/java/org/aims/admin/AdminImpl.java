package org.aims.admin;

import org.aims.dao.UserDAO;

public class AdminImpl implements UserDAO {

        private final String Email;
        private final String Password;
        public AdminImpl(String email, String password) {
            this.Email= email;
            this.Password= password;
        }
        public void showmenu() {
            System.out.println("Welcome to Admin Menu");
        }

        public boolean login() {
            return Email.equals("postgres@iitrpr.ac.in") && Password.equals("2020csb1068");
        }

        public boolean AddFaculty() {
            System.out.println("Welcome to Add Faculty");
            return true;
        }
        public boolean AddStudent() {
            System.out.println("Welcome to Add Student");
            return true;
        }

        public boolean AddAcademicStaff() {
            System.out.println("Welcome to Add Academic Staff");
            return true;
        }
}
