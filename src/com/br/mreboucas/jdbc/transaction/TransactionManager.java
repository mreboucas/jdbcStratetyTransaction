package com.br.mreboucas.jdbc.transaction;

import java.sql.SQLException;

/**
 * @author Marcelo Rebouças Apr 7, 2017 - 4:07:04 PM [marceloreboucas10@gmail.com]
 */
public class TransactionManager {

	
	public <T> T doTransaction(TransactionCallback tc) throws Exception {
		
		ConnectionTransfer ct = null;
		T t = null;
		try {
			
			ct = new ConnectionTransfer();
			
			t = tc.execute(ct);
			
			ct.commit();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			ct.rollbacks();
			throw new SQLException();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			ct.rollbacks();
			throw new Exception();
			
		} finally {
			ConnectionTransfer.closeConnections(ct);
		}
		
		return t;
	}
}