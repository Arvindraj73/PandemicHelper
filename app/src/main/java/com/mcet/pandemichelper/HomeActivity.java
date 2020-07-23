package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference reference;
    private FirebaseUser user;
    private FirebaseAuth auth;

    private MaterialToolbar mAppBar;
    private TextView role;

    private VideoDetailsModel videoDetailsModel;
    private ArrayList<VideoDetailsModel> arrayList;
    private VideoDetailsAdapter adapter;
    private RecyclerView mListView;
    private String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UC7QhK6RVJWM-yqeC2GMjlYA&maxResults=5&key=AIzaSyBzHbDmyGXgM5Sd11V-CIJw38-VqBTdOdk";
    private String sRole, name, deviceToken;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_home);

        Intent is = new Intent(this, LocationService.class);
        is.putExtra("na", "start");
        startService(is);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mListView = findViewById(R.id.recyclerView);
        mListView.setLayoutManager(linearLayoutManager);

        mAppBar = findViewById(R.id.appbar);
        role = findViewById(R.id.role);
        findViewById(R.id.card_donate).setOnClickListener(this);
        findViewById(R.id.card_sos).setOnClickListener(this);
        findViewById(R.id.card_health).setOnClickListener(this);
        findViewById(R.id.card_toll).setOnClickListener(this);
        findViewById(R.id.card_volunteer).setOnClickListener(this);
        findViewById(R.id.card_labour).setOnClickListener(this);
        findViewById(R.id.card_doc).setOnClickListener(this);
        findViewById(R.id.card_eservices).setOnClickListener(this);
        findViewById(R.id.card_courses).setOnClickListener(this);

        findViewById(R.id.orphan).setOnClickListener(this);
        findViewById(R.id.councel).setOnClickListener(this);
        findViewById(R.id.patients).setOnClickListener(this);
        findViewById(R.id.unorg).setOnClickListener(this);
        findViewById(R.id.store).setOnClickListener(this);
        findViewById(R.id.epass).setOnClickListener(this);

        arrayList = new ArrayList<>();

        adapter = new VideoDetailsAdapter(HomeActivity.this, arrayList);
        adapter.setOnItemClickListener(onItemClickListener);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("UserInfo/" + user.getUid());

        preferences = getApplicationContext().getSharedPreferences("UserData", MODE_PRIVATE);

        Log.d("pre", preferences.toString());
        sRole=preferences.getString("role","");
        role=findViewById(R.id.role);
        if(sRole.equals("Doctor")){
        role.setText("Patients");}
        else{
            role.setText(sRole);
        }

        mAppBar.setTitle(preferences.getString("name", "Wait"));

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    deviceToken = task.getResult().getToken();
                    Log.d("token", deviceToken);
                }
            }
        });

        mAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout:
                        logOut();
                        return true;
                    case R.id.en:
                        Toast.makeText(HomeActivity.this, "en", Toast.LENGTH_SHORT).show();
                        setLocale("en");
                        recreate();
                        return true;
                    case R.id.ml:
                        Toast.makeText(HomeActivity.this, "ml", Toast.LENGTH_SHORT).show();
                        setLocale("ml");
                        recreate();
                        return true;
                    default:
                        return false;
                }
            }
        });


        displayVideos();

    }

    private void setLocale(String lang) {

        //Toast.makeText(HomeActivity.this, lang, Toast.LENGTH_SHORT).show();
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("Lang", lang);
        editor.apply();

    }

    public void loadLocale() {

        Log.d("tag", "Inlocale");
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        String lang = preferences.getString("Lang", "");
        setLocale(lang);

    }

    private void logOut() {
        auth.signOut();
        preferences.getAll().clear();
        finish();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
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

        switch (v.getId()) {

            case R.id.card_donate:
                Intent i = new Intent(HomeActivity.this, DonateActivity.class);
                startActivity(i);
                break;

            case R.id.card_volunteer:
                if(sRole.equals("Doctor")){
                    startActivity(new Intent(HomeActivity.this,PatientsActivity.class));
                 break;
                }
                else if (sRole.equals("Health Worker")){
                    Intent workIntent = new Intent(HomeActivity.this, AdminHealthWorkerActivity.class);
                    workIntent.putExtra("id", "user");
                    startActivity(workIntent);
                    break;
                }
                else if(sRole.equals("Essential Worker")){
                    Intent workIntent = new Intent(HomeActivity.this, AdminEssentialWorkerActivity.class);
                    workIntent.putExtra("id", "user");
                    startActivity(workIntent);
                    break;
            }
                else if(sRole.equals("Unorganised Worker")){
                    Intent workIntent = new Intent(HomeActivity.this, AdminHealthWorkerActivity.class);
                    workIntent.putExtra("id", "unorg");
                    startActivity(workIntent);
                    break;
                }
                else if(sRole.equals("Volunteer")){
                    Intent workIntent = new Intent(HomeActivity.this, WorkAssignActivity.class);
                    workIntent.putExtra("id", "user");
                    startActivity(workIntent);
                    break;
                }
            case R.id.card_sos:
                startActivity(new Intent(HomeActivity.this, FirstRespondersActivity.class));
                break;

            case R.id.card_health:
                startActivity(new Intent(HomeActivity.this, HealthCareActivity.class));
                break;

            case R.id.card_toll:
                startActivity(new Intent(HomeActivity.this, TollFreeActivity.class));
                break;

            case R.id.card_labour:
                Intent i2 = new Intent(HomeActivity.this, MapsActivity.class);
                startActivity(i2);
                break;

            case R.id.card_doc:
                Intent docIntent = new Intent(HomeActivity.this, DoctorActivity.class);
                docIntent.putExtra("id", "doc");
                startActivity(docIntent);
                break;

            case R.id.card_eservices:
                Intent esIntent = new Intent(HomeActivity.this, EservicesActivity.class);
                esIntent.putExtra("name", preferences.getString("name", "Wait"));
                startActivity(esIntent);
                break;

            case R.id.card_courses:
                Intent cIntent = new Intent(HomeActivity.this, CoursesActivity.class);
                cIntent.putExtra("name", preferences.getString("name", "Wait"));
                cIntent.putExtra("id", "course");
                startActivity(cIntent);
                break;

            case R.id.patients:
                startActivity(new Intent(HomeActivity.this, PatientHelpActivity.class));
                break;

            case R.id.orphan:
                startActivity(new Intent(HomeActivity.this, OrphansActivity.class));
                break;

            case R.id.unorg:
                Intent k = new Intent(HomeActivity.this, DoctorActivity.class);
                k.putExtra("id", "unorg");
                startActivity(k);
                break;

            case R.id.councel:
                startActivity(new Intent(HomeActivity.this, CounsellingActivity.class));
                break;

            case R.id.store:
                Intent j = new Intent(HomeActivity.this, CoursesActivity.class);
                j.putExtra("id", "msme");
                startActivity(j);
                break;

            case R.id.epass:
                Intent l = new Intent(HomeActivity.this, EPassActivity.class);
                //j.putExtra("id", "msme");
                startActivity(l);
                break;
        }

    }
}
