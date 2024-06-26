
CREATE OR REPLACE FUNCTION INSERT_DEPARTMENT(
    dept_name VARCHAR(50)
)
RETURNS INTEGER
LANGUAGE plpgsql
AS $$
    BEGIN
        INSERT INTO departments ("name")
        VALUES (dept_name);
        RETURN 1;
    END;
$$;


CREATE OR REPLACE FUNCTION INSERT_STUDENT(
    student_name VARCHAR(50),
    entry_number VARCHAR(15),
    email VARCHAR(50),
    dept_id INTEGER,
    batch INTEGER,
    phone_number VARCHAR(10),
    address VARCHAR(100)
)
RETURNS INTEGER
LANGUAGE plpgsql
AS $$
    BEGIN
        INSERT INTO students ("name", "entry_number", "email", "dept_id", "batch", "phone_number", "address")
        VALUES (student_name, entry_number, email, dept_id, batch, phone_number, address);
        RETURN 1;
    END;
$$;

CREATE OR REPLACE FUNCTION INSERT_FACULTY(
    faculty_name VARCHAR(50),
    email VARCHAR(50),
    dept_id INTEGER,
    joining_date DATE,
    phone_number VARCHAR(10),
    address VARCHAR(100)
)
RETURNS INTEGER
LANGUAGE plpgsql
AS $$
    BEGIN
        INSERT INTO faculties ("name", "email", "dept_id", "joining_date", "phone_number", "address")
        VALUES (faculty_name, email, dept_id, joining_date, phone_number, address);
        RETURN 1;
    END;
$$;


CREATE OR REPLACE FUNCTION INSERT_ACADEMIC_EMPLOYEE(
    name VARCHAR(50),
    email VARCHAR(50),
    joining_date DATE,
    phone_number VARCHAR(10),
    address VARCHAR(100)
)
RETURNS INTEGER
LANGUAGE plpgsql
AS $$
    BEGIN
        INSERT INTO acad_empl("name", "email", "joining_date", "phone_number", "address")
        VALUES (name, email,joining_date, phone_number, address);
        RETURN 1;
    END;
$$;


CREATE OR REPLACE FUNCTION INSERT_LOGIN_LOG(
    email VARCHAR(50),
    login_time TIMESTAMP,
    logout_time TIMESTAMP
)
RETURNS INTEGER
LANGUAGE plpgsql
AS $$
    BEGIN
        INSERT INTO login_logs ("email", "login_time", "logout_time")
        VALUES (email, login_time, logout_time);
        RETURN 1;
    END;
$$;

CREATE OR REPLACE FUNCTION INSERT_PASSWORD(
    email VARCHAR(50),
    password VARCHAR(50),
    role VARCHAR(10)
)
RETURNS INTEGER
LANGUAGE plpgsql
AS $$
    BEGIN
        INSERT INTO passwords ("email", "password", "role")
        VALUES (email, password, role);
        RETURN 1;
    END;
$$;


CREATE OR REPLACE FUNCTION INSERT_COURSE_CATALOG(
    course_name VARCHAR(50),
    course_code VARCHAR(5),
    dept_id INTEGER,
    lectures INTEGER,
    tutorials INTEGER,
    practicals INTEGER,
    self_study INTEGER,
    credits REAL
)
RETURNS INTEGER
LANGUAGE plpgsql
AS $$
    BEGIN
        INSERT INTO courses_catalog ("course_name", "course_code", "dept_id", "lectures", "tutorials", "practicals", "self_study", "credits")
        VALUES (course_name, course_code, dept_id, lectures, tutorials, practicals, self_study, credits);
        RETURN 1;
    END;
$$;


CREATE OR REPLACE FUNCTION INSERT_COURSE_OFFERED(
    catalog_id INTEGER,
    faculty_id INTEGER,
    course_code VARCHAR(5),
    CGPA_cutoff REAL
)
RETURNS INTEGER
LANGUAGE plpgsql
AS $$
    BEGIN
        INSERT INTO courses_offering ("catalog_id", "faculty_id", "course_code", "CGPA")
        VALUES (catalog_id, faculty_id, course_code, CGPA_cutoff);
        RETURN 1;
    END;
$$;






CREATE OR REPLACE FUNCTION STUDENT_TABLE_CREATION()
RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
    DECLARE
    BEGIN
            EXECUTE '
                CREATE TABLE IF NOT EXISTS transcript_student_'||new.student_id||' (
                "catalog_id" INTEGER NOT NULL,
                "grade" VARCHAR(2) NOT NULL,
                "semester" VARCHAR(1) NOT NULL,
                "year" VARCHAR(4) NOT NULL,
                FOREIGN KEY ("catalog_id") REFERENCES "courses_catalog" ("catalog_id"),
                PRIMARY KEY ( "catalog_id", "semester", "year")
                );
            ';

            EXECUTE '
                CREATE TABLE IF NOT EXISTS courses_enrolled_student_'||new.student_id||' (
                "catalog_id" INTEGER NOT NULL,
                "grade" VARCHAR(2) ,
                FOREIGN KEY ("catalog_id") REFERENCES "courses_catalog" ("catalog_id"),
                PRIMARY KEY ( "catalog_id")
                );
            ';
        RETURN NEW;
    END
$$;


CREATE OR REPLACE TRIGGER CREATE_STUDENT_RECORDS_TABLES
AFTER INSERT ON students
FOR EACH ROW EXECUTE PROCEDURE STUDENT_TABLE_CREATION();



CREATE OR REPLACE FUNCTION FACULTY_TABLE_CREATION()
RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
    DECLARE
    BEGIN
            EXECUTE '
                CREATE TABLE IF NOT EXISTS transcript_faculty_'||new.faculty_id||' (
                "catalog_id" INTEGER NOT NULL,
                "semester" VARCHAR(1) NOT NULL,
                "year" VARCHAR(4) NOT NULL,
                FOREIGN KEY ("catalog_id") REFERENCES "courses_catalog" ("catalog_id"),
                PRIMARY KEY ( "catalog_id", "semester", "year")
                );
            ';

            EXECUTE '
                CREATE TABLE IF NOT EXISTS courses_teaching_faculty_'||new.faculty_id||' (
                "catalog_id" INTEGER NOT NULL,
                FOREIGN KEY ("catalog_id") REFERENCES "courses_catalog" ("catalog_id"),
                PRIMARY KEY ( "catalog_id")
                );
            ';
        RETURN NEW;
    END
$$;


CREATE OR REPLACE TRIGGER CREATE_FACULTY_RECORDS_TABLES
AFTER INSERT ON faculties
FOR EACH ROW EXECUTE PROCEDURE FACULTY_TABLE_CREATION();





CREATE OR REPLACE FUNCTION BATCH_TABLE_CREATION(
    batch_id INTEGER
)
RETURNS INTEGER
LANGUAGE plpgsql
AS
$$
    DECLARE
    BEGIN


            EXECUTE '
                CREATE TABLE IF NOT EXISTS batch_curriculum_'||batch_id||' (
                "department_id" INTEGER NOT NULL,
                "catalog_id" INTEGER NOT NULL,
                "type" VARCHAR(2) NOT NULL,
                FOREIGN KEY ("catalog_id") REFERENCES "courses_catalog" ("catalog_id"),
                FOREIGN KEY ("department_id") REFERENCES "departments" ("dept_id"),
                FOREIGN KEY ("type") REFERENCES "course_types" ("type_alias")
                PRIMARY KEY ("department_id", "catalog_id", "type")
                );
            ';

            EXECUTE '
                CREATE TABLE IF NOT EXISTS batch_credits_'||batch_id||' (
                "department_id" INTEGER NOT NULL ,
                "type" VARCHAR(2) NOT NULL,
                "credits" VARCHAR(4) NOT NULL,
                FOREIGN KEY ("department_id") REFERENCES "departments" ("dept_id"),
                FOREIGN KEY ("type") REFERENCES "course_types" ("type_alias")
                PRIMARY KEY ("department_id", "type")
                );
            ';

            EXECUTE'
                INSERT INTO batch ("batch") VALUES ('||batch_id||');
            ';
        RETURN 1;
    END
$$;


--