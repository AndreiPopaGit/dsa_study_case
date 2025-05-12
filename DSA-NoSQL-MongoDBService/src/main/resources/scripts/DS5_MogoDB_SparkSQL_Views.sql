----------------------------------------------------------------------------------
-- DS5_MogoDB_SparkSQL_Views.sql (Custom: Only Professors + Skills)
----------------------------------------------------------------------------------

-- SKILLS_TAXONOMY_VIEW
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/profiles/SkillsTaxonomyView');

SELECT schema_of_json('[
  {"skill_id":1,"name":"SQL","type":"Database","level":"Intermediate"}
]');

CREATE OR REPLACE VIEW skills_taxonomy_view AS
WITH json_view AS (
  SELECT from_json(json_raw.data,
    'ARRAY<STRUCT<
      skill_id: INT,
      name: STRING,
      type: STRING,
      level: STRING
    >>') array
  FROM (
    SELECT java_method(
      'org.spark.service.rest.QueryRESTDataService',
      'getRESTDataDocument',
      'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/profiles/SkillsTaxonomyView'
    ) AS data
  ) json_raw
)
SELECT v.*
FROM json_view LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM skills_taxonomy_view;

----------------------------------------------------------------------------------
-- PROFESSORS_PROFILES_VIEW

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/profiles/ProfessorProfileView');

SELECT schema_of_json('[
  {
    "professor_id":101,
    "title":"PhD",
    "expertise":["AI", "ML"],
    "country":"Romania"
  }
]');

CREATE OR REPLACE VIEW professors_profiles_view AS
WITH json_view AS (
  SELECT from_json(json_raw.data,
    'ARRAY<STRUCT<
      professor_id: INT,
      title: STRING,
      expertise: ARRAY<STRING>,
      country: STRING
    >>') array
  FROM (
    SELECT java_method(
      'org.spark.service.rest.QueryRESTDataService',
      'getRESTDataDocument',
      'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/profiles/ProfessorProfileView'
    ) AS data
  ) json_raw
)
SELECT v.*
FROM json_view LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM professors_profiles_view;
