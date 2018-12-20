package net.is_bg.ltf.config.customresources;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;



/**
 * example resource entry for hissar
<Resource name="jdbc/1202" auth="Container"
type="net.is_bg.ltf.config.customresources.CustomDataSource"
factory="org.apache.naming.factory.BeanFactory"
endPoint="https:localhost:9000"
url="jdbc:postgresql://localhost:9000/hissar"
username="hsr"
keyPass="pass"
keyStoreAlias="ltf_wsclient"
keyStoreFile="D://client.keystore"
keyStorePass="pass"
municipalityId="1202"/>
*/


public class CustomDataSource implements DataSource {
	
	private String bar = "Mybar";
	private String keyStoreFile = "keyStoreFile";
	private String keyStorePass = "keyStorePass";
	private String keyStoreAlias = "keyStoreAlias";
	private String keyPass = "keyPass";
	private String endPoint;
	private String municipalityId;
	private RequesterSettings settings;
	private String url = "url";
	private String username="";
	
	public CustomDataSource(){
		
	}
	
	
	/**Initializes the datasource needed properties*/
	private void initSettings(){
		settings = new  RequesterSettings();
		settings.getServerSettings().fillFromString(endPoint);
		settings.setSocketProtocol(RequesterSettings.socketProtocolFromString("ssl"));
		settings.setStoreType(RequesterSettings.storeTypeFromString("jks"));
		settings.setKeyAlias(keyStoreAlias);
		settings.setKeyPass(keyPass);
		settings.setKeystoreFile(keyStoreFile);
		settings.setKeystorePass(keyStorePass);
	}
	

	@Override
	public Connection getConnection() throws SQLException {
		initSettings();
		return new CustomConnectionWs(new SqlExecutor(settings, municipalityId), this);
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		initSettings();
		return new CustomConnectionWs(new SqlExecutor(settings, municipalityId) , this);
	}
	
	
	
	/**Class that calls ws service execute sql method & returns result*/
	private static class SqlExecutor implements ISqlExecutor {
		RequesterSettings settings;
		String municipalityId;
		
		public SqlExecutor(RequesterSettings settings, String municipalityId) {
			this.settings = settings;
			this.municipalityId = municipalityId;
		}
		
		@Override
		public IResultSetData execSel(String sql) {
			try {
				return  CallServicesWs.execSel(sql, settings, municipalityId);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public IResultSetData execU(String sql) {
			try {
				return  CallServicesWs.execU(sql, settings, municipalityId);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		/*public IResultSetData execSql(String sql)  {
			try {
				return  CallServicesWs.execSql(sql, settings, municipalityId);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}*/
	}
	
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	public String getBar() {
		return bar;
	}

	public void setBar(String bar) {
		this.bar = bar;
	}

	public String getKeyStoreFile() {
		return keyStoreFile;
	}

	public void setKeyStoreFile(String keyStoreFile) {
		this.keyStoreFile = keyStoreFile;
	}

	public String getKeyStorePass() {
		return keyStorePass;
	}

	public void setKeyStorePass(String keyStorePass) {
		this.keyStorePass = keyStorePass;
	}

	public String getKeyStoreAlias() {
		return keyStoreAlias;
	}

	public void setKeyStoreAlias(String keyStoreAlias) {
		this.keyStoreAlias = keyStoreAlias;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getMunicipalityId() {
		return municipalityId;
	}

	public void setMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
	}

	public String getKeyPass() {
		return keyPass;
	}

	public void setKeyPass(String keyPass) {
		this.keyPass = keyPass;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
