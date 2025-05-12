package org.datasource.jdbc.views.enrollments;

import lombok.Value;
import java.util.Date;

@Value
public class EnrollmentView {
    private int enrollmentId;
    private int studentId;
    private int courseId;
    private Date enrollDate;

    public EnrollmentView(int enrollmentId, int studentId, int courseId, Date enrollDate) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollDate = enrollDate;
    }

    public int getEnrollmentId() { return enrollmentId; }
    public int getStudentId() { return studentId; }
    public int getCourseId() { return courseId; }
    public Date getEnrollDate() { return enrollDate; }
}