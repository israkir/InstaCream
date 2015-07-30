package com.hakkicaner.instacream;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.*;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PhotosActivity extends ActionBarActivity {

    private SwipeRefreshLayout swipeContainer;
    private ArrayList<InstagramPhoto> instagramPhotos;
    private InstagramPhotosAdapter aPhotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(true);
                aPhotos.notifyDataSetChanged();
                aPhotos.clear();
                fetchPopularPhotos();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);

        instagramPhotos = new ArrayList<InstagramPhoto>();
        aPhotos = new InstagramPhotosAdapter(this, instagramPhotos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + getResources().getString(R.string.instagram_client_id);

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJson = null;
                try {
                    photosJson = response.getJSONArray("data");
                    Log.i("DEBUG", photosJson.toString());
                    for (int i = 0; i < photosJson.length(); i++) {
                        JSONObject photoJson = photosJson.getJSONObject(i);
                        InstagramPhoto instagramPhoto = new InstagramPhoto();
                        instagramPhoto.username = photoJson.getJSONObject("user").getString("username");
                        instagramPhoto.avatarUrl = photoJson.getJSONObject("user").getString("profile_picture");
                        instagramPhoto.caption = photoJson.getJSONObject("caption").getString("text");
                        instagramPhoto.createdTime = formatCreatedTime(photoJson.getString("created_time"));
                        instagramPhoto.imageUrl = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        instagramPhoto.likesCount = photoJson.getJSONObject("likes").getInt("count");
                        instagramPhotos.add(instagramPhoto);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                aPhotos.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("DEBUG", responseString);
            }
        });
    }

    private static final String formatCreatedTime(String epoch) {
        SimpleDateFormat sdf = new SimpleDateFormat("@MMM d hha", Locale.ENGLISH);
        return sdf.format(new Date(Long.parseLong(epoch)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
