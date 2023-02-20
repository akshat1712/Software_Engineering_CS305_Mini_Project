package org.aims.faculty;

import org.aims.dataAccess.facultyDAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.ResultSet;

import org.aims.dataAccess.userDAL;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FacultyImpl implements userDAL {

    private final String email;
    private final String password;

    private final Connection con;

    private final String connectionString = "jdbc:postgresql://localhost:5432/postgres";
    private final String username = "postgres";
    private final String databasePassword = "2020csb1068";

    private final facultyDAO facultyDAO = new facultyDAO();

    public FacultyImpl(String Email, String Password) throws PSQLException,SQLException {
        this.email = Email;
        this.password = Password;
        con = DriverManager.getConnection(connectionString, username, databasePassword);
    }


    public boolean login() {
        if( !email.matches("^[a-zA-Z0-9+_.-]+@iitrpr.ac.in"))
            return false;

        if(facultyDAO.login(email,password)){
            facultyDAO.loginLogs(email);
            return true;
        }
        else
            return false;
    }

    public void logout() throws PSQLException,SQLException {
        facultyDAO.logoutLogs(email);
    }

    public String offerCourse(String courseCode, double cgpaCutoff, String[] prerequisites) {

        if( !facultyDAO.checkCourseCatalog(courseCode))
            return "\nCourse Does Not Exist";

        if( facultyDAO.checkCourseOffering(courseCode))
            return "\nCourse Already Offered";

        if( !facultyDAO.checkSemesterStatus("ONGOING-CO"))
            return "\nCourse Offering is not Available";

        if( (cgpaCutoff < 0 || cgpaCutoff > 10) && cgpaCutoff != -1 )
            return "\nInvalid CGPA Cutoff";

        for (String s : prerequisites) {
            String[] split = s.split(",");
            if (!facultyDAO.checkCourseCatalog(split[0])) {
                return "\nPrerequisite Course Does Not Exist";
            }
            if (Integer.parseInt(split[1]) < 0 || Integer.parseInt(split[1]) > 10) {
                return "\nInvalid Grade";
            }
        }

        facultyDAO.insertCourse(email,courseCode,cgpaCutoff);

        int count = 0;
        for (String s : prerequisites) {
            String[] split = s.split(",");
            count += 1;
            facultyDAO.insertCoursePreReq(courseCode,split[0],split[1],count);
        }

        facultyDAO.insertCourseFaculty(email,courseCode);

        return "\nCourse Offered Successfully";
    }

    public String takeBackCourse(String courseCode)  {

        if( !facultyDAO.checkCourseCatalog(courseCode)){
            return "\nCourse Does Not Exist";
        }

        if( !facultyDAO.checkCourseOffering(courseCode)){
            return "\nCourse Not Offered";
        }

        if(!facultyDAO.checkSemesterStatus("ONGOING-CO")){
            return "\nCourse Offering is not Open";
        }

        if( facultyDAO.getfacultyidEmail(email)!=facultyDAO.getfacultyidCourse(courseCode))
            return "\nYou are not the Faculty of this Course";


        facultyDAO.deleteCourseOffering(email,courseCode);

        String [] studentEmail= facultyDAO.getStudentEmail();

        for(String s:studentEmail){
            facultyDAO.deleteCourseEnrollement(s,courseCode);
        }


        return "\nCourse Taken Back Successfully";
    }

    public String[] viewGrades(String email)  {
        return facultyDAO.viewGrades(email);
    }

    public String changePassword(String oldPassword, String newPassword)  {
        if( newPassword.matches("[\\w]*\\s[\\w]*")){
            return "\nPassword Cannot Contain Spaces";
        }

        if(facultyDAO.checkPassword(email,oldPassword)){
            facultyDAO.changePassword(email,newPassword);
            return "\nPassword Changed Successfully";
        }
        else
            return "\nIncorrect Old Password";
    }

    public String updateGrades(String path, String courseCode)  {

        if( !facultyDAO.checkCourseOffering(courseCode)){
            return "\nCourse Not Offered";
        }

        if( facultyDAO.getfacultyidEmail(email)!=facultyDAO.getfacultyidCourse(courseCode))
            return "\nYou are not the Faculty of this Course";

        if(!facultyDAO.checkSemesterStatus("ONGOING-GS")){
            return "\nCourse Offering is not Open for Grading";
        }


        try {
            BufferedReader br;
            br = new BufferedReader(new FileReader(path));
            String line = br.readLine();

            while (line != null) {
                String[] data = line.split(",");

                if (Integer.parseInt(data[1]) > 10 || Integer.parseInt(data[1]) < 0) {
                    return "\nInvalid Grade " + data[1] + " Present";
                }

                if( !facultyDAO.checkCourseEnrollment(data[0],courseCode)){
                    return "\nStudent Not Enrolled in the Course";
                }
                line = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e);
            return "\nFile Does Not Exist1";
        }


        try {
            BufferedReader br;
            br = new BufferedReader(new FileReader(path));
            String line = br.readLine();

            while (line != null) {
                String[] data = line.split(",");
                facultyDAO.updateGrade(data[0],courseCode,data[1]);
                line = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e);
            return "\nFile Does Not Exist2";
        }

        return "\nGrades Upgraded Successfully";
    }
}
