package dao;

import java.sql.Connection;
import java.sql.*;

import DatabaseUtils.DBConnectionPool;
import vo.User;

public class UserDao {
	
	private String LOGIN_QUERY = "SELECT id FROM users where name = ? AND password = ?";
	private String REGISTER_QUERY = "INSERT INTO users (name, password) VALUES (?, ?)";
	
	public Integer loginUser(User user) throws SQLException {
		
		try (Connection conn = DBConnectionPool.getConnection()) {
			PreparedStatement stmt = conn.prepareStatement(LOGIN_QUERY);
			
			stmt.setString(1, user.getUserName()); 
			stmt.setString(2, user.getPassword());
			
            ResultSet rs = stmt.executeQuery();
				
            if (rs.next()) {
                int userId = rs.getInt("id");
                return userId;
            }
        
	   }
		return null;
	}
	
	
	public Boolean registerUser(User user) throws SQLException {
		
		String username = user.getUserName();
        String password = user.getPassword();
        
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }

        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(REGISTER_QUERY)) {

            stmt.setString(1, username);
            stmt.setString(2, password); 

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
            	return true;
            }
            
        } 
		return false;
	}
	
}
