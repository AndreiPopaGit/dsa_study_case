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

## 🟠 JPA Oracle 21c XE Integration

This integration uses SpringBoot JPA with native SQL to expose complex business views via Oracle XE.

### 1. Java Classes

- **SalesView.java** – POJO combining invoice, line items, and product details.
- **SalesViewBuilderSQL.java** – Native SQL query executed via JPA.

```sql
SELECT i.invoice_Id, i.cust_Id, i.cust_Name, i.invoice_Date, 
       p.product_Code, p.prod_Name, l.quantity, l.unit_Price 
FROM INVOICES i 
JOIN INVOICE_LINE_ITEMS l ON i.invoice_id = l.invoice_id 
JOIN PRODUCTS p ON l.product_code = p.product_code
```

### 2. REST Endpoints (JPA Service)

- `/sales/SalesView` → Returns full sales transactional info.
- `/sales/ProductView` → Product metadata exposed.
- `/sales/InvoiceView` → Invoices from repository.
- `/sales/InvoicesSalesView` → Custom repository method for summary.

### 3. SparkSQL Views

- **sales_view** – Exposes SalesView for analytics
- **products_view** – Exposes product info (prodName, category, price)
- **invoices_view** – Basic invoice metadata

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

