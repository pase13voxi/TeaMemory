package coolpharaoh.tee.speicher.tea.timer.models.datatransfer.pojo;

public class InfusionPOJO {

    private int infusionindex;
    private String time;
    private String cooldowntime;
    private int temperaturecelsius;
    private int temperaturefahrenheit;

    public int getInfusionindex() {
        return infusionindex;
    }

    public void setInfusionindex(int infusionindex) {
        this.infusionindex = infusionindex;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCooldowntime() {
        return cooldowntime;
    }

    public void setCooldowntime(String cooldowntime) {
        this.cooldowntime = cooldowntime;
    }

    public int getTemperaturecelsius() {
        return temperaturecelsius;
    }

    public void setTemperaturecelsius(int temperaturecelsius) {
        this.temperaturecelsius = temperaturecelsius;
    }

    public int getTemperaturefahrenheit() {
        return temperaturefahrenheit;
    }

    public void setTemperaturefahrenheit(int temperaturefahrenheit) {
        this.temperaturefahrenheit = temperaturefahrenheit;
    }
}
