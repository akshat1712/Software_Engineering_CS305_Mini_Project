ENTRY NUMBER: 2020CSB1068
NAME: AKSHAT TOOLAJ SINHA

File Structure:

1.  We have Main File as stand along file in org.aims containing the entry method to our application
2.  We have a package org.aims.dataAccess which contains ways to access the database using predefined methods
3.  We have a package org.aims.service which contains the CLI component i.e. client layer
4.  We have a package org.aims.userimpl which contains the business login of our application, it can
    be viewed as the server layer

    Each Class file in service is an implementation of UserService
    Each Class file in userimpl is an implementation of UserDAL


For Running,

1.  Firstly, make the Mini_Project as the current working directory,
2.  We also need to initialize table and functions in the database, Those Commands are present in
    ./MODELS . There are two files, Firstly Copy the content of TABLES.sql in psql terminal and
    then copy the content of FUNCTIONS.sql in psql terminal.
3.  We also need to change "data_base_url" , "username" and "password" corresponding to your
    configuration of Postgres.
4.  Please make the required changes in ./src/main/resources/config.properties and ./src/test/resources/config.properties
5.  Then run the command ./gradlew build to build the project and run the test cases
6.  Then run the command ./gradlew --console=plain run to run the application
7.  Then follow the instructions on the screen

During Testing & Running , Many Lines will be printed on Console, Please Ignore them, They are for showing the working of the application.

I have also attached Multiple folders/files containing UML, Jacoco Test Coverage Report and other information