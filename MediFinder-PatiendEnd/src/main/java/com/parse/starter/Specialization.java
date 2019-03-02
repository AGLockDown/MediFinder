package com.parse.starter;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class Specialization extends AsyncTask<String, Void, String> {


    public String doInBackground(String... strings) {
        String result = "";

        URL url;

        HttpURLConnection urlConnection = null;
        String token=ApiSpecialist.token;
        Log.i("Hiiii12",String.valueOf(token));
        String s = "[";
        for (Map.Entry<String, String> entry : Emergency.Id.entrySet()){
            if(entry.getKey() != null)
            {
                s = s+entry.getValue();
                s=s+",";
            }
        }
        s=s.substring(0,s.length()-1);
        s=s+"]";
        try {


            url  = new URL("https://healthservice.priaid.ch/diagnosis?symptoms="+s+"&gender=male&year_of_birth=1998&token="+token+"&format=json&language=en-gb");

            Log.i("URL",url.toString());

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.connect();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();

            while (data != -1) {

                char current = (char) data;

                result += current;

                data = reader.read();
            }

            Log.i("URLContent", result);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onPostExecute(String result)
    {
        super.onPostExecute(result);

        try {
            JSONArray arr = new JSONArray(result);
            if(arr.length()==0)
            {
                addSymptoms.e1.setText("SPECIALISATION: Contact General Practioner");
            }
            else
            {
                String s="";

                for(int i=0;i<arr.length() && i<2;i++)
                {
                    JSONObject obj = arr.getJSONObject(i);
                    JSONObject issueobj = new JSONObject(obj.getString("Issue"));
                    Log.i("INFO",issueobj.getString("Name"));
                    s=s+'\n'+"ISSUE: "+issueobj.getString("Name");
                    addSymptoms.e1.setText(s);

                    JSONArray specarr = new JSONArray(obj.getString("Specialisation"));
                    //Log.i("SIZE",String.valueOf(specarr));
                    for(int j=0;j<specarr.length() && j<2 ;j++)
                    {
                        JSONObject specobj = specarr.getJSONObject(j);
                        Log.i("Specialisation",specobj.getString("Name"));
                        s=s+'\n'+"SPECIALISATION: "+specobj.getString("Name")+"\n";
                        addSymptoms.e1.setText(s);
                    }
                }
            }
        }
        catch(JSONException e)
        {
            Log.i("INFO","Contact General Practioner");

        }
    }
}
