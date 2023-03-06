package org.aims.service;

import org.aims.userimpl.AcademicEmployeeImpl;


import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import java.util.Scanner;

public class AcademicEmployeeService implements UserService {
    private AcademicEmployeeImpl AcademicEmployee;

    Scanner sc;
    public AcademicEmployeeService( AcademicEmployeeImpl AcademicEmployee) {
        this.AcademicEmployee = AcademicEmployee;
    }


    public void showmenu() {
        System.out.println("Welcome to Academic Staff Menu\n");

        String option="Z";

        this.sc = new Scanner(System.in);

        while (!option.equals("A")) {
            System.out.println("[A] LOGOUT");
            System.out.println("[B] Add Course in Catalog");
            System.out.println("[C] Start Semester");
            System.out.println("[D] End Semester");
            System.out.println("[E] View Grades");
            System.out.println("[F] Create Batch");
            System.out.println("[G] Change Password");
            System.out.println("[H] Generate Report");
            System.out.println("[I] Create Course Types");
            System.out.println("[J] Check Graduation");
            System.out.println("[K] Start Grade Submission");
            System.out.println("Enter your option");
            option = this.sc.nextLine();

            System.out.println(option);
            switch (option) {
                case "A" -> logoutService();   // Checking Done
                case "B" -> addCourseInCatalogService(); // Checking Done
                case "C" -> startSemesterService(); // Checking Done
                case "D" -> endSemesterService(); // Checking Done
                case "E" -> viewGradesService(); // Checking Done
                case "F" -> createCurriculumService(); // Checking Done
                case "G" -> changePasswordService();  // Checking Done
                case "H" -> generateReportService(); // Checking Done
                case "I" -> createCourseTypesService(); // Checking Done
                case "J" -> checkGraduationService(); // Checking Done
                case "K" -> startGradeSubmissionService(); // Checking Done
                default -> System.out.println("Invalid option");
            }
        }
    }

    public boolean login(String email, String password) {
        return AcademicEmployee.login(email, password);
    }

    private void logoutService() {
        String response = AcademicEmployee.logout();
        System.out.println(response);
    }
    private void addCourseInCatalogService() {
        try {
            System.out.println("Add Course in Catalog");
            System.out.print("Enter Course Code: ");
            String courseCode = this.sc.nextLine();
            System.out.print("Enter Course Name: ");
            String courseName = this.sc.nextLine();
            System.out.print("Enter Course Department: ");
            String courseDepartment = this.sc.nextLine();
            System.out.print("Enter Course Lectures Hours: ");
            int Lectures = this.sc.nextInt();
            System.out.print("Enter Course Tutorials Hours: ");
            int Tutorials = this.sc.nextInt();
            System.out.print("Enter Course Practical Hours: ");
            int Practical = this.sc.nextInt();
            System.out.print("Enter Course Self-Study Hours: ");
            int SelfStudy = this.sc.nextInt();
            System.out.print("Enter Course Credits: ");
            double Credits = this.sc.nextDouble();
            System.out.print("Enter Number of Pre-Requisite: ");
            int preRequisite = this.sc.nextInt();
            String[] preRequisiteList = new String[preRequisite + 1];
            if (preRequisite > 0)
                System.out.println("Enter Pre-Requisite Course Code each in new line: ");
            preRequisiteList[0] = courseCode;
            for (int i = 1; i < preRequisite + 1; i++) {
                String data = this.sc.nextLine();
                if (data.equals(""))
                    data = this.sc.nextLine();
                preRequisiteList[i] = data;
            }

            String response = AcademicEmployee.addCourseInCatalog(courseCode, courseName, courseDepartment, Lectures, Tutorials, Practical, SelfStudy, Credits, preRequisiteList);
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Error in Adding Course in Catalog");
        }
    }

    private void startSemesterService() {
        System.out.println("Enter the Semester Number");
        String semesterNumber = this.sc.nextLine();
        int semesterYear;
        while (true) {
            try {
                System.out.println("Enter the Semester Year");
                semesterYear = Integer.parseInt(this.sc.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Invalid Input");
            }
        }
        String response = AcademicEmployee.startSemester(semesterYear, semesterNumber);
        System.out.println(response);
    }

    private void endSemesterService() {
        String response = AcademicEmployee.endSemester();
        System.out.println(response);
    }

    private void viewGradesService() {
        System.out.println("View Grades");
        System.out.println("Enter the email address of the student");
        String email = this.sc.nextLine();
        String[] response = AcademicEmployee.viewGrades(email);
        if (response == null){
            System.out.println("No Grades Available");
            return;
        }
        for (String s : response) {
            System.out.println(s);
        }
    }

    private void createCurriculumService() {
        try {
            System.out.println("Create Batch");
            System.out.print("Enter the batch number: ");
            int batchNumber = this.sc.nextInt();
            System.out.print("Enter the Department: ");
            String department = this.sc.nextLine();
            if (department.equals(""))
                department = this.sc.nextLine();
            System.out.println("Enter Number of courses you want to enter in whole Curriculum: ");
            int numberOfCourses = sc.nextInt();
            String[] courses = new String[numberOfCourses];
            if (numberOfCourses > 0)
                System.out.println("Enter Course Code followed by type alias in new lines: ");
            for (int i = 0; i < numberOfCourses; i++) {
                String data = this.sc.nextLine();
                if (data.equals(""))
                    data = this.sc.nextLine();
                courses[i] = data;
            }
            System.out.println("Enter Number of credit-types you want to enter in whole Curriculum: ");
            int numberOfCreditTypes = this.sc.nextInt();

            String[] credits = new String[numberOfCreditTypes];
            if (numberOfCreditTypes > 0)
                System.out.println("Enter Credit Type followed by Number of Credits in new lines");
            for (int i = 0; i < numberOfCreditTypes; i++) {
                String data = this.sc.nextLine();
                if (data.equals(""))
                    data = this.sc.nextLine();
                credits[i] = data;
            }
            String response = AcademicEmployee.createCurriculum(batchNumber, courses, credits, department);
            System.out.println(response);
        }
        catch (Exception e) {
            System.out.println("Error in Creating Curriculum");
        }

    }

    private void changePasswordService() {
        System.out.println("Change Password");
        System.out.println("Enter the old password");
        String oldPassword = this.sc.nextLine();
        System.out.println("Enter the new password");
        String newPassword = this.sc.nextLine();
        String response = AcademicEmployee.changePassword(oldPassword, newPassword);
        System.out.println(response);
    }

    private void generateReportService() {
        System.out.println("Generate Report");
        try {
            Map<String, String[]> response;
            response = AcademicEmployee.generateReport();
            String path = "D:\\CS305\\Mini_Project\\Transcript";

            // Writing to a file
            for (Map.Entry<String, String[]> entry : response.entrySet()) {
                File fp = new File(path + "\\" + entry.getKey().substring(0,11) + ".txt");
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
        System.out.println("Enter the name of the Course Type: ");
        String courseType = this.sc.nextLine();
        System.out.println("Enter the alias of the Course Type: ");
        String alias = this.sc.nextLine();
        String response = AcademicEmployee.createCourseTypes(courseType, alias);
        System.out.println(response);
    }

    private void checkGraduationService() {
        System.out.println("Check Graduation");
        System.out.println("Enter the email address of the student");
        String email = this.sc.nextLine();
        String response = AcademicEmployee.checkGraduation(email);
        System.out.println(response);
    }

    private void startGradeSubmissionService() {
        System.out.println("Start Grade Submission");
        String response = AcademicEmployee.startGradeSubmission();
        System.out.println(response);
    }
}