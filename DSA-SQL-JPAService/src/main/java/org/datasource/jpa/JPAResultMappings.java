package org.datasource.jpa;

import org.datasource.jpa.views.classrooms.ClassroomsView;
import org.datasource.jpa.views.departments.DepartmentsView;
import org.datasource.jpa.views.professors.ProfessorsView;

import javax.persistence.*;

@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "ProfessorsViewMapping",
                classes = @ConstructorResult(
                        targetClass = ProfessorsView.class,
                        columns = {
                                @ColumnResult(name = "PROFESSOR_ID", type = Long.class),
                                @ColumnResult(name = "NAME", type = String.class),
                                @ColumnResult(name = "EMAIL", type = String.class),
                                @ColumnResult(name = "DEPT_ID", type = Long.class)
                        }
                )
        ),
        @SqlResultSetMapping(
                name = "DepartmentsViewMapping",
                classes = @ConstructorResult(
                        targetClass = DepartmentsView.class,
                        columns = {
                                @ColumnResult(name = "DEPT_ID", type = Long.class),
                                @ColumnResult(name = "NAME", type = String.class),
                                @ColumnResult(name = "BUILDING", type = String.class)
                        }
                )
        ),
        @SqlResultSetMapping(
                name = "ClassroomsViewMapping",
                classes = @ConstructorResult(
                        targetClass = ClassroomsView.class,
                        columns = {
                                @ColumnResult(name = "ROOM_ID", type = Long.class),
                                @ColumnResult(name = "BUILDING", type = String.class),
                                @ColumnResult(name = "ROOM_NUMBER", type = String.class),
                                @ColumnResult(name = "CAPACITY", type = Integer.class)
                        }
                )
        )
})
@Entity
public class JPAResultMappings {
    @Id
    private Long dummyId;
}
