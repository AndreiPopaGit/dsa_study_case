package org.datasource.xml.profiles;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "StudentProfiles")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class StudentProfileListView {

    @XmlElement(name = "StudentProfile")
    private List<StudentProfileView> profiles;
}
