package org.spark.service;

import org.springframework.boot.SpringApplication;
import org.apache.spark.sql.SparkSession;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.logging.Logger;

// --add-exports java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.base/java.net=ALL-UNNAMED
// java --add-exports java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.base/java.net=ALL-UNNAMED -jar DSA-SparkSQL-Service-2025.1.jar
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SprinBootSparkSQLStarter extends SpringBootServletInitializer {

    private static Logger logger = Logger.getLogger(SprinBootSparkSQLStarter.class.getName());

    public static void main(String[] args) {
        logger.info("Loading ... SparkStarterService with Spark Default Settings ... DSA");

        SpringApplication.run(SprinBootSparkSQLStarter.class, args);

        SparkSession spark = SparkSession.builder()
                .appName("SparkSQLService")
                .master("local[*]")
                .enableHiveSupport()
                .getOrCreate();

        try {
            InputStream inputStream = SprinBootSparkSQLStarter.class
                    .getClassLoader()
                    .getResourceAsStream("scripts/SparkSQL_OLAP_Multidimensional_Analytical.sql");

            if (inputStream == null) {
                throw new RuntimeException("SQL script file not found in classpath.");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sqlBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sqlBuilder.append(line).append("\n");
                if (line.trim().endsWith(";")) {
                    String sql = sqlBuilder.toString().trim();
                    sqlBuilder.setLength(0); // reset for next query
                    if (!sql.isEmpty()) {
                        logger.info("Executing SQL: " + sql.split("\n")[0]);
                        spark.sql(sql.substring(0, sql.length() - 1)); // remove trailing semicolon
                    }
                }
            }

        } catch (Exception e) {
            logger.severe("❌ Failed to load SparkSQL script: " + e.getMessage());
        }
    }
}

// java --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED