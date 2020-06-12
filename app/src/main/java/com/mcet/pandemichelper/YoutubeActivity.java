package com.mcet.pandemichelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubePlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class YoutubeActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<VideoDetailsModel> mVideoArrayList;
    private VideoAdapter mVideoDetailsAdapter;
    private String url="https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UC7QhK6RVJWM-yqeC2GMjlYA&maxResults=10&key=AIzaSyAuRciEQbgHCmxW3Yhwe0p6iuMz4B8A5jE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        mListView = findViewById(R.id.listVideos);

        mVideoArrayList = new ArrayList<>();

        mVideoDetailsAdapter = new VideoAdapter(YoutubeActivity.this,mVideoArrayList);

        displayVideos();

    }

    private void displayVideos() {

        final RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject object = jsonArray.getJSONObject(i);

                        VideoDetailsModel mVideoDetailsModel = new VideoDetailsModel();

                        mVideoDetailsModel.setVideoId(object.getJSONObject("id").getString("videoId"));
                        mVideoDetailsModel.setTitle(object.getJSONObject("snippet").getString("title"));
                        mVideoDetailsModel.setDesc(object.getJSONObject("snippet").getString("description"));
                        mVideoDetailsModel.setUrl(object.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url"));

                        mVideoArrayList.add(mVideoDetailsModel);

                    }

                    mListView.setAdapter(mVideoDetailsAdapter);
                    mVideoDetailsAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("TAG",error.getMessage());

            }
        });

        mRequestQueue.add(mStringRequest);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                VideoDetailsModel model = mVideoArrayList.get(position);
                Intent n = new Intent(YoutubeActivity.this,PlayVideoActivity.class);
                n.putExtra("videoId",model.getVideoId());
                startActivity(n);

            }
        });

    }
}
