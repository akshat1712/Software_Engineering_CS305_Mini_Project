package org.aims.adminTest;

import org.aims.userimpl.AdminImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestFile2 {

    static AdminImpl testAdmin;

    public TestFile2() throws Exception {
        testAdmin = new AdminImpl();
    }

    @AfterAll
    public static void clearing() throws Exception {
        testAdmin = null;

        try {
            String connectionString = "jdbc:postgresql://localhost:5432/postgres";
            String username = "postgres";
            String databasePassword = "2020csb1068";
            Connection con = DriverManager.getConnection(connectionString, username, databasePassword);


            con.createStatement().execute("DELETE FROM departments WHERE name='CSE'");

            con.createStatement().execute("DELETE FROM passwords WHERE email='ACAD_10@iitrpr.ac.in';");
            con.createStatement().execute("DELETE FROM acad_empl WHERE email='ACAD_10@iitrpr.ac.in';");

            con.createStatement().execute("DELETE FROM passwords WHERE email='FAC1@iitrpr.ac.in';");
            con.createStatement().execute("DELETE FROM faculties WHERE email='FAC1@iitrpr.ac.in';");

            con.createStatement().execute("DELETE FROM passwords WHERE email='STUDENT1@iitrpr.ac.in'");
            con.createStatement().execute("DELETE FROM students WHERE email='STUDENT1@iitrpr.ac.in'");


//          DROP TABLE courses_teaching_faculty_;
//          DROP TABLE transcript_faculty_;
//          DROP TABLE transcript_student_;
//          DROP TABLE courses_enrolled_student_;
        }
        catch (Exception e){
            System.out.println(e);
        }
    }


    @ParameterizedTest
    @CsvSource({"postgres@iitrpr.ac.in,2020csb1068,1","postgres@gmail.com,2020csb1068,2","postgres@iitrpr.ac.in,password,3"})
    @Order(1)
    public void testLogin(String email,String password,int type) throws Exception{
        boolean res=testAdmin.login(email,password);
        if(type==1){
            assertTrue(res);
        }
        else if(type==2){
            assertTrue(!res);
        }
        else if(type==3){
            assertTrue(!res);
        }
    }



    @ParameterizedTest
    @CsvSource({"CSE-1,1", "CSE,2", "CSE,3"})
    @Order(2)
    public void testAddDepartment(String dept,int type) throws Exception {
        String res=testAdmin.AddDepartment(dept);
        System.out.println(res);
        if(type==1){
            assertTrue(res.equals("Department name is invalid"));
        }
        else if(type==2){
            assertTrue(res.equals("Department added successfully"));
        }
        else if(type==3){
            assertTrue(res.equals("Department already exists"));
        }
    }



    @ParameterizedTest
    @CsvSource({
            "ACAD$10,ACAD_10@iitrpr.ac.in,2022-12-12,7539518426,IIT ROPAR,1",
            "ACAD10,ACAD_10@gmail.ac.in,2022-12-12,7539518426,IIT ROPAR,2",
            "ACAD10,ACAD_10@iitrpr.ac.in,2022-12-12,7abcde6,IIT ROPAR,3",
            "ACAD10,ACAD_10@iitrpr.ac.in,20-12-12,7539518426,IIT ROPAR,4",
            "ACAD_10,ACAD_10@iitrpr.ac.in,2022-12-12,7539518426,IIT ROPAR,5",
            "ACAD_10,ACAD_10@iitrpr.ac.in,2022-12-12,7539518426,IIT ROPAR,6"})
    @Order(3)
    public void testAddAcad(String name,String email,String joiningDate,String phone,String address,int type) throws Exception {
        String res=testAdmin.AddAcademicStaff(name,email,joiningDate,phone,address);

        if (type==1){
            assertTrue(res.equals("Name is invalid"));
        }
        else if(type==2){
            assertTrue(res.equals("Email is invalid"));
        }
        else if(type==3){
            assertTrue(res.equals("Phone number is invalid"));
        }
        else if(type==4){
            assertTrue(res.equals("Joining date is invalid"));
        }
        else if(type==5){
            assertTrue(res.equals("Academic staff added successfully"));
        }
        else if(type==6){
            assertTrue(res.equals("Academic staff already exists"));
        }
    }



    @ParameterizedTest
    @CsvSource({
            "FAC1$FAC2,FAC1@iitrpr.ac.in,Computer Science,2022-12-12,7539518426,IIT ROPAR,1",
            "FAC1,FAC1@gmail.ac.in,Computer Science,2022-12-12,7539518426,IIT ROPAR,2",
            "FAC1,FAC1@iitrpr.ac.in,Computer Science,2022-12-12,01332-252622,IIT ROPAR,3",
            "FAC1,FAC1@iitrpr.ac.in,Computer Science,2022-2-12,7539518426,IIT ROPAR,4",
            "FAC1,FAC1@iitrpr.ac.in,Computer_Science,2022-12-12,7539518426,IIT ROPAR,5",
            "FAC1,FAC1@iitrpr.ac.in,Computer ,2022-12-12,7539518426,IIT ROPAR,6",
            "FAC1,FAC1@iitrpr.ac.in,Computer Science,2022-12-12,7539518426,IIT ROPAR,7",
            "FAC1,FAC1@iitrpr.ac.in,Computer Science,2022-12-12,7539518426,IIT ROPAR,8",
    })
    @Order(4)
    public void testAddFaculty(String name,String email,String department,String joiningDate,String phone,String address,int type) throws Exception {
        String res = testAdmin.AddFaculty(name, email, department, joiningDate, phone, address);

        if (type == 1) {
            assertTrue(res.equals("Name is invalid"));
        } else if (type == 2) {
            assertTrue(res.equals("Email is invalid"));
        } else if (type == 3) {
            assertTrue(res.equals("Phone number is invalid"));
        } else if (type == 4) {
            assertTrue(res.equals("Joining date is invalid"));
        } else if (type == 5) {
            assertTrue(res.equals("Department is invalid"));
        } else if (type == 6) {
            assertTrue(res.equals("Department does not exist"));
        } else if (type == 7) {
            assertTrue(res.equals("Faculty added successfully"));
        } else if (type == 8) {
            assertTrue(res.equals("Faculty already exists"));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "STUDENT#1,STUDENT1@iitrpr.ac.in,2020CSB7000,Computer Science,2020,7539518426,IIT ROPAR,1",
            "STUDENT1,STUDENT1@gmail.ac.in,2020CSB7000,Computer Science,2020,7539518426,IIT ROPAR,2",
            "STUDENT1,STUDENT1@iitrpr.ac.in,2020csbe7000,Computer Science,2020,7539518426,IIT ROPAR,3",
            "STUDENT1,STUDENT1@iitrpr.ac.in,2020CSB7000,Computer-Science,2020,7539518426,IIT ROPAR,4",
            "STUDENT1,STUDENT1@iitrpr.ac.in,2020CSB7000,Computer Science,2020-S,7539518426,IIT ROPAR,5",
            "STUDENT1,STUDENT1@iitrpr.ac.in,2020CSB7000,Computer Science,2020,+9112345,IIT ROPAR,6",
            "STUDENT1,STUDENT1@iitrpr.ac.in,2020CSB7000,ComputerScience,2020,7539518426,IIT ROPAR,7",
            "STUDENT1,STUDENT1@iitrpr.ac.in,2020CSB7000,Computer Science,2015,7539518426,IIT ROPAR,8",
            "STUDENT1,STUDENT1@iitrpr.ac.in,2020CSB7000,Computer Science,2020,7539518426,IIT ROPAR,9",
            "STUDENT1,STUDENT1@iitrpr.ac.in,2020CSB7000,Computer Science,2020,7539518426,IIT ROPAR,10",
    })
    @Order(5)
    public void testAddStudent(String name,String email,String EntryNumber,String department,String batch,String phone,String address,int type) throws Exception {
        String res=testAdmin.AddStudent(name,email,EntryNumber,department,batch,phone,address);

        System.out.println(res);

        if(type==1){
            assertTrue(res.equals("Name is invalid"));
        }
        else if(type==2){
            assertTrue(res.equals("Email is invalid"));
        }
        else if(type==3){
            assertTrue(res.equals("Entry number is invalid"));
        }
        else if(type==4){
            assertTrue(res.equals("Department is invalid"));
        }
        else if(type==5){
            assertTrue(res.equals("Batch is invalid"));
        }
        else if(type==6){
            assertTrue(res.equals("Phone number is invalid"));
        }
        else if(type==7){
            assertTrue(res.equals("Department does not exist"));
        }
        else if(type==8){
            assertTrue(res.equals("Batch does not exist"));
        }
        else if(type==9){
            assertTrue(res.equals("Student added successfully"));
        }
        else if(type==10){
            assertTrue(res.equals("Student already exists"));
        }
    }
}
