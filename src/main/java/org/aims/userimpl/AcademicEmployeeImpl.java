package org.aims.userimpl;

import org.aims.dataAccess.academicDAO;
import org.postgresql.util.PSQLException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class AcademicEmployeeImpl implements userDAL {
    private String email;
    private String password;

    private academicDAO academicDAO;

    public AcademicEmployeeImpl(academicDAO AcademicDAO) throws SQLException {
        this.academicDAO = AcademicDAO;
    }


    public boolean login(String email, String password) {
        this.email = email;
        this.password = password;
        if (!email.matches("^[a-zA-Z0-9+_.-]+@iitrpr.ac.in"))
            return false;

        if (academicDAO.login(email, password)) {
            academicDAO.loginLogs(email);
            return true;
        } else
            return false;
    } //DONE

    public String logout() {
        academicDAO.logoutLogs(email);
        return "LOGGED OUT SUCCESSFULLY";
    } //DONE


    public String addCourseInCatalog(String courseCode, String courseName, String department,
                                     int lectures, int tutorial, int practicals, int self_study, double credits,
                                     String[] prerequisite) {

        if (academicDAO.checkCourseCatalog(courseCode))
            return "Course Already Exists";

        for (String s : prerequisite) {
            if (s.equals(courseCode))
                continue;
            if (!academicDAO.checkCourseCatalog(s))
                return "Prerequisite Course Does Not Exist";
        }

        if (academicDAO.getdepartmentid(department) == -1)
            return "Department Does Not Exist";


        academicDAO.insertCourseCatalog(courseName, courseCode, department, lectures, tutorial, practicals, self_study, credits);

        for (String s : prerequisite) {
            if (s.equals(courseCode))
                continue;
            academicDAO.insertCoursePre(courseCode, s);
        }
        return "COURSE ADDED IN CATALOG SUCCESSFULLY\n";
    } //DONE

    public String createCurriculum(int batch, String[] courses, String[] credits, String Department) throws PSQLException, SQLException {

        academicDAO.createbatch(batch);

        if (academicDAO.getdepartmentid(Department) == -1)
            return "Department Does Not Exist";

        for (String s : credits) {
            String[] split = s.split(" ");

            if (Double.parseDouble(split[1]) < 0) {
                return "Invalid Credits in " + split[0];
            }
        }

        for (String s : courses) {
            String[] split = s.split(" ");

            if (!academicDAO.checkCourseCatalog(split[0]))
                return "Course Does Not Exist in Catalog " + split[0];

            if (!academicDAO.checkCourseTypes(split[1]))
                return "Invalid Course Type " + split[1];
        }

        for (String s : courses) {
            String[] split = s.split(" ");
            academicDAO.createBatchCurriculum(batch, Department, split[0], split[1]);
        }
        for (String s : credits) {
            String[] split = s.split(" ");
            academicDAO.createBatchCredits(batch, Department, split[0], split[1]);
        }
        return "Curriculum Created Successfully\n";

    } // DONE

    public String startSemester(int Year, String Semester) {

        if (academicDAO.checkSemesterStatus("ONGOING-GS") || academicDAO.checkSemesterStatus("ONGOING-CO"))
            return "A Semester Already Ongoing";

        if (academicDAO.checkSemesterValidity(Semester, Year))
            return "Semester Already Exists";

        academicDAO.newSemester(Semester, Year);
        return "Semester Started Successfully";
    } // DONE

    public String endSemester() {
        if (academicDAO.checkSemesterStatus("ONGOING-CO"))
            return "Grade Submission Not started";

        String[] students = academicDAO.getStudentids();

        for (String s : students) {
            if (academicDAO.checkGradeSubmission(s))
                return "Grade Not Submitted for the student ";
        }

        for (String s : students) {
            academicDAO.updateStudentTranscript(s);
        }

        String[] faculties = academicDAO.getfacultyids();

        for (String s : faculties) {
            academicDAO.updateFacultyTranscript(s);
        }
        academicDAO.endSemester();
        return "Semester Ended";

    } //DONE

    public String[] viewGrades(String email) {
        return academicDAO.viewGrades(email);
    } // DONE

    public String changePassword(String oldPassword, String newPassword) {
        if (newPassword.matches("[\\w]*\\s+[\\w]*")) {
            return "\nPassword Cannot Contain Spaces";
        }

        if (academicDAO.checkPassword(email, oldPassword)) {
            academicDAO.changePassword(email, newPassword);
            return "\nPassword Changed Successfully";
        } else
            return "\nIncorrect Old Password";
    } // DONE

    public Map<String, String[]> generateReport() throws PSQLException, SQLException {
        String[] email = academicDAO.getStudentEmail();
        Map<String, String[]> transcriptAllStudents = new HashMap<>();
        for (String s : email) {
            String[] grades = viewGrades(s);
            transcriptAllStudents.put(s, grades);
        }
        return transcriptAllStudents;
    }

    public String createCourseTypes(String courseType, String alias) {
        academicDAO.CreateCourseTypes(courseType, alias);
        return "Course Type Created Successfully";
    }


    public String checkGraduation(String email) {

        String[] courses_Curriculum = academicDAO.getCurriculumCourse(email);

        for (String s : courses_Curriculum) {
            if (!academicDAO.checkCourseTranscript(email, s))
                return "Not all Courses Completed";
        }

        Map<String, Double> creditsTypeCount = academicDAO.getEnrolledCreditsType(email);

        int credits_Open_Elective = 0;
        for (Map.Entry<String, Double> entry : creditsTypeCount.entrySet()) {
            if (entry.getValue() < academicDAO.getCreditsType(email, entry.getKey()))
                return "Not Enough Credits of " + entry.getKey();
            else
                credits_Open_Elective += entry.getValue() - academicDAO.getCreditsType(email, entry.getKey());
        }

        if (credits_Open_Elective < academicDAO.getCreditsType(email, "OE"))
            return "Not Enough Credits of Open Elective";

        return "Student can Graduate";
    }


    public String startGradeSubmission() {
        if (!academicDAO.checkSemesterStatus("ONGOING-CO"))
            return "Cannot Start Grade Submission";
        academicDAO.updateSemesterStatus("ONGOING-CO", "ONGOING-GS");
        return "Grade Submission Started";
    }
}

