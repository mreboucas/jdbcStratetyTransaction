package java.jdbcStrategyTransaction;

import java.sql.SQLException;
import java.text.ParseException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.br.mreboucas.jdbc.transaction.ConnectionTransfer;
import com.br.mreboucas.jdbc.transaction.TransactionCallback;
import com.br.mreboucas.jdbc.transaction.TransactionManager;

public class JdbcTransactionTest {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	void testarConnection() {
		
		try {
			new TransactionManager().doTransaction(new TransactionCallback() {
				
				@Override
				public <T> T execute(ConnectionTransfer ct) throws SQLException, ParseException {
					
					/** SEUS MÉTODOS DE PERSISTÊNCIA AQUI
					
					metodExample1(ct);
					metodExample2(ct);
					metodExample3(ct);
					
					*/
					
					return null;
				}
			});
		} catch (Exception e) {
			logger.error("Transaction is not started correctly");
		}
	}
	
}
