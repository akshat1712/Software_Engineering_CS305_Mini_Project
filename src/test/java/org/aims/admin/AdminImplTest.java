package org.aims.admin;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AdminImplTest {

    AdminImpl TestAdmin1;
    AdminImpl TestAdmin2;
    {
        try {
            TestAdmin1 = new AdminImpl("postgres@iitrpr.ac.in", "2020csb1068");
            TestAdmin2 = new AdminImpl("postgres2@iitrpr.ac.in", "2020csb1068");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    void loginTest1() throws SQLException {
        assertTrue(TestAdmin1.login());
    }

    @Test
    @Order(2)
    void loginTest2() throws SQLException {
        assertFalse(TestAdmin2.login());
    }


    @Test
    @Order(3)
    void addDepartmentTest1() throws SQLException {
        String name= "";
        String response=TestAdmin1.AddDepartment(name);

    }

    @Test
    @Order(4)
    void addDepartmentTest2() {
        String name = "CSE";
    }

}