package com.tolgahanoktay.suggestme.RecyclerViewsAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.Iterator;

public class FragmentHomeRVA extends RecyclerView.Adapter<FragmentHomeRVA.ViewHolder> implements View.OnTouchListener {

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

    int readData = 0;


    public FragmentHomeRVA(Context context, ArrayList<String> movieID_List, ArrayList<String> movieImageList, ArrayList<String> movieNameList, ArrayList<String> movieStoryList,
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
        View view = layoutInflater.from(context).inflate(R.layout.movie_item,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Picasso.get().load(movieImageList.get(position)).into(holder.movie_image);
        holder.movie_name.setText(movieNameList.get(position));
        holder.getMovie_DirectorName.setText(movieDirectorList.get(position));
        holder.movie_rating.setText(movieRatingList.get(position));


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
                    intent.putExtra("cameHome", "0");
                    context.startActivity(intent);

                }
            }
        });

        String getRating = movieRatingList.get(position);

        for (int i = 0; i<movieRatingList.size(); i++){
            String cut = getRating.substring(0,1);

            int getData = Integer.parseInt(cut);
            readData = getData;
        }

        int tam =  10;
        int kalan = tam - readData;

        holder.pieChart.addPieSlice(
                new PieModel(
                        "IMDB",
                        Integer.parseInt(String.valueOf(readData)),
                        Color.parseColor("#E5B00B")));
        holder.pieChart.addPieSlice(
                new PieModel(
                        "KALAN",
                        Integer.parseInt(String.valueOf(kalan)),
                        Color.parseColor("#FFFFFF")));




        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onDown(MotionEvent e) {
                    holder.linearLayout_info.setAlpha((float) 0.8);
                    return true;
                }

                 @Override
                public void onLongPress(MotionEvent e) {
                    holder.linearLayout_info.setAlpha((float) 0.0);
                }
            });



            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });






    }

    @Override
    public int getItemCount() {
        return movieNameList.size();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()){

            case MotionEvent.ACTION_DOWN:
                ViewHolder viewHolder = new ViewHolder(view);
                viewHolder.linearLayout_info.setVisibility(View.VISIBLE);
                break;

            case MotionEvent.ACTION_MOVE:
                ViewHolder viewHolder1 = new ViewHolder(view);
                viewHolder1.linearLayout_info.setVisibility(View.VISIBLE);
                break;

            case MotionEvent.ACTION_UP:
                ViewHolder viewHolder2 = new ViewHolder(view);
                viewHolder2.linearLayout_info.setVisibility(View.VISIBLE);
                break;

            case MotionEvent.ACTION_CANCEL:
                ViewHolder viewHolder3 = new ViewHolder(view);
                viewHolder3.linearLayout_info.setVisibility(View.INVISIBLE);
                break;

            default:
                System.out.println("not found");


        }

        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView movie_name,movie_DirectorTV,getMovie_DirectorName,movie_rating;
        ImageView image_starRating,movie_image;
        LinearLayout linearLayout_info;
        PieChart pieChart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            MobileAds.initialize(context);

            mInterstitialAd = new InterstitialAd(context);

            //home - ca-app-pub-5868956382682991/6131936538
            mInterstitialAd.setAdUnitId("ca-app-pub-5868956382682991/6131936538");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    // Load the next interstitial.
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

            });

            movie_image = itemView.findViewById(R.id.movie_image);
            movie_name = itemView.findViewById(R.id.movie_name);
            movie_DirectorTV = itemView.findViewById(R.id.movie_director_textView);
            getMovie_DirectorName = itemView.findViewById(R.id.movie_DirectorName);
            movie_rating = itemView.findViewById(R.id.movie_rating);
            image_starRating = itemView.findViewById(R.id.image_starRating);
            linearLayout_info = itemView.findViewById(R.id.linearLayout_info);
            pieChart = itemView.findViewById(R.id.chart_imdb);


        }
    }


}
