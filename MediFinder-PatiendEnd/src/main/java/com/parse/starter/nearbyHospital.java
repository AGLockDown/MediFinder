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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class nearbyHospital extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //To Receive updated of Locations from Emergency clicked button
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_nearby_hospital);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Intent intent2=getIntent();
        final String lat=intent2.getStringExtra("Latitude");
        final String lon=intent2.getStringExtra("Longitude");
        Log.i("Latitude nearby",String.valueOf(lat));
        Log.i("Longitude nearby",String.valueOf(lon));
        Toast.makeText(getApplicationContext(),lat, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),lon, Toast.LENGTH_SHORT).show();

        //List View for showing Hospitals

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Hospital");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    if (objects.size() > 0) {
                        hospitalDetails hospital;
                        ArrayList<hospitalDetails> hospitals = new ArrayList<hospitalDetails>();
                        HashMap<String, Double> hm = new HashMap<>();
                        final ListView hospitalView=(ListView)findViewById(R.id.nearby);
                        HashMap<HashMap<Double,Double>,Double> final_map = new HashMap<>();
                        HashMap<String, String> phoneBook = new HashMap<>();

                        for (ParseObject object : objects) {
                            double MyLatitude = Double.parseDouble(lat);
                            double MyLongitude = Double.parseDouble(lon);
                            String latitude = object.getString("Latitude");
                            String longitude = object.getString("Longitude");
                            String ID = object.getString("ID");
                            String name = object.getString("name");
                            String phno = object.getString("phno");
                            phoneBook.put(name, phno);
                            name = name+" "+(ID);

                            double newLatitude = Double.parseDouble(latitude);
                            double newLongitude = Double.parseDouble(longitude);
                            ParseGeoPoint point = new ParseGeoPoint(MyLatitude, MyLongitude);
                            ParseGeoPoint point2 = new ParseGeoPoint(newLatitude, newLongitude);

                            Double distance = point.distanceInMilesTo(point2);
                            hm.put(name, distance * 1.63);
                            HashMap<Double, Double> latlng = new HashMap<>();
                            latlng.put(newLatitude, newLongitude);
                            final_map.put(latlng,distance*1.63);

                        }
                        List<Map.Entry<String, Double>> list = new LinkedList<>(hm.entrySet());
                        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
                            public int compare(Map.Entry<String, Double> o1,
                                               Map.Entry<String, Double> o2) {
                                return (o1.getValue()).compareTo(o2.getValue());
                            }
                        });

                        HashMap<String, Double> temp = new LinkedHashMap<>();
                        for (Map.Entry<String, Double> aa : list) {
                            temp.put(aa.getKey(), aa.getValue());
                        }

                        //This sorted HashMap provides the distance of the hospital in the sorted order
                        HashMap<String,String> sorted = new LinkedHashMap<>();
                        for (Map.Entry<String, Double> en : temp.entrySet()) {
                            sorted.put(en.getKey(),Double.toString(en.getValue()));
                            Log.i("InformationNames", "Key = " + en.getKey() + ", Value = " + en.getValue());
                        }

                        int checkbox[] = intent2.getIntArrayExtra("Checkbox");
                        HashMap<String,Integer> facility = new HashMap<>();
                        for ( Map.Entry<String, String> en : sorted.entrySet()) {
                            if(Double.parseDouble(en.getValue())>50.0)
                                break;
                            String nameID[] = en.getKey().split(" ");
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Hospital");
                            query.whereEqualTo("ID",nameID[1]);
                            List<ParseObject> facilities;
                            try{
                                facilities = query.find();
                                int count = 0;
                                for(int i=1;i<=8;i++){
                                    String s = "b"+Integer.toString(i);
                                    if(facilities.size()!=0 && facilities.get(0).getBoolean(s)){
                                        if(checkbox[i-1]==1)
                                            count++;
                                    }
                                }
                                facility.put(nameID[0]+" "+nameID[1]+" "+en.getValue(),count);
                            }
                            catch(com.parse.ParseException e1){
                                Log.i("Parse error",e1.getMessage());
                                return;
                            }
                        }
                        List<Map.Entry<String, Integer>> list1 = new LinkedList<>(facility.entrySet());
                        Collections.sort(list1, new Comparator<Map.Entry<String, Integer>>() {
                            public int compare(Map.Entry<String, Integer> o1,
                                               Map.Entry<String, Integer> o2) {
                                return (o1.getValue()).compareTo(o2.getValue())*(-1);
                            }
                        });

                        HashMap<String, Integer> temp1 = new LinkedHashMap<>();
                        for (Map.Entry<String, Integer> aa : list1) {
                            temp1.put(aa.getKey(), aa.getValue());
                        }
                        for(Map.Entry<String,Integer> aa:temp1.entrySet()){
                            hospital = new hospitalDetails();
                            String s[]=aa.getKey().split(" ");
                            hospital.setName(s[0]+" "+s[1]);
                            hospital.setPhone(phoneBook.get(s[0]));
                            hospital.setDistance(String.valueOf(s[2]));
                            hospitals.add(hospital);
                        }
                        hospitalView.setAdapter(new MyAdapter(nearbyHospital.this, hospitals));
                        List<Map.Entry<HashMap<Double,Double>, Double>> newlist = new LinkedList<>(final_map.entrySet());
                        Collections.sort(newlist, new Comparator<Map.Entry<HashMap<Double,Double>, Double>>() {
                            public int compare(Map.Entry<HashMap<Double,Double>, Double> o1,
                                               Map.Entry<HashMap<Double,Double>, Double> o2) {
                                return (o1.getValue()).compareTo(o2.getValue());
                            }
                        });

                        HashMap<HashMap<Double,Double>, Double> temp2 = new LinkedHashMap<>();
                        for (Map.Entry<HashMap<Double,Double>, Double> aa : newlist) {
                            temp2.put(aa.getKey(), aa.getValue());
                        }

                        //This sorted HashMap provides the distance of the hospital in the sorted order
                        HashMap<HashMap<Double,Double>, String> final_sorted = new LinkedHashMap<>();
                        for (Map.Entry<HashMap<Double,Double>, Double> en : temp2.entrySet()) {
                            final_sorted.put(en.getKey(),Double.toString(en.getValue()));
                            Log.i("InformationLatLong", "Key = " + en.getKey() + ", Value = " + en.getValue());
                        }
                        //On ListView Item Click Listener
                        hospitalView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                Intent intent = new Intent(nearbyHospital.this,DisplayHospitals.class);
                                hospitalDetails h1= (hospitalDetails) hospitalView.getItemAtPosition(position);
                                String name = String.valueOf(h1.getName());
                                intent.putExtra("hospital",name);
                                intent.putExtra("Lat",lat);
                                intent.putExtra("Lon",lon);
                                intent.putExtra("phno",h1.getPhone());
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(nearbyHospital.this, Emergency.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}