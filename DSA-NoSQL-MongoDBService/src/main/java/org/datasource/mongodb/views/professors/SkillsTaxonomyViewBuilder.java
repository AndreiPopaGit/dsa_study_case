package org.datasource.mongodb.views.professors;

import org.datasource.mongodb.MongoDataSourceConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkillsTaxonomyViewBuilder {

    private List<SkillsTaxonomyView> viewList;

    public List<SkillsTaxonomyView> getViewList() {
        return viewList;
    }

    @Autowired
    private MongoDataSourceConnector mongoDataSourceConnector;

    public SkillsTaxonomyViewBuilder build() {
        return this.select();
    }

    private SkillsTaxonomyViewBuilder select() {
        MongoDatabase database = mongoDataSourceConnector.getMongoDatabase();

        MongoCollection<SkillsTaxonomyView> collection =
                database.getCollection("SkillsTaxonomy", SkillsTaxonomyView.class);

        viewList = new ArrayList<>();
        collection.find().forEach(viewList::add);

        return this;
    }
}
