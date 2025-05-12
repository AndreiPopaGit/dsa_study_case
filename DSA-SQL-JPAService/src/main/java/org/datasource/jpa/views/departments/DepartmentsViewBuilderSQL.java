package org.datasource.jpa.views.departments;

import org.datasource.jpa.JPADataSourceConnector;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentsViewBuilderSQL {

    private final String SQL_SELECT = "SELECT DEPT_ID, NAME, BUILDING FROM DEPARTMENTS";

    private List<DepartmentsView> viewList = new ArrayList<>();

    public List<DepartmentsView> getViewList() {
        return viewList;
    }

    public DepartmentsViewBuilderSQL build() {
        return select();
    }

    protected DepartmentsViewBuilderSQL select() {
        EntityManager em = dataSourceConnector.getEntityManager();
        Query viewQuery = em.createNativeQuery(SQL_SELECT, "DepartmentsViewMapping");
        this.viewList = viewQuery.getResultList();
        return this;
    }

    protected final JPADataSourceConnector dataSourceConnector;

    public DepartmentsViewBuilderSQL(JPADataSourceConnector dataSourceConnector) {
        this.dataSourceConnector = dataSourceConnector;
    }
}
