package org.datasource.poi.attendance;

import lombok.Data;

@Data
public class StudentAttendanceView {
    private Integer studentId;
    private Integer courseId;
    private String date;
    private String status;
}
