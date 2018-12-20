package net.is_bg.ltf.config.customresources;


import com.cc.rest.client.ClientConfigurator.SOCKET_PROTOCOL;
import com.cc.rest.client.ClientConfigurator.STORE_TYPE;


/**
 * <pre>
 * 1.Determine application update settings
	- determine server location
	- determine application download directory
	- determine application download lib directory
 * @author lubo
 *</pre>
 */
class RequesterSettings {
	private ServerSettings serverSettings = new ServerSettings();
	
	private String keystoreFile;
	private String keystorePass;
	private String keyAlias;
	private String keyPass;
	private SOCKET_PROTOCOL socketProtocol;
	private STORE_TYPE storeType;
	private String application;
	private int readTimeOut = 600;
	
	public RequesterSettings(){
		application = "ltf";
	}


	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}
	

	public ServerSettings getServerSettings() {
		return serverSettings;
	}


	public String getKeystoreFile() {
		return keystoreFile;
	}


	public void setKeystoreFile(String keystoreFile) {
		this.keystoreFile = keystoreFile;
	}


	public String getKeystorePass() {
		return keystorePass;
	}


	public void setKeystorePass(String keystorePass) {
		this.keystorePass = keystorePass;
	}


	public String getKeyPass() {
		return keyPass;
	}


	public void setKeyPass(String keyPass) {
		this.keyPass = keyPass;
	}



	/***
	 * Read time out in seconds
	 * @return
	 */
	public int getReadTimeOut() {
		return readTimeOut;
	}

	/***
	 * Read time out in seconds
	 * @return
	 */
	public void setReadTimeOut(int seconds) {
		this.readTimeOut = seconds;
	}


	public STORE_TYPE getStoreType() {
		return storeType;
	}


	public void setStoreType(STORE_TYPE storeType) {
		this.storeType = storeType;
	}


	public String getKeyAlias() {
		return keyAlias;
	}


	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}


	public SOCKET_PROTOCOL getSocketProtocol() {
		return socketProtocol;
	}

	public void setSocketProtocol(SOCKET_PROTOCOL socketProtocol) {
		this.socketProtocol = socketProtocol;
	}
	
	public static SOCKET_PROTOCOL socketProtocolFromString(String socketProtocol){
		return socketProtocol ==null ? null :  socketProtocol.equals("ssl") ?  SOCKET_PROTOCOL.SSL : socketProtocol.equals("tls") ?  SOCKET_PROTOCOL.TLS : null;
	}
	
	public static STORE_TYPE storeTypeFromString(String storeType){
		return  storeType ==null ? null :   storeType.equals("pkcs12") ?  STORE_TYPE.PKCS12 : storeType.equals("jks") ?  STORE_TYPE.JKS : null;
	}


	
}
