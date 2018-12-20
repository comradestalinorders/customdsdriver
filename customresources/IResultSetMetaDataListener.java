package net.is_bg.ltf.config.customresources;


import java.sql.ResultSetMetaData;

interface IResultSetMetaDataListener {

	public void processMetaData(ResultSetMetaData metadata);
	
}