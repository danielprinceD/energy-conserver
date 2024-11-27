package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import DatabaseUtils.DBConnectionPool;
import vo.User;

public class UserDao {
	
	private final String LOGIN_QUERY = "SELECT id FROM users where name = ? AND password = ?";
	private final String REGISTER_QUERY = "INSERT INTO users (name, password) VALUES (?, ?)";
	private final String ALL_USERS_QUERY = "SELECT id, name FROM users"; 
	
	public User loginUser(User user) throws SQLException {
		
		try (Connection conn = DBConnectionPool.getConnection()) {
			PreparedStatement stmt = conn.prepareStatement(LOGIN_QUERY);
			
			stmt.setString(1, user.getUserName()); 
			stmt.setString(2, user.getPassword());
			
            ResultSet rs = stmt.executeQuery();
				
            if (rs.next()) {
                int userId = rs.getInt("id");
                user.setId(userId);
                return user;
            }
	   }
		return null;
	}
	
	public List<User> getAllUsers() {
	    List<User> users = new ArrayList<User>();
	    
	    
	    try (Connection conn = DBConnectionPool.getConnection(); 
	         PreparedStatement stmt = conn.prepareStatement(ALL_USERS_QUERY); 
	         ResultSet rs = stmt.executeQuery()) {
	        
	        while (rs.next()) {
	            User user = new User();
	            user.setId(rs.getInt("id"));
	            user.setUserName(rs.getString("name"));
	            
	            users.add(user);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); 
	    }
	    
	    return users;
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
