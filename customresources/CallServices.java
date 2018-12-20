package net.is_bg.ltf.config.customresources;

import com.cc.rest.client.ClientConfigurator;
import com.cc.rest.client.ClientConfigurator.Ssl;
import com.cc.rest.client.Requester;
import com.cc.rest.client.Requester.MEDIA_TYPE;
import com.cc.rest.client.enumerators.IREST_PATH;


class CallServices {

	private static final String EXEC_SQL = "execSQL";
	
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
	
	
	public static IResultSetData execSql(String sql, RequesterSettings dSettings, String municipality) throws Exception {
		configureClientConfigurator(dSettings);
		String cfName = dSettings.getServerSettings().toClientConfigurationName();
		return Requester.request(cfName).path(Enumerators.MAIN_PATH).subPath(new IREST_PATH() {
			@Override
			public String getPath() {
				return EXEC_SQL;
			}
		}).queryParam(Enumerators.getMunicipalityIdParam(), municipality)
		  .post(sql, MEDIA_TYPE.JSON,  MEDIA_TYPE.JSON).getResponseObject(ResultSetData.class);
	}
	
}
