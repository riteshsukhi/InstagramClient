package com.codepath.instagramclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "fc27e2ef4dd04d2eb9f74c2dd4c283b2";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        photos = new ArrayList<>();
        //1.Create an adapter linking it to a source
        aPhotos = new InstagramPhotosAdapter(this,photos);
        //2.Find the listview from the layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        //3.Set the adapter binding it to the list view
        lvPhotos.setAdapter(aPhotos);
        //Send  out api request to popular photos
        fetchPopularPhotos();
    }

    //Trigger API request
    public void fetchPopularPhotos(){
        /*
        Client ID: fc27e2ef4dd04d2eb9f74c2dd4c283b2
        https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
        -Response
                -TYPE: { “data” => [x] => “type”}(“image”or “video”)
        -Url: { “data” => [x] => “images” => “standard_resolution” => “url"}
                -Caption: { “data” => [x] => “caption” => “text”}
            -Author Name: { “data” => [x] => “user” => “username”}
        */

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        //Creating the network client
        AsyncHttpClient client = new AsyncHttpClient();
        //Trigger the get request
        client.get(url,null, new JsonHttpResponseHandler(){
            //onSuccess (worked, 200)

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                //Iterate each of the photo items and decode the items into a java object
                JSONArray photosJSON = null;
                try{
                    photosJSON = response.getJSONArray("data"); //array of posts
                    //iterate through the array
                    for(int i=0; i < photosJSON.length();i++){
                        //get json object at that position
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        //decode the attributes of the json into a model
                        InstagramPhoto photo = new InstagramPhoto();
                        //Author Name: { “data” => [x] => “user” => “username”}
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        // Caption: { “data” => [x] => “caption” => “text”}
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        //TYPE: { “data” => [x] => “type”}(“image”or “video”)
                        //photo.
                        // Url: { “data” => [x] => “images” => “standard_resolution” => “url"}
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        //Likes count
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        //Image height
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        //Time
                        photo.time = photoJSON.getLong("created_time");
                        //Get Profile Image
                        photo.profileImageUrl = photoJSON.getJSONObject("user").getString("profile_picture");

                        //Add decoded objects to photos
                        photos.add(photo);

                    }
                }catch(JSONException e){
                   e.printStackTrace();
                }

                //callback
                aPhotos.notifyDataSetChanged();

            }

            //onFailure


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               //
            }
        });

    }
}
