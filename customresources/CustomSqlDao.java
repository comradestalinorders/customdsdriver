package net.is_bg.ltf.config.customresources;


import net.is_bg.ltf.db.common.AbstractMainDao;
import net.is_bg.ltf.db.common.interfaces.IAbstractModel;
import net.is_bg.ltf.db.common.interfaces.IConnectionFactory;

class CustomSqlDao extends AbstractMainDao{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6656168418142962121L;

	public CustomSqlDao(IConnectionFactory connectionFactory) {
		super(connectionFactory);
		// TODO Auto-generated constructor stub
	}
	
	private IResultSetData performUpdate(String sql, String dataSource){
		final String sqll = sql;
		UpdateSqlStatementCnt  update = new  UpdateSqlStatementCnt() {
			@Override
			protected String getSqlString() {
				// TODO Auto-generated method stub
				return sqll;
			}
		};
		execute(update, dataSource);
		ResultSetData data = new ResultSetData();
		ColumnMetaData meta = new ColumnMetaData();
		meta.setColumnName("Update Count:");
		data.getColumMetaData().add(meta);
		Object [] d = new Object [1];
		d[0] = update.getUpdateCnt();
		data.getResult().add(d);
		return data;
	}
	
	private IResultSetData performSelect(String sql, String dataSource){
		//create select statement 
		CustomSelect<IAbstractModel> select = new CustomSelect<IAbstractModel>(sql);
		execute(select, dataSource);
		return select.getResultSetData();
	}
	
	private boolean isSelect(String sql){
		if(sql == null) return true;
		if(sql.trim().toLowerCase().startsWith("select")) return true;
		return false;
	}
	
	
	public IResultSetData execSql(String sql, String dataSource){
		if(isSelect(sql)) return performSelect(sql, dataSource);
		else return performUpdate(sql, dataSource);
	}
	
}