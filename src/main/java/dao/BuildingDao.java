package dao;

import java.util.ArrayList;
import java.util.List;
import DatabaseUtils.DBConnectionPool;
import java.sql.*;
import vo.BlockDetails;
import vo.Building;

public class BuildingDao {
	
	private static String CREATE_BUILDING_QUERY = "INSERT INTO building (name, location, created_by) VALUES (?, ?, ?)";
	private static String GET_BUILDING_DETAIL = "SELECT BB.building_id, BB.block_id, BLK.name AS block_name, \" +\n"
			+ "                       \"BBOWN.user_id AS owner_id, U.name AS owner_name, \" +\n"
			+ "                       \"COUNT(BLKDEV.device_id) AS total_devices \" +\n"
			+ "                       \"FROM block_device AS BLKDEV \" +\n"
			+ "                       \"LEFT JOIN building_block AS BB ON BB.build_block_id = BLKDEV.building_block_id \" +\n"
			+ "                       \"LEFT JOIN block AS BLK ON BLK.block_id = BB.block_id \" +\n"
			+ "                       \"LEFT JOIN building_block_ownership AS BBOWN ON BBOWN.building_block_id = BLKDEV.building_block_id \" +\n"
			+ "                       \"LEFT JOIN users AS U ON U.id = BBOWN.user_id \" +\n"
			+ "                       \"WHERE BB.building_id = ? \" +\n"
			+ "                       \"GROUP BY BB.building_id, BB.block_id, BBOWN.user_id, U.name";
	
	
	 String BUILDING_LIST_QUERY = "SELECT building_id, name, location FROM building WHERE createdBy = ?";
	
	 public Boolean createBuilding(List<Building> buildings) {

	        try (Connection conn = DBConnectionPool.getConnection()) {
	            conn.setAutoCommit(false);

	            try (PreparedStatement stmt = conn.prepareStatement(CREATE_BUILDING_QUERY)) {
	                for (Building building : buildings) {
	                    stmt.setString(1, building.getBuildingName());
	                    stmt.setString(2, building.getBuildingLocation());
	                    stmt.setInt(3, building.getCreatedBy());
	                    stmt.addBatch();
	                }

	                int[] result = stmt.executeBatch();
	                conn.commit();

	                for (int res : result) {
	                    if (res == PreparedStatement.EXECUTE_FAILED) {
	                        conn.rollback();
	                        return false;
	                    }
	                }

	                return true;
	            } catch (SQLException e) {
	                conn.rollback();
	                e.printStackTrace();
	                return false;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	 }
	

	 
	 public List<BlockDetails> getBuildingDetailsByBuildingId( Integer buildingId) {
		 List<BlockDetails> blockDetailsList = new ArrayList<>();
		 try (Connection conn = DBConnectionPool.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(GET_BUILDING_DETAIL)) {

	            stmt.setInt(1, buildingId);

	            try (ResultSet rs = stmt.executeQuery()) {
	            	
	                while (rs.next()) {
	                	
	                    int blockId = rs.getInt("block_id");
	                    String blockName = rs.getString("block_name");
	                    int ownerId = rs.getInt("owner_id");
	                    String ownerName = rs.getString("owner_name");
	                    int totalDevices = rs.getInt("total_devices");

	                    BlockDetails blockDetails = new BlockDetails();
	                    
	                    blockDetails.setBlockId(blockId);
	                    blockDetails.setBlockName(blockName);
	                    blockDetails.setOwnerId(ownerId);
	                    blockDetails.setOwnerName(ownerName);
	                    blockDetails.setTotalDevices(totalDevices);

	                    blockDetailsList.add(blockDetails);
	                }
	            }

	        } catch (SQLException e) {
	            e.printStackTrace(); 
	        }

	        return blockDetailsList;

	    }
	 
	 public List<Building> getBuildingList(Integer userId) {
	        List<Building> buildings = new ArrayList<Building>();


	        try (Connection conn = DBConnectionPool.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(BUILDING_LIST_QUERY)) {

	            stmt.setInt(1, userId);

	            try (ResultSet rs = stmt.executeQuery()) {

	                while (rs.next()) {
	                	
	                    Building building = new Building();
	                    building.setBuildingId(rs.getInt("building_id"));
	                    building.setBuildingName(rs.getString("name"));
	                    building.setBuildingLocation(rs.getString("location"));
	                    building.setCreatedBy(userId); 

	                    buildings.add(building);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace(); 
	        }

	        return buildings;
	    }
	 
	 
	 
}

