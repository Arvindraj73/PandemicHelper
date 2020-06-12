package com.mcet.pandemichelper;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class WorkViewHolder extends RecyclerView.ViewHolder {

    TextView work,num;
    CardView cardView;

    public WorkViewHolder(@NonNull View itemView) {
        super(itemView);

        work = itemView.findViewById(R.id.work);
        num = itemView.findViewById(R.id.num);
        cardView = itemView.findViewById(R.id.cardView);

    }
}
