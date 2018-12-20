package net.is_bg.ltf.config.customresources;




interface ISqlExecutor {
	//public  IResultSetData execSql(String sql);
	public  IResultSetData execU(String sql);
	public  IResultSetData execSel(String sql);
}


