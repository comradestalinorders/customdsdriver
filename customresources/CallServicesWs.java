package net.is_bg.ltf.config.customresources;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cc.rest.client.ClientConfigurator;
import com.cc.rest.client.ClientConfigurator.Ssl;
import com.cc.rest.client.Requester;
import com.cc.rest.client.enumerators.IREST_PATH;


class CallServicesWs {

	//private static final String EXEC_SQL = "execSQL";
	private static final String EXEC_SEL = "execSel";
	private static final String EXEC_U = "execU";
	
	static void configureClientConfigurator(RequesterSettings dSettings) {
		try{
			ServerSettings sSettings = dSettings.getServerSettings();
			Ssl ssl =  ClientConfigurator.configure(sSettings.toClientConfigurationName()).targetEndpoint(sSettings.toEndPoint()).readTimeout(dSettings.getReadTimeOut());
			if(sSettings.isSecure()){
				ssl.protocol(dSettings.getSocketProtocol()).
				keystore(dSettings.getStoreType(), dSettings.getKeystoreFile(), dSettings.getKeystorePass()).
				privateKey(dSettings.getKeyAlias(), dSettings.getKeyPass()).trustAllCerts().complete();
			}else{
				ssl.noSSL();
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static List<ColumnMetaData> processColumns(List<?> columns){
		List<ColumnMetaData> mdata = new ArrayList<ColumnMetaData>();
		for(Object o:columns) {
			ColumnMetaData md = new ColumnMetaData();
			md.setColumnName(o.toString());
			mdata.add(md);
		}
		return mdata;
	}
	
	private static List<Object []> processResult(List resultList){
		List<Object []> result = new ArrayList<Object []>();
		for(Object o : resultList) {
			List m = (List)o;
			Object [] val = new Object[m.size()];
			int i=0;
			for(Object oo: m) {
				val[i++] = oo;
			}
			result.add(val);
		}
		return result;
	}
	
	
	private static   IResultSetData toIresultSetData(Object data) {
		ResSetData ressetdata = new ResSetData();
		if(data instanceof LinkedHashMap) {
			LinkedHashMap lm = (LinkedHashMap)data;
			List columns = (List)lm.get("columns");
			List result = (List)lm.get("result");
			List columnMetaData = (List)lm.get("columnMetaData");
			if(columns != null) ressetdata.setColumnMetaData(processColumns(columns));
			if(columnMetaData != null) ressetdata.setColumnMetaData(processColumnsMetaData(columnMetaData));
			if(result!=null) {
				if((ressetdata.columnMetaData == null || ressetdata.columnMetaData.size() ==0) && result.size() > 0 ) {
					List<ColumnMetaData> cols = new ArrayList<ColumnMetaData>();
					List<Object []> cs = (List<Object []>)result.get(0);
					for(Object c : cs) {
						ColumnMetaData md = new ColumnMetaData();
						md.setColumnName("column");
						cols.add(md);
					}
					ressetdata.setColumnMetaData(cols);
				}
				ressetdata.setResult(processResult(result));
			}
		}
		return ressetdata;
	}
	
	/***
	 * Processes the new columnMetaData Structure
	 * @param columnMetaData
	 * @return
	 */
	private static List<ColumnMetaData> processColumnsMetaData(List columnMetaData) {
		List<ColumnMetaData> columnMeta = new ArrayList<ColumnMetaData>();
		Map<Object, Object> m;
		for(Object o : columnMetaData) {
			m = (Map)o;
			ColumnMetaData md = new ColumnMetaData();
			md.setColumnName(m.get("columnName").toString());
			columnMeta.add(md);
		}
		return columnMeta;
	}

	/*static IResultSetData execSql(String sql, RequesterSettings dSettings, String municipality) throws Exception {
		configureClientConfigurator(dSettings);
		String cfName = dSettings.getServerSettings().toClientConfigurationName();
		IREST_PATH path = Enumerators.getSelectivePath(Enumerators.MAIN_PATH.getPath() +"/"+EXEC_SQL);
		Object res = Requester.request(cfName).path(path).queryParam(Enumerators.getMunicipalityIdParam(), municipality).post(sql).getResponseAsObject(Object.class);
		return toIresultSetData(res);
	}*/
	
	static IResultSetData execU(String sql, RequesterSettings dSettings, String municipality) throws Exception {
		configureClientConfigurator(dSettings);
		String cfName = dSettings.getServerSettings().toClientConfigurationName();
		IREST_PATH path = Enumerators.getSelectivePath(Enumerators.MAIN_PATH.getPath() +"/"+ EXEC_U);
		Object res = Requester.request(cfName).path(path).queryParam(Enumerators.getMunicipalityIdParam(), municipality).post(sql).getResponseAsObject(Object.class);
		return toIresultSetData(res);
	}
	
	static IResultSetData execSel(String sql, RequesterSettings dSettings, String municipality) throws Exception {
		configureClientConfigurator(dSettings);
		String cfName = dSettings.getServerSettings().toClientConfigurationName();
		IREST_PATH path = Enumerators.getSelectivePath(Enumerators.MAIN_PATH.getPath() +"/"+ EXEC_SEL);
		Object res = Requester.request(cfName).path(path).queryParam(Enumerators.getMunicipalityIdParam(), municipality).post(sql).getResponseAsObject(Object.class);
		return toIresultSetData(res);
	}
	
	static class ResSetData implements IResultSetData{
		List<ColumnMetaData> columnMetaData = new ArrayList<ColumnMetaData>();
		Exception exception;
		List<String> columns = new ArrayList<String>();
		List<Object []> result = new ArrayList<Object []>();


		@Override
		public List<Object[]> getResult() {
			return result;
		}

		void setColumnMetaData(List<ColumnMetaData> columnMetaData) {
			this.columnMetaData = columnMetaData;
			columns = new ArrayList<String>();
			for(ColumnMetaData m :columnMetaData) {
				columns.add(m.getColumnName());
			}
		}

		void setException(Exception exception) {
			this.exception = exception;
		}

		void setResult(List<Object[]> result) {
			this.result = result;
		}

		@Override
		public List<String> getColumns() {
			return columns;
		}

		@Override
		public Exception getException() {
			return null;
		}
	}
	
}
