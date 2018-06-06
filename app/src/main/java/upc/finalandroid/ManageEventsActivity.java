package upc.finalandroid;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
public class ManageEventsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String CEROHORA = "0";
    private static final String DOS_PUNTOS = ":";
    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final int MY_PERMISSIONS_REQUEST_READ_CALENDAR = 1000;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 1001;

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    EditText etxt_titulo, etxt_descripcion, etxt_organizador, etxt_duracion;
    //
    //    //Widgets
    EditText etHora;
    ImageButton ibObtenerHora;

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    //Widgets
    EditText etFecha;
    ImageButton ibObtenerFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_events);
        //Widget EditText donde se mostrara la hora obtenida
        etHora = (EditText) findViewById(R.id.et_mostrar_hora_picker);
        //Widget ImageButton del cual usaremos el evento clic para obtener la hora
        ibObtenerHora = (ImageButton) findViewById(R.id.ib_obtener_hora);
        //Evento setOnClickListener - clic
        ibObtenerHora.setOnClickListener(this);
        //Widget EditText donde se mostrara la fecha obtenida
        etFecha = (EditText) findViewById(R.id.et_mostrar_fecha_picker);
        //Widget ImageButton del cual usaremos el evento clic para obtener la fecha
        ibObtenerFecha = (ImageButton) findViewById(R.id.ib_obtener_fecha);
        //Evento setOnClickListener - clic
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String eventName = extras.getString("event");
            etxt_titulo = (EditText)findViewById(R.id.etxt_titulo);
            etxt_titulo.setText(eventName);


        }



        ibObtenerFecha.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_obtener_hora:
                obtenerHora();
                break;
            case R.id.ib_obtener_fecha:
                obtenerFecha();
                break;

        }
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                etFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }


    public void addEvent(View view) {

        etxt_titulo = (EditText) findViewById(R.id.etxt_titulo);
        etxt_descripcion = (EditText) findViewById(R.id.etxt_descripcion);
        etxt_organizador = (EditText) findViewById(R.id.etxt_organizador);
        etxt_duracion = (EditText) findViewById(R.id.etxt_duracion);

        String eventTitle = etxt_titulo.getText().toString();
        String description = etxt_descripcion.getText().toString();
        String organizator = etxt_organizador.getText().toString();

        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(anio, mes, dia, hora, minuto);
        startMillis = beginTime.getTimeInMillis();
        beginTime.add(Calendar.MINUTE, Integer.parseInt(etxt_duracion.getText().toString()));
        int añoFinal=beginTime.get(Calendar.YEAR);
        int mesFinal=beginTime.get(Calendar.MONTH);
        int diaFinal=beginTime.get(Calendar.DAY_OF_MONTH);
        int horaFinal= beginTime.get(Calendar.HOUR_OF_DAY);
        int minutoFinal=beginTime.get(Calendar.MINUTE);

        Calendar endTime = Calendar.getInstance();
        endTime.set(añoFinal,mesFinal ,diaFinal ,horaFinal , minutoFinal);
        endMillis = endTime.getTimeInMillis();



        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, eventTitle);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
        values.put(CalendarContract.Events.ORGANIZER, organizator);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            long eventID = Long.parseLong(uri.getLastPathSegment());
            Log.i("Calendar", "Event Created, the event id is: " + eventID);

            Toast.makeText(ManageEventsActivity.this, "Evento creado", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
        }
        onBackPressed();
        //showEvents(eventTitle);
    }



    private void obtenerHora(){
                TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CEROHORA + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CEROHORA + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, false);

        recogerHora.show();
    }

}