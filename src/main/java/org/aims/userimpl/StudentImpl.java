package org.aims.userimpl;

import org.aims.dataAccess.studentDAO;


import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;


public class StudentImpl implements userDAL {

    private  String email;
    private  String password;

    private final Connection con;

    private final String connectionString = "jdbc:postgresql://localhost:5432/postgres";
    private final String username = "postgres";
    private final String databasePassword = "2020csb1068";

    private final studentDAO studentDAO;

    public StudentImpl( studentDAO StudentDAO) throws SQLException {
        this.studentDAO = StudentDAO;
        con = DriverManager.getConnection(connectionString, username, databasePassword);
    }

    public boolean login(String email, String password) {
        this.email = email;
        this.password = password;
        if (!email.matches("^[a-zA-Z0-9+_.-]+@iitrpr.ac.in"))
            return false;

        if (studentDAO.login(email, password)) {
            studentDAO.loginLogs(email);
            return true;
        } else
            return false;
    }

    public void logout() {
        studentDAO.logoutLogs(email);
    }

    public String changePassword(String oldPassword, String newPassword) {

        if (newPassword.matches("[\\w]*\\s+[\\w]*")) {
            return "Password Cannot Contain Spaces";
        }

        if (studentDAO.checkPassword(email, oldPassword)) {
            studentDAO.changePassword(email, newPassword);
            return "Password Changed Successfully";
        } else
            return "Incorrect Old Password";
    }

    public String registerCourse(String courseCode) {

        if (!studentDAO.checkCourseOffering(courseCode)) {
            return "Course Not Offered";
        }

        if (studentDAO.checkCourseEnrollment(email, courseCode)) {
            return "Course Already Enrolled";
        }

        if (!studentDAO.checkSemesterStatus("ONGOING-CO")) {
            return "Semester Not Started";
        }

        double credits_enrolled = studentDAO.creditsEnrolled(email);
        double credits_courses = studentDAO.getCreditsCourse(courseCode);

        System.out.println(credits_enrolled);
        System.out.println(credits_courses);

        String semester = studentDAO.getSemester();
        int year = studentDAO.getYear();

        double prevSemesterCredits = 0;
        if (semester.equals("2")) {
            prevSemesterCredits = studentDAO.creditsEarnedSemesterYear(email, "1", year) + studentDAO.creditsEarnedSemesterYear(email, "2", year - 1);
        } else {
            prevSemesterCredits = studentDAO.creditsEarnedSemesterYear(email, "2", year - 1) + studentDAO.creditsEarnedSemesterYear(email, "1", year - 1);
        }

        System.out.println(prevSemesterCredits);

        if (credits_courses + credits_enrolled > 0.625 * (prevSemesterCredits))
            return "Credits Exceeded";

        double CGPA = computeCGPA();

        double CGPA_pre_req = studentDAO.getCGPAcriteria(courseCode);

        System.out.println(CGPA_pre_req);

        if (CGPA_pre_req > CGPA)
            return "CGPA Criteria Not Satisfied";

        if (studentDAO.checkSemesterStatus("ONGOING-GR")) {
            return "Grade Submission has started, So you cannot register for new courses";
        }

        if (studentDAO.checkCourseTranscript(email, courseCode)) {
            return "Course Already Taken";
        }


        String[] preReq = studentDAO.getPreReqCollege(courseCode);

        for (String s : preReq) {
            if (!studentDAO.checkCourseTranscript(email, s)) {
                return "You Do not satify the College prerequisite";
            }
        }

        preReq = studentDAO.getPreReqOffer(courseCode);

        for (String s : preReq) {
            if (!studentDAO.checkCourseTranscript(email, s)) {
                return "You Do not satify the Offer prerequisite";
            }

            int grade1 = studentDAO.getGradeCourse(email, s);
            int grade2 = studentDAO.getReqGradeOffer(courseCode, s);

            if (grade1 < grade2)
                return "You Do not satify the Offer prerequisite";
        }


        if (studentDAO.insertCourseEnrollement(email, courseCode))
            return "Course Registered";
        else
            return "Course Not Registered";
    }

    public String dropCourse(String courseCode) {

        if (!studentDAO.checkCourseOffering(courseCode))
            return "Course Not Being Offered Right Now";

        if (!studentDAO.checkCourseEnrollment(email, courseCode))
            return "Course Not Registered";

        if (!studentDAO.checkSemesterStatus("ONGOING-CO"))
            return "Grade Submission has started, So you cannot drop the course";

        if (studentDAO.deleteCourseEnrollement(email, courseCode))
            return "Course Dropped";
        else
            return "Course Not Dropped";
    }

    public String[] viewGrades() {
        return studentDAO.viewGrades(email);
    }

    public double computeCGPA() {
        double credits_earned = studentDAO.creditsEarned(email);
        double grade_points = studentDAO.gradePointsEarned(email);
        if (grade_points == 0)
            return 0.00;
        return (double) Math.round((grade_points / credits_earned) * 100) / 100;
    }

    public String[] viewCoursesEnrolled() {
        return studentDAO.getCourseEnrolled(email);
    }

    public String[] viewCoursesOffered() {
        return studentDAO.getCourseOffered();
    }
}

