package org.datasource;

import org.datasource.jdbc.views.students.StudentView;
import org.datasource.jdbc.views.students.StudentViewBuilder;
import org.datasource.jdbc.views.courses.CourseView;
import org.datasource.jdbc.views.courses.CourseViewBuilder;
import org.datasource.jdbc.views.enrollments.EnrollmentView;
import org.datasource.jdbc.views.enrollments.EnrollmentViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/students")
public class RESTViewServiceJDBC {

	private static Logger logger = Logger.getLogger(RESTViewServiceJDBC.class.getName());

	@RequestMapping(value = "/ping", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String ping() {
		logger.info(">>>> DSA-SQL-JDBCService:: StudentViewService is Up!");
		return "Ping response from StudentViewService!";
	}

	@GetMapping("/StudentView")
	public List<StudentView> getStudentView() {
		return studentViewBuilder.build().getViewList();
	}

	@GetMapping("/courses/CourseView")
	public List<CourseView> getCourseView() {
		return courseViewBuilder.build().getViewList();
	}

	@GetMapping("/enrollments/EnrollmentView")
	public List<EnrollmentView> getEnrollmentView() {
		return enrollmentViewBuilder.build().getViewList();
	}

	@Autowired private StudentViewBuilder studentViewBuilder;
	@Autowired private CourseViewBuilder courseViewBuilder;
	@Autowired private EnrollmentViewBuilder enrollmentViewBuilder;

}
