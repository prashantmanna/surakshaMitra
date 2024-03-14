package com.surakshamitra;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class fetchData extends AsyncTask<Object,String,String> {
    String googleNearByPlacesData;
    GoogleMap googleMap;
    String url;

    @Override
    protected String doInBackground(Object... objects) {
        try
        {
            googleMap = (GoogleMap)objects[0];
            url =(String)objects[1];
            downloadUrl downloadUrl = new downloadUrl();
            googleNearByPlacesData = downloadUrl.ReadTheURL(url);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return googleNearByPlacesData;
    }


    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for(int i= 0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                JSONObject getLocation = jsonObject1.getJSONObject("geometry")
                        .getJSONObject("location");
                String lat = getLocation.getString("lat");
                String lon = getLocation.getString("lon");
                JSONObject getName = jsonArray.getJSONObject(i);
                String name = getName.getString("name");

                LatLng lng = new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(name);
                googleMap.addMarker(markerOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(lng));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lng,20));

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
