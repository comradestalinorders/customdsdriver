package net.is_bg.ltf.config.customresources;

import java.sql.ResultSet;



public class SomeFuckenUtils {

	/***
	 * Convert IResultSet to sql ResultSet
	 * @param data
	 * @return
	 */
	public static ResultSet toResultSet(IResultSetData data){
		return new CustomResultSet(data);
	}
}
