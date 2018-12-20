package net.is_bg.ltf.config.customresources;




class Utils {

	/***
	 * Convert IResultSet to sql ResultSet
	 * @param data
	 * @return
	 */
	static CustomResultSet toResultSet(IResultSetData data){
		return new CustomResultSet(data);
	}
}
