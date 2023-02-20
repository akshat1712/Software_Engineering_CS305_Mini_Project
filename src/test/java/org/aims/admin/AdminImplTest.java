package org.aims.admin;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AdminImplTest {

    AdminImpl testAdmin;

    {
        try {
            testAdmin = new AdminImpl("postgres@iitrpr.ac.in", "2020csb1068");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    void login() {
        assertTrue(testAdmin.login());
    }

    @Test
    @Order(2)
    void addDepartmentTest1() {
        try {
            assertEquals("Department added successfully\n", testAdmin.AddDepartment("CSE"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(3)
    void addDepartmentTest2() {
        try {
            assertEquals("Department name is invalid\n", testAdmin.AddDepartment("CS'E"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(4)
    void addDepartmentTest3() {
        try {
            assertEquals("Department already exists\n", testAdmin.AddDepartment("CSE"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(5)
    void addDepartmentTest4() {
        try {
            assertEquals("Department name is Empty\n", testAdmin.AddDepartment("   "));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(5)
    void addAcademicStaffTest1(){
        try {
            assertEquals("Academic staff added successfully\n",
                    testAdmin.AddAcademicStaff("Rahul", "rahul@iitrpr.ac.in","2022-12-12","9897946400","IIT ROPAR"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(6)
    void addAcademicStaffTest2(){
        try {
            assertEquals("Name is invalid\n",
                    testAdmin.AddAcademicStaff("Rahul@123", "rahul@iitrpr.ac.in","2022-12-12","9897946400","IIT ROPAR"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(7)
    void addAcademicStaffTest3() {
        try {
            assertEquals("Email is invalid\n",
                    testAdmin.AddAcademicStaff("Rahul", "rahul@gmail.ac.in", "2022-12-12", "9897946400", "IIT ROPAR"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(8)
    void addAcademicStaffTest4() {
        try {
            assertEquals("Phone number is invalid\n",
                    testAdmin.AddAcademicStaff("Rahul", "rahul@iitrpr.ac.in", "2022-12-12", "19897946400", "IIT ROPAR"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @Order(9)
    void addAcademicStaffTest5() {
        try {
            assertEquals("Phone number is invalid\n",
                    testAdmin.AddAcademicStaff("Rahul", "rahul@iitrpr.ac.in", "2022-12-12", "abcd", "IIT ROPAR"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @Order(10)
    void addAcademicStaffTest6() {
        try {
            assertEquals("Joining date is invalid\n",
                    testAdmin.AddAcademicStaff("Rahul", "rahul@iitrpr.ac.in", "20-12-12", "9897946400", "IIT ROPAR"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @Order(11)
    void addAcademicStaffTest7() {
        try {
            assertEquals("Address is invalid\n",
                    testAdmin.AddAcademicStaff("Rahul", "rahul@iitrpr.ac.in", "2020-12-12", "9897946400", "  "));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @Order(12)
    void addAcademicStaffTest8(){
        try {
            assertEquals("Academic staff already exists\n",
                    testAdmin.AddAcademicStaff("Rahul", "rahul2@iitrpr.ac.in","2022-12-12","9897946400","IIT ROPAR"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @Order(13)
    void addAcademicStaffTest9(){
        try {
            assertEquals("Academic staff already exists\n",
                    testAdmin.AddAcademicStaff("Rahul", "rahul@iitrpr.ac.in","2022-12-12","8897946400","IIT ROPAR"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @Order(14)
    void addAcademicStaffTest10(){
        try {
            assertEquals("Name is invalid\n",
                    testAdmin.AddAcademicStaff("   ", "rahul@iitrpr.ac.in","2022-12-12","9897946400","IIT ROPAR"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(15)
    void addStudentTest1(){
        try{
            assertEquals("Name is invalid",
                    testAdmin.AddStudent("Abhay@123","2020csb2000@iitrpr.ac.in","2020csb2000","CSE"
                            ,"2020","9897946400","IIT ROPAR"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(16)
    void addStudentTest2(){
        try{
            assertEquals("Email is invalid",
                    testAdmin.AddStudent("Abhay","20csb00@iitrpr.ac.in","2020csb2000","CSE"
                            ,"2020","9897946400","IIT ROPAR"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @Order(17)
    void addStudentTest3(){
        try{
            assertEquals("Entry number is invalid",
                    testAdmin.AddStudent("Abhay","2020csb2000@gmail.in","20200csb1023","CSE"
                            ,"2020","9897abc400","IIT ROPAR"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(18)
    void addStudentTest4(){
        try{
            assertEquals("Department is invalid",
                    testAdmin.AddStudent("Abhay","2020csb2000@gmail.in","2020csb2000","CSE & Electrical"
                            ,"2020","9897946400","IIT ROPAR"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(19)
    void addStudentTest5(){
        try{
            assertEquals("Phone Number is invalid",
                    testAdmin.AddStudent("Abhay","2020csb2000@gmail.in","2020csb2000","CSE"
                            ,"2020","01332-25691","IIT ROPAR"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(20)
    void addStudentTest6(){
        try{
            assertEquals("Batch is invalid",
                    testAdmin.AddStudent("Abhay","2020csb2000@gmail.in","2020csb2000","CSE"
                            ,"2019-S","9897946400","IIT ROPAR"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(21)
    void addStudentTest7(){
        try{
            assertEquals("Name is invalid",
                    testAdmin.AddStudent("   ","2020csb2000@gmail.in","2020csb2000","CSE"
                            ,"2019","9897946400","IIT ROPAR"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @Order(21)
    void addStudentTest8(){
        try{
            assertEquals("Address is invalid",
                    testAdmin.AddStudent("Abhay","2020csb2000@gmail.in","2020csb2000","CSE"
                            ,"2019","9897946400","   "));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }






// DELETE FROM departments WHERE name='CSE';
// DELETE FROM acad_empl WHERE email='rahul@iitrpr.ac.in';
// DELETE FROM passwords WHERE email='rahul@iitrpr.ac.in';
// DELETE FROM STUDENTS WHERE email='2020csb2000@iitrpr.ac.in

}