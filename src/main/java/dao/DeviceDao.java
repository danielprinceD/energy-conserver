package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Builder.DeviceQueryBuilder;
import DatabaseUtils.DBConnectionPool;
import vo.Device;
import vo.Entry;
import vo.LiveSensorData;

public class DeviceDao {
	private final String DEVICE_DATA_INSERT_QUERY = "INSERT INTO energy_entries (device_id, power, voltage, amphere ) VALUES (?, ?, ?, ? )";
	
	private final String LIVE_DEVICE_DATE_QUERY = "SELECT t1.device_id, t1.power, t1.voltage, t1.amphere, t1.time "
			+ "FROM energy_entries t1 "
			+ "LEFT JOIN block_device AS B_DEV ON B_DEV.device_id = t1.device_id "
			+ "WHERE B_DEV.block_id = ? "
			+ "AND t1.time = (SELECT MAX(t2.time) "
			+ "FROM energy_entries t2 WHERE t2.device_id = t1.device_id) ";
	
	public Boolean addSensorData(Entry entry) {
		
		try (Connection conn = DBConnectionPool.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(DEVICE_DATA_INSERT_QUERY)) {
	             
	            stmt.setInt(1, entry.getDeviceId());
	            stmt.setDouble(2, entry.getPower());
	            stmt.setDouble(3, entry.getVoltage());
	            stmt.setDouble(4, entry.getAmphere());
	            
	            int rowsAffected = stmt.executeUpdate();
	            return rowsAffected > 0;
	        } catch (SQLException e) {
	            e.printStackTrace(); 
	            return false;  
	        }
		
	}
	
	public Boolean addDeviceToBlock(List<Device> devices, Integer blockId) throws SQLException {
	    String insertDeviceQuery = "INSERT INTO device (name) VALUES (?)";  // Insert into the device table
	    String insertBlockDeviceQuery = "INSERT INTO block_device (block_id, device_id) VALUES (?, ?)"; // Insert into the block_device table
	    
	    if(devices == null || blockId == null || devices.size() <= 0) {
        	return false;
        }
	    
	    try (Connection conn = DBConnectionPool.getConnection()) {
	        conn.setAutoCommit(false);  // Start transaction
	        
	        
	        
	        for (Device device : devices) {
	            // Step 1: Insert the device into the `device` table
	            try (PreparedStatement stmtDeviceInsert = conn.prepareStatement(insertDeviceQuery, Statement.RETURN_GENERATED_KEYS)) {
	                stmtDeviceInsert.setString(1, device.getDeviceName()); 
	                
	                stmtDeviceInsert.executeUpdate();
	                
	                ResultSet rs = stmtDeviceInsert.getGeneratedKeys();
	                int deviceId = 0;
	                if (rs.next()) {
	                    deviceId = rs.getInt(1);  
	                }

	                
	                try (PreparedStatement stmtBlockDeviceInsert = conn.prepareStatement(insertBlockDeviceQuery)) {
	                    stmtBlockDeviceInsert.setInt(1, blockId);  
	                    stmtBlockDeviceInsert.setInt(2, deviceId);  

	                    stmtBlockDeviceInsert.executeUpdate();  
	                }
	            }
	        }

	        conn.commit(); 
	        return true;  
	    } 
	}

	public List<Entry> getSensorData(){
		List<Entry> entries = new ArrayList<Entry>();
		return entries;
	}
	
	public List<LiveSensorData> getLiveSensorData(Integer blockId) {
        List<LiveSensorData> sensorDataList = new ArrayList<>();

        try (Connection conn = DBConnectionPool.getConnection()) {
        		PreparedStatement stmt = conn.prepareStatement(LIVE_DEVICE_DATE_QUERY);
        		stmt.setInt(1, blockId);
             ResultSet rs = stmt.executeQuery();
        		
            while (rs.next()) {
            	
                LiveSensorData liveData = new LiveSensorData();
                liveData.setDeviceId(rs.getInt("device_id"));
                liveData.setPower(rs.getDouble("power"));
                liveData.setVoltage(rs.getDouble("voltage"));
                liveData.setAmphere(rs.getDouble("amphere"));
                liveData.setDateTime(rs.getString("time"));
                sensorDataList.add(liveData);
                
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sensorDataList;
    }
	
}
