ENTRY NUMBER: 2020CSB1068
NAME: AKSHAT TOOLAJ SINHA

File Structure:

1.  We have Main File as stand along file in org.aims containing the entry method to our application
2.  We have a package org.aims.dataAccess which contains ways to access the database using predefined methods
3.  We have a package org.aims.service which contains the CLI component i.e. client layer
4.  We have a package org.aims.userimpl which contains the business login of our application, it can
    viewed as the server layer

    Each Class file in service is an implementation of UserService
    Each Class file in userimpl is an implementation of UserDAL