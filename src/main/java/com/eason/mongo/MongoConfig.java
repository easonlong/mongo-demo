package com.eason.mongo;



/**
 * @author longyaokun
 * @date 2016年3月29日
 *
 */
public interface MongoConfig {
	/**
	 * 重置数据集合
	 * 
	 * @return
	 */
	public MongoConfig reset();

	public MongoOperation collection();
	
	public Object get(String key);
}