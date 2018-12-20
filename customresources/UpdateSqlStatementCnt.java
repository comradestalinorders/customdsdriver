package net.is_bg.ltf.config.customresources;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import net.is_bg.ltf.db.common.SqlStatement;

abstract class UpdateSqlStatementCnt extends SqlStatement{
	
		int updateCount;
		
		@Override
		protected void executeStatement(PreparedStatement arg0) throws SQLException {
			updateCount = arg0.executeUpdate();
		}
		
		public int getUpdateCnt(){
			return updateCount;
		}
	
}
