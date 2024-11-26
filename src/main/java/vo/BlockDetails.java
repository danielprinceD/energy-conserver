package vo;

public class BlockDetails extends Block {
	
	private Integer totalDevices;
	private String ownerName;
	public void setTotalDevices(Integer totalDevices) {
		this.totalDevices = totalDevices;
	}
	
	public Integer getTotalDevices() {
		return totalDevices;
	}
	
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getOwnerName() {
		return ownerName;
	}

}
