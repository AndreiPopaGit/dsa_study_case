package org.datasource.jdbc.views.enrollments;

import org.datasource.jdbc.JDBCDataSourceConnector;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnrollmentViewBuilder {

	private String SQL_ENROLLMENTS_SELECT = "SELECT enrollment_id, student_id, course_id, enroll_date FROM enrollments";
	private List<EnrollmentView> enrollmentViewList = new ArrayList<>();

	public List<EnrollmentView> getViewList() {
		return this.enrollmentViewList;
	}

	public EnrollmentViewBuilder build() {
		try (Connection jdbcConnection = jdbcConnector.getConnection()) {
			Statement selectStmt = jdbcConnection.createStatement();
			ResultSet rs = selectStmt.executeQuery(SQL_ENROLLMENTS_SELECT);

			enrollmentViewList = new ArrayList<>();
			while (rs.next()) {
				int enrollmentId = rs.getInt("enrollment_id");
				int studentId = rs.getInt("student_id");
				int courseId = rs.getInt("course_id");
				java.sql.Date enrollDate = rs.getDate("enroll_date");

				enrollmentViewList.add(new EnrollmentView(enrollmentId, studentId, courseId, enrollDate));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return this;
	}

	private JDBCDataSourceConnector jdbcConnector;

	public EnrollmentViewBuilder(JDBCDataSourceConnector jdbcConnector) {
		this.jdbcConnector = jdbcConnector;
	}
}
