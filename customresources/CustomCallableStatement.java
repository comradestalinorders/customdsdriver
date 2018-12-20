package net.is_bg.ltf.config.customresources;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class SqlParsedProcParamsResult {
	/**A map with the parsed proc parameters - the key is the the parameter index in arguments list, the value is ? or a set value*/
	String sql;               //the transformed sql selet string
	String procName;          //the extracted procdedure name
	String [] args;           // all arguments of the proc including input  /  output arguments
	List<String> inArgs;      //a list that contains only in arguments
	boolean hasRetparam;      //true if proc has return argument
	Map<Integer, Integer> outParamIndexSubstitutionMap; // a map that translates original output param index into index of  the reuterned resultse after select is executed
	Map<Integer, BindVariableInfo> outParamsMap;        //separate map - only of output arguments
	Map<Integer, BindVariableInfo> inParamsMap;         
	String sqltrans;
	
	SqlParsedProcParamsResult() {
	}
	String getSql() {
		return sql;
	}
	String getProcName() {
		return procName;
	}
	String[] getArgs() {
		return args;
	}
	List<String> getInArgs() {
		return inArgs;
	}
	boolean isHasRetparam() {
		return hasRetparam;
	}
	Map<Integer, Integer> getOutParamIndexSubstitutionMap() {
		return outParamIndexSubstitutionMap;
	}
	Map<Integer, BindVariableInfo> getOutParamsMap() {
		return outParamsMap;
	}
	Map<Integer, BindVariableInfo> getInParamsMap() {
		return inParamsMap;
	}
	String getSqltrans() {
		return sqltrans;
	}
}


/**Helper class that contains the transformed SQL*/
class SqlParsedProcParams{
	SqlParsedProcParamsResult result = new SqlParsedProcParamsResult();
	
	
	private SqlParsedProcParams(String sql){
		result.sql = sql;
	}
	
	/***
	 * Transforms call proc statement into a select statement &  fills the args array 
	 * @param sql
	 * @return
	 */
	private void parseSql() {
		result.procName = getFunctionName(result.sql);
		result.hasRetparam = hasReturnParam(result.sql);
		String arglist = result.sql.replaceFirst(Constants.CALL_STMT_BEGIN, "").replaceAll(result.procName, "");
		int argstart = arglist.indexOf("(");
		int argend = arglist.lastIndexOf(")");
		arglist = arglist.substring(argstart + 1, argend);
		if(result.hasRetparam) arglist = " ?," + arglist; arglist = arglist.trim();
		result.args = arglist.split(",");
	}
	
	/***
	 * Extracts the input parameters in a separate list!
	 */
   private 	void extractInParams(Map<Integer, BindVariableInfo> values) {
		//loop through all arguments & take only input arguments
		result.inArgs = new ArrayList<String>();
		int startIndex = 0;
		for(String a: result.args) {
			startIndex++;
			BindVariableInfo ii = values.get(startIndex); if(ii == null) continue;
			//take only input arguments
			if(a.contains("?") && !ii.IsOutputParam()) {
				result.inArgs.add(a);
			}
		}
	}
	
	
	
	/**Creates out param substitution map the key is the original param index, the value is the new param index.
	 * Must be called after all parameters are set*/
	private void splitParams(Map<Integer, BindVariableInfo> values){
		result.inParamsMap = new HashMap<Integer, BindVariableInfo>();
		result.outParamsMap = new HashMap<Integer, BindVariableInfo>();
		List<BindVariableInfo> outParams = new ArrayList<BindVariableInfo>();
		List<BindVariableInfo> inParams = new ArrayList<BindVariableInfo>();
		
		//fill in / out params in separate list
		for(BindVariableInfo b : values.values()) {if(b.IsOutputParam()) { outParams.add(b); result.outParamsMap.put(b.getPosition(), b);} else inParams.add(b);}
		
		//sort out params by index
		Collections.sort(outParams, new Comparator<BindVariableInfo>() {
			@Override
			public int compare(BindVariableInfo arg0, BindVariableInfo arg1) {
				return arg0.getPosition() - arg1.getPosition();
			}
		});
		
		//sort in params by param index
		Collections.sort(inParams, new Comparator<BindVariableInfo>() {
			@Override
			public int compare(BindVariableInfo arg0, BindVariableInfo arg1) {
				return arg0.getPosition() - arg1.getPosition();
			}
		});
		int startIndex = 1;
		result.outParamIndexSubstitutionMap = new HashMap<Integer, Integer>();
		for(BindVariableInfo b: outParams) {
			result.outParamIndexSubstitutionMap.put(b.getPosition(), startIndex++);
		}
		
		
		//leave only input parameters in inParamsMap map
		result.inParamsMap.clear();
		startIndex = 1;
		for(BindVariableInfo b:inParams) {result.inParamsMap.put(startIndex++, b);}
	}
	
	
	/**
	 * Returns a transformed procedure call as an sql select String.
	 * Leaves the values map only with input parameters!
	 * Creates mapping between original & new outParam Indexes...
	 * @param values
	 * @return
	 */
	static SqlParsedProcParamsResult prepareCall(String sql, Map<Integer, BindVariableInfo> values) {
		   SqlParsedProcParams p = new SqlParsedProcParams(sql);
		   p.parseSql();
		   p.extractInParams(values);
		   p.splitParams(values);
		   p.result.sqltrans = null;
		   int i =0;
		   String ar="(";
		   for(String a : p.result.inArgs) {
			   if(i > 0) ar+=", " + a; else ar+= a;
			   i++;
		   }
		   ar+=")";
		   p.result.sqltrans = " select * from " + p.result.procName + "  " + ar;
		   return p.result;
	}
	
	
	private static boolean hasReturnParam(String aSql) {
		return aSql.matches(Constants.HAS_RETURN_PARAM_CALL_STMT_BEGIN);
	}
	
	private static String getFunctionName(String aSql) {
		String fullName = aSql.replaceFirst(Constants.CALL_STMT_BEGIN, "").replaceFirst(Constants.CALL_STMT_END, "");
		return fullName;
	}
}


/**Callable statement implementation!!!*/
class CustomCallableStatement extends CustomPreparedStatement  implements CallableStatement{
	
	private String sql;

	public static void main(String [] args) {
		System.out.println("Test");
		SqlParsedProcParamsResult r=  SqlParsedProcParams.prepareCall("{ ?  = call  PAYDOCUMENT_PKG.InterestList  (  ?, ?,? ,?  )}", new HashMap<Integer, BindVariableInfo>());
		/*SqlParsedProcParams t = new SqlParsedProcParams("{ ?  = call  PAYDOCUMENT_PKG.InterestList  (  ?, ?,? ,?  )}");
		t.prepareCall(new HashMap<Integer, BindVariableInfo>());*/
		System.out.println(r);
	}
	
	public CustomCallableStatement(CustomConnectionWs customConnection, String sql, ISqlExecutor sqlExecutor) {
		super(customConnection, sql);
		this.sql = sql;
	}
	
	
	@Override
	public int executeUpdate() throws SQLException {
		SqlParsedProcParams.prepareCall(sql, values);
		return super.executeUpdate();
	}
	
	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		SqlParsedProcParams.prepareCall(sql, values);
		return super.executeQuery(sql);
	}
	
	@Override
	public boolean execute() throws SQLException {
		SqlParsedProcParams.prepareCall(sql, values);
		return super.execute();
	}
	
	@Override
	public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
		values.put(parameterIndex, new BindVariableInfo(new Object(), sqlType, parameterIndex, true));
	}

	@Override
	public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
		throw new UnsupportedOperationException("Registering out parameter by name is not supported....");
	}

	@Override
	public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
		registerOutParameter(parameterIndex, sqlType);
	}

	@Override
	public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
		registerOutParameter(parameterIndex, sqlType);
	}

	@Override
	public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
		throw new UnsupportedOperationException("Registering out parameter by name is not supported....");
	}

	@Override
	public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
		throw new UnsupportedOperationException("Registering out parameter by name is not supported....");
	}

	@Override
	public Array getArray(int parameterIndex) throws SQLException {
		return null;
	}

	@Override
	public Array getArray(String parameterName) throws SQLException {
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getBoolean(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getBoolean(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte getByte(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte getByte(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getBytes(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getBytes(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(String parameterName, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDouble(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDouble(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFloat(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFloat(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLong(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLong(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Reader getNCharacterStream(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getNCharacterStream(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ref getRef(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ref getRef(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowId getRowId(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowId getRowId(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getShort(int parameterIndex) throws SQLException {
		return 0;
	}

	@Override
	public short getShort(String parameterName) throws SQLException {
		return 0;
	}

	@Override
	public String getString(int parameterIndex) throws SQLException {
		return null;
	}

	@Override
	public String getString(String parameterName) throws SQLException {
		return null;
	}

	@Override
	public Time getTime(int parameterIndex) throws SQLException {
		return null;
	}

	@Override
	public Time getTime(String parameterName) throws SQLException {
		return null;
	}

	@Override
	public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
		return null;
	}

	@Override
	public Time getTime(String parameterName, Calendar cal) throws SQLException {
		return null;
	}

	@Override
	public Timestamp getTimestamp(int parameterIndex) throws SQLException {
		return null;
	}

	@Override
	public Timestamp getTimestamp(String parameterName) throws SQLException {
		return null;
	}

	@Override
	public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
		return null;
	}

	@Override
	public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getURL(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getURL(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBlob(String parameterName, Blob x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBoolean(String parameterName, boolean x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setByte(String parameterName, byte x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBytes(String parameterName, byte[] x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClob(String parameterName, Clob x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClob(String parameterName, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClob(String parameterName, Reader reader, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDate(String parameterName, Date x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDouble(String parameterName, double x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFloat(String parameterName, float x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInt(String parameterName, int x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLong(String parameterName, long x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNClob(String parameterName, NClob value) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNClob(String parameterName, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNString(String parameterName, String value) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNull(String parameterName, int sqlType) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObject(String parameterName, Object x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRowId(String parameterName, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setShort(String parameterName, short x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setString(String parameterName, String x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTime(String parameterName, Time x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setURL(String parameterName, URL val) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean wasNull() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	
}
