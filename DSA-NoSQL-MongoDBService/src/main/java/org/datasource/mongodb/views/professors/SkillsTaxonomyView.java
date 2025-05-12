package org.datasource.mongodb.views.professors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class SkillsTaxonomyView {
    private int skill_id;
    private String name;
    private String type;
    private String level;
}
