package net.is_bg.ltf.config.customresources;


class ColumnMetaData {

	private String columnName;
	private int columnType;
	
	private String columnClassName;
	private String catalogName;
	private String tableName;
	private int precision;
	private int displaySize;
	
	
	String getColumnName() {
		return columnName;
	}
	void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	int getColumnType() {
		return columnType;
	}
	void setColumnType(int columnType) {
		this.columnType = columnType;
	}
	String getColumnClassName() {
		return columnClassName;
	}
	void setColumnClassName(String columnClassName) {
		this.columnClassName = columnClassName;
	}
	String getCatalogName() {
		return catalogName;
	}
	void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	String getTableName() {
		return tableName;
	}
	void setTableName(String tableName) {
		this.tableName = tableName;
	}
	int getPrecision() {
		return precision;
	}
	void setPrecision(int precision) {
		this.precision = precision;
	}
	int getDisplaySize() {
		return displaySize;
	}
	void setDisplaySize(int displaySize) {
		this.displaySize = displaySize;
	}
}