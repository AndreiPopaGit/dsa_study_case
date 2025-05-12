package org.datasource.jpa.views.classrooms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor(force = true)
public class ClassroomsView {
    private Long roomId;
    private String building;
    private String roomNumber;
    private Integer capacity;
}
