# ✅ Full DSA OLAP Integration Summary – Labs 6 to 9

---

## 🔵 Lab 6 – PostgreSQL JDBC Integration with SparkSQL

This lab focused on exposing PostgreSQL data using REST and integrating it into SparkSQL as views.

### 1. Data Services via DSA-SQL-JDBCService

REST endpoints were configured using SpringBoot:

- **StudentView**: students metadata
- **CourseView**: course info
- **EnrollmentView**: enrollment facts (student–course relationship)

### 2. SparkSQL Views and OLAP Queries

**View: OLAP_FACTS_ENROLLMENTS**  
→ Fact table built from EnrollmentView.

**View: OLAP_DIM_STUDENTS & OLAP_DIM_COURSES**  
→ Dimensional tables from StudentView and CourseView.

**View: OLAP_VIEW_ENROLLMENTS_DETAIL**  
→ Join facts with dimensions: see student, course, and enrollment info.

**OLAP_VIEW_ENROLL_COUNT_BY_COURSE**  
→ Aggregate count of enrollments per course.

**OLAP_VIEW_ENROLL_COUNT_BY_MONTH**  
→ Monthly enrollment breakdown via `year()` and `month()`.

**OLAP_VIEW_ENROLL_BY_COURSE_YEAR**  
→ Yearly total enrollments per course.

**OLAP_VIEW_AVG_CREDITS_PER_STUDENT**  
→ Average credits per student.

---

### 🔵 JPA Oracle Service (Spring Boot)

**Base URL:**
[http://localhost:8091/DSA\_SQL\_JPAService/rest/oracle](http://localhost:8091/DSA_SQL_JPAService/rest/oracle)

#### 🔹 Health Check

* `GET /ping`  → Check if the JPA service is running

```
http://localhost:8091/DSA_SQL_JPAService/rest/oracle/ping
```

#### 📘 Professors Data

* `GET /ProfessorsView`  → List of professors with: `professorId`, `name`, `email`, `deptId`

```
http://localhost:8091/DSA_SQL_JPAService/rest/oracle/ProfessorsView
```

#### 🏢 Departments Data

* `GET /DepartmentsView`  → List of departments with: `deptId`, `name`, `building`

```
http://localhost:8091/DSA_SQL_JPAService/rest/oracle/DepartmentsView
```

#### 🏫 Classrooms Data

* `GET /ClassroomsView`  → List of classrooms with: `roomId`, `building`, `roomNumber`, `capacity`

```
http://localhost:8091/DSA_SQL_JPAService/rest/oracle/ClassroomsView
```

---

### 🔶 SparkSQL OLAP Service

**Base URL:**
[http://localhost:9990/DSA-SparkSQL-Service/rest](http://localhost:9990/DSA-SparkSQL-Service/rest)

#### 🔹 Health Check

* `GET /ping`  → Confirm SparkSQL REST service is up

```
http://localhost:9990/DSA-SparkSQL-Service/rest/ping
```

#### 📊 Query Spark Views (results)

* `GET /view/{VIEW_NAME}`  → Return full JSON result of the given Spark view

**Examples:**

```
http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_AVG_CLASSROOM_CAPACITY_BY_BUILDING
http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_PROFESSOR_DISTRIBUTION_GROUPED
http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_PROFESSOR_LIST_BY_DEPARTMENT
```

#### 📝 View Schema Only

* `GET /STRUCT/{VIEW_NAME}`  → Return SQL schema for a given view

**Examples:**

```
http://localhost:9990/DSA-SparkSQL-Service/rest/STRUCT/OLAP_VIEW_AVG_CLASSROOM_CAPACITY_BY_BUILDING
http://localhost:9990/DSA-SparkSQL-Service/rest/STRUCT/OLAP_VIEW_PROFESSOR_DISTRIBUTION_GROUPED
http://localhost:9990/DSA-SparkSQL-Service/rest/STRUCT/OLAP_VIEW_PROFESSOR_LIST_BY_DEPARTMENT
```

---

## 📊 OLAP Queries Summary

### 1. `OLAP_VIEW_AVG_CLASSROOM_CAPACITY_BY_BUILDING`

**Goal**: Calculate the average number of seats per classroom per building.

```sql
SELECT
  building,
  ROUND(AVG(capacity), 2) AS avgCapacity,
  COUNT(*) AS totalRooms
FROM classrooms_view
GROUP BY building
ORDER BY avgCapacity DESC;
```

### 2. `OLAP_VIEW_PROFESSOR_DISTRIBUTION_GROUPED`

**Goal**: Show number of professors per department, grouped with and without building info.

```sql
SELECT
  d.name AS departmentName,
  d.building AS departmentBuilding,
  COUNT(p.professorId) AS totalProfessors
FROM professors_view p
JOIN departments_view d ON p.deptId = d.deptId
GROUP BY GROUPING SETS (
  (d.name, d.building),
  (d.name),
  ()
)
ORDER BY departmentName NULLS LAST;
```

### 3. `OLAP_VIEW_PROFESSOR_LIST_BY_DEPARTMENT`

**Goal**: Aggregate a list of professor names per department.

```sql
SELECT
  d.name AS departmentName,
  COUNT(p.professorId) AS totalProfessors,
  COLLECT_LIST(p.name) AS professorNames
FROM professors_view p
JOIN departments_view d ON p.deptId = d.deptId
GROUP BY d.name
ORDER BY totalProfessors DESC;
```

---

## 🟡 Lab 7 – XLSX Attendance Data with SparkSQL

We imported structured Excel attendance data using RESTHeart and integrated it into SparkSQL.

### Views

**STUDENT_ATTENDANCE_VIEW**  
→ REST-backed base view from XLSX file.

**OLAP_VIEW_ATTENDANCE_ANALYTICS**  
→ Per student:
- Total sessions
- Present / Absent counts
- Attendance ratio

**OLAP_VIEW_ATTENDANCE_VARIABILITY**  
→ Shows stddev of presence per student across multiple courses.

---

## 🟣 Lab 8 – MongoDB + SparkSQL OLAP

### 1. Mongo Collections

- **ProfessorsProfiles**
- **SkillsTaxonomy**

Inserted via Mongo Compass (~20 documents each).

### 2. SparkSQL Views

**skills_taxonomy_view**  
→ Skill name, type, and level.

**professors_profiles_view**  
→ Professor ID, title, expertise (array), country.

### 3. OLAP Views

**OLAP_VIEW_SKILL_DISTRIBUTION**  
→ Count skills by type and level + collect skill names per group.

**OLAP_VIEW_PROFESSOR_COUNTRY_TITLE**  
→ Count of professors grouped by country and title.

**OLAP_VIEW_STUDENT_PROFESSOR_SKILL_MATCH**  
→ Cross-join to find matching skills between students and professors.

---

## 🌐 Full REST API Endpoints

### 🔹 PostgreSQL – DSA-SQL-JDBCService (port `8090`)
**Data:**
- [`StudentView`](http://localhost:8090/DSA-SQL-JDBCService/rest/students/StudentView)
- [`CourseView`](http://localhost:8090/DSA-SQL-JDBCService/rest/students/courses/CourseView)
- [`EnrollmentView`](http://localhost:8090/DSA-SQL-JDBCService/rest/students/enrollments/EnrollmentView)

**SparkSQL Views:**
- [`OLAP_VIEW_ENROLLMENTS_DETAIL`](http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_ENROLLMENTS_DETAIL)
- [`OLAP_VIEW_ENROLL_COUNT_BY_COURSE`](http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_ENROLL_COUNT_BY_COURSE)
- [`OLAP_VIEW_ENROLL_COUNT_BY_MONTH`](http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_ENROLL_COUNT_BY_MONTH)
- [`OLAP_VIEW_ENROLL_BY_COURSE_YEAR`](http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_ENROLL_BY_COURSE_YEAR)
- [`OLAP_VIEW_AVG_CREDITS_PER_STUDENT`](http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_AVG_CREDITS_PER_STUDENT)

---

### 🟢 MongoDB – DSA-NoSQL-MongoDBService (port `8093`)
**Data:**
- [`ProfessorsProfilesView`](http://localhost:8093/DSA-NoSQL-MongoDBService/rest/profiles/ProfessorsProfilesView)
- [`SkillsTaxonomyView`](http://localhost:8093/DSA-NoSQL-MongoDBService/rest/profiles/SkillsTaxonomyView)

**SparkSQL Views:**
- [`OLAP_VIEW_SKILL_DISTRIBUTION`](http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_SKILL_DISTRIBUTION)
- [`OLAP_VIEW_PROFESSOR_COUNTRY_TITLE`](http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_PROFESSOR_COUNTRY_TITLE)
- [`OLAP_VIEW_STUDENT_PROFESSOR_SKILL_MATCH`](http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_STUDENT_PROFESSOR_SKILL_MATCH)

---

### 🟡 XLSX – DSA-DOC-XLSService (port `8094`)
**Data:**
- [`StudentAttendanceView`](http://localhost:8094/DSA-DOC-XLSService/rest/students/StudentAttendanceView)

**SparkSQL Views:**
- [`OLAP_VIEW_ATTENDANCE_ANALYTICS`](http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_ATTENDANCE_ANALYTICS)
- [`OLAP_VIEW_ATTENDANCE_VARIABILITY`](http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_ATTENDANCE_VARIABILITY)

---

### 🟣 XML – DSA-DOC-XMLService (port `8092`)
**Data:**
- [`StudentProfileView`](http://localhost:8092/DSA-DOC-XMLService/rest/profiles/StudentProfileView)

**SparkSQL Views:**
- [`OLAP_VIEW_STUDENT_CONTACTS`](http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_STUDENT_CONTACTS)
- [`OLAP_VIEW_SCHOLARSHIP_SUMMARY`](http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_SCHOLARSHIP_SUMMARY)

---

### 🟠 Oracle – DSA_SQL_JPAService (port `8091`)
**Data Views:**
http://localhost:9990/DSA-SparkSQL-Service/rest/view/{VIEW_NAME}

**SparkSQL Views:**
- [`Template`](http://localhost:9990/DSA-SparkSQL-Service/rest/view/)

---

## ✅ Ping Endpoints
- [PostgreSQL](http://localhost:8090/DSA-SQL-JDBCService/rest/ping)
- [XLSX](http://localhost:8094/DSA-DOC-XLSService/rest/ping)
- [XML](http://localhost:8092/DSA-DOC-XMLService/rest/ping)
- [MongoDB](http://localhost:8093/DSA-NoSQL-MongoDBService/rest/profiles/ping)
- [SparkSQL](http://localhost:9990/DSA-SparkSQL-Service/rest/ping)

