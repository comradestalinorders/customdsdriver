package net.is_bg.ltf.config.customresources;




/***
 * The protocol, ip and port of update center server!!!
 * @author lubo
 *
 */
class ServerSettings {
	private String ip = "10.240.110.181";
	private String port = "8080";
	private String protocol = "http";
	private boolean secure = false;
	public static final String CONTEXT= "bankws";
	
	public ServerSettings(){
		
	}
	
	public ServerSettings(String protocol, String ip, String port){
		this.protocol = protocol;
		this.ip = ip;
		this.port = port;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
		secure = protocol.equals("https");
	}
	
	/**
	 * Returns protocol:ip:port - suitable for configuration name!!!! 
	 * @return
	 */
	public String toClientConfigurationName(){
		return protocol + ":" + ip + ":" + port;
	}
	
	/***
	 * Gets server end point by ServerSettings properties!!!
	 * @return
	 */
	public String toEndPoint(){
		return protocol+"://" + ip + ":" + port + "/" + CONTEXT;
	}
	
	/***
	 * Fills the server settings from string in format protocol:ip:port
	 */
	public void fillFromString(String settings){
		String [] a = settings.split(":");
		protocol = a[0].toLowerCase(); ip = a[1]; port = a[2];
		secure = protocol.equals("https");
	}

	public boolean isSecure() {
		return secure;
	}
	
	
}
