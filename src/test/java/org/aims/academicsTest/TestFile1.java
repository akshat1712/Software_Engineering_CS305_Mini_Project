package org.aims.academicsTest;

import org.aims.service.AcademicEmployeeService;
import org.aims.service.FacultyService;
import org.aims.userimpl.AcademicEmployeeImpl;
import org.aims.userimpl.FacultyImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestFile1 {

    private static AcademicEmployeeImpl mockAcademicEmployeeImpl;
    private static AcademicEmployeeService testAcademicService;

    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    @BeforeAll
    public static void setup() {
        mockAcademicEmployeeImpl = mock( AcademicEmployeeImpl.class);
        testAcademicService = new AcademicEmployeeService(mockAcademicEmployeeImpl);
        System.setOut(new PrintStream(outContent));

    }

    @AfterAll
    public static void teardown() {
        System.setOut(originalOut);
    }

    @Test
    @Order(1)
    public void testLogin1(){
        when(mockAcademicEmployeeImpl.login("ACAD_1@iitrpr.ac.in","1234")).thenReturn(true);
        assertTrue(testAcademicService.login("ACAD_1@iitrpr.ac.in","1234"));
    }

    @Test
    @Order(2)
    public void testLogin2(){
        when(mockAcademicEmployeeImpl.login("ACAD_1@iitrpr.ac.in","1234")).thenReturn(false);
        assertTrue(!testAcademicService.login("ACAD_1@iitrpr.ac.in","1234"));
    }

    @Test
    @Order(3)
    public void testGradeSubmission(){
        when(mockAcademicEmployeeImpl.startGradeSubmission()).thenReturn("Grade Submission Started");
        String input="K\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        testAcademicService.showmenu();
        assertTrue(outContent.toString().contains("Grade Submission Started"));
    }


    @Test
    @Order(4)
    public void testCheckGraduation(){
        when(mockAcademicEmployeeImpl.checkGraduation("2020csb1068@iitrpr.ac.in")).thenReturn("Student can Graduate");
        String input="J\n2020csb1068@iitrpr.ac.in\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        testAcademicService.showmenu();
        assertTrue(outContent.toString().contains("Student can Graduate"));
    }

    @Test
    @Order(5)
    public void testCreateCourseTypes(){
        when(mockAcademicEmployeeImpl.createCourseTypes("NEW ELECTIVE","NE")).thenReturn("Course Type Created Successfully");
        String input="I\nNEW ELECTIVE\nNE\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        testAcademicService.showmenu();
        assertTrue(outContent.toString().contains("Course Type Created Successfully"));
    }


    @Test
    @Order(6)
    public void testChangePassword() {
        when(mockAcademicEmployeeImpl.changePassword("1234","123")).thenReturn("Password Changed Successfully");
        String input="G\n1234\n123\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        testAcademicService.showmenu();
        assertTrue(outContent.toString().contains("Password Changed Successfully"));
    }

    @Test
    @Order(7)
    public void testViewGrades1(){
        when(mockAcademicEmployeeImpl.viewGrades("2020csb1068@iitrpr.ac.in")).thenReturn(new String[]{"CS100 || 1 || 2022 || 8","CS104 || 1 || 2021 || 9"});
        String input="E\n2020csb1068@iitrpr.ac.in\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        testAcademicService.showmenu();
        assertTrue(outContent.toString().contains("CS100 || 1 || 2022 || 8"));
        assertTrue(outContent.toString().contains("CS104 || 1 || 2021 || 9"));
    }

    @Test
    @Order(8)
    public void testViewGrades2(){
        when(mockAcademicEmployeeImpl.viewGrades("2020csb1068@iitrpr.ac.in")).thenReturn(null);
        String input="E\n2020csb1068@iitrpr.ac.in\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        testAcademicService.showmenu();
        assertTrue(outContent.toString().contains("No Grades Available"));
    }


    @Test
    @Order(9)
    public void testEndSemester(){
        when(mockAcademicEmployeeImpl.endSemester()).thenReturn("Semester Ended");
        String input="D\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        testAcademicService.showmenu();
        assertTrue(outContent.toString().contains("Semester Ended"));
    }


    @Test
    @Order(10)
    public void testStartSemester(){
        when(mockAcademicEmployeeImpl.startSemester(2022,"2")).thenReturn("Semester Started Successfully");
        String input="C\n2\nNEW_SEMESTER\n2022\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        testAcademicService.showmenu();
        assertTrue(outContent.toString().contains("Semester Started Successfully"));
    }


    @Test
    @Order(11)
    public void testGenerateReport() throws Exception{
        Map<String,String[]> res=new HashMap<>();
        res.put("2020csb5000",new String[]{"CS100 || 8 || 1 || 2021","CS104 || 10 || 1 || 2021"});
        when(mockAcademicEmployeeImpl.generateReport()).thenReturn(res);

        String input="H\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        testAcademicService.showmenu();
    }

    // HAVE TO SEE WHY THIS IS NOT WORKING
    @Test
    @Order(12)
    public void testInsertCatalog1() {
        String [] prereq=new String[]{"CS100","CS99"};
        when(mockAcademicEmployeeImpl.addCourseInCatalog("CS100","TEST","CSE",1,1,1,1,1.0,prereq)).thenReturn("Course Added Successfully");
        String input="B\nCS100\nTEST\nCSE\n1\n1\n1\n1\n1.0\n1\nCS99\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        testAcademicService.showmenu();
        assertTrue(outContent.toString().contains("Course Added Successfully"));
    }

    @Test
    @Order(13)
    public void testInsertCatalog2() {
        String input="B\nCS100\nTEST\nCSE\nHELLO\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        testAcademicService.showmenu();
        assertTrue(outContent.toString().contains("Error in Adding Course in Catalog"));
    }


    @Test
    @Order(14)
    public void testCreateCurriculum1() throws Exception {
        when(mockAcademicEmployeeImpl.createCurriculum(2020,new String[]{"CS101 PC"},new String[]{"PC 3"},"CSE")).thenReturn("Curriculum Created Successfully");
        String input="F\n2020\nCSE\n1\nCS101 PC\n1\nPC 3\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        testAcademicService.showmenu();
        assertTrue(outContent.toString().contains("Curriculum Created Successfully"));
    }


    @Test
    @Order(15)
    public void testCreateCurriculum2() throws Exception {
        String input="F\n2020\nCSE\nCSE\nA\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        testAcademicService.showmenu();
        assertTrue(outContent.toString().contains("Error in Creating Curriculum"));
    }

}
