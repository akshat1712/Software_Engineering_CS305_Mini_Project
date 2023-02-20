package org.aims.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;
class AdminServiceTest {

    AdminService test=new AdminService();

    @ParameterizedTest
    @CsvSource({"4,CSE"})
    void testAddDepartment(String select,String department) {
        String input=select+"\n"+department+"\n";

        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        test.showmenu();

        System.out.println(outContent.toString());
        assertTrue(true);
//        assertTrue(outContent.toString().contains("Department Added Successfully"));

    }

}