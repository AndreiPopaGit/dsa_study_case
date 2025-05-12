package org.datasource.xml.profiles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;


@XmlRootElement(name = "StudentProfile")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class StudentProfileView implements Serializable {

    @XmlElement(name = "StudentId")
    private Integer studentId;

    @XmlElement(name = "Phone")
    private String phone;

    @XmlElement(name = "EmergencyContact")
    private String emergencyContact;

    @XmlElement(name = "Status")
    private String status;

    @XmlElement(name = "Scholarship")
    private Integer scholarship;
}
