package com.parse.starter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Emergency extends AppCompatActivity {
    public static ArrayList<String> SymList =new ArrayList<String>();
    public static Map<String, String> Id = new HashMap<String, String>();
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;

    CheckBox s1,s2,s3,s4,s5,s6,s7,s8;
    Intent intent1;

    //Location
    LocationManager locationManager;

    LocationListener locationListener;
    String Latitude;
    String Longitude;
    public static String gender;
    public static String age;
    public static boolean choice1;
    public static boolean choice2;
    public static boolean choice3;
    public static boolean choice4;
    public static boolean choice5;
    public static boolean choice6;
    public static boolean choice7;
    public static boolean choice8;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                //locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,locationListener,null);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }

    }

    public void selectSpecialist(View view){

        switch (view.getId()) {
            case R.id.spec1:
                if (s1.isChecked()){
                    Toast.makeText(getApplicationContext(), "spec1", Toast.LENGTH_LONG).show();
                    choice1=true;
                }
                break;
            case R.id.spec2:
                if (s2.isChecked()) {
                    Toast.makeText(getApplicationContext(), "spec2", Toast.LENGTH_LONG).show();
                    choice2=true;

                }
                break;
            case R.id.spec3:
                if (s3.isChecked()) {
                    Toast.makeText(getApplicationContext(), "spec3", Toast.LENGTH_LONG).show();
                    choice3 = true;
                }
                break;
            case R.id.spec4:
                if (s4.isChecked()) {
                    Toast.makeText(getApplicationContext(), "spec4", Toast.LENGTH_LONG).show();
                    choice4 = true;
                }
                break;
            case R.id.spec5:
                if (s5.isChecked()) {
                    Toast.makeText(getApplicationContext(), "spec5", Toast.LENGTH_LONG).show();
                    choice5 = true;
                }
                break;
            case R.id.spec6:
                if (s6.isChecked()) {
                    Toast.makeText(getApplicationContext(), "spec6", Toast.LENGTH_LONG).show();
                    choice6 = true;
                }
                break;
            case R.id.spec7:
                if (s7.isChecked()){
                    Toast.makeText(getApplicationContext(), "spec7", Toast.LENGTH_LONG).show();
                    choice7=true;}
                break;
            case R.id.spec8:
                if (s8.isChecked()){
                    Toast.makeText(getApplicationContext(), "spec8", Toast.LENGTH_LONG).show();
                    choice8=true;
                }
                break;
            default:
                Toast.makeText(getApplicationContext(), "No Option Selected", Toast.LENGTH_LONG).show();
                break;


        }

    }
    public void emergency(View view){

        intent1=new Intent(Emergency.this,nearbyHospital.class);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);
        gender= (String) radioSexButton.getText();
        EditText editText=(EditText)findViewById(R.id.ageVal);

        age=String.valueOf(editText.getText());


        //Toast.makeText(getApplicationContext(),"It's Emergrncy",Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // ask for permission

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        } else {

            // we have permission!
            //locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,locationListener,null);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }
        //Log.i("Lat Inside",String.valueOf(Latitude));



    }

    public void knowSpecialist(View view)
    {
        //Taking Gender input from Radio Button
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);
        gender= (String) radioSexButton.getText();

        // Taking Age Input from EditText


        EditText editText=(EditText)findViewById(R.id.ageVal);

        age=String.valueOf(editText.getText());
        Toast.makeText(this,age,Toast.LENGTH_LONG).show();

        // Clearing all selected symptoms after coming back
        SymList.clear();
        Id.clear();
        Log.i("All","symlist cleared");
        Intent intent=new Intent(Emergency.this, addSymptoms.class);
        intent.putExtra("Gender",gender);
        startActivity(intent);

    }
    public void LogOut(View view){
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Intent intent = new Intent(Emergency.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        choice1=choice2=choice3=choice4=choice5=choice6=choice7=choice8=false;
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_emergency);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Selecting specialist

        s1 = (CheckBox) findViewById(R.id.spec1);
        s2 = (CheckBox) findViewById(R.id.spec2);
        s3 = (CheckBox) findViewById(R.id.spec3);
        s4 = (CheckBox) findViewById(R.id.spec4);
        s5 = (CheckBox) findViewById(R.id.spec5);
        s6 = (CheckBox) findViewById(R.id.spec6);
        s7 = (CheckBox) findViewById(R.id.spec7);
        s8 = (CheckBox) findViewById(R.id.spec8);

        //Code for getting Location

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                Log.i("Location-Latitude", String.valueOf(location.getLatitude()));
                Log.i("Location-Longitude", String.valueOf(location.getLongitude()));
                Latitude=String.valueOf(location.getLatitude());
                Longitude=String.valueOf(location.getLongitude());

                int[] items = new int[8];
                if(s1.isChecked())
                    items[0]=1;
                if(s2.isChecked())
                    items[1]=1;
                if(s3.isChecked())
                    items[2]=1;
                if(s4.isChecked())
                    items[3]=1;
                if(s5.isChecked())
                    items[4]=1;
                if(s6.isChecked())
                    items[5]=1;
                if(s7.isChecked())
                    items[6]=1;
                if(s8.isChecked())
                    items[7]=1;
                intent1.putExtra("Checkbox", items);
                /*
                ParseObject latlng=new ParseObject("Latlng");
                latlng.put("Latitude",String.valueOf(location.getLatitude()));
                latlng.put("Longitude",String.valueOf(location.getLongitude()));
                latlng.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Log.i("successfull","saved");
                        }
                        else{
                            Log.i("Failed",e.toString());
                        }
                    }
                });*/
                intent1.putExtra("Latitude",Latitude);
                intent1.putExtra("Longitude",Longitude);
                startActivity(intent1);

                locationManager.removeUpdates(this);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
            }

        };

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Emergency.this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}