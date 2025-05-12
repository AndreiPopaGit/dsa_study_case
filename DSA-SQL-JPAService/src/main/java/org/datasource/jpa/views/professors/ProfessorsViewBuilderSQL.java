package org.datasource.jpa.views.professors;

import org.datasource.jpa.JPADataSourceConnector;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfessorsViewBuilderSQL {

    private final String SQL_SELECT = "SELECT PROFESSOR_ID, NAME, EMAIL, DEPT_ID FROM PROFESSORS";

    private List<ProfessorsView> viewList = new ArrayList<>();

    public List<ProfessorsView> getViewList() {
        return viewList;
    }

    public ProfessorsViewBuilderSQL build() {
        return select();
    }

    protected ProfessorsViewBuilderSQL select() {
        EntityManager em = dataSourceConnector.getEntityManager();
        Query viewQuery = em.createNativeQuery(SQL_SELECT, "ProfessorsViewMapping");
        this.viewList = viewQuery.getResultList();
        return this;
    }

    protected final JPADataSourceConnector dataSourceConnector;

    public ProfessorsViewBuilderSQL(JPADataSourceConnector dataSourceConnector) {
        this.dataSourceConnector = dataSourceConnector;
    }
}
