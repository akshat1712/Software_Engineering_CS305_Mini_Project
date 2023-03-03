package org.aims.adminTest;

import org.aims.service.AdminService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestFile1 {

    AdminService testAdmin=new AdminService();

    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;


    @BeforeAll
    public static void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }
    @AfterAll
    public static void restoreStreams() {
        System.setOut(originalOut);

        try {
            String connectionString = "jdbc:postgresql://localhost:5432/postgres";
            String username = "postgres";
            String databasePassword = "2020csb1068";
            Connection con = DriverManager.getConnection(connectionString, username, databasePassword);


            con.createStatement().execute("DELETE FROM departments WHERE name='CSE'");
        }
        catch (Exception e){
            System.out.println(e);
        }


    }
    String correctEmail="postgres@iitrpr.ac.in";
    String correctPassword="2020csb1068";

    @Test
    @Order(1)
    public void testLogin1(){
        assertTrue(testAdmin.login("postgres@iitrpr.ac.in","2020csb1068"));
    }
    @Test
    @Order(2)
    public void testLogin2(){
        assertFalse(testAdmin.login("postgres@iitrpr.ac.in","2020csbadmin"));
    }
    @Test
    @Order(3)
    public void testLogin3(){
        assertFalse(testAdmin.login("postgres@gmail.com","2020csb1068"));
    }
    @Test
    @Order(4)
    public void testLogin4(){
        assertFalse(testAdmin.login("postgres@gmail.com","2020csbadmin"));
    }


    @ParameterizedTest
    @CsvSource({"A"})
    @Order(5)
    public void testMenu1(String chosen_1){
        String input=chosen_1+System.lineSeparator();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        testAdmin.showmenu();
        assertTrue(outContent.toString().contains("Logging out"));
    }


    @ParameterizedTest
    @CsvSource({"-1","7","1.2","CHOSEN"})
    @Order(6)
    public void testMenu2(String chosen_1){
        String input=chosen_1+"\n"+"A"+"\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        testAdmin.showmenu();
        assertTrue(outContent.toString().contains("INVALID OPTION"));
    }

    @ParameterizedTest
    @CsvSource({"E,CSE,1","E,CSE,2"})
    @Order(6)
    public void testAddDepartment2(String chose,String Department,int type){
        String input=chose+"\n"+Department+"\nA\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        testAdmin.login(correctEmail,correctPassword);
        testAdmin.showmenu();
        if(type==1){
            assertTrue(outContent.toString().contains("Department added successfully"));
        }
        else{
            assertTrue(outContent.toString().contains("Department already exists"));
        }
    }



}
