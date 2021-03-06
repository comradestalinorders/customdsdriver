package net.is_bg.ltf.config.customresources;


import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


class CustomCommonStatement implements PreparedStatement{
	private ISqlExecutor sqlExecutor;
	private String sql;
	/** The Constant DATE_FORMAT. */
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	
	/** The Constant TIMESTAMP_FORMAT. */
	private static final SimpleDateFormat TIMESTAMP_FORMAT  = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
	
	/** The Constant TIME_FORMAT. */
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	
	protected Map<Integer,BindVariableInfo> values  = new HashMap<Integer,BindVariableInfo>();
	
	protected CustomResultSet resultSet;
	
	private  CustomConnectionWs customConnectionWs;
	
	
	public CustomCommonStatement(CustomConnectionWs customConnection, String sql) {
		this.sqlExecutor = customConnection.getSqlExecutor();
		this.sql = sql;
	}
	
	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		resultSet = Utils.toResultSet(sqlExecutor.execSel(sqlForLog(sql)));
		return resultSet;
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		resultSet = Utils.toResultSet(sqlExecutor.execU(sqlForLog(sql)));
		return 0;
	}

	@Override
	public void close() throws SQLException {
		
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		return 0;
	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMaxRows() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancel() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCursorName(String name) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		Utils.toResultSet(sqlExecutor.execSel(sqlForLog(sql)));
		return true;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		return null;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		return 0;
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		return false;
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return 0;
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		
	}

	@Override
	public int getFetchSize() throws SQLException {
		return 0;
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		return 0;
	}

	@Override
	public int getResultSetType() throws SQLException {
		return 0;
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		
	}

	@Override
	public void clearBatch() throws SQLException {
		
	}

	@Override
	public int[] executeBatch() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Connection getConnection() throws SQLException {
		return customConnectionWs;
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		return false;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		return null;
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		return 0;
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		return 0;
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		return 0;
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		return false;
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		return false;
	}

	@Override
	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		return false;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		return 0;
	}

	@Override
	public boolean isClosed() throws SQLException {
		return false;
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		
	}

	@Override
	public boolean isPoolable() throws SQLException {
		return false;
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		return this.executeQuery(sql);
	}

	@Override
	public int executeUpdate() throws SQLException {
		this.executeUpdate(sql);
		return 0;
	}

	@Override
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		values.put(parameterIndex, new BindVariableInfo(null, sqlType, parameterIndex));
	}

	@Override
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		values.put(parameterIndex, new BindVariableInfo(x, Types.BOOLEAN, parameterIndex));
	}

	@Override
	public void setByte(int parameterIndex, byte x) throws SQLException {
		values.put(parameterIndex, new BindVariableInfo(x, Types.BINARY, parameterIndex));
	}

	@Override
	public void setShort(int parameterIndex, short x) throws SQLException {
		values.put(parameterIndex, new BindVariableInfo(x, Types.SMALLINT, parameterIndex));
	}

	@Override
	public void setInt(int parameterIndex, int x) throws SQLException {
		values.put(parameterIndex, new BindVariableInfo(x, Types.INTEGER, parameterIndex));
	}

	@Override
	public void setLong(int parameterIndex, long x) throws SQLException {
		values.put(parameterIndex, new BindVariableInfo(x, Types.NUMERIC, parameterIndex));
	}

	@Override
	public void setFloat(int parameterIndex, float x) throws SQLException {
		values.put(parameterIndex, new BindVariableInfo(x, Types.FLOAT, parameterIndex));
	}

	@Override
	public void setDouble(int parameterIndex, double x) throws SQLException {
		values.put(parameterIndex, new BindVariableInfo(x, Types.DOUBLE, parameterIndex));
	}

	@Override
	public void setBigDecimal(int parameterIndex, BigDecimal x)
			throws SQLException {
		values.put(parameterIndex, new BindVariableInfo(x, Types.DECIMAL, parameterIndex));
	}

	@Override
	public void setString(int parameterIndex, String x) throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(x, Types.VARCHAR, parameterIndex));
	}

	@Override
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(x, Types.DECIMAL, parameterIndex));
	}

	@Override
	public void setDate(int parameterIndex, Date x) throws SQLException {
		// TODO Auto-generated method stub
		//bindaBindVariableData.setDate(x, parameterIndex);
		values.put(parameterIndex, new BindVariableInfo(net.is_bg.ltf.util.DateUtil.toSQLDate(x), Types.DATE, parameterIndex));
	}

	@Override
	public void setTime(int parameterIndex, Time x) throws SQLException {
		// TODO Auto-generated method stub
		//bindaBindVariableData.setTime(x, parameterIndex);
		values.put(parameterIndex, new BindVariableInfo(x, Types.TIME, parameterIndex));
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x)
			throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(x, Types.TIMESTAMP, parameterIndex));
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub
		//bindaBindVariableData.setAsciiStream(x, parameterIndex);
	}

	@Override
	public void setUnicodeStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub
		//bindaBindVariableData.setUnicodeStream(x, parameterIndex);
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearParameters() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType)
			throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(x, targetSqlType, parameterIndex));
	}

	@Override
	public void setObject(int parameterIndex, Object x) throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(x, Types.OTHER, parameterIndex));
	}

	@Override
	public boolean execute() throws SQLException {
		return execute(sql);
	}

	@Override
	public void addBatch() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, int length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRef(int parameterIndex, Ref x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(x, Types.OTHER, parameterIndex));
	}

	@Override
	public void setClob(int parameterIndex, Clob x) throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(x, Types.OTHER, parameterIndex));
	}

	@Override
	public void setArray(int parameterIndex, Array x) throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(x, Types.ARRAY, parameterIndex));
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDate(int parameterIndex, Date x, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(x, Types.DATE, parameterIndex));
	}

	@Override
	public void setTime(int parameterIndex, Time x, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(x, Types.TIME, parameterIndex));
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(x, Types.TIMESTAMP, parameterIndex));
	}

	@Override
	public void setNull(int parameterIndex, int sqlType, String typeName)
			throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(null, sqlType, parameterIndex));
	}

	@Override
	public void setURL(int parameterIndex, URL x) throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(x, Types.OTHER, parameterIndex));
	}

	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(x, Types.OTHER, parameterIndex));
	}

	@Override
	public void setNString(int parameterIndex, String value)
			throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(value, Types.VARCHAR, parameterIndex));
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(value, Types.OTHER, parameterIndex));
	}

	@Override
	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(inputStream, Types.OTHER, parameterIndex));
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(reader, Types.OTHER, parameterIndex));
	}

	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType,
			int scaleOrLength) throws SQLException {
		// TODO Auto-generated method stub
		values.put(parameterIndex, new BindVariableInfo(x, Types.OTHER, parameterIndex));
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	private String sqlForLog(String sql){
		if(sql!=null){
			char[] chars=sql.toCharArray();
			StringBuilder sb=new StringBuilder();
			for(int i=0,j=1;i<chars.length;i++){
				if('?'==chars[i]){
					BindVariableInfo pstmp=values.get(j);
					j++;
					if(pstmp!=null){
						sb.append(strForLog(pstmp));
						continue;
					}
				}
				sb.append(chars[i]);
			}
			
			String s = sb.toString();
			System.out.println(s);
			return s;
		}
		return null;
	}

	
	private String strForLog(BindVariableInfo aPstmp) {
		if(aPstmp.getValue()!=null){
			switch(aPstmp.getType()){
			//TODO 
			case Types.INTEGER:
			case Types.DOUBLE:
			case Types.DECIMAL:
			case Types.FLOAT:
			case Types.BIGINT:
			case Types.NUMERIC:
				return ""+aPstmp.getValue();
			case Types.BOOLEAN:
				return (Boolean)aPstmp.getValue()?"true":"false";
			case Types.DATE:
				return "'"+DATE_FORMAT.format(aPstmp.getValue())+"'";
			case Types.TIMESTAMP:
				return "'"+TIMESTAMP_FORMAT.format(aPstmp.getValue())+"'";
			case Types.TIME:
				return "'"+TIME_FORMAT.format(aPstmp.getValue())+"'";
			case Types.CHAR:
			case Types.NCHAR:
			case Types.VARCHAR:
			case Types.NVARCHAR:
			case Types.LONGVARCHAR:
			case Types.LONGNVARCHAR:
				return "'"+aPstmp.getValue()+"'";
			default:
				return aPstmp.getValue().getClass().getName();
			}
		}
		return "null";
	}
	
}


class BindVariableInfo {
	
	/** The value. */
	private Object 	value;
	
	/** The type. */
	private int		type;
	
	/** The position. */
	private int 	position;
	
	/** The outputparam. */
	private boolean outputparam = false;

	/**
	 * Instantiates a new bind variable info.
	 *
	 * @param value the value
	 * @param type the type
	 * @param position the position
	 */
	public BindVariableInfo(Object value,int type,int position){
		this.value=value;
		this.type=type;
		this.position=position;
		this.outputparam = false;
	}
	
	/**
	 * Instantiates a new bind variable info.
	 *
	 * @param value the value
	 * @param type the type
	 * @param position the position
	 * @param output the output
	 */
	public BindVariableInfo(Object value,int type,int position, boolean output){
		this.value=value;
		this.type=type;
		this.position=position;
		this.outputparam = output;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Object getValue() {
		// TODO Auto-generated method stub
		return value;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public int getType() {
		// TODO Auto-generated method stub
		return type;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public int getPosition() {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * Checks if is output param.
	 *
	 * @return true, if successful
	 */
	public boolean IsOutputParam() {
		// TODO Auto-generated method stub
		return outputparam;
	}	
}
