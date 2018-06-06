package upc.finalandroid.Model;

public class WeatherOverview {
    private Float message;
    private Integer cnt;
    private String code;
    private OpenWeatherMap[] list;
    private City city;

    public Float getMessage() {
        return message;
    }

    public void setMessage(Float message) {
        this.message = message;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public OpenWeatherMap[] getList() {
        return list;
    }

    public void setList(OpenWeatherMap[] list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}