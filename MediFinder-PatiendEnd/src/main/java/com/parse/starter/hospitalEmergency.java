package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class hospitalEmergency extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.content_hospital_emergency);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final String activeUser= ParseUser.getCurrentUser().getUsername();


        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Emergency");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    ArrayList<patientDetails> patients = new ArrayList<patientDetails>();
                    patientDetails dummy=new patientDetails();
                    dummy.setName("Patient Name");
                    dummy.setLocation("Space Seprated Latitude and Longitude");
                    patients.add(dummy);
                    final ListView patientView=(ListView)findViewById(R.id.emergencyHospital);
                    for(ParseObject object:objects){
                        patientDetails patient=new patientDetails();
                        boolean status=object.getBoolean("Notify");
                        String hosName=object.getString("hospitalName");
                        String name=object.getString("patientName");
                        String lat=object.getString("patientLat");
                        String lon=object.getString("patientLon");

                        String location=lat+" "+lon;
                        if((String.valueOf(hosName).equals(activeUser.toString())) && (status)) {
                            Log.i("pagal hai saala",hosName);
                            patient.setName(name);
                            patient.setLocation(location);
                            patients.add(patient);
                        }
                    }
                    patientView.setAdapter(new MyAdapter2(hospitalEmergency.this, patients));
                    patientView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            if(position!=0)
                            {
                                Intent intent = new Intent(hospitalEmergency.this,displayPatient.class);
                                patientDetails h1= (patientDetails) patientView.getItemAtPosition(position);
                                String name = String.valueOf(h1.getName());
                                intent.putExtra("patient",name);
                                startActivity(intent);}
                        }
                    });

                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(hospitalEmergency.this, HospitalScreenActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
