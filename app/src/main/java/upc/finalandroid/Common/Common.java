package upc.finalandroid.Common;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static String API_KEY="b5cf731f2ab4f263990c5e85a38db38a";
    public static String API_LINK = "http://api.openweathermap.org/data/2.5/weather";

    public static String API_LINK_FORECAST_DAYS = "http://api.openweathermap.org/data/2.5/forecast/daily";

    public static String API_LINK_FORECAST_HOURS = "http://api.openweathermap.org/data/2.5/forecast";

    @NonNull
    public static String apiRequest(String lat, String lng){

        StringBuilder sb= new StringBuilder(API_LINK);
        sb.append(String.format("?lat=%s&lon=%s&APPID=%s&units=metric",lat,lng,API_KEY));
        return sb.toString();
    }
    public static String unixTimeStampToDateTime(double unixTimeStamp){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        date.setTime((long)unixTimeStamp*1000);
        return dateFormat.format(date);
    }


    public static String apiRequestForecastHoursByCity(String city){

        StringBuilder sb= new StringBuilder(API_LINK_FORECAST_HOURS);
        sb.append(String.format("?q=%s&APPID=%s&units=metric",city,API_KEY));
        return sb.toString();
    }

    public static String apiRequestForecastDays(String city,int days){

        StringBuilder sb= new StringBuilder(API_LINK_FORECAST_DAYS);
        sb.append(String.format("?q=%s&cnt=%s&APPID=%s&units=metric",city,days,API_KEY));
        return sb.toString();
    }

    public static String apiRequestByCity(String city){

        StringBuilder sb= new StringBuilder(API_LINK);
        sb.append(String.format("?q=%s&APPID=%s&units=metric",city,API_KEY));
        return sb.toString();
    }




    public static String getImage(String icon){
        return String.format("http://openweathermap.org/img/w/%s.png",icon);
    }

    public static String getDateNow(){
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
