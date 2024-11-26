package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import DatabaseUtils.DBConnectionPool;
import vo.Device;

public class BlockDao {
	
	private final String COLLECT_DEVICE_LIST_QUERY = "SELECT B_DEV.device_id, D.name  " +
            "FROM block_device AS B_DEV " +
            "LEFT JOIN building_block AS B_BLK ON B_BLK.build_block_id = B_DEV.building_block_id " +
            "LEFT JOIN device AS D ON D.device_id = B_DEV.device_id " +
            "WHERE B_BLK.block_id = ?";
	
	 public List<Device> getBlockDevices(Integer blockId) {
	        List<Device> devices = new ArrayList<Device>();

	        

	        try (Connection conn = DBConnectionPool.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(COLLECT_DEVICE_LIST_QUERY)) {

	            stmt.setInt(1, blockId);

	            try (ResultSet rs = stmt.executeQuery()) {

	                while (rs.next()) {
	                	
	                    Device device = new Device();
	                    device.setDeviceId(rs.getInt("device_id"));
	                    device.setDeviceName(rs.getString("name"));
	                    device.setBlockId(rs.getInt("block_id"));

	                    devices.add(device);
	                }
	            }

	        } catch (SQLException e) {
	            e.printStackTrace(); // Handle SQL exceptions (you may want to log this)
	        }

	        return devices;
	    }
	
}
