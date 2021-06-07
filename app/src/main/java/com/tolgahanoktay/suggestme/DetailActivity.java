package com.tolgahanoktay.suggestme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import com.tolgahanoktay.suggestme.RecyclerViewsAdapters.ActorsRVA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DetailActivity extends AppCompatActivity {

    ImageView imageViewBack,imageViewFav,imageViewStar,imageViewCount,imageViewList,imageViewTime,imageViewNetflix,imageViewAmazon,imageViewMovie_Picture,image_news,imageViewBack2;

    TextView textViewRating,textViewGenre,textViewYear,textViewCount,textViewList,textViewTime,textView_Director,textViewStory_Area,textViewActors,textViewMovie_Name,textView_newsTitle;

    LinearLayout linearLayout,linearLayout2;

    RecyclerView recyclerViewActors;

    YouTubePlayerView youTubePlayer;

    String movieID,movieFavCount;

    private FirebaseDatabase database;
    DatabaseReference databaseReference;

    ArrayList<String> actorImageList;
    ArrayList<String> actorNameList;

    ArrayList<String> documentID_array;
    ArrayList<Integer> id_array;

    ActorsRVA actorsRVA;

    private FirebaseFirestore firebaseFirestore;
    private SQLiteDatabase sqLiteDatabase;

    int favCounter = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageViewBack = findViewById(R.id.image_back_button);
        //imageViewBack2 = findViewById(R.id.image_back_button2);
        imageViewFav = findViewById(R.id.image_add_favorie);
        imageViewStar = findViewById(R.id.image_starRating_detail);
        imageViewCount = findViewById(R.id.image_view_count);
        imageViewList = findViewById(R.id.image_added_list);
        imageViewTime = findViewById(R.id.image_time);
        imageViewNetflix = findViewById(R.id.image_netflix);
        imageViewAmazon = findViewById(R.id.image_amazon);
        imageViewMovie_Picture = findViewById(R.id.movie_image_detail);
        image_news = findViewById(R.id.imageView_newsImage);


        textViewRating = findViewById(R.id.movie_rating_detail);
        textViewGenre = findViewById(R.id.movie_genre_detail);
        textViewYear = findViewById(R.id.movie_year_detail);
        textViewCount = findViewById(R.id.text_views_count);
        textViewList = findViewById(R.id.text_added_list_count);
        textViewTime = findViewById(R.id.text_time);
        textViewStory_Area = findViewById(R.id.textView_storyArea);
        textView_Director = findViewById(R.id.textView_Director);
        textViewActors = findViewById(R.id.textView_actor_title);
        textViewMovie_Name = findViewById(R.id.movie_name_detail);
        textView_newsTitle = findViewById(R.id.textView_name_newsDetail);

        linearLayout = findViewById(R.id.linear_detailAboutContent);
        linearLayout2 = findViewById(R.id.linearActor);


        recyclerViewActors = findViewById(R.id.recyclerView_actorsPhoto);
        recyclerViewActors.setHasFixedSize(true);
        recyclerViewActors.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false));

        youTubePlayer = findViewById(R.id.youtube_trailer);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        actorImageList = new ArrayList<>();
        actorNameList = new ArrayList<>();

        documentID_array = new ArrayList<>();
        id_array = new ArrayList<Integer>();

        firebaseFirestore = FirebaseFirestore.getInstance();


        String chechkExtrasHome = getIntent().getStringExtra("cameHome");
        String getCheckValue = chechkExtrasHome;
        final int parseCheck = Integer.valueOf(getCheckValue);

        //movieID = getIntent().getStringExtra("movieID");

        queryFav();

        if (parseCheck == 0){

            String getMovieID= getIntent().getStringExtra("movieID");
            movieID = getMovieID;

            for (int i=0; i<documentID_array.size(); i++) {
                if (documentID_array.contains(movieID)) {
                    imageViewFav.setBackgroundResource(R.drawable.choosen_fav);
                    System.out.println("Document name : " + documentID_array.get(i));
                } else {
                    imageViewFav.setBackgroundResource(R.drawable.ic_favorite_heart);
                }
            }

           String getMovieImage = getIntent().getStringExtra("movieImage");
           String getMovieName = getIntent().getStringExtra("movieName");
           String getMovieStory = getIntent().getStringExtra("movieStory");
           String getMovieDirector = getIntent().getStringExtra("movieDirector");
           String getMovieYear = getIntent().getStringExtra("movieYear");
           String getMovieRating = getIntent().getStringExtra("movieRating");
           String getMovieRuntime = getIntent().getStringExtra("movieRuntime");
           String getMovieGenre = getIntent().getStringExtra("movieGenre");
           String getMovieChildGenre = getIntent().getStringExtra("movieChildGenre");
           String getMovieWatchPlt = getIntent().getStringExtra("movieWatchPlt");
           final String getMovieYoutube = getIntent().getStringExtra("movieYoutube");
           String getMovieViewerCount = getIntent().getStringExtra("movieViewerCount");
           String getFavCount= getIntent().getStringExtra("movieFavCount");

           String movieDownload = getMovieImage;

           movieFavCount = getFavCount;

           Picasso.get().load(movieDownload).into(imageViewMovie_Picture);
           textViewMovie_Name.setText(getMovieName);
           textViewRating.setText(getMovieRating + " IMDB");
           textViewGenre.setText(getMovieGenre +"," + getMovieChildGenre);
           textViewYear.setText(getMovieYear);

           if (getMovieGenre.equals("Movie")){
               textViewTime.setText(getMovieRuntime+" min");
           }else {
               textViewTime.setText(getMovieRuntime);
           }

           textViewStory_Area.setText(getMovieStory);
           textView_Director.setText(getMovieDirector);
           textViewCount.setText(getMovieViewerCount);
           textViewList.setText(getFavCount);

           if (getMovieWatchPlt.equals("Netflix")){
               imageViewAmazon.setVisibility(View.GONE);
           }else if (getMovieWatchPlt.equals("Amazon")){
               imageViewNetflix.setVisibility(View.GONE);
           }else{
               System.out.println("Both Of These");
           }

            youTubePlayer.enterFullScreen();
            youTubePlayer.exitFullScreen();

            youTubePlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    String youtubeCode = getMovieYoutube;
                    youTubePlayer.cueVideo(youtubeCode,0);
                    super.onReady(youTubePlayer);
                }
            });

            counterVisits(getMovieViewerCount,getMovieID);

        }else if (parseCheck == 1){

            String getMovieID= getIntent().getStringExtra("movieID");
            movieID = getMovieID;

            String getMovieImage = getIntent().getStringExtra("movieImage");
            String getMovieName = getIntent().getStringExtra("movieName");
            String getMovieStory = getIntent().getStringExtra("movieStory");
            String getMovieDirector = getIntent().getStringExtra("movieDirector");
            String getMovieYear = getIntent().getStringExtra("movieYear");
            String getMovieRating = getIntent().getStringExtra("movieRating");
            String getMovieRuntime = getIntent().getStringExtra("movieRuntime");
            String getMovieGenre = getIntent().getStringExtra("movieGenre");
            String getMovieChildGenre = getIntent().getStringExtra("movieChildGenre");
            String getMovieWatchPlt = getIntent().getStringExtra("movieWatchPlt");
            final String getMovieYoutube = getIntent().getStringExtra("movieYoutube");
            String getMovieViewerCount = getIntent().getStringExtra("movieViewerCount");
            String getFavCount= getIntent().getStringExtra("movieFavCount");

            String movieDownload = getMovieImage;

            movieFavCount = getFavCount;

            Picasso.get().load(movieDownload).into(imageViewMovie_Picture);
            textViewMovie_Name.setText(getMovieName);
            textViewRating.setText(getMovieRating + " IMDB");
            textViewGenre.setText(getMovieGenre +"," + getMovieChildGenre);
            textViewYear.setText(getMovieYear);

            if (getMovieGenre.equals("Movie")){
                textViewTime.setText(getMovieRuntime+" min");
            }else {
                textViewTime.setText(getMovieRuntime);
            }

            textViewStory_Area.setText(getMovieStory);
            textView_Director.setText(getMovieDirector);
            textViewCount.setText(getMovieViewerCount);
            textViewList.setText(getFavCount);

            if (getMovieWatchPlt.equals("Netflix")){
                imageViewAmazon.setVisibility(View.GONE);
            }else if (getMovieWatchPlt.equals("Amazon")){
                imageViewNetflix.setVisibility(View.GONE);
            }else{
                System.out.println("Both Of These");
            }

            youTubePlayer.enterFullScreen();
            youTubePlayer.exitFullScreen();

            youTubePlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    String youtubeCode = getMovieYoutube;
                    youTubePlayer.cueVideo(youtubeCode,0);
                    super.onReady(youTubePlayer);
                }
            });

            counterVisits(getMovieViewerCount,getMovieID);

        }else if (parseCheck == 2){

            String getMovieID= getIntent().getStringExtra("movieID");
            movieID = getMovieID;

            for (int i=0; i<documentID_array.size(); i++) {
                if (documentID_array.contains(movieID)) {
                    imageViewFav.setBackgroundResource(R.drawable.choosen_fav);
                    System.out.println("Document name : " + documentID_array.get(i));
                } else {
                    imageViewFav.setBackgroundResource(R.drawable.ic_favorite_heart);
                }
            }

            String getMovieImage = getIntent().getStringExtra("movieImage");
            String getMovieName = getIntent().getStringExtra("movieName");
            String getMovieStory = getIntent().getStringExtra("movieStory");
            String getMovieDirector = getIntent().getStringExtra("movieDirector");
            String getMovieYear = getIntent().getStringExtra("movieYear");
            String getMovieRating = getIntent().getStringExtra("movieRating");
            String getMovieRuntime = getIntent().getStringExtra("movieRuntime");
            String getMovieGenre = getIntent().getStringExtra("movieGenre");
            String getMovieChildGenre = getIntent().getStringExtra("movieChildGenre");
            String getMovieWatchPlt = getIntent().getStringExtra("movieWatchPlt");
            final String getMovieYoutube = getIntent().getStringExtra("movieYoutube");
            String getMovieViewerCount = getIntent().getStringExtra("movieViewerCount");
            String getFavCount= getIntent().getStringExtra("movieFavCount");

            String movieDownload = getMovieImage;

            movieFavCount = getFavCount;

            Picasso.get().load(movieDownload).into(imageViewMovie_Picture);
            textViewMovie_Name.setText(getMovieName);
            textViewRating.setText(getMovieRating + " IMDB");
            textViewGenre.setText(getMovieGenre +"," + getMovieChildGenre);
            textViewYear.setText(getMovieYear);

            if (getMovieGenre.equals("Movie")){
                textViewTime.setText(getMovieRuntime+" min");
            }else {
                textViewTime.setText(getMovieRuntime);
            }

            textViewStory_Area.setText(getMovieStory);
            textView_Director.setText(getMovieDirector);
            textViewCount.setText(getMovieViewerCount);
            textViewList.setText(getFavCount);

            if (getMovieWatchPlt.equals("Netflix")){
                imageViewAmazon.setVisibility(View.GONE);
            }else if (getMovieWatchPlt.equals("Amazon")){
                imageViewNetflix.setVisibility(View.GONE);
            }else{
                System.out.println("Both Of These");
            }

            youTubePlayer.enterFullScreen();
            youTubePlayer.exitFullScreen();

            youTubePlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    String youtubeCode = getMovieYoutube;
                    youTubePlayer.cueVideo(youtubeCode,0);
                    super.onReady(youTubePlayer);
                }
            });

            counterVisits(getMovieViewerCount,getMovieID);

        }else if (parseCheck == 3){

            String getMovieID= getIntent().getStringExtra("movieID");
            movieID = getMovieID;

            for (int i=0; i<documentID_array.size(); i++) {
                if (documentID_array.contains(movieID)) {
                    imageViewFav.setBackgroundResource(R.drawable.choosen_fav);
                    System.out.println("Document name : " + documentID_array.get(i));
                } else {
                    imageViewFav.setBackgroundResource(R.drawable.ic_favorite_heart);
                }
            }

            String getMovieImage = getIntent().getStringExtra("movieImage");
            String getMovieName = getIntent().getStringExtra("movieName");
            String getMovieStory = getIntent().getStringExtra("movieStory");
            String getMovieDirector = getIntent().getStringExtra("movieDirector");
            String getMovieYear = getIntent().getStringExtra("movieYear");
            String getMovieRating = getIntent().getStringExtra("movieRating");
            String getMovieRuntime = getIntent().getStringExtra("movieRuntime");
            String getMovieGenre = getIntent().getStringExtra("movieGenre");
            String getMovieChildGenre = getIntent().getStringExtra("movieChildGenre");
            String getMovieWatchPlt = getIntent().getStringExtra("movieWatchPlt");
            final String getMovieYoutube = getIntent().getStringExtra("movieYoutube");
            String getMovieViewerCount = getIntent().getStringExtra("movieViewerCount");
            String getFavCount= getIntent().getStringExtra("movieFavCount");

            String movieDownload = getMovieImage;

            movieFavCount = getFavCount;

            Picasso.get().load(movieDownload).into(imageViewMovie_Picture);
            textViewMovie_Name.setText(getMovieName);
            textViewRating.setText(getMovieRating + " IMDB");
            textViewGenre.setText(getMovieGenre +"," + getMovieChildGenre);
            textViewYear.setText(getMovieYear);

            if (getMovieGenre.equals("Movie")){
                textViewTime.setText(getMovieRuntime+" min");
            }else {
                textViewTime.setText(getMovieRuntime);
            }

            textViewStory_Area.setText(getMovieStory);
            textView_Director.setText(getMovieDirector);
            textViewCount.setText(getMovieViewerCount);
            textViewList.setText(getFavCount);

            if (getMovieWatchPlt.equals("Netflix")){
                imageViewAmazon.setVisibility(View.GONE);
            }else if (getMovieWatchPlt.equals("Amazon")){
                imageViewNetflix.setVisibility(View.GONE);
            }else{
                System.out.println("Both Of These");
            }

            youTubePlayer.enterFullScreen();
            youTubePlayer.exitFullScreen();

            youTubePlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    String youtubeCode = getMovieYoutube;
                    youTubePlayer.cueVideo(youtubeCode,0);
                    super.onReady(youTubePlayer);
                }
            });

            counterVisits(getMovieViewerCount,getMovieID);

        }else if (parseCheck == 4){

            String getMovieID= getIntent().getStringExtra("movieID");
            movieID = getMovieID;

            for (int i=0; i<documentID_array.size(); i++) {
                if (documentID_array.contains(movieID)) {
                    imageViewFav.setBackgroundResource(R.drawable.choosen_fav);
                    System.out.println("Document name : " + documentID_array.get(i));
                } else {
                    imageViewFav.setBackgroundResource(R.drawable.ic_favorite_heart);
                }
            }

            String getMovieImage = getIntent().getStringExtra("movieImage");
            String getMovieName = getIntent().getStringExtra("movieName");
            String getMovieStory = getIntent().getStringExtra("movieStory");
            String getMovieDirector = getIntent().getStringExtra("movieDirector");
            String getMovieYear = getIntent().getStringExtra("movieYear");
            String getMovieRating = getIntent().getStringExtra("movieRating");
            String getMovieRuntime = getIntent().getStringExtra("movieRuntime");
            String getMovieGenre = getIntent().getStringExtra("movieGenre");
            String getMovieChildGenre = getIntent().getStringExtra("movieChildGenre");
            String getMovieWatchPlt = getIntent().getStringExtra("movieWatchPlt");
            final String getMovieYoutube = getIntent().getStringExtra("movieYoutube");
            String getMovieViewerCount = getIntent().getStringExtra("movieViewerCount");
            String getFavCount= getIntent().getStringExtra("movieFavCount");

            String movieDownload = getMovieImage;

            movieFavCount = getFavCount;

            Picasso.get().load(movieDownload).into(imageViewMovie_Picture);
            textViewMovie_Name.setText(getMovieName);
            textViewRating.setText(getMovieRating + " IMDB");
            textViewGenre.setText(getMovieGenre +"," + getMovieChildGenre);
            textViewYear.setText(getMovieYear);

            if (getMovieGenre.equals("Movie")){
                textViewTime.setText(getMovieRuntime+" min");
            }else {
                textViewTime.setText(getMovieRuntime);
            }

            textViewStory_Area.setText(getMovieStory);
            textView_Director.setText(getMovieDirector);
            textViewCount.setText(getMovieViewerCount);
            textViewList.setText(getFavCount);

            if (getMovieWatchPlt.equals("Netflix")){
                imageViewAmazon.setVisibility(View.GONE);
            }else if (getMovieWatchPlt.equals("Amazon")){
                imageViewNetflix.setVisibility(View.GONE);
            }else{
                System.out.println("Both Of These");
            }

            youTubePlayer.enterFullScreen();
            youTubePlayer.exitFullScreen();

            youTubePlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    String youtubeCode = getMovieYoutube;
                    youTubePlayer.cueVideo(youtubeCode,0);
                    super.onReady(youTubePlayer);
                }
            });

            counterVisits(getMovieViewerCount,getMovieID);

        }else if (parseCheck == 5){

            String getMovieID= getIntent().getStringExtra("movieID");
            movieID = getMovieID;

            for (int i=0; i<documentID_array.size(); i++) {
                if (documentID_array.contains(movieID)) {
                    imageViewFav.setBackgroundResource(R.drawable.choosen_fav);
                    System.out.println("Document name : " + documentID_array.get(i));
                } else {
                    imageViewFav.setBackgroundResource(R.drawable.ic_favorite_heart);
                }
            }

            String getMovieImage = getIntent().getStringExtra("movieImage");
            String getMovieName = getIntent().getStringExtra("movieName");
            String getMovieStory = getIntent().getStringExtra("movieStory");
            String getMovieDirector = getIntent().getStringExtra("movieDirector");
            String getMovieYear = getIntent().getStringExtra("movieYear");
            String getMovieRating = getIntent().getStringExtra("movieRating");
            String getMovieRuntime = getIntent().getStringExtra("movieRuntime");
            String getMovieGenre = getIntent().getStringExtra("movieGenre");
            String getMovieChildGenre = getIntent().getStringExtra("movieChildGenre");
            String getMovieWatchPlt = getIntent().getStringExtra("movieWatchPlt");
            final String getMovieYoutube = getIntent().getStringExtra("movieYoutube");
            String getMovieViewerCount = getIntent().getStringExtra("movieViewerCount");
            String getFavCount= getIntent().getStringExtra("movieFavCount");

            String movieDownload = getMovieImage;

            movieFavCount = getFavCount;

            Picasso.get().load(movieDownload).into(imageViewMovie_Picture);
            textViewMovie_Name.setText(getMovieName);
            textViewRating.setText(getMovieRating + " IMDB");
            textViewGenre.setText(getMovieGenre +"," + getMovieChildGenre);
            textViewYear.setText(getMovieYear);

            if (getMovieGenre.equals("Movie")){
                textViewTime.setText(getMovieRuntime+" min");
            }else {
                textViewTime.setText(getMovieRuntime);
            }

            textViewStory_Area.setText(getMovieStory);
            textView_Director.setText(getMovieDirector);
            textViewCount.setText(getMovieViewerCount);
            textViewList.setText(getFavCount);

            if (getMovieWatchPlt.equals("Netflix")){
                imageViewAmazon.setVisibility(View.GONE);
            }else if (getMovieWatchPlt.equals("Amazon")){
                imageViewNetflix.setVisibility(View.GONE);
            }else{
                System.out.println("Both Of These");
            }

            youTubePlayer.enterFullScreen();
            youTubePlayer.exitFullScreen();

            youTubePlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    String youtubeCode = getMovieYoutube;
                    youTubePlayer.cueVideo(youtubeCode,0);
                    super.onReady(youTubePlayer);
                }
            });

            counterVisits(getMovieViewerCount,getMovieID);

        }else if (parseCheck == 6){

            String getMovieID= getIntent().getStringExtra("movieID");
            movieID = getMovieID;

            youTubePlayer.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.GONE);
            image_news.setVisibility(View.VISIBLE);
            textView_newsTitle.setVisibility(View.VISIBLE);


            for (int i=0; i<documentID_array.size(); i++) {
                if (documentID_array.contains(movieID)) {
                    imageViewFav.setBackgroundResource(R.drawable.choosen_fav);
                    System.out.println("Document name : " + documentID_array.get(i));
                } else {
                    imageViewFav.setBackgroundResource(R.drawable.ic_favorite_heart);
                }
            }

            String getMovieImage = getIntent().getStringExtra("movieImage");
            String getMovieName = getIntent().getStringExtra("movieName");
            String getMovieStory = getIntent().getStringExtra("movieStory");;
            String getMovieViewerCount = getIntent().getStringExtra("movieViewerCount");

            String movieDownload = getMovieImage;

            Picasso.get().load(movieDownload).into(image_news);
            textView_newsTitle.setText(getMovieName);

            textViewStory_Area.setText(getMovieStory);
            textViewCount.setText(getMovieViewerCount);

            youTubePlayer.enterFullScreen();
            youTubePlayer.exitFullScreen();

            counterVisitsNews(getMovieViewerCount,getMovieID);

        } else{
            System.out.println("not found");
        }

        imageViewFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favCounter++;

                if (favCounter % 2 == 0){

                    imageViewFav.setBackgroundResource(R.drawable.choosen_fav);

                    counterAddedFav(movieFavCount,movieID);
                    saveContents(movieID);
                    Toast.makeText(getApplicationContext(),movieID,Toast.LENGTH_LONG).show();

                }else{
                    imageViewFav.setBackgroundResource(R.drawable.ic_favorite_heart);

                    reduceAddedFav(movieFavCount,movieID);
                    deleteContents(movieID);
                }


            }
        });

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getActors();

    }

    public void getActors() {

        String itemID = movieID;

        DatabaseReference newReference = database.getReference("Contents");

        Query query = newReference.child(itemID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //System.out.println("dataSnapshot Children: " + dataSnapshot.getChildren());
                //System.out.println("dataSnapshot Value: " + dataSnapshot.getValue());
                //System.out.println("dataSnapshot Key: " + dataSnapshot.getKey());

                actorImageList.clear();
                actorNameList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    //System.out.println("data value: " + ds.getValue());

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    String actorPhoto = hashMap.get("actorPhoto");
                    String actorName = hashMap.get("actorName");


                    actorImageList.add(actorPhoto);
                    actorNameList.add(actorName);

                    actorsRVA = new ActorsRVA(getApplicationContext(),actorImageList,actorNameList);
                    recyclerViewActors.setAdapter(actorsRVA);
                    actorsRVA.notifyDataSetChanged();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void counterVisits(String getCount,String getDocId){
        int rate = Integer.valueOf(getCount);
        int plusRate = rate + 1;
        String sendRate = String.valueOf(plusRate);

        DocumentReference documentReference = firebaseFirestore.collection("Contents").document(getDocId);

        documentReference.update("clickCounter", sendRate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(getApplicationContext(),"ClickCounter Successfully Updated",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Please Check Your Network Connection",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void counterVisitsNews(String getCount,String getDocId){
        int rate = Integer.valueOf(getCount);
        int plusRate = rate + 1;
        String sendRate = String.valueOf(plusRate);

        DocumentReference documentReference = firebaseFirestore.collection("News").document(getDocId);

        documentReference.update("clickCounter", sendRate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(getApplicationContext(),"ClickCounter Successfully Updated",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Please Check Your Network Connection",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void counterAddedFav(String getCount,String getDocId){
        int rate = Integer.valueOf(getCount);
        int plusRate = rate + 1;
        String sendRate = String.valueOf(plusRate);

        DocumentReference documentReference = firebaseFirestore.collection("Contents").document(getDocId);

        documentReference.update("addedList", sendRate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(getApplicationContext(),"ClickCounter Successfully Updated",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Please Check Your Network Connection",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void reduceAddedFav(String getCount,String getDocId){
        int rate = Integer.valueOf(getCount);
        int plusRate = rate - 1;
        String sendRate = String.valueOf(plusRate);

        DocumentReference documentReference = firebaseFirestore.collection("Contents").document(getDocId);

        documentReference.update("addedList", sendRate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(getApplicationContext(),"ClickCounter Successfully Updated",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Please Check Your Network Connection",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void saveContents(String documentId){

        sqLiteDatabase = this.openOrCreateDatabase("MyFavContentsList",MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS myfavcontentslist(id INTEGER PRİMARY KEY, documentName VARCHAR)");

        String sendQuery = "INSERT INTO myfavcontentslist (documentName) VALUES (?)";
        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sendQuery);
        sqLiteStatement.bindString(1,documentId);
        sqLiteStatement.execute();
    }

    public void deleteContents(String documentId){

        sqLiteDatabase = this.openOrCreateDatabase("MyFavContentsList",MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS myfavcontentslist(id INTEGER PRİMARY KEY, documentName VARCHAR)");

        String sqlString = "DELETE FROM myfavcontentslist WHERE documentName = ?";
        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sqlString);
        sqLiteStatement.bindString(1,documentId);
        sqLiteStatement.execute();
    }

    public void queryFav() {

        try {
            SQLiteDatabase database = this.openOrCreateDatabase("MyFavContentsList", MODE_PRIVATE, null);

            Cursor cursor = database.rawQuery("SELECT * FROM myfavcontentslist", null);
            int documentIdX = cursor.getColumnIndex("documentName");
            int idIx = cursor.getColumnIndex("id");

            while (cursor.moveToNext()) {
                documentID_array.add(cursor.getString(documentIdX));
                id_array.add(cursor.getInt(idIx));


            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}