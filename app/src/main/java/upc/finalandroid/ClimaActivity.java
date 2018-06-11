package upc.finalandroid;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;

import upc.finalandroid.Common.Common;
import upc.finalandroid.Helper.Helper;
import upc.finalandroid.Model.OpenWeatherMap;

public class ClimaActivity extends AppCompatActivity {
    private Context mContext;
    TextView txtCity, txtLastUpdate, txtDescription, txtHumidity, txtTime, txtCelsius;
    ImageView imageView;
    Button btn_calcular;
    EditText eLat, eLon;


    OpenWeatherMap openWeatherMap = new OpenWeatherMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clima);

        txtCity = (TextView) findViewById(R.id.txtCity);
        txtLastUpdate = (TextView) findViewById(R.id.txtLastUpdate);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtHumidity = (TextView) findViewById(R.id.txtHumidity);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtCelsius = (TextView) findViewById(R.id.txtCelsius);
        imageView = (ImageView) findViewById(R.id.imageView);
        btn_calcular = (Button)findViewById(R.id.btn_consultarClima);
        eLat = (EditText) findViewById(R.id.editText5);
        eLon = (EditText) findViewById(R.id.editText6);

        // Get the application context
        mContext = getApplicationContext();


        // Get the widgets reference from XML layout
        btn_calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new GetWeather().execute(Common.apiRequest(String.valueOf(eLat.getText()),String.valueOf(eLon.getText())));
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });



    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }






    private class GetWeather extends AsyncTask<String,Void,String> {
        ProgressDialog pd = new ProgressDialog(ClimaActivity.this);


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
            if(s.contains("Error: Not found city")){
                pd.dismiss();
                return;
            }


            Gson gson = new Gson();
            Type mType = new TypeToken<OpenWeatherMap>(){}.getType();
            openWeatherMap = gson.fromJson(s,mType);
            pd.dismiss();

            txtCity.setText(String.format("%s,%s",openWeatherMap.getName(),openWeatherMap.getSys().getCountry()));
            txtLastUpdate.setText(String.format("Last Updated: %s", Common.getDateNow()));
            txtDescription.setText(String.format("%s",openWeatherMap.getWeather().get(0).getDescription()));
            txtHumidity.setText(String.format("%d%%",openWeatherMap.getMain().getHumidity()));
            txtTime.setText(String.format("%s/%s",Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunrise()),Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunset())));
            txtCelsius.setText(String.format("%.2f °C",openWeatherMap.getMain().getTemp()));

            Picasso.with(ClimaActivity.this)
                    .load(Common.getImage(openWeatherMap.getWeather().get(0).getIcon()))
                    .into(imageView);

        }
    }
}