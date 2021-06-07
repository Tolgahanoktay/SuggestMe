package com.tolgahanoktay.suggestme;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderAdapterTwo extends PagerAdapter {

    private Context context;

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

    int getPosition = 0;

    public SliderAdapterTwo(Context context, ArrayList<String> movieID_List, ArrayList<String> movieImageList, ArrayList<String> movieNameList, ArrayList<String> movieStoryList, ArrayList<String> movieDirectorList,
                            ArrayList<String> movieYearList, ArrayList<String> movieRatingList, ArrayList<String> movieRuntimeList, ArrayList<String> movieGenreList, ArrayList<String> movieChildGenreList,
                            ArrayList<String> movieWatchPltList, ArrayList<String> youtubeCodeList, ArrayList<String> movieViewerList, ArrayList<String> movieFavCountList) {
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

    @Override
    public int getCount() {
        return movieImageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }



    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout_two,container,false);

        MobileAds.initialize(context);

        mInterstitialAd = new InterstitialAd(context);

        //home - ca-app-pub-5868956382682991/6131936538
        mInterstitialAd.setAdUnitId("ca-app-pub-5868956382682991/4707174229");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        ImageView imageBanner = view.findViewById(R.id.img_banner_two);

        Picasso.get().load(movieImageList.get(position)).into(imageBanner);


        imageBanner.setOnClickListener(new View.OnClickListener() {
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
                    intent.putExtra("cameHome", "2");
                    context.startActivity(intent);
                }
            }
        });
        //imageBanner.setImageResource(sliderImages[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
