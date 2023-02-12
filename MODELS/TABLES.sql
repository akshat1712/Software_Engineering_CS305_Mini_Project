CREATE TABLE IF NOT EXISTS "departments" (
    "dept_id" SERIAL PRIMARY KEY,
    "name" VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS "students" (
    "student_id" SERIAL PRIMARY KEY,
    "name" VARCHAR(50) NOT NULL,
    "entry_number" VARCHAR(15) NOT NULL UNIQUE,
    "email" VARCHAR(50) NOT NULL UNIQUE,
    "dept_id" INTEGER NOT NULL,
    "batch" INTEGER NOT NULL,
    "phone_number" VARCHAR(10) NOT NULL UNIQUE,
    "address" VARCHAR(100) NOT NULL,
    FOREIGN KEY ("dept_id") REFERENCES "departments" ("dept_id")
);

CREATE TABLE IF NOT EXISTS "faculties" (
    "faculty_id" SERIAL PRIMARY KEY,
    "name" VARCHAR(50) NOT NULL,
    "email" VARCHAR(50) NOT NULL UNIQUE,
    "dept_id" INTEGER NOT NULL,
    "joining_date" DATE NOT NULL,
    "phone_number" VARCHAR(10) NOT NULL UNIQUE,
    "address" VARCHAR(100) NOT NULL,
    FOREIGN KEY ("dept_id") REFERENCES "departments" ("dept_id")
);

CREATE TABLE IF NOT EXISTS "acad_empl" (
    "empl_id" SERIAL PRIMARY KEY,
    "name" VARCHAR(50) NOT NULL,
    "email" VARCHAR(50) NOT NULL UNIQUE,
    "joining_date" DATE NOT NULL,
    "phone_number" VARCHAR(10) NOT NULL UNIQUE,
    "address" VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS "login_logs" (
    "log_id" SERIAL PRIMARY KEY,
    "email" VARCHAR(50) NOT NULL,
    "login_time" TIMESTAMP NOT NULL,
    "logout_time" TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "passwords" (
    "email" VARCHAR(50) NOT NULL,
    "password" VARCHAR(50) NOT NULL,
    "role" VARCHAR(10) NOT NULL,
    PRIMARY KEY ("email")
);

--  In pre_req, it will be course followed by grade

CREATE TABLE IF NOT EXISTS "courses_catalog" (
    "catalog_id" SERIAL PRIMARY KEY,
    "course_code" VARCHAR(5) NOT NULL,
    "course_name" VARCHAR(50) NOT NULL,
    "dept_id" INTEGER NOT NULL,
    "lectures" VARCHAR(1) NOT NULL,
    "tutorials" VARCHAR(1) NOT NULL,
    "practicals" VARCHAR(1) NOT NULL,
    "self_study" VARCHAR(1) NOT NULL,
    "credits" VARCHAR(1) NOT NULL,
    FOREIGN KEY ("dept_id") REFERENCES "departments" ("dept_id")
);

CREATE TABLE IF NOT EXISTS "courses_pre_req" (
    "pre_req_id" SERIAL PRIMARY KEY,
    "catalog_id" INTEGER NOT NULL,
    "pre_req" VARCHAR(5) NOT NULL,
    "grade" VARCHAR(2) NOT NULL,
    FOREIGN KEY ("catalog_id") REFERENCES "courses_catalog" ("catalog_id")
);


CREATE TABLE IF NOT EXISTS "courses_offering" (
    "offering_id" SERIAL PRIMARY KEY,
    "catalog_id" INTEGER,
    "faculty_id" INTEGER NOT NULL,
    "course_code" VARCHAR(5) NOT NULL UNIQUE,
    "CGPA" VARCHAR(1) NOT NULL,
    FOREIGN KEY ("catalog_id") REFERENCES "courses_catalog" ("catalog_id"),
    FOREIGN KEY ("faculty_id") REFERENCES "faculties" ("faculty_id")
);

CREATE TABLE IF NOT EXISTS "capstone" (
    "student_id" INTEGER NOT NULL,
    "faculty_id" INTEGER NOT NULL,
    FOREIGN KEY ("student_id") REFERENCES "students" ("student_id"),
    FOREIGN KEY ("faculty_id") REFERENCES "faculties" ("faculty_id"),
    PRIMARY KEY ("student_id")
);

CREATE TABLE IF NOT EXISTS "time_semester"(
    "semester" VARCHAR(1) NOT NULL,
    "year" INTEGER NOT NULL,
    "status" VARCHAR(10) NOT NULL,
    PRIMARY KEY ("semester", "year")
);


-- FOR EACH BATCH, I have to make a table for each batch which tells the credits requirement for passing the
-- engineering and

-- THIS IS FOR ONLY COURSES, THERE IS DIFFERENT TABLE FOR CREDITS

-- TABLE WILL Batch_Curriculum_<YEAR>
-- COLUMNS WILL BE DEPARTMENT, COURSE_CODE,TYPE
-- TYPE WILL BE Program Core ( PC ), Program Elective ( PE ),HS CORE ( HC ) , HS ELECTIVE (HE )
-- GENERAL ENGINEERING ( GE ) , SCIENCE CORE ( SC ) , SCIENCE ELECTIVE ( SE ) , OPEN ELECTIVE ( OE )
-- CAPSTONE ( CP ), INTERNSHIP ( IN ), EXTRA-CURRICULAR ( EC )

-- TABLE WILL be credits_curriculum_<YEAR>
-- COLUMNS WILL BE DEPARTMENT, TYPE, CREDITS