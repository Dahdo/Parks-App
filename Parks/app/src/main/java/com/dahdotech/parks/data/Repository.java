package com.dahdotech.parks.data;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dahdotech.parks.controller.AppController;
import com.dahdotech.parks.model.Images;
import com.dahdotech.parks.model.Park;
import com.dahdotech.parks.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    static List<Park> parkList = new ArrayList<>();

    public static void getParks(final AsyncResponse callback){
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, Util.PARKS_URL, null, response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Park park = new Park();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            park.setId(jsonObject.getString("id"));
                            park.setFullName(jsonObject.getString("fullName"));
                            park.setLatitude(jsonObject.getString("latitude"));
                            park.setLongitude(jsonObject.getString("longitude"));
                            park.setParkCode(jsonObject.getString("parkCode"));
                            park.setStates(jsonObject.getString("states"));

                            JSONArray imageList = jsonObject.getJSONArray("images");
                            List<Images> list = new ArrayList<>();
                            for (int j = 0; j < imageList.length(); j++) {
                                Images images = new Images();
                                images.setCredit(imageList.getJSONObject(j).getString("credit"));
                                images.setTitle(imageList.getJSONObject(j).getString("title"));
                                images.setUrl(imageList.getJSONObject(j).getString("url"));

                                list.add(images);
                            }
                            park.setImages(list);
                            park.setWeatherInfo(jsonObject.getString("weatherInfo"));
                            park.setName(jsonObject.getString("name"));
                            park.setDesignation(jsonObject.getString("designation"));
                            parkList.add(park);
                        }
                        if(callback != null){ callback.processPark(parkList);}

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    error.printStackTrace();
                });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
