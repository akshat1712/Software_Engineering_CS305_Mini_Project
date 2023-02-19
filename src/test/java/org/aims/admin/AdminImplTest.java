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
            assertEquals("Department name is Empty\n", testAdmin.AddDepartment(" "));
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





}