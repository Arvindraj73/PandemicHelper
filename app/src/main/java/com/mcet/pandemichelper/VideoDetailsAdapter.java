package com.mcet.pandemichelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class VideoDetailsAdapter extends RecyclerView.Adapter<VideoDetailsAdapter.VideoDetailsHolder> {

    Activity activity;
    ArrayList<VideoDetailsModel> arrayList;
    private View.OnClickListener mOnItemClickListener;

    public VideoDetailsAdapter(Activity activity, ArrayList<VideoDetailsModel> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public VideoDetailsAdapter.VideoDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_layout,null);

        VideoDetailsHolder videoDetailsHolder = new VideoDetailsHolder(v);

        return videoDetailsHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull VideoDetailsAdapter.VideoDetailsHolder holder, int position) {

        VideoDetailsModel videoDetailsModel = arrayList.get(position);

        if (position == 4){
            //holder.textView.setText("More Videos");
            holder.textView.setVisibility(View.GONE);
            holder.imageView.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
//            holder.lv.setVisibility(View.GONE);
//            holder.moreView.setVisibility(View.VISIBLE);
           // holder.cardView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        }else
        {
            Picasso.get().load(videoDetailsModel.getUrl()).into(holder.imageView);
            holder.textView.setText(videoDetailsModel.getTitle());
        }

    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        mOnItemClickListener = itemClickListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }

    public class VideoDetailsHolder extends RecyclerView.ViewHolder{

        protected ImageView imageView;
        protected TextView textView,moreView;
        protected MaterialCardView cardView;
        protected LinearLayout lv;

        private VideoDetailsHolder(View v) {
            super(v);

            this.imageView = v.findViewById(R.id.imageView2);
            this.textView = v.findViewById(R.id.title);
            //this.moreView = v.findViewById(R.id.more);
            this.cardView = v.findViewById(R.id.card_youtube);
            this.lv = v.findViewById(R.id.lv);
            v.setTag(this);
            v.setOnClickListener(mOnItemClickListener);
        }
    }

}
