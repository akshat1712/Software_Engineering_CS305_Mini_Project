package org.aims.student;

import org.aims.dataAccess.studentDAO;
import org.aims.dataAccess.userDAL;
import org.postgresql.util.PSQLException;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentImpl implements userDAL {

    private final String email;
    private final String password;

    private final Connection con;

    private final String connectionString = "jdbc:postgresql://localhost:5432/postgres";
    private final String username = "postgres";
    private final String databasePassword = "2020csb1068";

    private final studentDAO studentDAO = new studentDAO();

    public StudentImpl(String Email, String Password) throws SQLException {
        this.email = Email;
        this.password = Password;
        con = DriverManager.getConnection(connectionString, username, databasePassword);
    }

    public boolean login() {

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

        if (newPassword.matches("[\\w]*\\s[\\w]*")) {
            return "\nPassword Cannot Contain Spaces";
        }

        if (studentDAO.checkPassword(email, oldPassword)) {
            studentDAO.changePassword(email, newPassword);
            return "\nPassword Changed Successfully";
        } else
            return "\nIncorrect Old Password";
    }

    public String registerCourse(String courseCode) throws PSQLException, SQLException {

        if (studentDAO.checkCourseOffering(courseCode)) {
            return "\nCourse Not Offered";
        }

        if (studentDAO.checkCourseEnrollment(email, courseCode)) {
            return "\nCourse Already Enrolled";
        }

        if (studentDAO.checkSemesterStatus("ONGOING-CO")) {
            return "\nSemester Not Started";
        }

        double credits_enrolled_ = studentDAO.creditsEnrolled(email);
        double credits_courses = studentDAO.getCreditsCourse(courseCode);


        String semester = studentDAO.getSemester();
        int year = studentDAO.getYear();

        double prevSemesterCredits = 0;
        if (semester.equals("2")) {
            prevSemesterCredits = studentDAO.creditsEarnedSemesterYear(email, "1", year) + studentDAO.creditsEarnedSemesterYear(email, "2", year - 1);
        } else {
            prevSemesterCredits = studentDAO.creditsEarnedSemesterYear(email, "2", year - 1) + studentDAO.creditsEarnedSemesterYear(email, "2", year - 1);
        }


        if (credits_courses + credits_enrolled_ > 0.625 * (prevSemesterCredits))
            return "\nCredits Exceeded";

        double CGPA = computeCGPA();

        double CGPA_pre_req = studentDAO.getCGPAcriteria(courseCode);

        if (CGPA_pre_req > CGPA)
            return "\nCGPA Criteria Not Satisfied";

        if (studentDAO.checkSemesterStatus("ONGOING-GR")) {
            return "\nGrade Submission has started, So you cannot register for new courses";
        }

        if (studentDAO.checkCourseTranscript(email, courseCode)) {
            return "\nCourse Already Taken";
        }


        String[] preReq = studentDAO.getPreReqCollege(courseCode);

        for (String s : preReq) {
            if (!studentDAO.checkCourseTranscript(email, s)) {
                return "\nYou Do not satify the College prerequisite";
            }
        }

//        ResultSet rs7 = con.createStatement().executeQuery("SELECT * FROM courses_pre_req_offering WHERE offering_id=" + rs1.getString("offering_id"));
//        while (rs7.next()) {
//            String preReq = rs7.getString("pre_req");
//            String preReqGrade = rs7.getString("grade");
//            ResultSet rs8 = con.createStatement().executeQuery("SELECT * FROM courses_catalog WHERE course_code='" + preReq + "'");
//            if (rs8.next()) {
//                String query = "Select * from transcript_student_" + rs2.getString("student_id") + " P where P.grade>='" + preReqGrade + "' AND P.catalog_id=" + rs8.getString("catalog_id");
////                System.out.println(query);
//                ResultSet rs9 = con.createStatement().executeQuery(query);
//                if (!rs9.next()) {
//                    return "\nYou Do not satify the Faculty prerequisite";
//                }
//            }
//        }
//
        if (studentDAO.insertCourseEnrollement(email, courseCode))
            return "\nCourse Registered";
        else
            return "\nCourse Not Registered";
    }

    public String dropCourse(String courseCode) {

        if (!studentDAO.checkCourseOffering(courseCode))
            return "\nCourse Not Being Offered Right Now";

        if (!studentDAO.checkCourseEnrollment(email, courseCode))
            return "\nCourse Not Registered";

        if (!studentDAO.checkSemesterStatus("ONGOING-CO"))
            return "\nGrade Submission has started, So you cannot drop the course";

        if (studentDAO.deleteCourseEnrollement(email, courseCode))
            return "\nCourse Dropped";
        else
            return "\nCourse Not Dropped";
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

