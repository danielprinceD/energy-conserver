package vo;

public class Entry {
    private Integer entryId;
    private Integer deviceId;
    private Double power;
    private Double voltage;
    private String dateTime;
    private Double amphere;
    
    public Double getAmphere() {
    	return amphere;
    }
    
    public void setAmphere(Double amphere) {
    	this.amphere = amphere;
    }

    public Integer getEntryId() {
        return entryId;
    }

    public void setEntryId(Integer entryId) {
        this.entryId = entryId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Double getPower() {
        return power;
    }

    public void setPower(Double power) {
        this.power = power;
    }

    public Double getVoltage() {
        return voltage;
    }

    public void setVoltage(Double voltage) {
        this.voltage = voltage;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}

