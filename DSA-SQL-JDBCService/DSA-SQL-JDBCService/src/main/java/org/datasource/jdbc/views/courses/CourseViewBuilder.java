package org.datasource.jdbc.views.courses;

import org.datasource.jdbc.JDBCDataSourceConnector;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseViewBuilder {

	private String SQL_COURSES_SELECT = "SELECT course_id, course_name, credits FROM courses";
	private List<CourseView> courseViewList = new ArrayList<>();

	public List<CourseView> getViewList() {
		return this.courseViewList;
	}

	public CourseViewBuilder build() {
		try (Connection jdbcConnection = jdbcConnector.getConnection()) {
			Statement selectStmt = jdbcConnection.createStatement();
			ResultSet rs = selectStmt.executeQuery(SQL_COURSES_SELECT);

			courseViewList = new ArrayList<>();
			while (rs.next()) {
				int courseId = rs.getInt("course_id");
				String courseName = rs.getString("course_name");
				int credits = rs.getInt("credits");

				courseViewList.add(new CourseView(courseId, courseName, credits));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return this;
	}

	private JDBCDataSourceConnector jdbcConnector;

	public CourseViewBuilder(JDBCDataSourceConnector jdbcConnector) {
		this.jdbcConnector = jdbcConnector;
	}
}
