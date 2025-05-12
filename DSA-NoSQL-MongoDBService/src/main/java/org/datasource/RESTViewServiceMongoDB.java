package org.datasource;

import org.datasource.mongodb.views.professors.ProfessorProfileView;
import org.datasource.mongodb.views.professors.ProfessorProfileViewBuilder;
import org.datasource.mongodb.views.professors.SkillsTaxonomyView;
import org.datasource.mongodb.views.professors.SkillsTaxonomyViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/profiles")
public class RESTViewServiceMongoDB {

	private static final Logger logger = Logger.getLogger(RESTViewServiceMongoDB.class.getName());

	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> RESTViewServiceMongoDB for Profiles is Up!");
		return "Ping response from RESTViewServiceMongoDB!";
	}

	@GetMapping(value = "/ProfessorsProfilesView",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<ProfessorProfileView> get_ProfessorsProfilesView() throws Exception {
		return professorProfileViewBuilder.build().getViewList();
	}

	@GetMapping(value = "/SkillsTaxonomyView",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<SkillsTaxonomyView> get_SkillsTaxonomyView() throws Exception {
		return skillsTaxonomyViewBuilder.build().getViewList();
	}

	@Autowired
	private ProfessorProfileViewBuilder professorProfileViewBuilder;

	@Autowired
	private SkillsTaxonomyViewBuilder skillsTaxonomyViewBuilder;
}
