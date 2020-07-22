package com.mcet.pandemichelper;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class EpassViewHolder extends RecyclerView.ViewHolder {
    TextView name,from,to,date,status;
    CardView cardView;
    public EpassViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.Epassname);
        from = itemView.findViewById(R.id.from);
        to=itemView.findViewById(R.id.to);
        date=itemView.findViewById(R.id.date);
        status=itemView.findViewById(R.id.status);

        cardView = itemView.findViewById(R.id.EpasscardView);

    }
}
