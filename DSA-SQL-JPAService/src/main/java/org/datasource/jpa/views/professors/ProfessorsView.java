package org.datasource.jpa.views.professors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor(force = true)
public class ProfessorsView {
    private Long professorId;
    private String name;
    private String email;
    private Long deptId;
}
