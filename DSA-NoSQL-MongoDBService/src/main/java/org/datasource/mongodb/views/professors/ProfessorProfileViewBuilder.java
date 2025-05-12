package org.datasource.mongodb.views.professors;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.datasource.mongodb.MongoDataSourceConnector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfessorProfileViewBuilder {

    private final MongoDataSourceConnector connector;
    private List<ProfessorProfileView> viewList;

    public ProfessorProfileViewBuilder(MongoDataSourceConnector connector) {
        this.connector = connector;
    }

    public List<ProfessorProfileView> getViewList() {
        return viewList;
    }

    public ProfessorProfileViewBuilder build() throws Exception {
        return this.select();
    }

    public ProfessorProfileViewBuilder select() throws Exception {
        MongoDatabase db = connector.getMongoDatabase();

        MongoCollection<ProfessorProfileView> collection =
                db.getCollection("ProfessorsProfiles", ProfessorProfileView.class);

        this.viewList = collection.find().into(new ArrayList<>());
        return this;
    }
}
