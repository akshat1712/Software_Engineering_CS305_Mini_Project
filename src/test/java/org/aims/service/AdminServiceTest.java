package org.aims.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class AdminServiceTest {

    AdminService test=new AdminService();


    private final static InputStream systemIn = System.in;
    private final static PrintStream systemOut = System.out;
    private ByteArrayInputStream typeIn;
    private static ByteArrayOutputStream typeOut;


    @BeforeEach
    void setUp() throws Exception {
        typeOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(typeOut));
    }
    @AfterEach
    void tearDown() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }


    @Test
    void Showmenu_Department_Test1() {
        String input = "4"+System.lineSeparator()+"CSE"+System.lineSeparator()+"0"+System.lineSeparator();

        System.setIn(new ByteArrayInputStream(input.getBytes()));


        test.showmenu();


        String output = typeOut.toString();
        assertTrue(output.contains("Department Added Successfully"));

    }

}