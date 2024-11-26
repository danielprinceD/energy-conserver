package vo;

public class Device {
    private Integer deviceId;
    private Integer deviceName;
    private Integer buildingBlockId;

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(Integer deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getBuildingBlockId() {
        return buildingBlockId;
    }

    public void setBuildingBlockId(Integer buildingBlockId) {
        this.buildingBlockId = buildingBlockId;
    }
}
