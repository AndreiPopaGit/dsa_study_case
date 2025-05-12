-- === BASE VIEW DEFINITIONS (MUST RUN FIRST) ===

CREATE OR REPLACE TEMP VIEW students_view AS
WITH json_view AS (
  SELECT from_json(json_raw.data,
    'ARRAY<STRUCT<studentId: INT, name: STRING, email: STRING, enrollmentDate: STRING>>') array
  FROM (SELECT java_method(
    'org.spark.service.rest.QueryRESTDataService',
    'getRESTDataDocument',
    'http://localhost:8090/DSA-SQL-JDBCService/rest/students/StudentView') as data) json_raw
)
SELECT v.* FROM json_view LATERAL VIEW explode(json_view.array) AS v;

CREATE OR REPLACE TEMP VIEW courses_view AS
WITH json_view AS (
  SELECT from_json(json_raw.data,
    'ARRAY<STRUCT<courseId: INT, courseName: STRING, credits: INT>>') array
  FROM (SELECT java_method(
    'org.spark.service.rest.QueryRESTDataService',
    'getRESTDataDocument',
    'http://localhost:8090/DSA-SQL-JDBCService/rest/students/courses/CourseView') as data) json_raw
)
SELECT v.* FROM json_view LATERAL VIEW explode(json_view.array) AS v;

CREATE OR REPLACE TEMP VIEW enrollments_view AS
WITH json_view AS (
  SELECT from_json(json_raw.data,
    'ARRAY<STRUCT<enrollmentId: INT, studentId: INT, courseId: INT, enrollDate: STRING>>') array
  FROM (SELECT java_method(
    'org.spark.service.rest.QueryRESTDataService',
    'getRESTDataDocument',
    'http://localhost:8090/DSA-SQL-JDBCService/rest/students/enrollments/EnrollmentView') as data) json_raw
)
SELECT v.* FROM json_view LATERAL VIEW explode(json_view.array) AS v;

-- === FACT TABLE: Enrollments ===
CREATE OR REPLACE TEMP VIEW OLAP_FACTS_ENROLLMENTS AS
SELECT
    studentId,
    courseId,
    enrollDate
FROM enrollments_view;

-- === DIMENSIONS ===
CREATE OR REPLACE TEMP VIEW OLAP_DIM_STUDENTS AS
SELECT
    studentId,
    name AS studentName,
    email
FROM students_view;

CREATE OR REPLACE TEMP VIEW OLAP_DIM_COURSES AS
SELECT
    courseId,
    courseName,
    credits
FROM courses_view;

-- === ANALYTICAL JOIN: Students with Courses via Enrollments ===
CREATE OR REPLACE TEMP VIEW OLAP_VIEW_ENROLLMENTS_DETAIL AS
SELECT
    s.studentName,
    s.email,
    c.courseName,
    c.credits,
    f.enrollDate
FROM OLAP_FACTS_ENROLLMENTS f
         INNER JOIN OLAP_DIM_STUDENTS s ON f.studentId = s.studentId
         INNER JOIN OLAP_DIM_COURSES c ON f.courseId = c.courseId;

-- === Aggregation: Enrollments Count by Course ===
CREATE OR REPLACE TEMP VIEW OLAP_VIEW_ENROLL_COUNT_BY_COURSE AS
SELECT
    c.courseName,
    COUNT(*) AS enrollmentCount
FROM OLAP_FACTS_ENROLLMENTS f
         INNER JOIN OLAP_DIM_COURSES c ON f.courseId = c.courseId
GROUP BY c.courseName;

-- === Aggregation: Enrollments Count by Month ===
CREATE OR REPLACE TEMP VIEW OLAP_VIEW_ENROLL_COUNT_BY_MONTH AS
SELECT
    year(enrollDate) AS year,
    month(enrollDate) AS month,
    COUNT(*) AS monthlyEnrollments
FROM OLAP_FACTS_ENROLLMENTS
GROUP BY year(enrollDate), month(enrollDate)
ORDER BY year, month;

-- === Aggregation: Enrollment Count by Course and Year ===
CREATE OR REPLACE TEMP VIEW OLAP_VIEW_ENROLL_BY_COURSE_YEAR AS
SELECT
    c.courseName,
    year(f.enrollDate) AS year,
    COUNT(*) AS totalEnrollments
FROM OLAP_FACTS_ENROLLMENTS f
    INNER JOIN OLAP_DIM_COURSES c ON f.courseId = c.courseId
GROUP BY c.courseName, year(f.enrollDate)
ORDER BY year, totalEnrollments DESC;

-- === Aggregation: Average Credits per Student ===
CREATE OR REPLACE TEMP VIEW OLAP_VIEW_AVG_CREDITS_PER_STUDENT AS
SELECT
    s.studentName,
    AVG(c.credits) AS avgCredits
FROM OLAP_FACTS_ENROLLMENTS f
         INNER JOIN OLAP_DIM_STUDENTS s ON f.studentId = s.studentId
         INNER JOIN OLAP_DIM_COURSES c ON f.courseId = c.courseId
GROUP BY s.studentName
ORDER BY avgCredits DESC;

-- === TEST VIEWS ===
SELECT * FROM OLAP_FACTS_ENROLLMENTS;
SELECT * FROM OLAP_DIM_STUDENTS;
SELECT * FROM OLAP_DIM_COURSES;
SELECT * FROM OLAP_VIEW_ENROLLMENTS_DETAIL;
SELECT * FROM OLAP_VIEW_ENROLL_COUNT_BY_COURSE;
SELECT * FROM OLAP_VIEW_ENROLL_COUNT_BY_MONTH;
SELECT * FROM OLAP_VIEW_ENROLL_BY_COURSE_YEAR;
SELECT * FROM OLAP_VIEW_AVG_CREDITS_PER_STUDENT;


-- =========================XML FILE=================================--

-- === REMOTE VIEW: student_profile_view (from XML) ===
CREATE OR REPLACE TEMP VIEW student_profile_view AS
WITH json_view AS (
  SELECT from_json(json_raw.data,
      'ARRAY<STRUCT<
        studentId: INT,
        phone: STRING,
        emergencyContact: STRING,
        status: STRING,
        scholarship: INT
      >>') array
  FROM (
    SELECT java_method(
      'org.spark.service.rest.QueryRESTDataService',
      'getRESTDataDocument',
      'http://localhost:8092/DSA-DOC-XMLService/rest/profiles/StudentProfileView'
    ) AS data
  ) json_raw
)
SELECT v.*
FROM json_view LATERAL VIEW explode(json_view.array) AS v;

-- === JOIN: Students with Profile Phone Info ===
CREATE OR REPLACE TEMP VIEW OLAP_VIEW_STUDENT_CONTACTS AS
SELECT
    s.studentId,
    s.studentName,
    s.email,
    p.phone,
    p.status
FROM OLAP_DIM_STUDENTS s
         INNER JOIN student_profile_view p ON s.studentId = p.studentId;

-- === AGGREGATION: Total Scholarships by Status and First Letter of Name ===
CREATE OR REPLACE TEMP VIEW OLAP_VIEW_SCHOLARSHIP_SUMMARY AS
SELECT
    p.status,
    SUBSTRING(s.studentName, 1, 1) AS nameInitial,
    COUNT(*) AS studentCount,
    SUM(p.scholarship) AS totalScholarship,
    ROUND(AVG(p.scholarship), 2) AS avgScholarship
FROM student_profile_view p
         JOIN OLAP_DIM_STUDENTS s ON p.studentId = s.studentId
GROUP BY p.status, SUBSTRING(s.studentName, 1, 1)
ORDER BY p.status, nameInitial;


--- === XLSX file === ----

-- Link to XLS REST View for Attendance
CREATE OR REPLACE VIEW STUDENT_ATTENDANCE_VIEW AS
WITH json_view AS (
  SELECT from_json(json_raw.data,
    'ARRAY<STRUCT<studentId: INT, courseId: INT, date: STRING, status: STRING>>') array
  FROM (
    SELECT java_method(
      'org.spark.service.rest.QueryRESTDataService',
      'getRESTDataDocument',
      'http://localhost:8094/DSA-DOC-XLSService/rest/students/StudentAttendanceView') AS data
  ) json_raw
)
SELECT v.* FROM json_view LATERAL VIEW explode(json_view.array) AS v;

-- 1. Number of absences per student
CREATE OR REPLACE TEMP VIEW OLAP_VIEW_ATTENDANCE_ANALYTICS AS
SELECT
    p.studentId,
    p.name AS studentName,
    p.email,
    COUNT(*) AS totalSessions,
    SUM(CASE WHEN LOWER(a.status) = 'present' THEN 1 ELSE 0 END) AS totalPresent,
    SUM(CASE WHEN LOWER(a.status) = 'absent' THEN 1 ELSE 0 END) AS totalAbsent,
    ROUND(SUM(CASE WHEN LOWER(a.status) = 'present' THEN 1 ELSE 0 END) * 1.0 / COUNT(*), 2) AS attendanceRatio
FROM STUDENT_ATTENDANCE_VIEW a
         JOIN students_view p ON a.studentId = p.studentId
GROUP BY p.studentId, p.name, p.email
ORDER BY attendanceRatio DESC;

--Attendance Deviation per Course
CREATE OR REPLACE TEMP VIEW OLAP_VIEW_ATTENDANCE_VARIABILITY AS
SELECT
    p.studentId,
    p.name AS studentName,
    p.email,
    COUNT(DISTINCT a.courseId) AS courseCount,
    ROUND(STDDEV_POP(
                  CASE WHEN LOWER(a.status) = 'present' THEN 1.0 ELSE 0.0 END
          ), 3) AS attendanceStdDev
FROM STUDENT_ATTENDANCE_VIEW a
         JOIN students_view p ON a.studentId = p.studentId
GROUP BY p.studentId, p.name, p.email
ORDER BY attendanceStdDev DESC;

-- === NoSQL MongoDB: Skills and Professors Views ===

-- Skills View
CREATE OR REPLACE TEMP VIEW skills_taxonomy_view AS
WITH json_view AS (
SELECT from_json(json_raw.data,
'ARRAY<STRUCT<skill_id: INT, name: STRING, type: STRING, level: STRING>>') array
FROM (
SELECT java_method(
'org.spark.service.rest.QueryRESTDataService',
'getRESTDataDocument',
'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/profiles/SkillsTaxonomyView'
) AS data
) json_raw
)
SELECT v.* FROM json_view LATERAL VIEW explode(json_view.array) AS v;

-- Professors View
CREATE OR REPLACE TEMP VIEW professors_profiles_view AS
WITH json_view AS (
  SELECT from_json(json_raw.data,
    'ARRAY<STRUCT<professor_id: INT, title: STRING, expertise: ARRAY<STRING>, country: STRING>>') array
  FROM (
    SELECT java_method(
      'org.spark.service.rest.QueryRESTDataService',
      'getRESTDataDocument',
      'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/profiles/ProfessorsProfilesView'
    ) AS data
  ) json_raw
)
SELECT v.*
FROM json_view LATERAL VIEW explode(json_view.array) AS v;



-- 1. Simple query:  skills distribution
CREATE OR REPLACE TEMP VIEW OLAP_VIEW_SKILL_DISTRIBUTION AS
SELECT
    type,
    level,
    COUNT(*) AS skillCount,
    COLLECT_LIST(name) AS skillNames
FROM skills_taxonomy_view
GROUP BY type, level
ORDER BY type, level;


-- 2. OLAP: Count professors by country and title
CREATE OR REPLACE TEMP VIEW OLAP_VIEW_PROFESSOR_COUNTRY_TITLE AS
SELECT country, title, COUNT(*) AS professorCount
FROM professors_profiles_view
GROUP BY country, title
ORDER BY professorCount DESC;

-- 3. OLAP: Join students and professors by expertise match (skill names)
CREATE OR REPLACE TEMP VIEW OLAP_VIEW_STUDENT_PROFESSOR_SKILL_MATCH AS
SELECT DISTINCT
    s.studentId,
    s.studentName,
    s.email,
    p.professor_id,
    p.title,
    exp AS matchedSkill
FROM OLAP_DIM_STUDENTS s
         CROSS JOIN professors_profiles_view p
    LATERAL VIEW explode(p.expertise) AS exp
ORDER BY s.studentId, matchedSkill;

