package com.br.mreboucas.jdbc.run;

import com.br.mreboucas.jdbc.transaction.ConnectionTransfer;

/**
 * @author Marcelo Rebouças Apr 10, 2017 - 2:56:39 PM [marceloreboucas10@gmail.com]
 */
public class RunTransactionManager {
	
	public static void main(String[] args) {
		
		try {
			System.out.println("");
			ConnectionTransfer ct = new ConnectionTransfer();
			
			ct.closeConnections();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}