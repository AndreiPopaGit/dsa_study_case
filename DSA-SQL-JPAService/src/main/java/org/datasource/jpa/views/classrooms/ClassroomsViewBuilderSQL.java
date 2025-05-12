package org.datasource.jpa.views.classrooms;

import org.datasource.jpa.JPADataSourceConnector;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClassroomsViewBuilderSQL {

    private final String SQL_SELECT = "SELECT ROOM_ID, BUILDING, ROOM_NUMBER, CAPACITY FROM CLASSROOMS";

    private List<ClassroomsView> viewList = new ArrayList<>();

    public List<ClassroomsView> getViewList() {
        return viewList;
    }

    public ClassroomsViewBuilderSQL build() {
        return select();
    }

    protected ClassroomsViewBuilderSQL select() {
        EntityManager em = dataSourceConnector.getEntityManager();
        Query viewQuery = em.createNativeQuery(SQL_SELECT, "ClassroomsViewMapping");
        this.viewList = viewQuery.getResultList();
        return this;
    }

    protected final JPADataSourceConnector dataSourceConnector;

    public ClassroomsViewBuilderSQL(JPADataSourceConnector dataSourceConnector) {
        this.dataSourceConnector = dataSourceConnector;
    }
}
