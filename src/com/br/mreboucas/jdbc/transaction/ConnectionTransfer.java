package com.br.mreboucas.jdbc.transaction;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;


/**
 * @author Marcelo Rebouças Oct 13, 2016 - 2:17:57 PM [marceloreboucas10@gmail.com]
 */
public class ConnectionTransfer {

	//Driver banco oracle
    private static String className; 
    //Url de conexão
	private static String URL_CONNECTION_BANCO; 
	private static String USER_KEY_BANCO = "";
	private static String KEY_BANCO = "";
	public static boolean isProducao = Boolean.FALSE;
	
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rset = null;
	private PreparedStatement pstmt = null;
	private boolean isTransaction = false;
	
	static {
		
		setarVariaveisGlobaisDeConexao();
		
	}

	public ConnectionTransfer() throws Exception 
	{
		final String errorMsgOpenDbConnection = "Erro ao abrir conexão com o banco de dados.";
		
		try {

			Class.forName(this.className).newInstance();
			conn = DriverManager.getConnection(this.URL_CONNECTION_BANCO, this.USER_KEY_BANCO, this.KEY_BANCO);
			conn.setAutoCommit(false);
			
			System.out.println("--- Connections Opened ---");
			
		} catch (InstantiationException e1) {
			e1.printStackTrace();
			throw new Exception(errorMsgOpenDbConnection);
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
			throw new Exception(errorMsgOpenDbConnection);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			throw new Exception(errorMsgOpenDbConnection);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(errorMsgOpenDbConnection);
		}
	}
	
	public ConnectionTransfer(String user, String key) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName(this.className).newInstance();
		conn = DriverManager.getConnection(this.URL_CONNECTION_BANCO, user, key);
		conn.setAutoCommit(false);

		System.out.println("--- Connections Opened ---");
	}
	
	/**
    * <P><B>Note:</B> A <code>ConnectionTransfer</code>
    * Please use the static method: ConnectionTransfer.closeConnections(ConnectionTransfer ct) 
    * </p>
    */
	@Deprecated
	public boolean closeConnections()
	{
		try {
			
			if (rset != null)
				rset.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null && !conn.isClosed())
				conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("--- Connections Closed ---");

		return true;
	}
	
	public static boolean closeConnections(ConnectionTransfer ct)
	{
		try {
			
			if (ct != null) {
			
				if (ct.getRset() != null)
					ct.getRset().close();
				if (ct.getPstmt() != null)
					ct.getPstmt().close();
				if (ct.getConn() != null && !ct.getConn().isClosed())
					ct.getConn().close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("--- Connections Closed ---");

		return true;
	}

	public void commit() throws SQLException
	{
		if (conn != null && !conn.isClosed()){
			conn.commit();
			System.out.println("--- Commit Executed ---");
		}else{
			System.out.println("--- Commit Not Executed, because connection is " + (conn==null?"null":"closed") + " ---");
		}
	}

	public void rollbacks() {
		try {
			
			if (conn != null && !conn.isClosed()) {
				conn.rollback();
				System.out.println("--- Rollback Executed ---");
			}else{
				System.out.println("--- Rollback Not Executed, because connection is " + (conn==null?"null":"closed") + " ---");
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author Marcelo Rebouças Jan 14, 2016 - 11:14:19 AM
	 * @return void
	 */
	public void closeResultSetAndPreparedStatement() {
		
		try {
			
			if (getRset() != null) {
				getRset().close();
			}
			
			if (getPstmt() != null) {
				getPstmt().close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author Marcelo Rebouças Nov 9, 2015 - 12:00:01 PM
	 * @param sql
	 * @throws SQLException void
	 */
	public void setPreparedStatement(String sql) throws SQLException {
		//setPstmt(getConn().prepareStatement(sql));
		this.pstmt = getConn().prepareStatement(sql);
	}

	/**
	 * @author Marcelo Rebouças Mar 2, 2017 - 2:10:51 PM [marceloreboucas10@gmail.com]
	 * @description executa a consulta por meio do statement 		
	 * @param sql (String)
	 * @throws SQLException void
	 */
	public void executeQuerySelectStatement(String sql) throws SQLException {
		setStatement();
		//setRset(getStmt().executeQuery(sql));
		this.rset = getStmt().executeQuery(sql.toString());
	}
	/**
	 * @author Marcelo Rebouças Mar 2, 2017 - 2:10:56 PM [marceloreboucas10@gmail.com]
	 * @description executa a consulta por meio do statement 		
	 * @param sql (StringBuilder)
	 * @throws SQLException void
	 */
	public void executeQuerySelectStatement(StringBuilder sql) throws SQLException {
		executeQuerySelectStatement(sql.toString());
	}
	
	public Integer executeInsertOrUpdateOrDeleteStatement(String sql) throws SQLException
	{
		setStatement();

		return getStmt().executeUpdate(sql.toString());
	}

	/**
	 * @author Marcelo Rebouças Jan 5, 2016 - 11:50:14 AM
	 * @param sql
	 * @return Boolean
	 * @throws SQLException 
	 */
	public Integer executeInsertOrUpdateOrDeleteStatement(StringBuilder sql) throws SQLException
	{
		setStatement();

		return getStmt().executeUpdate(sql.toString());
	}

	
	/**
	 * @author Marcelo Rebouças Dec 10, 2015 - 4:27:49 PM
	 * @param sql
	 * @throws SQLException void
	 */
	private void setStatement() throws SQLException {
		//setStmt(getConn().createStatement());
		this.stmt = getConn().createStatement();
	}
	
	public void setPreparedStatement(StringBuilder sql) throws SQLException {
		setPreparedStatement(sql.toString());
	}
	
	/**
	 * @author Marcelo Rebouças Nov 9, 2015 - 11:59:57 AM
	 * @description: executa a query do objeto prepared statement e seta o 
	 * resultado no resultset
	 * @obs: antes de invocar esse método, é necessário invocar o método setPreparedStatement(String sql)
	 * @throws SQLException void
	 */
	public void executeQuerySelect() throws SQLException {
		//setRset(getPstmt().executeQuery());
		this.rset = getPstmt().executeQuery();
	}
	
	/**
	 * @author Marcelo Rebouças Mar 10, 2017 - 4:49:42 PM [marceloreboucas10@gmail.com]
	 * @description monta os parametros (?) baseado no tamanho da lista 		
	 * @param list
	 * @return String
	 */
	public String getParamsListPreparedStatement(List list) {
		
		String params = "";
		
		if (list != null && !list.isEmpty()) {

			int m = 1;
			while (list.size() >= m) {
				
				params += "?,";
				m++;
				
			}
			
			params = params.substring(0, params.length() - 1);
		}
		
		return params;
		
	}
	
	public Integer executeInsertOrUpdateOrDeletePreparedStatement() throws SQLException {
		return getPstmt().executeUpdate();
	}
	
	/**
	 * @author Marcelo Rebouças Feb 24, 2016 - 10:07:21 AM
	 * @description gera a pk a partir de uma sequence 			
	 * @param sequence
	 * @return Long
	 * @throws SQLException 
	 */
	public Long getPrimaryKey(final String sequence) throws SQLException {

        Long key = null;

        setPreparedStatement(" SELECT " + sequence + ".NEXTVAL KEY FROM DUAL");
        executeQuerySelect();

        if (getRset().next()) {
            key = getRset().getLong("KEY");
        }
        
        return key;
    }

	public Connection getConn() {return conn;}
	//public void setConn(Connection conn) {this.conn = conn;}

	public ResultSet getRset() {return rset;}
	//public void setRset(ResultSet rset) {this.rset = rset;}

	public boolean getIsTransaction() {return isTransaction;}
	//public void setIsTransaction(boolean isTransaction) {this.isTransaction = isTransaction;}

	public PreparedStatement getPstmt() {
		return pstmt;
	}

/*	public void setPstmt(PreparedStatement pstmt) {
		this.pstmt = pstmt;
	}
*/
	/**
	 * @return the stmt
	 */
	public Statement getStmt() {
		return stmt;
	}
	
	
	/**
	 * @author: Marcelo Rebouças - Mar 5, 2013 - 8:00:12 AM
	 * @description: responsável por setar as variáveis globais de conexão dessa class. 
	 * 				 Esse procedimento é útil, pois existe um grande legado com conexão via JDBC puro.	
	 * @returns: void
	 */
	private static void setarVariaveisGlobaisDeConexao() {
		
		try {
			
			Properties properties = readDataBaseProperties();
			
			String isProd = properties.getProperty("connection.is_prod").trim();
			
			String typeDb = "desen";
			
			if (isProd.equalsIgnoreCase("true")) {
				typeDb = "prod";
				isProducao = Boolean.TRUE;
			}
			
			/**
			 * seta as propriedades globais de conexão
			 */
			className = properties.getProperty("connection.driver_class").trim();
			URL_CONNECTION_BANCO = properties.getProperty("connection.url." + typeDb).trim();
			USER_KEY_BANCO = properties.getProperty("connection.username." + typeDb).trim();
			KEY_BANCO = properties.getProperty("connection.password." + typeDb).trim();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Verifique o arquivo db.properties. Ele deverá ser criado no mesmo nível da pasta src do seu projeto.");
		}
	}
	
	/**
	 * @author: Marcelo Rebouças - Mar 5, 2013 - 8:18:16 AM
	 * @throws IOException 
	 * @description: recupera o arquivo db.properties
	 * @returns: Properties
	 */
	private static Properties readDataBaseProperties() throws IOException {
		Properties allProperties = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		allProperties.load(loader.getResourceAsStream("db.properties"));

		return allProperties;
	}
}