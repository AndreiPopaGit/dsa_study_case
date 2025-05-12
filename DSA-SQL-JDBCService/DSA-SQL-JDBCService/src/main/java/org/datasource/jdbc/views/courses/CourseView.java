package org.datasource.jdbc.views.courses;

import lombok.Value;

@Value
public class CourseView {
    private int courseId;
    private String courseName;
    private int credits;

    public CourseView(int courseId, String courseName, int credits) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
    }

    public int getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public int getCredits() { return credits; }
}