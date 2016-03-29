package com.eason.mongo;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

@ContextConfiguration(value = "classpath:spring-mongo.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MongoTest {

	private static final String MONGO_DB_NAME = "eason";

	private static final int CAPPED_SIZE = 1000;
	@Autowired
	private MongoClient mongo;

	private DBCollection collection;

	private DBCollection cappedCollection;

	@Before
	public void setUp() {
		this.collection = mongo.getDB(MongoTest.MONGO_DB_NAME).getCollection("user");
		this.cappedCollection = mongo.getDB(MongoTest.MONGO_DB_NAME).getCollection("user.capped");
		if (!cappedCollection.isCapped()) {
			// 将已存在的集合转成固定集合
			mongo.getDB(MongoTest.MONGO_DB_NAME).command(
			        BasicDBObjectBuilder.start("convertToCapped", "user.capped").add("size", CAPPED_SIZE).get());
		}

	}

	@Test
	public void testIndex() {
		DBObject index = BasicDBObjectBuilder.start().add("username", 1).get();
		collection.createIndex(index);
	}

	//创建固定集合
	@Test
	public void testCreateCappedCollection() {
		this.cappedCollection = mongo.getDB(MongoTest.MONGO_DB_NAME).createCollection("user.capped",
		        BasicDBObjectBuilder.start("capped", true).add("size", CAPPED_SIZE).get());
	}

	@Test
	public void testCappedCollection() {
		List<DBObject> users = this.generateUserData();
		this.collection.insert(users);
		this.cappedCollection.insert(users);
		System.out.println("collection size:" + this.collection.count());
		System.out.println("cappedCollection size:" + this.cappedCollection.count());

	}

	private List<DBObject> generateUserData() {
		List<DBObject> users = new ArrayList<DBObject>();
		for (int i = 0; i < 1000; i++) {
			users.add(BasicDBObjectBuilder.start("username", "eason" + i).get());
		}
		return users;
	}
}
