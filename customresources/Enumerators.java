
package net.is_bg.ltf.config.customresources;

import com.cc.rest.client.enumerators.IPARAM;
import com.cc.rest.client.enumerators.IREST_PATH;

class Enumerators {
	

	
	
	final static IREST_PATH MAIN_PATH = new  IREST_PATH() {
		@Override
		public String getPath() {
			return "/ddebit" +"/dsk";
		}
	}; 
	

	
	private static class SELECTIVE_PATHS implements IREST_PATH{
		private String path;
		void setPath(String path){
			this.path = path;
		}
		
		@Override
		public String getPath() {
			return path;
		}
	}
	
	static IREST_PATH getSelectivePath(String path) {
		SELECTIVE_PATHS selPath = new SELECTIVE_PATHS();
		selPath.setPath(path);
		return selPath;
	}
	
	private static class SELECTIVE_QUERY_PARAM implements IPARAM{
		private String path;
		void setStringValue(String path){
			this.path = path;
		}
		
		@Override
		public String getStringValue() {
			// TODO Auto-generated method stub
			return path;
		}
	}
	
	static IPARAM getMunicipalityIdParam(){
		SELECTIVE_QUERY_PARAM p = new SELECTIVE_QUERY_PARAM();
		p.setStringValue("municipalityId");
		return p;
	}
	

}
