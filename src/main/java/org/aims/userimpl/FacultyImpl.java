package org.aims.userimpl;

import org.aims.dataAccess.facultyDAO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.SQLException;

public class FacultyImpl implements userDAL {
    private String email;
    private String password;
    private final facultyDAO facultyDAO;
    public FacultyImpl( facultyDAO FacultyDAO )  {
        this.facultyDAO = FacultyDAO;
    }
    public boolean login(String email, String password) {
        try {
            this.email = email;
            this.password = password;
            if (!email.matches("^[a-zA-Z0-9+_.-]+@iitrpr.ac.in"))
                return false;
            if (facultyDAO.login(email, password)) {
                facultyDAO.loginLogs(email);
                return true;
            } else
                return false;
        } catch (SQLException e) {
            return false;
        }
    }
    public void logout()  {
        try {
            facultyDAO.logoutLogs(email);
        }catch(SQLException e){}
    }
    public String offerCourse(String courseCode, double cgpaCutoff, String[] prerequisites) {
        try {
            if (!facultyDAO.checkCourseCatalog(courseCode))
                return "Course Does Not Exist";
            if (facultyDAO.checkCourseOffering(courseCode))
                return "Course Already Offered";
            if (!facultyDAO.checkSemesterStatus("ONGOING-CO"))
                return "Course Offering is not Available";
            if ((cgpaCutoff < 0 || cgpaCutoff > 10) && cgpaCutoff != -1)
                return "Invalid CGPA Cutoff";
            for (String s : prerequisites) {
                String[] split = s.split(",");
                if (!facultyDAO.checkCourseCatalog(split[0])) {
                    return "Prerequisite Course Does Not Exist";
                }
                if (Integer.parseInt(split[1]) < 0 || Integer.parseInt(split[1]) > 10) {
                    return "Invalid Grade";
                }
            }
            facultyDAO.insertCourse(email, courseCode, cgpaCutoff);
            int count = 0;
            for (String s : prerequisites) {
                String[] split = s.split(",");
                count += 1;
                facultyDAO.insertCoursePreReq(courseCode, split[0], split[1], count);
            }
            facultyDAO.insertCourseFaculty(email, courseCode);
            return "Course Offered Successfully";
        } catch(SQLException e){
            return "Error";
        }
    }
    public String takeBackCourse(String courseCode) {
        try {
            if (!facultyDAO.checkCourseCatalog(courseCode)) {
                return "Course Does Not Exist";
            }
            if (!facultyDAO.checkCourseOffering(courseCode)) {
                return "Course Not Offered";
            }
            if (!facultyDAO.checkSemesterStatus("ONGOING-CO")) {
                return "Course Offering is not Open";
            }
            if (facultyDAO.getfacultyidEmail(email) != facultyDAO.getfacultyidCourse(courseCode))
                return "You are not the Faculty of this Course";
            facultyDAO.deleteCourseOffering(email, courseCode);
            String[] studentEmail = facultyDAO.getStudentEmail();
            for (String s : studentEmail) {
                facultyDAO.deleteCourseEnrollement(s, courseCode);
            }
            return "Course Taken Back Successfully";
        }
        catch(SQLException e){
            return "Error";
        }
    }
    public String[] viewGrades(String email) {
        try{
        return facultyDAO.viewGrades(email);
        }catch(SQLException e){
            return null;
        }
    }
    public String changePassword(String oldPassword, String newPassword) {
        try {
            if (newPassword.matches("[\\w]*\\s+[\\w]*")) {
                return "Password Cannot Contain Spaces";
            }
            if (facultyDAO.checkPassword(email, oldPassword)) {
                facultyDAO.changePassword(email, newPassword);
                return "Password Changed Successfully";
            } else
                return "Incorrect Old Password";
        } catch (SQLException e) {
            return "Error";
        }
    }
    public String updateGrades(String path, String courseCode) {
        try {
            if (!facultyDAO.checkCourseOffering(courseCode)) {
                return "Course Not Offered";
            }
            if (facultyDAO.getfacultyidEmail(email) != facultyDAO.getfacultyidCourse(courseCode))
                return "You are not the Faculty of this Course";
            if (!facultyDAO.checkSemesterStatus("ONGOING-GS")) {
                return "Course Offering is not Open for Grading";
            }
            try {
                BufferedReader br;
                br = new BufferedReader(new FileReader(path));
                String line = br.readLine();

                while (line != null) {
                    String[] data = line.split(",");

                    if (Integer.parseInt(data[1]) > 10 || Integer.parseInt(data[1]) < 0) {
                        return "Invalid Grade " + data[1] + " Present";
                    }

                    if (!facultyDAO.checkCourseEnrollment(data[0], courseCode)) {
                        return "Student Not Enrolled in the Course";
                    }
                    line = br.readLine();
                }
                br.close();
            } catch (Exception e) {
                return "File Does Not Exist1";
            }
            try {
                BufferedReader br;
                br = new BufferedReader(new FileReader(path));
                String line = br.readLine();

                while (line != null) {
                    String[] data = line.split(",");
                    facultyDAO.updateGrade(data[0], courseCode, data[1]);
                    line = br.readLine();
                }
                br.close();
            } catch (Exception e) {
                return "File Does Not Exist2";
            }
            return "Grades Upgraded Successfully";
        } catch (SQLException e) {
            return "Error";
        }
    }
}
