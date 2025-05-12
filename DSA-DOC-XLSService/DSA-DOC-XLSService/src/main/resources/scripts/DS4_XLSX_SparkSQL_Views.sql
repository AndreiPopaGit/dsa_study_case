-- DSA_XLSX_SparkSQL_Views.sql (UPDATED for StudentAttendanceView only)

-- 1. Get Data Source JSON Schema
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8094/DSA-DOC-XLSService/rest/students/StudentAttendanceView'
       );

SELECT schema_of_json('[
  {"studentId":1,"courseId":101,"date":"2024-12-01","status":"Present"}
]');

-- 2. Create Remote View
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
SELECT v.*
FROM json_view LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View
SELECT * FROM STUDENT_ATTENDANCE_VIEW;
