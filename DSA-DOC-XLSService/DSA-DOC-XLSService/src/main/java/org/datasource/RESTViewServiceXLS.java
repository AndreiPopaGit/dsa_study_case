package org.datasource;

import org.datasource.poi.attendance.StudentAttendanceView;
import org.datasource.poi.attendance.StudentAttendanceViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

/*
 * REST Service URL
 * http://localhost:8094/DSA-DOC-XLSService/rest/students/StudentAttendanceView
 */
@RestController
@RequestMapping("/students")
public class RESTViewServiceXLS {
	private static final Logger logger = Logger.getLogger(RESTViewServiceXLS.class.getName());

	@RequestMapping(value = "/ping", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> REST XLS Data Source is Up!");
		return "PING response from XLS Data Source!";
	}

	@RequestMapping(value = "/StudentAttendanceView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<StudentAttendanceView> getStudentAttendanceView() throws Exception {
		return studentAttendanceViewBuilder.build().getViewList();
	}

	@Autowired
	private StudentAttendanceViewBuilder studentAttendanceViewBuilder;
}

