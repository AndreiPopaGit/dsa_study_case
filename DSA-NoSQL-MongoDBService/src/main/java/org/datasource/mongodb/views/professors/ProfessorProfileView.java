package org.datasource.mongodb.views.professors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ProfessorProfileView {
    private int professor_id;
    private String title;
    private List<String> expertise;
    private String country;
}