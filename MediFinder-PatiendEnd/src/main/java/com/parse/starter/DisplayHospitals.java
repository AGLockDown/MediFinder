package com.parse.starter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class DisplayHospitals extends AppCompatActivity {

    public EditText e2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_display_hospitals);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        e2=(EditText)findViewById(R.id.showDetails);
        Intent intent = getIntent();
        String id = intent.getStringExtra("hospital");
        final String[] nameID = id.split(" ");
        MyService.emergencyHospitalID = nameID[1];
        ParseObject emergency = new ParseObject("Emergency");
        final String phoneNum = String.valueOf(intent.getStringExtra("phno"));
        emergency.put("HospitalID",nameID[1]);
        emergency.put("status","TRUE");
        emergency.put("Notify",true);
        emergency.put("patientName", ParseUser.getCurrentUser().getUsername());
        emergency.put("patientLat",intent.getStringExtra("Lat"));
        emergency.put("patientLon",intent.getStringExtra("Lon"));
        emergency.put("hospitalName",nameID[0]);
        emergency.put("patientGender",Emergency.gender);
        emergency.put("patientAge",Emergency.age);
        emergency.put("b1",Emergency.choice1);
        emergency.put("b2",Emergency.choice2);
        emergency.put("b3",Emergency.choice3);
        emergency.put("b4",Emergency.choice4);
        emergency.put("b5",Emergency.choice5);
        emergency.put("b6",Emergency.choice6);
        emergency.put("b7",Emergency.choice7);
        emergency.put("b8",Emergency.choice8);
        emergency.saveInBackground();
        final Button call = (Button) findViewById(R.id.callButton);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Emergency");
        query.whereEqualTo("HospitalID",String.valueOf(nameID[1]).trim());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        for(ParseObject object:objects){
                            String s = object.getString("HospitalID");
                            ParseQuery<ParseObject> hospital = ParseQuery.getQuery("Hospital");
                            hospital.whereEqualTo("ID",s);
                            hospital.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if(e==null){
                                        if(objects.size()>0){
                                            String s="";
                                            for(ParseObject object:objects){
                                                String open = String.valueOf(object.get("open"));
                                                String name = object.getString("name");
                                                String phone = object.getString("phno");
                                                s=s+"Name and contact of the hospital: "+name+" "+phone+"\n";

                                                for(int i=0;i<4;i++)
                                                {
                                                    if(object.getBoolean("b"+Integer.toString(i)))
                                                    {
                                                        String open1 = String.valueOf(object.get("open"+Integer.toString(i)));
                                                        String close1= String.valueOf(object.get("close"+Integer.toString(i)));
                                                        s=s+"Specialisation" + Integer.toString(i)+":" +"\n";
                                                        s=s+"Open Time: "+open1+" Close Time: "+close1;
                                                    }
                                                }
                                                e2.setText(s);
                                            }
                                        }
                                    }
                                }
                            });
                            object.put("status","TRUE");
                            object.saveInBackground();
                            call.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:"+phoneNum));
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                }
            }});
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(DisplayHospitals.this, nearbyHospital.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void mapLauncher(View view){
        Intent intent = getIntent();

        String id = intent.getStringExtra("hospital");
        Log.i("String ",id);
        String[] nameID = id.split(" ");
        Log.i("String ",String.valueOf(nameID[1]));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Hospital");
        query.whereEqualTo("ID",String.valueOf(nameID[1]).trim());
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        for(ParseObject object:objects){
                            Intent intent1=getIntent();
                            String currentLatitude = intent1.getStringExtra("Lat");
                            String currentLongitude = intent1.getStringExtra("Lon");
                            String hospitalLatitude = String.valueOf(object.get("Latitude"));
                            String hospitalLongitude =String.valueOf(object.get("Longitude"));
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+currentLatitude+","+currentLongitude+"&daddr="+ hospitalLatitude +","+ hospitalLongitude));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
                            startActivity(intent);
                        }
                    }
                }
            }});
    }
}
