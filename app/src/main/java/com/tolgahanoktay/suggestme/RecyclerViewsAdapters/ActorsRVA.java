package com.tolgahanoktay.suggestme.RecyclerViewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tolgahanoktay.suggestme.R;

import java.util.ArrayList;

public class ActorsRVA extends RecyclerView.Adapter<ActorsRVA.ViewHolder> {
    Context context;
    LayoutInflater layoutInflater;
    private ArrayList<String> actorImageList;
    private ArrayList<String> actorNameList;

    public ActorsRVA(Context context, ArrayList<String> actorImageList, ArrayList<String> actorNameList) {
        this.context = context;
        this.actorImageList = actorImageList;
        this.actorNameList = actorNameList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.from(context).inflate(R.layout.actor_item,parent,false);
        return new ActorsRVA.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Picasso.get().load(actorImageList.get(position)).into(holder.imageView);
        holder.textView.setText(actorNameList.get(position));

    }

    @Override
    public int getItemCount() {
        return actorNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.actor_image);
            textView = itemView.findViewById(R.id.textView_actor_name);
        }
    }
}
