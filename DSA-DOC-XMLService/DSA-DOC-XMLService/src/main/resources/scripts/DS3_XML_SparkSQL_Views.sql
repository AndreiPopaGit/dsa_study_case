----------------------------------------------------------------------------------
-- STUDENT PROFILE VIEW
----------------------------------------------------------------------------------

-- 1. REST Call Preview
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8092/DSA-DOC-XMLService/rest/profiles/StudentProfileView');

-- 2. JSON Schema Preview
SELECT schema_of_json('[
  {
    "studentId": 1,
    "phone": "+40728892359",
    "emergencyContact": "Eva Vlad",
    "status": "Active",
    "scholarship": 500
  }
]');

-- 3. Create Remote View
-- DROP VIEW student_profile_view;
CREATE OR REPLACE VIEW student_profile_view AS
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

-- 4. Test View
SELECT * FROM student_profile_view;
