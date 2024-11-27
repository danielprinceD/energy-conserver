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
	            e.printStackTrace();
	        }

	        return devices;
	    }
	
	 
	 public Boolean createBlockForBuildingId(Integer buildingId, List<Block> blocks) {
		    String insertBlockQuery = "INSERT INTO block(name) VALUES(?)";
		    String insertBuildingBlockQuery = "INSERT INTO building_block (building_id, block_id) VALUES(?, ?)";
		    String insertOwnershipQuery = "INSERT INTO building_block_ownership (building_block_id, user_id) VALUES (?, ?)";

		    try (Connection conn = DBConnectionPool.getConnection()) {
		        conn.setAutoCommit(false); // Start transaction

		        for (Block block : blocks) {
		            // Insert into the block table
		            try (PreparedStatement stmtBlockInsert = conn.prepareStatement(insertBlockQuery, Statement.RETURN_GENERATED_KEYS)) {
		                stmtBlockInsert.setString(1, block.getBlockName());
		                stmtBlockInsert.executeUpdate();

		                // Get the generated block_id for the block inserted
		                ResultSet rs = stmtBlockInsert.getGeneratedKeys();
		                int generatedBlockId = 0;
		                if (rs.next()) {
		                    generatedBlockId = rs.getInt(1);
		                }

		                // Insert into the building_block table
		                try (PreparedStatement stmtBuildingBlockInsert = conn.prepareStatement(insertBuildingBlockQuery, Statement.RETURN_GENERATED_KEYS)) {
		                    stmtBuildingBlockInsert.setInt(1, buildingId);
		                    stmtBuildingBlockInsert.setInt(2, generatedBlockId);
		                    stmtBuildingBlockInsert.executeUpdate();

		                    // Get the generated building_block_id
		                    ResultSet rsBB = stmtBuildingBlockInsert.getGeneratedKeys();
		                    int generatedBBId = 0;
		                    if (rsBB.next()) {
		                        generatedBBId = rsBB.getInt(1);
		                    }

		                    // Insert into the building_block_ownership table
		                    try (PreparedStatement stmtOwnershipInsert = conn.prepareStatement(insertOwnershipQuery)) {
		                        stmtOwnershipInsert.setInt(1, generatedBBId); // Use the generated building_block_id
		                        stmtOwnershipInsert.setInt(2, block.getOwnerId());
		                        stmtOwnershipInsert.executeUpdate();
		                    }
		                }
		            } catch (SQLException e) {
		                conn.rollback(); // Rollback the transaction in case of an error
		                e.printStackTrace();
		                return false;
		            }
		        }

		        conn.commit(); // Commit transaction if all queries are successful
		        return true;

		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
		}

	 
}

