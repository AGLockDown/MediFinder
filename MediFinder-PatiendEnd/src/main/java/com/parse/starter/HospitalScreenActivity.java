package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class HospitalScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_hospital_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void updateInfo(View view){
        Intent intent = new Intent(HospitalScreenActivity.this, SignUp2Activity.class);
        startActivity(intent);
    }

    public void emergency(View view){
        Intent intent = new Intent(HospitalScreenActivity.this, hospitalEmergency.class);
        startActivity(intent);
    }

    public void logOut(View view) {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                Intent intent = new Intent(HospitalScreenActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                finish();
                Intent intent = new Intent(HospitalScreenActivity.this, MainActivity.class);
                startActivity(intent);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
