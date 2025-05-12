package org.datasource.xml.profiles;

import org.datasource.xml.XMLResourceFileDataSourceConnector;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;

@Service
public class StudentProfileViewBuilder {

    private static final Logger logger = Logger.getLogger(StudentProfileViewBuilder.class.getName());

    // XML file and loader
    private final XMLResourceFileDataSourceConnector dataSourceConnector;
    private File xmlFile;

    // JAXB-mapped root list
    private StudentProfileListView profileListView;

    // Direct access to the list
    private List<StudentProfileView> profileViewList;

    public StudentProfileViewBuilder(XMLResourceFileDataSourceConnector dataSourceConnector) throws Exception {
        this.dataSourceConnector = dataSourceConnector;
        this.xmlFile = new File(
                getClass().getClassLoader().getResource("datasource/StudentProfile.xml").toURI()
        );
    }

    public StudentProfileViewBuilder build() throws Exception {
        return this.select().map();
    }

    public List<StudentProfileView> getProfileViewList() {
        return this.profileViewList;
    }

    private StudentProfileViewBuilder select() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(StudentProfileListView.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        this.profileListView = (StudentProfileListView) jaxbUnmarshaller.unmarshal(xmlFile);
        logger.info("StudentProfileViewBuilder: profileListView=" + profileListView);
        return this;
    }

    private StudentProfileViewBuilder map() {
        this.profileViewList = this.profileListView.getProfiles();
        return this;
    }
}
