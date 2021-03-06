package main.java.products;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.ascending;
import static java.util.Arrays.asList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
public class HelloController {
    private MongoClient mongoClient;
    private MongoDatabase db;
    

    public HelloController() {
        mongoClient = new MongoClient();
        db = mongoClient.getDatabase("test");	
    }
    
    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
    
	private String outSet = "";
	private int limitCounter = 0;
	
    @RequestMapping("/all")
    public String all() {

		FindIterable<Document> iterable = db.getCollection("restaurants").find();
		iterable.forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				if (limitCounter < 50) {
					outSet += document.toString();
				}
				limitCounter++;
			}
		});
		return outSet;
    }
    
    @RequestMapping("/add")
	public String add() {
		try {
			//if (2+2==4) {
			//	return "hmm";
			//}
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
			db.getCollection("restaurants").insertOne(
				new Document("address",
					new Document()
					.append("street", "2 Avenue")
					.append("zipcode", "10075")
					.append("building", "1480")
					.append("coord", asList(-73.9557413, 40.7720266))
				)
				.append("borough", "Manhattan")
				.append("cuisine", "Polska")
				.append("grades",
					asList(
						new Document()
						.append("date", format.parse("2014-10-01T00:00:00Z"))
						.append("grade", "A")
						.append("score", 11),
						new Document()
						.append("date", format.parse("2014-01-16T00:00:00Z"))
						.append("grade", "B")
						.append("score", 17)
					)
				)
				.append("name", "Vella")
				.append("restaurant_id", "41704620")
			);
		} catch (ParseException ex) {
			Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
		}
		return "Document added";
    }
}
