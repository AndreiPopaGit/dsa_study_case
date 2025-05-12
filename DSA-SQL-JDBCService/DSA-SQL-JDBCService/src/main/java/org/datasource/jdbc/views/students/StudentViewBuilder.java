package org.datasource.jdbc.views.students;

import org.datasource.jdbc.JDBCDataSourceConnector;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentViewBuilder {
	private String SQL_STUDENTS_SELECT = "SELECT student_id, name, email, enrollment_date FROM students";

	private List<StudentView> studentsViewList = new ArrayList<>();

	public List<StudentView> getViewList() {
		return this.studentsViewList;
	}

	public StudentViewBuilder build() {
		try (Connection jdbcConnection = jdbcConnector.getConnection()) {
			Statement selectStmt = jdbcConnection.createStatement();
			ResultSet rs = selectStmt.executeQuery(SQL_STUDENTS_SELECT);

			studentsViewList = new ArrayList<>();
			while (rs.next()) {
				Integer studentId = rs.getInt("student_id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				java.sql.Date enrollmentDate = rs.getDate("enrollment_date");

				this.studentsViewList.add(new StudentView(studentId, name, email, enrollmentDate));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this;
	}

	private JDBCDataSourceConnector jdbcConnector;

	public StudentViewBuilder(JDBCDataSourceConnector jdbcConnector) {
		this.jdbcConnector = jdbcConnector;
	}
}
