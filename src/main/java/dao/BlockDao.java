package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import DatabaseUtils.DBConnectionPool;
import vo.Block;
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
	
	 public Boolean createBlockForBuildingId(Integer buildingId, List<Block> blocks) {
		    Connection conn = null;
		    PreparedStatement stmtBlockInsert = null;
		    PreparedStatement stmtBuildBlockInsert = null;
		    PreparedStatement stmtOwnershipInsert = null;
		    
		    String insertBlockQuery = "INSERT INTO block(name) VALUES(?)";
		    String selectBuildBlockQuery = "SELECT build_block_id FROM building_block WHERE building_id = ? AND block_id = ?";
		    String insertOwnershipQuery = "INSERT INTO building_block_ownership (build_block_id, user_id) VALUES (?, ?)";

		    try {
		    	
		        conn = DBConnectionPool.getConnection();
		        conn.setAutoCommit(false);

		        for (Block block : blocks) {
		        	
		            stmtBlockInsert = conn.prepareStatement(insertBlockQuery, Statement.RETURN_GENERATED_KEYS);
		            stmtBlockInsert.setString(1, block.getBlockName());
		            stmtBlockInsert.executeUpdate();

		            ResultSet rs = stmtBlockInsert.getGeneratedKeys();
		            int generatedBlockId = 0;
		            if (rs.next()) {
		                generatedBlockId = rs.getInt(1);
		            }

		            stmtBuildBlockInsert = conn.prepareStatement(selectBuildBlockQuery);
		            stmtBuildBlockInsert.setInt(1, buildingId);
		            stmtBuildBlockInsert.setInt(2, generatedBlockId);
		            ResultSet buildBlockResult = stmtBuildBlockInsert.executeQuery();

		            int buildBlockId = 0;
		            if (buildBlockResult.next()) {
		                buildBlockId = buildBlockResult.getInt("build_block_id");
		            }

		            stmtOwnershipInsert = conn.prepareStatement(insertOwnershipQuery);
		            stmtOwnershipInsert.setInt(1, buildBlockId);
		            stmtOwnershipInsert.setInt(2, block.getOwnerId());
		            stmtOwnershipInsert.executeUpdate();
		        }

		        conn.commit();
		        return true;

		    } catch (SQLException e) {
		        e.printStackTrace();
		        try {
		            if (conn != null) {
		                conn.rollback();
		            }
		        } catch (SQLException se) {
		            se.printStackTrace();
		        }
		        return false;
		    } finally {
		        try {
		            if (stmtBlockInsert != null) stmtBlockInsert.close();
		            if (stmtBuildBlockInsert != null) stmtBuildBlockInsert.close();
		            if (stmtOwnershipInsert != null) stmtOwnershipInsert.close();
		            if (conn != null) conn.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		}
	 
}


