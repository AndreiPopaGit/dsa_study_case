package org.datasource;

import org.datasource.xml.XMLResourceFileDataSourceConnector;
import org.datasource.xml.profiles.StudentProfileView;
import org.datasource.xml.profiles.StudentProfileViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/profiles")
public class RESTViewServiceXML {

	private static final Logger logger = Logger.getLogger(RESTViewServiceXML.class.getName());

	@Autowired
	private XMLResourceFileDataSourceConnector xmlDataSourceConnector;

	@Autowired
	private StudentProfileViewBuilder studentProfileViewBuilder;

	@RequestMapping(value = "/ping", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String testDataSource() {
		logger.info(">>>> REST XML Data Source is Up!");
		return "PING response from XML Data Source!";
	}

	@RequestMapping(value = "/StudentProfileView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<StudentProfileView> getStudentProfiles() throws Exception {
		return studentProfileViewBuilder.build().getProfileViewList();
	}
}
