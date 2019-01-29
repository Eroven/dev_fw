package me.zhaotb.mongodb.dao;

import com.mongodb.BasicDBObject;
import me.zhaotb.mongo.dao.AcctItemDao;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
public class AcctItemDaoImp implements AcctItemDao {

    private MongoTemplate mongoTemplate;

    public AcctItemDaoImp(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Long sumCharge(String businessId) {
        CountOperation.CountOperationBuilder count = Aggregation.count();
        GroupOperation as = Aggregation.group().sum("$charge").as("allCharge");
        MatchOperation match = Aggregation.match(Criteria.where("businessId").is(businessId));
        Aggregation sum = Aggregation.newAggregation(match, as);
        AggregationResults<Document> aggregate = mongoTemplate.aggregate(sum, "acct", Document.class);
        return aggregate.getUniqueMappedResult().getLong("allCharge");
    }
}