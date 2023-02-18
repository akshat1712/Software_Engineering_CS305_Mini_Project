package org.aims.service;

import org.aims.academics.AcademicEmployeeImpl;


import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

public class AcademicEmployeeService implements UserService {
    private AcademicEmployeeImpl AcademicEmployee;

    public AcademicEmployeeService() {

    }

    public void showmenu() {
        System.out.println("Welcome to Academic Staff Menu\n");

        int option = -1;

        Scanner sc = new Scanner(System.in);

        while (option != 0) {
            System.out.println("[0] LOGOUT");
            System.out.println("[1] Add Course in Catalog");
            System.out.println("[2] Start Semester");
            System.out.println("[3] End Semester");
            System.out.println("[4] View Grades");
            System.out.println("[5] Create Batch");
            System.out.println("[6] Change Password");
            System.out.println("[7] Generate Report");
            System.out.println("[8] Create Course Types");
            System.out.println("[9] Check Graduation");
            System.out.print("\nEnter your option: ");
            option = sc.nextInt();
            System.out.println();
            switch (option) {
                case 0 -> logoutService();   // Checking Done
                case 1 -> addCourseInCatalogService(); // Checking Done
                case 2 -> startSemesterService(); // Checking Done
                case 3 -> endSemesterService(); // Checking Done
                case 4 -> viewGradesService(); // Checking Done
                case 5 -> createCurriculumService(); // Checking Done
                case 6 -> changePasswordService();  // Checking Done
                case 7 -> generateReportService(); // Checking Done
                case 8 -> createCourseTypesService(); // Checking Done
                case 9 -> checkGraduationService(); // Checking Done
                default -> System.out.println("\nInvalid option\n");
            }
        }
    }

    public boolean login(String email, String password) {
        try {
            AcademicEmployee = new AcademicEmployeeImpl(email, password);
            return AcademicEmployee.login();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logoutService() {
        try {
            String response = AcademicEmployee.logout();
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addCourseInCatalogService() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Add Course in Catalog\n");
            System.out.print("Enter Course Code: ");
            String courseCode = sc.nextLine();
            System.out.print("Enter Course Name: ");
            String courseName = sc.nextLine();
            System.out.print("Enter Course Department: ");
            String courseDepartment = sc.nextLine();
            System.out.print("Enter Course Lectures Hours: ");
            int Lectures = sc.nextInt();
            System.out.print("Enter Course Tutorials Hours: ");
            int Tutorials = sc.nextInt();
            System.out.print("Enter Course Practical Hours: ");
            int Practical = sc.nextInt();
            System.out.print("Enter Course Self-Study Hours: ");
            int SelfStudy = sc.nextInt();
            System.out.print("Enter Course Credits: ");
            double Credits = sc.nextDouble();
            System.out.print("Enter Number of Pre-Requisite: ");
            int preRequisite = sc.nextInt();
            String[] preRequisiteList = new String[preRequisite + 1];
            if (preRequisite > 0)
                System.out.println("Enter Pre-Requisite Course Code each in new line: ");
            preRequisiteList[0] = courseCode;
            for (int i = 1; i < preRequisite + 1; i++) {
                String data = sc.nextLine();
                if (data.equals(""))
                    data = sc.nextLine();
                preRequisiteList[i] = data;
            }

            System.out.println();
            String response = AcademicEmployee.addCourseInCatalog(courseCode, courseName, courseDepartment, Lectures, Tutorials, Practical, SelfStudy, Credits, preRequisiteList);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in Adding Course in Catalog\n");
        }
    }

    private void startSemesterService() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the Semester Number: ");
        String semesterNumber = sc.nextLine();
        System.out.print("Enter the Semester Year: ");
        int semesterYear = sc.nextInt();
        System.out.println();
        try {
            String response = AcademicEmployee.startSemester(semesterYear, semesterNumber);
            System.out.println(response);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in Starting the semester\n");
        }
    }

    private void endSemesterService() {
        try {
            String response = AcademicEmployee.endSemester();
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in Ending the semester\n");
        }
    }

    private void viewGradesService() {
        System.out.println("View Grades");
        System.out.print("Enter the email address of the student: ");
        Scanner sc = new Scanner(System.in);
        String email = sc.nextLine();
        try {
            String[] response = AcademicEmployee.viewGrades(email);
            for (String s : response) {
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createCurriculumService() {
        System.out.println("Create Batch");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the batch number: ");
        int batchNumber = sc.nextInt();
        System.out.print("Enter the Department: ");
        String department = sc.nextLine();

        if (department.equals(""))
            department = sc.nextLine();

        System.out.print("\nEnter Number of courses you want to enter in whole Curriculum: ");
        int numberOfCourses = sc.nextInt();
        String[] courses = new String[numberOfCourses];

        if (numberOfCourses > 0)
            System.out.println("Enter Course Code followed by type alias in new lines: ");
        for (int i = 0; i < numberOfCourses; i++) {
            String data = sc.nextLine();
            if (data.equals(""))
                data = sc.nextLine();
            courses[i] = data;
        }

        System.out.print("\nEnter Number of credit-types you want to enter in whole Curriculum: ");
        int numberOfCreditTypes = sc.nextInt();

        String[] credits = new String[numberOfCreditTypes];

        if (numberOfCreditTypes > 0)
            System.out.println("Enter Credit Type followed by Number of Credits in new lines");

        for (int i = 0; i < numberOfCreditTypes; i++) {
            String data = sc.nextLine();
            if (data.equals(""))
                data = sc.nextLine();
            credits[i] = data;
        }

        System.out.println();
        try {
            String response = AcademicEmployee.createCurriculum(batchNumber, courses, credits, department);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void changePasswordService() {
        System.out.println("\nChange Password");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the old password: ");
        String oldPassword = sc.nextLine();
        System.out.println();
        System.out.print("Enter the new password: ");
        String newPassword = sc.nextLine();
        System.out.println();
        System.out.println();
        try {
            String response = AcademicEmployee.changePassword(oldPassword, newPassword);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateReportService() {
        System.out.println("Generate Report");

        try {
            Map<String, String[]> response;
            response = AcademicEmployee.generateReport();

            String path = "D:\\CS305\\Mini_Project\\Transcript";


            // Writing to a file
            for (Map.Entry<String, String[]> entry : response.entrySet()) {
                File fp = new File(path + "\\" + entry.getKey() + ".txt");

                for (String s : entry.getValue()) {
                    try {
                        FileWriter fw = new FileWriter(fp, true);
                        fw.write(s);
                        fw.write("\r\n");
                        fw.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createCourseTypesService() {
        System.out.println("Create Course Types");
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter the name of the Course Type: ");
        String courseType = sc.nextLine();
        System.out.print("\nEnter the alias of the Course Type: ");
        String alias = sc.nextLine();
        System.out.println();
        try {
            String response = AcademicEmployee.createCourseTypes(courseType, alias);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void checkGraduationService() {
        System.out.println("Check Graduation");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the email address of the student: ");
        String email = sc.nextLine();
        System.out.println();
        try {
            String response = AcademicEmployee.checkGraduation(email);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}