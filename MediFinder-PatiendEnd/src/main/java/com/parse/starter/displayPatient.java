package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class displayPatient extends AppCompatActivity {

    TextView details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_display_patient);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        details = (TextView)findViewById(R.id.details);

        String name = getIntent().getStringExtra("patient");

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Emergency");
        List<ParseObject> objs;
        query1.whereEqualTo("patientName",name);
        query1.whereEqualTo("Notify", true);
        query1.setLimit(1);

        try{
            objs = query1.find();
            displayData(objs.get(0));
            ParseObject obj = objs.get(0);
            obj.put("Notify", false);
            obj.saveInBackground();
        }
        catch(ParseException e){
            Log.i("Error!!!!!!",e.toString());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(displayPatient.this, hospitalEmergency.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void displayData(ParseObject objs){
        String name = objs.getString("patientName");
        String age = objs.getString("patientAge");
        String gender = objs.getString("patientGender");

        boolean s1 = objs.getBoolean("b1");
        boolean s2 = objs.getBoolean("b2");
        boolean s3 = objs.getBoolean("b3");
        boolean s4 = objs.getBoolean("b4");
        boolean s5 = objs.getBoolean("b5");
        boolean s6 = objs.getBoolean("b6");
        boolean s7 = objs.getBoolean("b7");
        boolean s8 = objs.getBoolean("b8");

        String disp = name+"\nAge : " + age + "\nGender : " + gender + "\nFacilities Required :\n";
        if(s1){
            disp += "Facility 1\n";
        }
        if(s2){
            disp += "Facility 2\n";
        }
        if(s3){
            disp += "Facility 3\n";
        }
        if(s4){
            disp += "Facility 4\n";
        }
        if(s5){
            disp += "Facility 5\n";
        }
        if(s6){
            disp += "Facility 6\n";
        }
        if(s7){
            disp += "Facility 7\n";
        }
        if(s8){
            disp += "Facility 8\n";
        }

        details.setText(disp);
    }
}