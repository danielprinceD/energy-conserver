package DatabaseUtils;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

public class DBConnectionPool {
	public static BasicDataSource dataSource;
	
	protected static String USERNAME = "root";
	protected static String PASSWORD = "password";
	protected static String DATABASE_NAME = "energy";
	
	static {
		
		dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/" + DATABASE_NAME );
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        
        dataSource.setInitialSize(5);     	 // Initial pool size
        dataSource.setMaxTotal(20);       	 // Maximum pool size
        dataSource.setMaxIdle(10);        	 // Maximum idle connections
        dataSource.setMinIdle(5);        	 // Minimum idle connections
        dataSource.setMaxWaitMillis(10000);  // Max wait time for a connection		
	}
	
	public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
	
	public static void close() throws SQLException {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
