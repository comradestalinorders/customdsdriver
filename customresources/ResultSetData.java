package net.is_bg.ltf.config.customresources;

import java.util.ArrayList;
import java.util.List;

class ResultSetData implements IResultSetData{
		private List<Object[]> res = new ArrayList<Object[]>();
		private List<String> columns = new ArrayList<String>();
		private Exception exception;
		private List<ColumnMetaData> columndata = new ArrayList<ColumnMetaData>();
		
		public List<String> getColumns()
		{
		  return this.columns;
		}
		
		public List<Object[]> getResult()
		{
		  return this.res;
		}
		
		public Exception getException()
		{
		  return this.exception;
		}
		
		public void setException(Exception exception)
		{
		  this.exception = exception;
		}

		public List<ColumnMetaData> getColumMetaData() {
			return columndata;
		}
}