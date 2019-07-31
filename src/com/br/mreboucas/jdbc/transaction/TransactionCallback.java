package com.br.mreboucas.jdbc.transaction;


/**
 * @author Marcelo Rebou�as Apr 7, 2017 - 4:26:17 PM [marceloreboucas10@gmail.com]
 */
public interface TransactionCallback {
	
    public <T> T execute(ConnectionTransfer ct) throws Exception;
    
}