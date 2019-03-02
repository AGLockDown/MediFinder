/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.parse.ParseAnalytics;
import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity {

  Intent mServiceIntent;
  private MyService mSensorService;
  Context ctx;
  public Context getCtx() {
    return ctx;
  }
  ParseUser currentUser = ParseUser.getCurrentUser();
  public void goToPatient(View view)
  {
    if(currentUser==null || currentUser.getBoolean("isHospital"))
      startActivity(new Intent(MainActivity.this, PatientLogin.class));
    else
      startActivity(new Intent(MainActivity.this, Emergency.class));
  }

  protected boolean isCurrentUserHospital(){
    return ParseUser.getCurrentUser().getBoolean("isHospital");
  }

  public void goToHospital(View view){
    Intent intent;
    if(ParseUser.getCurrentUser() != null && isCurrentUserHospital()){
      Log.i("User123",ParseUser.getCurrentUser().toString());
      Log.i("User",ParseUser.getCurrentUser().getUsername());
      intent = new Intent(MainActivity.this, HospitalScreenActivity.class);
    }
    else{
      intent = new Intent(MainActivity.this, LoginActivity.class);
    }
    startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    setContentView(R.layout.activity_main);
    ctx = this;
    setContentView(R.layout.activity_main);
    mSensorService = new MyService(getCtx());
    mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
    if (!isMyServiceRunning(mSensorService.getClass())) {
      startService(mServiceIntent);
    }
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }
  private boolean isMyServiceRunning(Class<?> serviceClass) {
    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
      if (serviceClass.getName().equals(service.service.getClassName())) {
        Log.i ("isMyServiceRunning?", true+"");
        return true;
      }
    }
    Log.i ("isMyServiceRunning?", false+"");
    return false;
  }


  @Override
  protected void onDestroy() {
    stopService(mServiceIntent);
    Log.i("MAINACT", "onDestroy!");
    super.onDestroy();

  }
}