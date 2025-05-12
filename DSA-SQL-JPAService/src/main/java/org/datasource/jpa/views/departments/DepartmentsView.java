package org.datasource.jpa.views.departments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor(force = true)
public class DepartmentsView {
    private Long deptId;
    private String name;
    private String building;
}
