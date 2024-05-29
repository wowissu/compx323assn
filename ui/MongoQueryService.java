
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Updates.set;

public class MongoQueryService implements QueryService {

  String uri = "mongodb://localhost:27017/";
  // private MongoClient conn;
  // private MongoDatabase db;
  private String dbname = "compx323";

  public MongoQueryService() {

  }

  public void close() {
  }

  public void updateStudentName(Integer studentID, String newName) throws Exception {
    MongoClient mongoClient = MongoClients.create(uri);
    MongoDatabase database = mongoClient.getDatabase(dbname);
    MongoCollection<Document> studentCollection = database.getCollection("student");

    studentCollection.updateOne(eq("_id", studentID), set("name", newName));
  }

  public Object[][] students(Integer studentID, String eventType) {
    try (MongoClient mongoClient = MongoClients.create(uri)) {
      MongoDatabase database = mongoClient.getDatabase(dbname);
      MongoCollection<Document> studentCollection = database.getCollection("student");

      System.out.println("studentID: " + studentID);
      System.out.println("eventType: " + eventType);

      AggregateIterable<Document> iterable = studentCollection.aggregate(Arrays.asList(
          match(studentID != null ? eq("_id", studentID) : exists("_id")),
          lookup("class", "_id", "students", "class"),
          unwind("$class"),
          lookup("event", "class._id", "class", "events"),
          unwind("$events"),
          match((eventType != null && eventType != "All") ? eq("events.type", eventType) : exists("_id")),
          lookup("location", "events.location", "_id", "location"),
          unwind("$location"),
          project(new Document("_id", 0)
              .append("studentID", "$_id")
              .append("studentName", "$name")
              .append("className", "$class.name")
              .append("locationRoom", "$location.room")
              .append("eventType", "$events.type")
              .append("eventTime", "$events.time"))));

      List<Object[]> rowsList = new ArrayList<>();

      for (Document doc : iterable) {
        Object[] row = new Object[] {
            doc.getInteger("studentID"),
            doc.getString("studentName"),
            doc.getString("className"),
            doc.getString("locationRoom"),
            doc.getString("eventType"),
            doc.getString("eventTime"),
        };

        System.out.println(doc.toJson());

        rowsList.add(row);
      }

      Object[][] rows = new Object[rowsList.size()][];
      for (int i = 0; i < rowsList.size(); i++) {
        rows[i] = rowsList.get(i);
      }

      return rows;
    }
  }
}