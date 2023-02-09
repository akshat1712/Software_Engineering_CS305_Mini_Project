package org.example;
import java.sql.*;
import java.util.Scanner;
public class Login {


    private boolean checklogin(String query, Connection con){
        try{
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(query);

            return rs.next();
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }

    }
    public void loginForm(){
        String Email;
        String Password;
        int Type;
        Scanner sc= new Scanner(System.in);


        System.out.print("Enter Your Email Address: ");
        Email = sc.nextLine();

        System.out.print("Enter Your Password: ");
        Password = sc.nextLine();

        System.out.println("Your Type of Account\n [1] Academic Employee\n [2] Faculty \n [3] Student \n");
        System.out.print("Please enter your choice: ");
        Type=sc.nextInt();

        if(Type!=1 & Type!=2 & Type!=3){
            System.out.println("Please Enter a Correct number");
            loginForm();
        }
        else{
            try {
                Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "2020csb1068");
                if( Type==1){
                    String query = String.format("SELECT empl_id FROM acad_empl WHERE email ='%s' AND password ='%s';",Email,Password);
                    if( checklogin(query,con)){
                        System.out.println("LOGIN SUCCESFUL");
                        user acadEmpl = new AcademicEmployee();
                        acadEmpl.showmenu();
                    }
                    else {
                        System.out.println("LOGIN UNSUCCESFUL");
                    }

                } else if (Type==2){
                    String query = String.format("SELECT faculty_id FROM faculties WHERE email ='%s' AND password ='%s';",Email,Password);
                    if( checklogin(query,con)){
                        System.out.println("LOGIN SUCCESFUL");
                        user faculty = new Faculty();
                        faculty.showmenu();
                    }
                    else {
                        System.out.println("LOGIN UNSUCCESFUL");
                    }

                } else{
                    String query= String.format("SELECT student_id FROM students WHERE email ='%s' AND password ='%s';",Email,Password);
                    if( checklogin(query,con)){
                        System.out.println("LOGIN SUCCESFUL");
                        user student = new Student();
                        student.showmenu();
                    }
                    else {
                        System.out.println("LOGIN UNSUCCESFUL");
                    }
                }
                con.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
