<b>JDBC Transaction Manager</b><br/>
Bliblioteca útil para gerenciar as transações JDBC sem haver a necessidade de gerenciar as transações de banco de dados (open, close, commit or rollback).

Exemplo de utilização:


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



Empacotamento:

No momento do seu empacotamento não é necessário levar a pasta e arquivo: db/db.properties.

O diretório e arquivo: db/db.properties que não deve ser empacotado deverá ser criado no seu projeto com suas devidas credenciais de acesso ao banco de dados, conforme imagem:

![properties_example](/uploads/fcc4f741e1df5e5126c241599267a655/properties_example.png)

