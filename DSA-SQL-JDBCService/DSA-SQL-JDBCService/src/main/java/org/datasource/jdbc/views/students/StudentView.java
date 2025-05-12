package org.datasource.jdbc.views.students;

import lombok.Value;
import java.sql.Date;

@Value
public class StudentView {
	private Integer studentId;
	private String name;
	private String email;
	private Date enrollmentDate;

	public StudentView(Integer studentId, String name, String email, Date enrollmentDate) {
		this.studentId = studentId;
		this.name = name;
		this.email = email;
		this.enrollmentDate = enrollmentDate;
	}

	// Getters
	public Integer getStudentId() { return studentId; }
	public String getName() { return name; }
	public String getEmail() { return email; }
	public Date getEnrollmentDate() { return enrollmentDate; }
}