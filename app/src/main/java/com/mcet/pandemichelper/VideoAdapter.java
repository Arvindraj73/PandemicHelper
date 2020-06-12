package com.mcet.pandemichelper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcet.pandemichelper.VideoDetailsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.xml.transform.Templates;

public class VideoAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<VideoDetailsModel> mVideoArrayList;
    LayoutInflater layoutInflater;
    public VideoAdapter(Activity activity, ArrayList<VideoDetailsModel> mVideoArrayList) {

        this.activity = activity;
        this.mVideoArrayList = mVideoArrayList;

    }

    @Override
    public int getCount() {
        return this.mVideoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mVideoArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (layoutInflater == null){
            layoutInflater = this.activity.getLayoutInflater();
        }

        if (convertView == null){

            convertView = layoutInflater.inflate(R.layout.videos_layout,null);

        }

        ImageView imageView = convertView.findViewById(R.id.imageView2);
        TextView textView = convertView.findViewById(R.id.title);

        VideoDetailsModel videoDetailsModel = (VideoDetailsModel)this.mVideoArrayList.get(position);

        Picasso.get().load(videoDetailsModel.getUrl()).into(imageView);

        textView.setText(videoDetailsModel.getTitle());

        return convertView;

    }
}
