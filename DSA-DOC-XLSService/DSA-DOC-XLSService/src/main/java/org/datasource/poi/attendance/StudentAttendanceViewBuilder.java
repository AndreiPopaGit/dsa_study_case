package org.datasource.poi.attendance;

import org.datasource.poi.XLSXResourceFileDataSourceConnector;
import org.datasource.poi.XLSXViewBuilder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class StudentAttendanceViewBuilder extends XLSXViewBuilder<StudentAttendanceView> {

    public StudentAttendanceViewBuilder(XLSXResourceFileDataSourceConnector connector) throws Exception {
        super(connector);

        this.setViewAdapter(tuple -> {
            StudentAttendanceView view = new StudentAttendanceView();

            // Safely convert numeric types to Integer
            Object sid = tuple.get("studentId");
            Object cid = tuple.get("courseId");
            view.setStudentId(sid instanceof Number ? ((Number) sid).intValue() : null);
            view.setCourseId(cid instanceof Number ? ((Number) cid).intValue() : null);

            // Format date to string
            Object dateObj = tuple.get("date");
            if (dateObj instanceof Date) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                view.setDate(sdf.format((Date) dateObj));
            } else if (dateObj instanceof String) {
                view.setDate((String) dateObj);
            }

            view.setStatus((String) tuple.get("status"));
            return view;
        });
    }
}
