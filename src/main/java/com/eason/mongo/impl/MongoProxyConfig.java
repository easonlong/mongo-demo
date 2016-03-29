package com.eason.mongo.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eason.mongo.MongoConfig;
import com.eason.mongo.MongoOperation;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;


/**
 * @author longyaokun
 * @date 2016年3月29日
 *
 */
public class MongoProxyConfig implements MongoConfig {

	/**
	 * 重置数据集合(All)
	 */
	private static final DBObject CLEAN = BasicDBObjectBuilder.start().get();

	private final Map<String, Object> configs = new HashMap<String, Object>();

	private final MongoWrapCollection wrap;

	private final DBCollection collection;

	public MongoProxyConfig(MongoClient client, String db, String collection) {
		super();
		this.wrap = new MongoWrapCollection();
		this.collection = client.getDB(db).getCollection(collection);
	}

	@Override
	public Object get(String key) {
		return this.configs.get(key);
	}

	public MongoProxyConfig reset() {
		this.collection().remove(MongoProxyConfig.CLEAN);
		return this;
	}

	public MongoWrapCollection collection() {
		return this.wrap;
	}

	private class MongoWrapCollection implements MongoOperation {

		public void index(DBObject index) {
			MongoProxyConfig.this.collection.createIndex(index);
		}

		public void index(DBObject index, String name) {
			MongoProxyConfig.this.collection.createIndex(index, name);
		}

		public WriteResult remove(DBObject query) {
			return this.remove(query, WriteConcern.MAJORITY);
		}

		public WriteResult remove(DBObject query, WriteConcern concern) {
			return MongoProxyConfig.this.collection.remove(query, concern);
		}

		@Override
		public WriteResult save(DBObject entity) {
			return this.save(entity, WriteConcern.MAJORITY);
		}

		public WriteResult save(DBObject entity, WriteConcern concern) {
			return MongoProxyConfig.this.collection.save(entity, concern);
		}

		@Override
		public WriteResult update(DBObject query, DBObject entity) {
			return this.update(query, entity, false, false, WriteConcern.MAJORITY);
		}

		@Override
		public WriteResult update(DBObject query, DBObject entity, boolean upsert, boolean batch) {
			return this.update(query, entity, upsert, batch, WriteConcern.MAJORITY);
		}

		public WriteResult update(DBObject query, DBObject entity, boolean upsert, boolean batch, WriteConcern concern) {
			return MongoProxyConfig.this.collection.update(query, entity, upsert, batch, concern);
		}

		@Override
		public DBCursor find(DBObject query) {
			return MongoProxyConfig.this.collection.find(query);
		}

		@Override
		public DBCursor find(DBObject query, DBObject filter) {
			return MongoProxyConfig.this.collection.find(query, filter);
		}

		@Override
		public DBObject findOne(DBObject query) {
			return MongoProxyConfig.this.collection.findOne(query);
		}

		@Override
		public DBObject findOne(DBObject query, DBObject filter) {
			return MongoProxyConfig.this.collection.findOne(query, filter);
		}

		public DBObject findAndModify(DBObject query, DBObject entity) {
			return MongoProxyConfig.this.collection.findAndModify(query, entity);
		}

		public DBObject findAndModify(DBObject query, DBObject fields, DBObject sort, boolean remove, DBObject update, boolean returnNew, boolean upsert) {
			return MongoProxyConfig.this.collection.findAndModify(query, fields, sort, remove, update, returnNew, upsert);
		}

		public AggregationOutput aggregate(final DBObject... ops) {
			return MongoProxyConfig.this.collection.aggregate(Arrays.asList(ops));
		}

		@SuppressWarnings("unchecked")
		public List<Object> distinct(String key, DBObject query) {
			return MongoProxyConfig.this.collection.distinct(key, query);
		}
	}
}