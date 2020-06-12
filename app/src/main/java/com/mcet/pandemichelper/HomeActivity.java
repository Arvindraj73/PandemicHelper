package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.FileStore;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference reference;
    private FirebaseUser user;
    private FirebaseAuth auth;

    private MaterialToolbar mAppBar;

    private VideoDetailsModel videoDetailsModel;
    private ArrayList<VideoDetailsModel> arrayList;
    private VideoDetailsAdapter adapter;
    private RecyclerView mListView;
    private String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UC7QhK6RVJWM-yqeC2GMjlYA&maxResults=5&key=AIzaSyAuRciEQbgHCmxW3Yhwe0p6iuMz4B8A5jE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent is = new Intent(this,LocationService.class);
        is.putExtra("na","start");
        startService(is);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mListView = findViewById(R.id.recyclerView);
        mListView.setLayoutManager(linearLayoutManager);

        mAppBar = findViewById(R.id.appbar);
        findViewById(R.id.card_donate).setOnClickListener(this);
        findViewById(R.id.card_sos).setOnClickListener(this);
        findViewById(R.id.card_health).setOnClickListener(this);
        findViewById(R.id.card_toll).setOnClickListener(this);
        findViewById(R.id.card_volunteer).setOnClickListener(this);
        findViewById(R.id.card_labour).setOnClickListener(this);

        arrayList = new ArrayList<>();

        adapter = new VideoDetailsAdapter(HomeActivity.this,arrayList);
        adapter.setOnItemClickListener(onItemClickListener);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("UserInfo/"+user.getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel model = dataSnapshot.getValue(UserModel.class);
                mAppBar.setTitle(model.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout :
                        logOut();
                        return true;

                    default: return false;
                }
            }
        });


        displayVideos();

    }

    private void logOut() {
        auth.signOut();
        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
    }

    private void displayVideos() {

        final RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    //Log.d("RESPONSE", response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);

                        videoDetailsModel = new VideoDetailsModel();

                        videoDetailsModel.setVideoId(object.getJSONObject("id").getString("videoId"));
                        videoDetailsModel.setTitle(object.getJSONObject("snippet").getString("title"));
                        videoDetailsModel.setDesc(object.getJSONObject("snippet").getString("description"));
                        videoDetailsModel.setUrl(object.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url"));


//                        Log.d("TAGARRAY",videoDetailsModel.getTitle());
//
//
//                        Log.d("TAGARRAY",videoDetailsModel.getDesc());
//
//
//                        Log.d("TAGARRAY",arrayList.toString());

                        arrayList.add(videoDetailsModel);

                        //Log.d("TAGARRAY",arrayList.toString());

                    }

                    mListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("TAG", error.getMessage());

            }
        });

        mRequestQueue.add(mStringRequest);
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            //Toast.makeText(HomeActivity.this,String.valueOf(position),Toast.LENGTH_SHORT).show();

            if(position == 4){
                startActivity(new Intent(HomeActivity.this,YoutubeActivity.class));
            }
            else{
                VideoDetailsModel model = arrayList.get(position);
                Log.d("VIDEO",model.getVideoId());
                Intent n = new Intent(HomeActivity.this,PlayVideoActivity.class);
                n.putExtra("videoId",model.getVideoId());
                startActivity(n);
            }

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (isMyServiceRunning(LocationService.class)) {
            Log.d("SER","Stopped");
            stopService(new Intent(HomeActivity.this, LocationService.class));
        } else {
            Log.d("SER","Started");
            startService(new Intent(HomeActivity.this, LocationService.class));
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.card_donate:
                Intent i = new Intent(HomeActivity.this, DonateActivity.class);
//                i.putExtra("phone",phone);
//                i.putExtra("name",name);
                startActivity(i);
                break;

            case R.id.card_volunteer:
                startActivity(new Intent(HomeActivity.this,VolunteerActivity.class));
                break;

            case R.id.card_sos:
                startActivity(new Intent(HomeActivity.this,FirstRespondersActivity.class));
                break;

            case R.id.card_health:
                startActivity(new Intent(HomeActivity.this,HealthCareActivity.class));
                break;

            case R.id.card_toll:
                startActivity(new Intent(HomeActivity.this,TollFreeActivity.class));
                break;

            case R.id.card_labour:
                Intent i2 = new Intent(HomeActivity.this,MapsActivity.class);
//                i2.putExtra("phone",phone);
                startActivity(i2);
                break;
        }

    }
}
