package com.tolgahanoktay.suggestme.RecyclerViewsAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;
import com.tolgahanoktay.suggestme.DetailActivity;
import com.tolgahanoktay.suggestme.R;

import java.util.ArrayList;

public class FavoriesRVA extends RecyclerView.Adapter<FavoriesRVA.ViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    private ArrayList<String> movieID_List;
    private ArrayList<String> movieImageList;
    private ArrayList<String> movieNameList;
    private ArrayList<String> movieStoryList;
    private ArrayList<String> movieDirectorList;
    private ArrayList<String> movieYearList;
    private ArrayList<String> movieRatingList;
    private ArrayList<String> movieRuntimeList;
    private ArrayList<String> movieGenreList;
    private ArrayList<String> movieChildGenreList;
    private ArrayList<String> movieWatchPltList;
    private ArrayList<String> youtubeCodeList;
    private ArrayList<String> movieViewerList;
    private ArrayList<String> movieFavCountList;

    private InterstitialAd mInterstitialAd;


    public FavoriesRVA(Context context, ArrayList<String> movieID_List, ArrayList<String> movieImageList, ArrayList<String> movieNameList, ArrayList<String> movieStoryList,
                       ArrayList<String> movieDirectorList, ArrayList<String> movieYearList, ArrayList<String> movieRatingList, ArrayList<String> movieRuntimeList, ArrayList<String> movieGenreList,
                       ArrayList<String> movieChildGenreList, ArrayList<String> movieWatchPltList, ArrayList<String> youtubeCodeList, ArrayList<String> movieViewerList, ArrayList<String> movieFavCountList) {


        this.context = context;
        this.movieID_List = movieID_List;
        this.movieImageList = movieImageList;
        this.movieNameList = movieNameList;
        this.movieStoryList = movieStoryList;
        this.movieDirectorList = movieDirectorList;
        this.movieYearList = movieYearList;
        this.movieRatingList = movieRatingList;
        this.movieRuntimeList = movieRuntimeList;
        this.movieGenreList = movieGenreList;
        this.movieChildGenreList = movieChildGenreList;
        this.movieWatchPltList = movieWatchPltList;
        this.youtubeCodeList = youtubeCodeList;
        this.movieViewerList = movieViewerList;
        this.movieFavCountList = movieFavCountList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.from(context).inflate(R.layout.search_item,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Picasso.get().load(movieImageList.get(position)).into(holder.imageView);
        holder.contentTitle.setText(movieNameList.get(position));
        holder.contentStory.setText(movieStoryList.get(position));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("movieID", movieID_List.get(position));
                    intent.putExtra("movieImage", movieImageList.get(position));
                    intent.putExtra("movieName", movieNameList.get(position));
                    intent.putExtra("movieStory", movieStoryList.get(position));
                    intent.putExtra("movieDirector", movieDirectorList.get(position));
                    intent.putExtra("movieYear", movieYearList.get(position));
                    intent.putExtra("movieRating", movieRatingList.get(position));
                    intent.putExtra("movieRuntime", movieRuntimeList.get(position));
                    intent.putExtra("movieGenre", movieGenreList.get(position));
                    intent.putExtra("movieChildGenre", movieChildGenreList.get(position));
                    intent.putExtra("movieWatchPlt", movieWatchPltList.get(position));
                    intent.putExtra("movieYoutube", youtubeCodeList.get(position));
                    intent.putExtra("movieViewerCount", movieViewerList.get(position));
                    intent.putExtra("movieFavCount", movieFavCountList.get(position));
                    intent.putExtra("cameHome", "4");
                    context.startActivity(intent);

                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return movieID_List.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView contentTitle,contentStory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            MobileAds.initialize(context);

            mInterstitialAd = new InterstitialAd(context);

            //home - ca-app-pub-5868956382682991/6131936538
            mInterstitialAd.setAdUnitId("ca-app-pub-5868956382682991/4033043344");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    // Load the next interstitial.
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

            });

            imageView = itemView.findViewById(R.id.querySearch_image);
            contentTitle = itemView.findViewById(R.id.querySearch_title);
            contentStory = itemView.findViewById(R.id.querySearch_story);


        }
    }


}
