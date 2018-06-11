package upc.finalandroid;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.opengl.Visibility;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.speech.RecognizerIntent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.Frame;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import upc.finalandroid.Common.Common;
import upc.finalandroid.Helper.Helper;
import upc.finalandroid.Model.OpenWeatherMap;
import upc.finalandroid.Model.WeatherOverview;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,  OnMapReadyCallback{
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;
    private TextView txvResult;
    OpenWeatherMap openWeatherMap = new OpenWeatherMap();
    WeatherOverview weatherOverview = new WeatherOverview();
    private Context mContext;
    CardView cardviewCurretTime, cardviewNextHours, cardviewNextDays;

    TextView txt_cityT, txt_descriptionT, txt_humidityT, txt_timeT, txt_celsiusT;
    
    TextView txt_cityH, txt_weatherT1,txt_weatherT2,txt_weatherT3,txt_weatherT4,txt_weatherT5;
    ArrayList<TextView> txtHours;
    ArrayList<TextView> txtDays;


    TextView txt_cityD, txt_weatherD1,txt_weatherD2,txt_weatherD3,txt_weatherD4,txt_weatherD5,txt_weatherD6,txt_weatherD7,txt_weatherD8,txt_weatherD9;


    RelativeLayout mRelativeLayout;
    CardView cv;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;


    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        txvResult = (TextView) findViewById(R.id.txvResult);


        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Este dispositivo no soporta reconocimiento de voz", Toast.LENGTH_SHORT).show();
        }


    }


    public void voiceFunctions(View view) {

        /*FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*//*
        MapsActivity map = new MapsActivity();
        fragmentTransaction.replace(R.id.flMap, map);
        fragmentTransaction.commit();*/





        //String phrase = "clima Barcelona próximos días";

       /*String phrase = "clima Barcelona mañana";
        String phrase = "clima Barcelona proximas horas";
        String phrase = "clima Barcelona proximos dias;

        String phrase = "mostrar tareas";
        String phrase = "agregar tarea estudiar examen;
        String phrase = "eliminar tarea estudiar examen";


        String phrase = "ubicacion Barcelona";

        String phrase = "agregar evento exposicion el 1 de junio de 2018";
        String phrase ="eliminar evento exposicion";
        String phrase = "mostrar eventos";*/
        /*

        String words[] = phrase.split(" ");
        String weatherCity = "";
        String weatherLocation = "";
        String weatherTime = "";
        int queryWeatherStatus = 0;
        if (words[words.length - 2].contains("próxim") && (words[words.length - 1].equalsIgnoreCase("horas") || words[words.length - 1].equalsIgnoreCase("días"))) {
            for (int i = 1; i < words.length - 2; i++) {
                if (i == words.length - 3) {
                    weatherCity += words[i];
                } else {
                    weatherCity += words[i] + " ";
                }


            }

            if (words[words.length - 1].equalsIgnoreCase("horas")) {
                new MainActivity.GetWeatherForecastNextHours().execute(Common.apiRequestForecastHoursByCity(weatherCity));

            } else {
                new MainActivity.GetWeatherForecastNextDays().execute(Common.apiRequestForecastHoursByCity(weatherCity));

            }
        }*/
    }

    DbHelper dbHelper;

    private boolean isEventAlreadyExist(String eventTitle) {
        final String[] INSTANCE_PROJECTION = new String[]{
                CalendarContract.Instances.EVENT_ID,      // 0
                CalendarContract.Instances.BEGIN,         // 1
                CalendarContract.Instances.TITLE          // 2
        };

        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2017, 11, 18, 6, 00);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2018, 1, 24, 8, 0);
        endMillis = endTime.getTimeInMillis();

        // The ID of the recurring event whose instances you are searching for in the Instances table
        String selection = CalendarContract.Instances.TITLE + " = ?";
        String[] selectionArgs = new String[]{eventTitle};

        // Construct the query with the desired date range.
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);

        // Submit the query
        Cursor cur = getContentResolver().query(builder.build(), INSTANCE_PROJECTION, selection, selectionArgs, null);

        return cur.getCount() > 0;
    }


    public void removeEvent(String eventTitle) {

        boolean exist = isEventAlreadyExist(eventTitle);
        if (exist) {
            final String[] INSTANCE_PROJECTION = new String[]{
                    CalendarContract.Instances.EVENT_ID,      // 0
                    CalendarContract.Instances.BEGIN,         // 1
                    CalendarContract.Instances.TITLE          // 2
            };
            // The indices for the projection array above.
            final int PROJECTION_ID_INDEX = 0;
            final int PROJECTION_BEGIN_INDEX = 1;
            final int PROJECTION_TITLE_INDEX = 2;

            // Specify the date range you want to search for recurring event instances
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(2017, 9, 23, 8, 0);
            long startMillis = beginTime.getTimeInMillis();
            Calendar endTime = Calendar.getInstance();
            endTime.set(2018, 12, 31, 8, 0);
            long endMillis = endTime.getTimeInMillis();


            // The ID of the recurring event whose instances you are searching for in the Instances table
            String selection = CalendarContract.Instances.TITLE + " = ?";
            String[] selectionArgs = new String[]{eventTitle};

            // Construct the query with the desired date range.
            Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
            ContentUris.appendId(builder, startMillis);
            ContentUris.appendId(builder, endMillis);

            // Submit the query
            Cursor cur = getContentResolver().query(builder.build(), INSTANCE_PROJECTION, selection, selectionArgs, null);

            while (cur.moveToNext()) {
                // Get the field values
                long eventID = cur.getLong(PROJECTION_ID_INDEX);
                long beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
                String title = cur.getString(PROJECTION_TITLE_INDEX);

                Uri deleteUri = null;
                deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
                int rows = getContentResolver().delete(deleteUri, null, null);
                Log.i("Calendar", "Rows deleted: " + rows);

            }
            Toast.makeText(MainActivity.this, eventTitle + " eliminado", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap= googleMap;

    }


    //weather next 5 days
    private class GetWeatherForecastNextDays extends AsyncTask<String, Void, String> {
        ProgressDialog pd = new ProgressDialog(MainActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please wait...");
            pd.show();

        }


        @Override
        protected String doInBackground(String... params) {
            String stream = null;
            String urlString = params[0];

            Helper http = new Helper();
            stream = http.getHTTPData(urlString);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.contains("Error: Not found city")) {
                pd.dismiss();
                return;
            }


            Gson gson = new Gson();
            Type mType = new TypeToken<WeatherOverview>() {
            }.getType();
            weatherOverview = gson.fromJson(s, mType);
            pd.dismiss();
            Calendar currentDate = Calendar.getInstance();
            int currentDay= currentDate.get(Calendar.DAY_OF_MONTH);


            currentDate.add(Calendar.DAY_OF_MONTH, 3);

            int limitDay = currentDate.get(Calendar.DAY_OF_MONTH);

            OpenWeatherMap[] weatherTomorrow = weatherOverview.getList();

            hideCards();
            cardviewNextDays.setVisibility(View.VISIBLE);
            String city = String.format("%s", weatherOverview.getCity().getName());
            System.out.println(city);
            txt_cityD.setText(city);
            String respuesta = city;

            int j = 0;
            for (int i = 0; i < weatherTomorrow.length; i++) {

                Date dateWeather = new Date(weatherTomorrow[i].getDt() * 1000);

                Calendar weatherDay = Calendar.getInstance();
                weatherDay.setTime(dateWeather);
                int day = weatherDay.get(Calendar.DAY_OF_MONTH);
                if(day==7 ){
                    String a= "";
                }
                int hour = weatherDay.get(Calendar.HOUR_OF_DAY);
                if(day==7 && hour ==9){

                    String a= "";

                }
                int month = weatherDay.get(Calendar.MONTH);
                int year = weatherDay.get(Calendar.YEAR);
                int minute = weatherDay.get(Calendar.MINUTE);

                if ( day<= limitDay && (hour>= 10 && hour <= 17) && j <= 8) {

                    if ( day>currentDay ){

                        String description = String.format("%s", weatherTomorrow[i].getWeather().get(0).getDescription());
                        String celsius = String.format("%.2f °C", weatherTomorrow[i].getMain().getTemp());
                        String date = day + "/" + month + "/" + year;

                        String hourComplete = hour+ ":" + minute;
                        System.out.println(description + " " + celsius + " " + date + " " + hourComplete + "\n");
                        respuesta += description + " " + celsius + " " + date + " " + hourComplete + "\n";

                        txtDays.get(j).setText(description + " " + celsius + " " + date + " " + hourComplete );
                        j++;

                    }


                    /*Picasso.with(Main.this)
                    .load(Common.getImage(openWeatherMap.getWeather().get(0).getIcon()))
                    .into(imageView);*/
                }


            }

            //generateCardView(respuesta);

        }
    }

    //weather next 5 hours
    private class GetWeatherForecastNextHours extends AsyncTask<String, Void, String> {
        ProgressDialog pd = new ProgressDialog(MainActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please wait...");
            pd.show();

        }


        @Override
        protected String doInBackground(String... params) {
            String stream = null;
            String urlString = params[0];

            Helper http = new Helper();
            stream = http.getHTTPData(urlString);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.contains("Error: Not found city")) {
                pd.dismiss();
                return;
            }


            Gson gson = new Gson();
            Type mType = new TypeToken<WeatherOverview>() {
            }.getType();
            weatherOverview = gson.fromJson(s, mType);
            pd.dismiss();


            OpenWeatherMap[] weatherTomorrow = weatherOverview.getList();
            String city = String.format("%s", weatherOverview.getCity().getName());

            hideCards();
            cardviewNextHours.setVisibility(View.VISIBLE);
            txt_cityH.setText(city);
            System.out.println(city + "\n");
            String respuesta = city + "\n";

            for (int i = 0; i < 5; i++) {

                Date dateWeather = new Date(weatherTomorrow[i].getDt() * 1000);
                Calendar weatherDay = Calendar.getInstance();
                weatherDay.setTime(dateWeather);
                int day = weatherDay.get(Calendar.DAY_OF_MONTH);
                int hour = weatherDay.get(Calendar.HOUR_OF_DAY);
                int month = weatherDay.get(Calendar.MONTH);
                int year = weatherDay.get(Calendar.YEAR);
                int minute = weatherDay.get(Calendar.MINUTE);


                String description = String.format("%s", weatherTomorrow[i].getWeather().get(0).getDescription());
                String celsius = String.format("%.2f °C", weatherTomorrow[i].getMain().getTemp());
                String date = day + "/" + month + "/" + year;

                String hourComplete = hour + ":" + minute;
                System.out.println(description + " " + celsius + " " + date + " " + hourComplete + "\n");



                txtHours.get(i).setText(description + " " + celsius + " " + date + " " + hourComplete + "\n");

                respuesta += description + " " + celsius + " " + date + " " + hourComplete + "\n";
                    /*Picasso.with(Main.this)
                    .load(Common.getImage(openWeatherMap.getWeather().get(0).getIcon()))
                    .into(imageView);*/


            }
            //generateCardView(respuesta);


        }
    }

    //weather tomorrow
    private class GetWeatherTomorrow extends AsyncTask<String, Void, String> {
        ProgressDialog pd = new ProgressDialog(MainActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please wait...");
            pd.show();

        }


        @Override
        protected String doInBackground(String... params) {
            String stream = null;
            String urlString = params[0];

            Helper http = new Helper();
            stream = http.getHTTPData(urlString);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.contains("Error: Not found city")) {
                pd.dismiss();
                return;
            }


            Gson gson = new Gson();
            Type mType = new TypeToken<WeatherOverview>() {
            }.getType();
            weatherOverview = gson.fromJson(s, mType);
            pd.dismiss();
            Calendar currentDate = Calendar.getInstance();
            currentDate.add(Calendar.DAY_OF_MONTH, 1);

            int nextDay = currentDate.get(Calendar.DAY_OF_MONTH);

            OpenWeatherMap[] weatherTomorrow = weatherOverview.getList();
            String city = String.format("%s", weatherOverview.getCity().getName());
            hideCards();


            cardviewNextHours.setVisibility(View.VISIBLE);
            txt_cityH.setText(city);
            int j = 0;
            for (int i = 0; i < weatherTomorrow.length; i++) {

                Date dateWeather = new Date(weatherTomorrow[i].getDt() * 1000);
                Calendar weatherDay = Calendar.getInstance();
                weatherDay.setTime(dateWeather);


                int day = weatherDay.get(Calendar.DAY_OF_MONTH);
                int hour = weatherDay.get(Calendar.HOUR_OF_DAY);
                int month = weatherDay.get(Calendar.MONTH);
                int year = weatherDay.get(Calendar.YEAR);
                int minute = weatherDay.get(Calendar.MINUTE);


                if (nextDay == day &&  hour >= 6  && j<5) {

                   String lastUpdate = String.format("Last Updated: %s", Common.getDateNow());
                    String description = String.format("%s", weatherTomorrow[i].getWeather().get(0).getDescription());
                    String humidity = String.format("%d%%", weatherTomorrow[i].getMain().getHumidity());
                    String time = String.format("%s/%s", Common.unixTimeStampToDateTime(weatherTomorrow[i].getSys().getSunrise()), Common.unixTimeStampToDateTime(weatherTomorrow[i].getSys().getSunset()));
                    String celsius = String.format("%.2f °C", weatherTomorrow[i].getMain().getTemp());

                    System.out.println(city + " " + lastUpdate + " " + description + " " + humidity + " " + time + " " + celsius);


                    txtHours.get(j).setText(day+"/"+month+"/"+year+" "+hour+":"+minute + " " + description + " " + humidity + " " + time + " " + celsius);
                    j++;
                    //generateCardView(city + " " + lastUpdate + " " + description + " " + humidity + " " + time + " " + celsius);
      /*Picasso.with(Main.this)
                    .load(Common.getImage(openWeatherMap.getWeather().get(0).getIcon()))
                    .into(imageView);*/
                }


            }
            //generateCardView(respuesta);


        }
    }

    //current weather
    private class GetWeather extends AsyncTask<String, Void, String> {
        ProgressDialog pd = new ProgressDialog(MainActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please wait...");
            pd.show();

        }


        @Override
        protected String doInBackground(String... params) {
            String stream = null;
            String urlString = params[0];

            Helper http = new Helper();
            stream = http.getHTTPData(urlString);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.contains("Error: Not found city")) {
                pd.dismiss();
                return;
            }


            Gson gson = new Gson();
            Type mType = new TypeToken<OpenWeatherMap>() {
            }.getType();
            openWeatherMap = gson.fromJson(s, mType);
            pd.dismiss();

            String city = String.format("%s,%s", openWeatherMap.getName(), openWeatherMap.getSys().getCountry());
            String lastUpdate = String.format("Last Updated: %s", Common.getDateNow());
            String description = String.format("%s", openWeatherMap.getWeather().get(0).getDescription());
            String humidity = String.format("%d%%", openWeatherMap.getMain().getHumidity());
            String time = String.format("%s/%s", Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunrise()), Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunset()));
            String celsius = String.format("%.2f °C", openWeatherMap.getMain().getTemp());

            hideCards();
            cardviewCurretTime.setVisibility(View.VISIBLE);
            txt_cityT.setText(city);
            txt_descriptionT.setText(description);
            txt_humidityT.setText(humidity);
            txt_timeT.setText(time);
            txt_celsiusT.setText(celsius);

            System.out.println(city + " " + lastUpdate + " " + description + " " + humidity + " " + time + " " + celsius);
            //generateCardView(city + " " + lastUpdate + " " + description + " " + humidity + " " + time + " " + celsius);


           /*Picasso.with(Main.this)
                    .load(Common.getImage(openWeatherMap.getWeather().get(0).getIcon()))
                    .into(imageView);*/

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txt_query.setText(result.get(0));
                    String phrase = result.get(0);
        /*String phrase = "clima Barcelona mañana";
        String phrase = "clima Barcelona proximas horas";
        String phrase = "clima Barcelona proximos dias;

        String phrase = "mostrar tareas";
        String phrase = "agregar tarea estudiar examen;
        String phrase = "eliminar tarea estudiar examen";


        String phrase = "ubicacion Barcelona";

        String phrase = "agregar evento exposicion;
        String phrase ="eliminar evento exposicion";
        String phrase = "mostrar eventos";*/


                    String words[] = phrase.split(" ");
                    String weatherCity = "";
                    int statusQuery=1;

                    if (words[0].equalsIgnoreCase("clima") && words.length >= 2) {


                        if (words.length >= 2) {


                            if ((words[words.length - 1].equalsIgnoreCase("hoy") || words[words.length - 1].equalsIgnoreCase("mañana"))) {

                                for (int i = 1; i < words.length - 1; i++) {
                                    if (i == words.length - 2) {
                                        weatherCity += words[i];
                                    } else {
                                        weatherCity += words[i] + " ";
                                    }


                                }
                                if (words[words.length - 1].equalsIgnoreCase("hoy")) {
                                    statusQuery=0;
                                    deleteLastQuery();
                                    dbHelper.insertLastQuert(phrase);
                                    new MainActivity.GetWeather().execute(Common.apiRequestByCity(weatherCity));


                                } else {
                                    statusQuery=0;

                                    deleteLastQuery();
                                    dbHelper.insertLastQuert(phrase);
                                    new MainActivity.GetWeatherTomorrow().execute(Common.apiRequestForecastHoursByCity(weatherCity));

                                }
                            } else {
                                if (words[words.length - 2].contains("próxim") && (words[words.length - 1].equalsIgnoreCase("horas") || words[words.length - 1].equalsIgnoreCase("días"))) {
                                    for (int i = 1; i < words.length - 2; i++) {
                                        if (i == words.length - 3) {
                                            weatherCity += words[i];
                                        } else {
                                            weatherCity += words[i] + " ";
                                        }


                                    }

                                    if (words[words.length - 1].equalsIgnoreCase("horas")) {
                                        statusQuery=0;

                                        deleteLastQuery();
                                        dbHelper.insertLastQuert(phrase);
                                        new MainActivity.GetWeatherForecastNextHours().execute(Common.apiRequestForecastHoursByCity(weatherCity));

                                    } else {
                                        statusQuery=0;

                                        deleteLastQuery();
                                        dbHelper.insertLastQuert(phrase);
                                        new MainActivity.GetWeatherForecastNextDays().execute(Common.apiRequestForecastHoursByCity(weatherCity));

                                    }
                                } else {

                                    String firstLetter = String.valueOf(words[words.length - 1].charAt(0));
                                    String firstLetterCap = String.valueOf(words[words.length - 1].charAt(0)).toUpperCase();

                                    if (firstLetter.equals(firstLetterCap)) {

                                        for (int i = 1; i < words.length; i++) {
                                            if (i == words.length - 1) {
                                                weatherCity += words[i];
                                            } else {
                                                weatherCity += words[i] + " ";
                                            }


                                        }
                                        statusQuery=0;

                                        deleteLastQuery();
                                        dbHelper.insertLastQuert(phrase);
                                        new MainActivity.GetWeather().execute(Common.apiRequestByCity(weatherCity));


                                    } else {
                                        statusQuery=1;
                                    }
                                }


                            }


                        } else {
                            statusQuery=1;
                        }

                    }
                    if ((words[1].equalsIgnoreCase("tarea") || words[1].equalsIgnoreCase("tareas"))) {

                        if (words.length >= 2) {
                            if (words[0].equalsIgnoreCase("mostrar")) {
                                Intent t = new Intent(MainActivity.this, TareasActivity.class);
                                startActivity(t);

                                deleteLastQuery();
                                dbHelper.insertLastQuert(phrase);
                            }
                            if (words[0].equalsIgnoreCase("agregar")) {
                                String task = "";
                                for (int i = 2; i < words.length; i++) {
                                    if (i == words.length - 1) {
                                        task += words[i];
                                    } else {
                                        task += words[i] + " ";
                                    }


                                }

                                deleteLastQuery();
                                dbHelper.insertLastQuert(phrase);
                                dbHelper.insertNewTask(task);
                                statusQuery=0;
                                Toast.makeText(MainActivity.this, "Tarea creada", Toast.LENGTH_SHORT).show();
                            }
                            if (words[0].equalsIgnoreCase("confirmar")) {
                                String task = "";
                                for (int i = 2; i < words.length; i++) {
                                    if (i == words.length - 1) {
                                        task += words[i];
                                    } else {
                                        task += words[i] + " ";
                                    }


                                }

                                deleteLastQuery();
                                dbHelper.insertLastQuert(phrase);
                                dbHelper.deleteTask(task);
                                statusQuery=0;
                                Toast.makeText(MainActivity.this, "Tarea confirmada", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            statusQuery=1;

                        }

                    }

                    if (phrase.equalsIgnoreCase("ubicación actual")) {
                        if (words.length == 2) {
                            hideCards();
                            getLatLon();
                            cv.setVisibility(View.VISIBLE);
                            statusQuery=0;

                            deleteLastQuery();
                            dbHelper.insertLastQuert(phrase);
                        } else {
                            statusQuery=1;
                        }

                    }

                    if (words[1].contains("evento")) {
                        if (words.length >= 2) {
                            if (words[0].equalsIgnoreCase("mostrar") && words.length == 2) {
                                Intent t = new Intent(MainActivity.this, CalendarioActivity.class);
                                t.putExtra("loadEvents", "yes");
                                startActivity(t);
                                statusQuery=0;

                                deleteLastQuery();
                                dbHelper.insertLastQuert(phrase);
                            }
                            if (words[0].equalsIgnoreCase("agregar")) {
                                String event = "";
                                for (int i = 2; i < words.length; i++) {
                                    if (i == words.length - 1) {
                                        event += words[i];
                                    } else {
                                        event += words[i] + " ";
                                    }


                                }
                                Intent f = new Intent(MainActivity.this, ManageEventsActivity.class);
                                f.putExtra("event", event);
                                startActivity(f);
                                statusQuery=0;

                                deleteLastQuery();
                                dbHelper.insertLastQuert(phrase);

                            }
                            if (words[0].equalsIgnoreCase("eliminar") && words.length == 3) {
                                String event = "";
                                for (int i = 2; i < words.length; i++) {
                                    if (i == words.length - 1) {
                                        event += words[i];
                                    } else {
                                        event += words[i] + " ";
                                    }


                                }

                                deleteLastQuery();
                                dbHelper.insertLastQuert(phrase);
                                removeEvent(event);
                                statusQuery=0;
                            }

                        } else {
                            statusQuery=1;
                        }

                    }
                if(statusQuery==1){

                    Toast.makeText(MainActivity.this, "Comando no reconocido", Toast.LENGTH_SHORT).show();

                }

                }
                break;
        }
    }


    private GoogleMap mGoogleMap;






    public void getLatLon() {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);



        locationCallback = new LocationCallback() {
            @Override

            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    Double lat= Double.parseDouble(String.valueOf(location.getLatitude()));
                    Double lon = Double.parseDouble(String.valueOf(location.getLongitude()));

                    mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("Usted está aquí"));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));


                }

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    public void hideCards(){
        cardviewCurretTime.setVisibility(View.GONE);

        cardviewNextHours.setVisibility(View.GONE);

        cardviewNextDays.setVisibility(View.GONE);

        cv.setVisibility(View.GONE);

    }

    TextView txt_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cv = (CardView) findViewById(R.id.cardview);
        dbHelper = new DbHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DbHelper(this);
        mContext = getApplicationContext();
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        txt_cityT =(TextView) findViewById(R.id.txt_cityDT1);
        txt_descriptionT=(TextView) findViewById(R.id.txt_description);
        txt_humidityT=(TextView) findViewById(R.id.txt_humidiy);
        txt_timeT=(TextView) findViewById(R.id.txt_time);
        txt_celsiusT=(TextView) findViewById(R.id.txt_gradosC);

        txt_cityH=(TextView) findViewById(R.id.txt_cityDT3);

        txtHours = new ArrayList<TextView>() ;
        txtDays = new ArrayList<TextView> ();

        txt_weatherT1=(TextView) findViewById(R.id.txt_weatherTime1);
        txtHours.add(txt_weatherT1);
        txt_weatherT2=(TextView) findViewById(R.id.txt_weatherTime2);
        txtHours.add(txt_weatherT2);
        txt_weatherT3=(TextView) findViewById(R.id.txt_weatherTime3);
        txtHours.add(txt_weatherT3);
        txt_weatherT4=(TextView) findViewById(R.id.txt_weatherTime4);
        txtHours.add(txt_weatherT4);
        txt_weatherT5=(TextView) findViewById(R.id.txt_weatherTime5);
        txtHours.add(txt_weatherT5);

        txt_cityD=(TextView) findViewById(R.id.txt_cityDT2);
        txt_weatherD1=(TextView) findViewById(R.id.txt_weatherDays1);
        txtDays.add(txt_weatherD1);
        txt_weatherD2=(TextView) findViewById(R.id.txt_weatherDays2);
        txtDays.add(txt_weatherD2);
        txt_weatherD3=(TextView) findViewById(R.id.txt_weatherDays3);
        txtDays.add(txt_weatherD3);
        txt_weatherD4=(TextView) findViewById(R.id.txt_weatherDays4);
        txtDays.add(txt_weatherD4);
        txt_weatherD5=(TextView) findViewById(R.id.txt_weatherDays5);
        txtDays.add(txt_weatherD5);
        txt_weatherD6=(TextView) findViewById(R.id.txt_weatherDays6);
        txtDays.add(txt_weatherD6);
        txt_weatherD7=(TextView) findViewById(R.id.txt_weatherDays7);
        txtDays.add(txt_weatherD7);
        txt_weatherD8=(TextView) findViewById(R.id.txt_weatherDays8);
        txtDays.add(txt_weatherD8);
        txt_weatherD9=(TextView) findViewById(R.id.txt_weatherDays9);
        txtDays.add(txt_weatherD9);


        cardviewCurretTime=(CardView)findViewById(R.id.cardviewTodayTomo);
        cardviewNextHours=(CardView)findViewById(R.id.cardviewHours);
        cardviewNextDays=(CardView)findViewById(R.id.cardviewDays);

        hideCards();


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ArrayList<String> queries = dbHelper.getLastQuery();
        txt_query = (TextView)findViewById(R.id.txt_query);
        if(queries.isEmpty()){
           
            txt_query.setText("");
        }else{

            txt_query.setText(queries.get(0));
        }
        
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }
    
    public  void deleteLastQuery(){
        ArrayList<String> queries = dbHelper.getLastQuery();
        //txt_query = (TextView)findViewById(R.id.txt_query);
        if(!queries.isEmpty()){

            
            dbHelper.deleteLastQuery(dbHelper.getLastQuery().get(0));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id=item.getItemId();
        switch (id){

            case R.id.nav_clima:
                Intent i= new Intent(MainActivity.this,ClimaActivity.class);
                startActivity(i);
                break;
            case R.id.nav_calendario:
                Intent g= new Intent(MainActivity.this,CalendarioActivity.class);
                startActivity(g);
                break;
            case R.id.nav_ubicacion:
                Intent s= new Intent(MainActivity.this,UbicacionActivity.class);
                startActivity(s);
                break;
            case R.id.nav_tareas:
                Intent t= new Intent(MainActivity.this,TareasActivity.class);
                startActivity(t);
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
