package Builder;

public class DeviceQueryBuilder {
	private StringBuilder deviceQuery = new StringBuilder();
	
	public DeviceQueryBuilder(String query) {
		deviceQuery.append(query);
	}
	
	public String getDeviceQuery() {
		return deviceQuery.toString();
	}
	
	public void addStartAndEndDuration(String start , String end) {
		deviceQuery.append(" start = ? AND end = ? ");
	}
	
	public DeviceQueryBuilder build() {
		return this;
	}
	
}
