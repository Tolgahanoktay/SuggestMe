package com.tolgahanoktay.suggestme.Fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.tolgahanoktay.suggestme.MainActivity;
import com.tolgahanoktay.suggestme.R;
import com.tolgahanoktay.suggestme.RecyclerViewsAdapters.FragmentHomeRVA;
import com.tolgahanoktay.suggestme.SliderAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentHome extends Fragment {


    ViewPager viewPager;
    SliderAdapter sliderAdapter;
    LinearLayout linearLayout_dots;
    TextView[] slide_dots;
    TextView textView_tvShows,textView_movies,textView_myList,textView_changeableOne,textView_changeableTwo,textView_changeableThree;

    FragmentSeeItem fragmentSeeItem;


    RecyclerView recyclerView_popular,recyclerViews_mostClicks,recyclerViews_newest,recyclerViews_tensionAndfear,recyclerViews_action_adventure,recyclerViews_scienceFiction,
    recyclerViews_seeMovies,recyclerViews_realStory,recyclerViews_drama,recyclerViews_highScore,recyclerViews_tvShows,recyclerViews_changeableOne,recyclerViews_changeableTwo,recyclerViews_changeableThree;

    FragmentHomeRVA fragmentHomeRVA_Adapter;

    ArrayList<String> movieID_List;
    ArrayList<String> movieImageList;
    ArrayList<String> movieNameList;
    ArrayList<String> movieStoryList;
    ArrayList<String> movieDirectorList;
    ArrayList<String> movieYearList;
    ArrayList<String> movieRatingList;
    ArrayList<String> movieRuntimeList;
    ArrayList<String> movieGenreList;
    ArrayList<String> movieChildGenreList;
    ArrayList<String> movieWatchPltList;
    ArrayList<String> movieYoutubeCodeList;
    ArrayList<String> movieViewerList;
    ArrayList<String> movieFavCountList;

    int size = 0;

    private FirebaseFirestore firebaseFirestore;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        viewPager = view.findViewById(R.id.view_pager);
        linearLayout_dots = view.findViewById(R.id.slider_dots);

        textView_tvShows = view.findViewById(R.id.textView_tvShows);
        textView_movies = view.findViewById(R.id.textView_movies);
        textView_myList = view.findViewById(R.id.textView_myListFragment);


        textView_tvShows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                fragmentSeeItem = new FragmentSeeItem();
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_fragment,fragmentSeeItem).addToBackStack(null).commit();

                Bundle result = new Bundle();
                result.putString("categoryName", "Tv Shows");
                fragmentSeeItem.setArguments(result);
            }
        });

        textView_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                fragmentSeeItem = new FragmentSeeItem();
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_fragment,fragmentSeeItem).addToBackStack(null).commit();

                Bundle result = new Bundle();
                result.putString("categoryName", "Movies");
                fragmentSeeItem.setArguments(result);
            }
        });

        textView_myList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                fragmentSeeItem = new FragmentSeeItem();
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_fragment,fragmentSeeItem).addToBackStack(null).commit();

                Bundle result = new Bundle();
                result.putString("categoryName", "My List");
                fragmentSeeItem.setArguments(result);
            }
        });




        //addDots(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        movieID_List =new ArrayList<>();
        movieImageList =new ArrayList<>();
        movieNameList = new ArrayList<>();
        movieStoryList = new ArrayList<>();
        movieDirectorList = new ArrayList<>();
        movieYearList = new ArrayList<>();
        movieRatingList =new ArrayList<>();
        movieRuntimeList = new ArrayList<>();
        movieGenreList = new ArrayList<>();
        movieChildGenreList = new ArrayList<>();
        movieWatchPltList = new ArrayList<>();
        movieYoutubeCodeList = new ArrayList<>();
        movieViewerList = new ArrayList<>();
        movieFavCountList = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();


        recyclerView_popular = view.findViewById(R.id.recyclerViews_popular);
        recyclerView_popular.setHasFixedSize(true);
        recyclerView_popular.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        //recyclerView_myList.setPadding(5, 0, 10, 0);

        recyclerViews_mostClicks = view.findViewById(R.id.recyclerViews_mostClicks);
        recyclerViews_mostClicks.setHasFixedSize(true);
        recyclerViews_mostClicks.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        recyclerViews_newest = view.findViewById(R.id.recyclerViews_newest);
        recyclerViews_newest.setHasFixedSize(true);
        recyclerViews_newest.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        recyclerViews_tensionAndfear = view.findViewById(R.id.recyclerViews_tensionAndfear);
        recyclerViews_tensionAndfear.setHasFixedSize(true);
        recyclerViews_tensionAndfear.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        recyclerViews_action_adventure = view.findViewById(R.id.recyclerViews_action_adventure);
        recyclerViews_action_adventure.setHasFixedSize(true);
        recyclerViews_action_adventure.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        recyclerViews_scienceFiction = view.findViewById(R.id.recyclerViews_scienceFiction);
        recyclerViews_scienceFiction.setHasFixedSize(true);
        recyclerViews_scienceFiction.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        recyclerViews_seeMovies = view.findViewById(R.id.recyclerViews_seeMovies);
        recyclerViews_seeMovies.setHasFixedSize(true);
        recyclerViews_seeMovies.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        recyclerViews_realStory = view.findViewById(R.id.recyclerViews_realStory);
        recyclerViews_realStory.setHasFixedSize(true);
        recyclerViews_realStory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        recyclerViews_drama = view.findViewById(R.id.recyclerViews_drama);
        recyclerViews_drama.setHasFixedSize(true);
        recyclerViews_drama.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        recyclerViews_highScore = view.findViewById(R.id.recyclerViews_highScore);
        recyclerViews_highScore.setHasFixedSize(true);
        recyclerViews_highScore.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        recyclerViews_tvShows = view.findViewById(R.id.recyclerViews_tvShows);
        recyclerViews_tvShows.setHasFixedSize(true);
        recyclerViews_tvShows.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        recyclerViews_changeableOne = view.findViewById(R.id.recyclerViews_changeableOne);
        recyclerViews_changeableOne.setHasFixedSize(true);
        recyclerViews_changeableOne.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        recyclerViews_changeableTwo = view.findViewById(R.id.recyclerViews_changeableTwo);
        recyclerViews_changeableTwo.setHasFixedSize(true);
        recyclerViews_changeableTwo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        recyclerViews_changeableThree = view.findViewById(R.id.recyclerViews_changeableThree);
        recyclerViews_changeableThree.setHasFixedSize(true);
        recyclerViews_changeableThree.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));


        loadMovieHeader();
        uploadPopular();
        /*uploadMostClicks();
        uploadNewContent();
        uploadTensionFear();
        uploadActionAdventure();
        uploadScienceFiction();
        uploadMovies();
        uploadTvShows();
        uploadRealStory();
        uploadDrama();
        uploadTakenHighScore();*/

        return view;
    }

    public void uploadPopular(){
        CollectionReference collectionReference = firebaseFirestore.collection("Contents");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){
                    movieID_List.clear();
                    movieImageList.clear();
                    movieNameList.clear();
                    movieStoryList.clear();
                    movieDirectorList.clear();
                    movieYearList.clear();
                    movieRatingList.clear();
                    movieRuntimeList.clear();
                    movieGenreList.clear();
                    movieChildGenreList.clear();
                    movieWatchPltList.clear();
                    movieYoutubeCodeList.clear();
                    movieViewerList.clear();
                    movieFavCountList.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String movieImage = (String) data.get("movieImage");
                        String movieName = (String) data.get("movieName");
                        String movieStory = (String) data.get("movieStory");
                        String movieDirector = (String) data.get("movieDirector");
                        String movieYear = (String) data.get("movieYear");
                        String movieRating = (String) data.get("movieRating");
                        String movieRuntime = (String) data.get("movieRuntime");
                        String movieGenre = (String) data.get("mainCategory");
                        String movieChildGenre = (String) data.get("childCategory");
                        String movieWatchPlt = (String) data.get("watchPlatform");
                        String movieYoutubeCode = (String) data.get("trailerCode");
                        String movieViewerCount = (String) data.get("clickCounter");
                        String movieFavCount = (String) data.get("addedList");

                        movieID_List.add(documentName);
                        movieImageList.add(movieImage);
                        movieNameList.add(movieName);
                        movieStoryList.add(movieStory);
                        movieDirectorList.add(movieDirector);
                        movieYearList.add(movieYear);
                        movieRatingList.add(movieRating);
                        movieRuntimeList.add(movieRuntime);
                        movieGenreList.add(movieGenre);
                        movieChildGenreList.add(movieChildGenre);
                        movieWatchPltList.add(movieWatchPlt);
                        movieYoutubeCodeList.add(movieYoutubeCode);
                        movieViewerList.add(movieViewerCount);
                        movieFavCountList.add(movieFavCount);

                        fragmentHomeRVA_Adapter = new FragmentHomeRVA(getActivity(),movieID_List,movieImageList,movieNameList,movieStoryList,movieDirectorList,movieYearList,movieRatingList,movieRuntimeList,movieGenreList,movieChildGenreList,movieWatchPltList,movieYoutubeCodeList,movieViewerList,movieFavCountList);
                        recyclerView_popular.setAdapter(fragmentHomeRVA_Adapter);
                        fragmentHomeRVA_Adapter.notifyDataSetChanged();

                    }
                }
            }
        });

    }


    public void uploadMostClicks(){
        CollectionReference collectionReference = firebaseFirestore.collection("Movies");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    movieID_List.clear();
                    movieImageList.clear();
                    movieNameList.clear();
                    movieStoryList.clear();
                    movieDirectorList.clear();
                    movieYearList.clear();
                    movieRatingList.clear();
                    movieRuntimeList.clear();
                    movieGenreList.clear();
                    movieChildGenreList.clear();
                    movieWatchPltList.clear();
                    movieYoutubeCodeList.clear();
                    movieViewerList.clear();
                    movieFavCountList.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String movieImage = (String) data.get("movieImage");
                        String movieName = (String) data.get("movieName");
                        String movieStory = (String) data.get("movieStory");
                        String movieDirector = (String) data.get("movieDirector");
                        String movieYear = (String) data.get("movieYear");
                        String movieRating = (String) data.get("movieRating");
                        String movieRuntime = (String) data.get("movieRuntime");
                        String movieGenre = (String) data.get("mainCategory");
                        String movieChildGenre = (String) data.get("childCategory");
                        String movieWatchPlt = (String) data.get("watchPlatform");
                        String movieYoutubeCode = (String) data.get("trailerCode");
                        String movieViewerCount = (String) data.get("clickCounter");
                        String movieFavCount = (String) data.get("addedList");

                        movieID_List.add(documentName);
                        movieImageList.add(movieImage);
                        movieNameList.add(movieName);
                        movieStoryList.add(movieStory);
                        movieDirectorList.add(movieDirector);
                        movieYearList.add(movieYear);
                        movieRatingList.add(movieRating);
                        movieRuntimeList.add(movieRuntime);
                        movieGenreList.add(movieGenre);
                        movieChildGenreList.add(movieChildGenre);
                        movieWatchPltList.add(movieWatchPlt);
                        movieYoutubeCodeList.add(movieYoutubeCode);
                        movieViewerList.add(movieViewerCount);
                        movieFavCountList.add(movieFavCount);

                        fragmentHomeRVA_Adapter = new FragmentHomeRVA(getActivity(),movieID_List,movieImageList,movieNameList,movieStoryList,movieDirectorList,movieYearList,movieRatingList,movieRuntimeList,movieGenreList,movieChildGenreList,movieWatchPltList,movieYoutubeCodeList,movieViewerList,movieFavCountList);
                        recyclerView_popular.setAdapter(fragmentHomeRVA_Adapter);
                        fragmentHomeRVA_Adapter.notifyDataSetChanged();

                    }
                }
            }
        });

    }


    public void uploadNewContent(){
        CollectionReference collectionReference = firebaseFirestore.collection("Movies");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    movieID_List.clear();
                    movieImageList.clear();
                    movieNameList.clear();
                    movieStoryList.clear();
                    movieDirectorList.clear();
                    movieYearList.clear();
                    movieRatingList.clear();
                    movieRuntimeList.clear();
                    movieGenreList.clear();
                    movieChildGenreList.clear();
                    movieWatchPltList.clear();
                    movieYoutubeCodeList.clear();
                    movieViewerList.clear();
                    movieFavCountList.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String movieImage = (String) data.get("movieImage");
                        String movieName = (String) data.get("movieName");
                        String movieStory = (String) data.get("movieStory");
                        String movieDirector = (String) data.get("movieDirector");
                        String movieYear = (String) data.get("movieYear");
                        String movieRating = (String) data.get("movieRating");
                        String movieRuntime = (String) data.get("movieRuntime");
                        String movieGenre = (String) data.get("mainCategory");
                        String movieChildGenre = (String) data.get("childCategory");
                        String movieWatchPlt = (String) data.get("watchPlatform");
                        String movieYoutubeCode = (String) data.get("trailerCode");
                        String movieViewerCount = (String) data.get("clickCounter");
                        String movieFavCount = (String) data.get("addedList");

                        movieID_List.add(documentName);
                        movieImageList.add(movieImage);
                        movieNameList.add(movieName);
                        movieStoryList.add(movieStory);
                        movieDirectorList.add(movieDirector);
                        movieYearList.add(movieYear);
                        movieRatingList.add(movieRating);
                        movieRuntimeList.add(movieRuntime);
                        movieGenreList.add(movieGenre);
                        movieChildGenreList.add(movieChildGenre);
                        movieWatchPltList.add(movieWatchPlt);
                        movieYoutubeCodeList.add(movieYoutubeCode);
                        movieViewerList.add(movieViewerCount);
                        movieFavCountList.add(movieFavCount);

                        fragmentHomeRVA_Adapter = new FragmentHomeRVA(getActivity(),movieID_List,movieImageList,movieNameList,movieStoryList,movieDirectorList,movieYearList,movieRatingList,movieRuntimeList,movieGenreList,movieChildGenreList,movieWatchPltList,movieYoutubeCodeList,movieViewerList,movieFavCountList);
                        recyclerView_popular.setAdapter(fragmentHomeRVA_Adapter);
                        fragmentHomeRVA_Adapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }


    public void uploadTensionFear(){

        //tension fear

        CollectionReference collectionReference = firebaseFirestore.collection("Movies");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    movieID_List.clear();
                    movieImageList.clear();
                    movieNameList.clear();
                    movieStoryList.clear();
                    movieDirectorList.clear();
                    movieYearList.clear();
                    movieRatingList.clear();
                    movieRuntimeList.clear();
                    movieGenreList.clear();
                    movieChildGenreList.clear();
                    movieWatchPltList.clear();
                    movieYoutubeCodeList.clear();
                    movieViewerList.clear();
                    movieFavCountList.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String movieImage = (String) data.get("movieImage");
                        String movieName = (String) data.get("movieName");
                        String movieStory = (String) data.get("movieStory");
                        String movieDirector = (String) data.get("movieDirector");
                        String movieYear = (String) data.get("movieYear");
                        String movieRating = (String) data.get("movieRating");
                        String movieRuntime = (String) data.get("movieRuntime");
                        String movieGenre = (String) data.get("mainCategory");
                        String movieChildGenre = (String) data.get("childCategory");
                        String movieWatchPlt = (String) data.get("watchPlatform");
                        String movieYoutubeCode = (String) data.get("trailerCode");
                        String movieViewerCount = (String) data.get("clickCounter");
                        String movieFavCount = (String) data.get("addedList");

                        movieID_List.add(documentName);
                        movieImageList.add(movieImage);
                        movieNameList.add(movieName);
                        movieStoryList.add(movieStory);
                        movieDirectorList.add(movieDirector);
                        movieYearList.add(movieYear);
                        movieRatingList.add(movieRating);
                        movieRuntimeList.add(movieRuntime);
                        movieGenreList.add(movieGenre);
                        movieChildGenreList.add(movieChildGenre);
                        movieWatchPltList.add(movieWatchPlt);
                        movieYoutubeCodeList.add(movieYoutubeCode);
                        movieViewerList.add(movieViewerCount);
                        movieFavCountList.add(movieFavCount);

                        fragmentHomeRVA_Adapter = new FragmentHomeRVA(getActivity(),movieID_List,movieImageList,movieNameList,movieStoryList,movieDirectorList,movieYearList,movieRatingList,movieRuntimeList,movieGenreList,movieChildGenreList,movieWatchPltList,movieYoutubeCodeList,movieViewerList,movieFavCountList);
                        recyclerView_popular.setAdapter(fragmentHomeRVA_Adapter);
                        fragmentHomeRVA_Adapter.notifyDataSetChanged();

                    }
                }
            }
        });

    }


    public void uploadActionAdventure(){

        CollectionReference collectionReference = firebaseFirestore.collection("Movies");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    movieID_List.clear();
                    movieImageList.clear();
                    movieNameList.clear();
                    movieStoryList.clear();
                    movieDirectorList.clear();
                    movieYearList.clear();
                    movieRatingList.clear();
                    movieRuntimeList.clear();
                    movieGenreList.clear();
                    movieChildGenreList.clear();
                    movieWatchPltList.clear();
                    movieYoutubeCodeList.clear();
                    movieViewerList.clear();
                    movieFavCountList.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String movieImage = (String) data.get("movieImage");
                        String movieName = (String) data.get("movieName");
                        String movieStory = (String) data.get("movieStory");
                        String movieDirector = (String) data.get("movieDirector");
                        String movieYear = (String) data.get("movieYear");
                        String movieRating = (String) data.get("movieRating");
                        String movieRuntime = (String) data.get("movieRuntime");
                        String movieGenre = (String) data.get("mainCategory");
                        String movieChildGenre = (String) data.get("childCategory");
                        String movieWatchPlt = (String) data.get("watchPlatform");
                        String movieYoutubeCode = (String) data.get("trailerCode");
                        String movieViewerCount = (String) data.get("clickCounter");
                        String movieFavCount = (String) data.get("addedList");

                        movieID_List.add(documentName);
                        movieImageList.add(movieImage);
                        movieNameList.add(movieName);
                        movieStoryList.add(movieStory);
                        movieDirectorList.add(movieDirector);
                        movieYearList.add(movieYear);
                        movieRatingList.add(movieRating);
                        movieRuntimeList.add(movieRuntime);
                        movieGenreList.add(movieGenre);
                        movieChildGenreList.add(movieChildGenre);
                        movieWatchPltList.add(movieWatchPlt);
                        movieYoutubeCodeList.add(movieYoutubeCode);
                        movieViewerList.add(movieViewerCount);
                        movieFavCountList.add(movieFavCount);

                        fragmentHomeRVA_Adapter = new FragmentHomeRVA(getActivity(),movieID_List,movieImageList,movieNameList,movieStoryList,movieDirectorList,movieYearList,movieRatingList,movieRuntimeList,movieGenreList,movieChildGenreList,movieWatchPltList,movieYoutubeCodeList,movieViewerList,movieFavCountList);
                        recyclerView_popular.setAdapter(fragmentHomeRVA_Adapter);
                        fragmentHomeRVA_Adapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }


    public void uploadScienceFiction(){

        CollectionReference collectionReference = firebaseFirestore.collection("Movies");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    movieID_List.clear();
                    movieImageList.clear();
                    movieNameList.clear();
                    movieStoryList.clear();
                    movieDirectorList.clear();
                    movieYearList.clear();
                    movieRatingList.clear();
                    movieRuntimeList.clear();
                    movieGenreList.clear();
                    movieChildGenreList.clear();
                    movieWatchPltList.clear();
                    movieYoutubeCodeList.clear();
                    movieViewerList.clear();
                    movieFavCountList.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String movieImage = (String) data.get("movieImage");
                        String movieName = (String) data.get("movieName");
                        String movieStory = (String) data.get("movieStory");
                        String movieDirector = (String) data.get("movieDirector");
                        String movieYear = (String) data.get("movieYear");
                        String movieRating = (String) data.get("movieRating");
                        String movieRuntime = (String) data.get("movieRuntime");
                        String movieGenre = (String) data.get("mainCategory");
                        String movieChildGenre = (String) data.get("childCategory");
                        String movieWatchPlt = (String) data.get("watchPlatform");
                        String movieYoutubeCode = (String) data.get("trailerCode");
                        String movieViewerCount = (String) data.get("clickCounter");
                        String movieFavCount = (String) data.get("addedList");

                        movieID_List.add(documentName);
                        movieImageList.add(movieImage);
                        movieNameList.add(movieName);
                        movieStoryList.add(movieStory);
                        movieDirectorList.add(movieDirector);
                        movieYearList.add(movieYear);
                        movieRatingList.add(movieRating);
                        movieRuntimeList.add(movieRuntime);
                        movieGenreList.add(movieGenre);
                        movieChildGenreList.add(movieChildGenre);
                        movieWatchPltList.add(movieWatchPlt);
                        movieYoutubeCodeList.add(movieYoutubeCode);
                        movieViewerList.add(movieViewerCount);
                        movieFavCountList.add(movieFavCount);

                        fragmentHomeRVA_Adapter = new FragmentHomeRVA(getActivity(),movieID_List,movieImageList,movieNameList,movieStoryList,movieDirectorList,movieYearList,movieRatingList,movieRuntimeList,movieGenreList,movieChildGenreList,movieWatchPltList,movieYoutubeCodeList,movieViewerList,movieFavCountList);
                        recyclerView_popular.setAdapter(fragmentHomeRVA_Adapter);
                        fragmentHomeRVA_Adapter.notifyDataSetChanged();

                    }
                }
            }
        });

    }

    public void uploadMovies(){

        CollectionReference collectionReference = firebaseFirestore.collection("Movies");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    movieID_List.clear();
                    movieImageList.clear();
                    movieNameList.clear();
                    movieStoryList.clear();
                    movieDirectorList.clear();
                    movieYearList.clear();
                    movieRatingList.clear();
                    movieRuntimeList.clear();
                    movieGenreList.clear();
                    movieChildGenreList.clear();
                    movieWatchPltList.clear();
                    movieYoutubeCodeList.clear();
                    movieViewerList.clear();
                    movieFavCountList.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String movieImage = (String) data.get("movieImage");
                        String movieName = (String) data.get("movieName");
                        String movieStory = (String) data.get("movieStory");
                        String movieDirector = (String) data.get("movieDirector");
                        String movieYear = (String) data.get("movieYear");
                        String movieRating = (String) data.get("movieRating");
                        String movieRuntime = (String) data.get("movieRuntime");
                        String movieGenre = (String) data.get("mainCategory");
                        String movieChildGenre = (String) data.get("childCategory");
                        String movieWatchPlt = (String) data.get("watchPlatform");
                        String movieYoutubeCode = (String) data.get("trailerCode");
                        String movieViewerCount = (String) data.get("clickCounter");
                        String movieFavCount = (String) data.get("addedList");

                        movieID_List.add(documentName);
                        movieImageList.add(movieImage);
                        movieNameList.add(movieName);
                        movieStoryList.add(movieStory);
                        movieDirectorList.add(movieDirector);
                        movieYearList.add(movieYear);
                        movieRatingList.add(movieRating);
                        movieRuntimeList.add(movieRuntime);
                        movieGenreList.add(movieGenre);
                        movieChildGenreList.add(movieChildGenre);
                        movieWatchPltList.add(movieWatchPlt);
                        movieYoutubeCodeList.add(movieYoutubeCode);
                        movieViewerList.add(movieViewerCount);
                        movieFavCountList.add(movieFavCount);

                        fragmentHomeRVA_Adapter = new FragmentHomeRVA(getActivity(),movieID_List,movieImageList,movieNameList,movieStoryList,movieDirectorList,movieYearList,movieRatingList,movieRuntimeList,movieGenreList,movieChildGenreList,movieWatchPltList,movieYoutubeCodeList,movieViewerList,movieFavCountList);
                        recyclerView_popular.setAdapter(fragmentHomeRVA_Adapter);
                        fragmentHomeRVA_Adapter.notifyDataSetChanged();

                    }
                }
            }
        });

    }


    public void uploadTvShows(){

        //date and count

        CollectionReference collectionReference = firebaseFirestore.collection("Movies");

        collectionReference.orderBy("clickCounter", Query.Direction.DESCENDING).limit(20).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    movieID_List.clear();
                    movieImageList.clear();
                    movieNameList.clear();
                    movieStoryList.clear();
                    movieDirectorList.clear();
                    movieYearList.clear();
                    movieRatingList.clear();
                    movieRuntimeList.clear();
                    movieGenreList.clear();
                    movieChildGenreList.clear();
                    movieWatchPltList.clear();
                    movieYoutubeCodeList.clear();
                    movieViewerList.clear();
                    movieFavCountList.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String movieImage = (String) data.get("movieImage");
                        String movieName = (String) data.get("movieName");
                        String movieStory = (String) data.get("movieStory");
                        String movieDirector = (String) data.get("movieDirector");
                        String movieYear = (String) data.get("movieYear");
                        String movieRating = (String) data.get("movieRating");
                        String movieRuntime = (String) data.get("movieRuntime");
                        String movieGenre = (String) data.get("mainCategory");
                        String movieChildGenre = (String) data.get("childCategory");
                        String movieWatchPlt = (String) data.get("watchPlatform");
                        String movieYoutubeCode = (String) data.get("trailerCode");
                        String movieViewerCount = (String) data.get("clickCounter");
                        String movieFavCount = (String) data.get("addedList");

                        movieID_List.add(documentName);
                        movieImageList.add(movieImage);
                        movieNameList.add(movieName);
                        movieStoryList.add(movieStory);
                        movieDirectorList.add(movieDirector);
                        movieYearList.add(movieYear);
                        movieRatingList.add(movieRating);
                        movieRuntimeList.add(movieRuntime);
                        movieGenreList.add(movieGenre);
                        movieChildGenreList.add(movieChildGenre);
                        movieWatchPltList.add(movieWatchPlt);
                        movieYoutubeCodeList.add(movieYoutubeCode);
                        movieViewerList.add(movieViewerCount);
                        movieFavCountList.add(movieFavCount);

                        fragmentHomeRVA_Adapter = new FragmentHomeRVA(getActivity(),movieID_List,movieImageList,movieNameList,movieStoryList,movieDirectorList,movieYearList,movieRatingList,movieRuntimeList,movieGenreList,movieChildGenreList,movieWatchPltList,movieYoutubeCodeList,movieViewerList,movieFavCountList);
                        recyclerView_popular.setAdapter(fragmentHomeRVA_Adapter);
                        fragmentHomeRVA_Adapter.notifyDataSetChanged();


                    }
                }
            }
        });

    }

    public void uploadRealStory(){
        CollectionReference collectionReference = firebaseFirestore.collection("Movies");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    movieID_List.clear();
                    movieImageList.clear();
                    movieNameList.clear();
                    movieStoryList.clear();
                    movieDirectorList.clear();
                    movieYearList.clear();
                    movieRatingList.clear();
                    movieRuntimeList.clear();
                    movieGenreList.clear();
                    movieChildGenreList.clear();
                    movieWatchPltList.clear();
                    movieYoutubeCodeList.clear();
                    movieViewerList.clear();
                    movieFavCountList.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String movieImage = (String) data.get("movieImage");
                        String movieName = (String) data.get("movieName");
                        String movieStory = (String) data.get("movieStory");
                        String movieDirector = (String) data.get("movieDirector");
                        String movieYear = (String) data.get("movieYear");
                        String movieRating = (String) data.get("movieRating");
                        String movieRuntime = (String) data.get("movieRuntime");
                        String movieGenre = (String) data.get("mainCategory");
                        String movieChildGenre = (String) data.get("childCategory");
                        String movieWatchPlt = (String) data.get("watchPlatform");
                        String movieYoutubeCode = (String) data.get("trailerCode");
                        String movieViewerCount = (String) data.get("clickCounter");
                        String movieFavCount = (String) data.get("addedList");

                        movieID_List.add(documentName);
                        movieImageList.add(movieImage);
                        movieNameList.add(movieName);
                        movieStoryList.add(movieStory);
                        movieDirectorList.add(movieDirector);
                        movieYearList.add(movieYear);
                        movieRatingList.add(movieRating);
                        movieRuntimeList.add(movieRuntime);
                        movieGenreList.add(movieGenre);
                        movieChildGenreList.add(movieChildGenre);
                        movieWatchPltList.add(movieWatchPlt);
                        movieYoutubeCodeList.add(movieYoutubeCode);
                        movieViewerList.add(movieViewerCount);
                        movieFavCountList.add(movieFavCount);

                        fragmentHomeRVA_Adapter = new FragmentHomeRVA(getActivity(),movieID_List,movieImageList,movieNameList,movieStoryList,movieDirectorList,movieYearList,movieRatingList,movieRuntimeList,movieGenreList,movieChildGenreList,movieWatchPltList,movieYoutubeCodeList,movieViewerList,movieFavCountList);
                        recyclerView_popular.setAdapter(fragmentHomeRVA_Adapter);
                        fragmentHomeRVA_Adapter.notifyDataSetChanged();


                    }
                }
            }
        });

    }

    public void uploadDrama(){
        CollectionReference collectionReference = firebaseFirestore.collection("Movies");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    movieID_List.clear();
                    movieImageList.clear();
                    movieNameList.clear();
                    movieStoryList.clear();
                    movieDirectorList.clear();
                    movieYearList.clear();
                    movieRatingList.clear();
                    movieRuntimeList.clear();
                    movieGenreList.clear();
                    movieChildGenreList.clear();
                    movieWatchPltList.clear();
                    movieYoutubeCodeList.clear();
                    movieViewerList.clear();
                    movieFavCountList.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String movieImage = (String) data.get("movieImage");
                        String movieName = (String) data.get("movieName");
                        String movieStory = (String) data.get("movieStory");
                        String movieDirector = (String) data.get("movieDirector");
                        String movieYear = (String) data.get("movieYear");
                        String movieRating = (String) data.get("movieRating");
                        String movieRuntime = (String) data.get("movieRuntime");
                        String movieGenre = (String) data.get("mainCategory");
                        String movieChildGenre = (String) data.get("childCategory");
                        String movieWatchPlt = (String) data.get("watchPlatform");
                        String movieYoutubeCode = (String) data.get("trailerCode");
                        String movieViewerCount = (String) data.get("clickCounter");
                        String movieFavCount = (String) data.get("addedList");

                        movieID_List.add(documentName);
                        movieImageList.add(movieImage);
                        movieNameList.add(movieName);
                        movieStoryList.add(movieStory);
                        movieDirectorList.add(movieDirector);
                        movieYearList.add(movieYear);
                        movieRatingList.add(movieRating);
                        movieRuntimeList.add(movieRuntime);
                        movieGenreList.add(movieGenre);
                        movieChildGenreList.add(movieChildGenre);
                        movieWatchPltList.add(movieWatchPlt);
                        movieYoutubeCodeList.add(movieYoutubeCode);
                        movieViewerList.add(movieViewerCount);
                        movieFavCountList.add(movieFavCount);

                        fragmentHomeRVA_Adapter = new FragmentHomeRVA(getActivity(),movieID_List,movieImageList,movieNameList,movieStoryList,movieDirectorList,movieYearList,movieRatingList,movieRuntimeList,movieGenreList,movieChildGenreList,movieWatchPltList,movieYoutubeCodeList,movieViewerList,movieFavCountList);
                        recyclerView_popular.setAdapter(fragmentHomeRVA_Adapter);
                        fragmentHomeRVA_Adapter.notifyDataSetChanged();


                    }
                }
            }
        });

    }


    public void uploadTakenHighScore(){
        CollectionReference collectionReference = firebaseFirestore.collection("Movies");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    movieID_List.clear();
                    movieImageList.clear();
                    movieNameList.clear();
                    movieStoryList.clear();
                    movieDirectorList.clear();
                    movieYearList.clear();
                    movieRatingList.clear();
                    movieRuntimeList.clear();
                    movieGenreList.clear();
                    movieChildGenreList.clear();
                    movieWatchPltList.clear();
                    movieYoutubeCodeList.clear();
                    movieViewerList.clear();
                    movieFavCountList.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String movieImage = (String) data.get("movieImage");
                        String movieName = (String) data.get("movieName");
                        String movieStory = (String) data.get("movieStory");
                        String movieDirector = (String) data.get("movieDirector");
                        String movieYear = (String) data.get("movieYear");
                        String movieRating = (String) data.get("movieRating");
                        String movieRuntime = (String) data.get("movieRuntime");
                        String movieGenre = (String) data.get("mainCategory");
                        String movieChildGenre = (String) data.get("childCategory");
                        String movieWatchPlt = (String) data.get("watchPlatform");
                        String movieYoutubeCode = (String) data.get("trailerCode");
                        String movieViewerCount = (String) data.get("clickCounter");
                        String movieFavCount = (String) data.get("addedList");

                        movieID_List.add(documentName);
                        movieImageList.add(movieImage);
                        movieNameList.add(movieName);
                        movieStoryList.add(movieStory);
                        movieDirectorList.add(movieDirector);
                        movieYearList.add(movieYear);
                        movieRatingList.add(movieRating);
                        movieRuntimeList.add(movieRuntime);
                        movieGenreList.add(movieGenre);
                        movieChildGenreList.add(movieChildGenre);
                        movieWatchPltList.add(movieWatchPlt);
                        movieYoutubeCodeList.add(movieYoutubeCode);
                        movieViewerList.add(movieViewerCount);
                        movieFavCountList.add(movieFavCount);

                        fragmentHomeRVA_Adapter = new FragmentHomeRVA(getActivity(),movieID_List,movieImageList,movieNameList,movieStoryList,movieDirectorList,movieYearList,movieRatingList,movieRuntimeList,movieGenreList,movieChildGenreList,movieWatchPltList,movieYoutubeCodeList,movieViewerList,movieFavCountList);
                        recyclerView_popular.setAdapter(fragmentHomeRVA_Adapter);
                        fragmentHomeRVA_Adapter.notifyDataSetChanged();


                    }
                }
            }
        });

    }


    public void uploadChangeableCategoryOne(){
        CollectionReference collectionReference = firebaseFirestore.collection("Movies");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    movieID_List.clear();
                    movieImageList.clear();
                    movieNameList.clear();
                    movieStoryList.clear();
                    movieDirectorList.clear();
                    movieYearList.clear();
                    movieRatingList.clear();
                    movieRuntimeList.clear();
                    movieGenreList.clear();
                    movieChildGenreList.clear();
                    movieWatchPltList.clear();
                    movieYoutubeCodeList.clear();
                    movieViewerList.clear();
                    movieFavCountList.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String movieImage = (String) data.get("movieImage");
                        String movieName = (String) data.get("movieName");
                        String movieStory = (String) data.get("movieStory");
                        String movieDirector = (String) data.get("movieDirector");
                        String movieYear = (String) data.get("movieYear");
                        String movieRating = (String) data.get("movieRating");
                        String movieRuntime = (String) data.get("movieRuntime");
                        String movieGenre = (String) data.get("mainCategory");
                        String movieChildGenre = (String) data.get("childCategory");
                        String movieWatchPlt = (String) data.get("watchPlatform");
                        String movieYoutubeCode = (String) data.get("trailerCode");
                        String movieViewerCount = (String) data.get("clickCounter");
                        String movieFavCount = (String) data.get("addedList");

                        movieID_List.add(documentName);
                        movieImageList.add(movieImage);
                        movieNameList.add(movieName);
                        movieStoryList.add(movieStory);
                        movieDirectorList.add(movieDirector);
                        movieYearList.add(movieYear);
                        movieRatingList.add(movieRating);
                        movieRuntimeList.add(movieRuntime);
                        movieGenreList.add(movieGenre);
                        movieChildGenreList.add(movieChildGenre);
                        movieWatchPltList.add(movieWatchPlt);
                        movieYoutubeCodeList.add(movieYoutubeCode);
                        movieViewerList.add(movieViewerCount);
                        movieFavCountList.add(movieFavCount);

                        fragmentHomeRVA_Adapter = new FragmentHomeRVA(getActivity(),movieID_List,movieImageList,movieNameList,movieStoryList,movieDirectorList,movieYearList,movieRatingList,movieRuntimeList,movieGenreList,movieChildGenreList,movieWatchPltList,movieYoutubeCodeList,movieViewerList,movieFavCountList);
                        recyclerView_popular.setAdapter(fragmentHomeRVA_Adapter);
                        fragmentHomeRVA_Adapter.notifyDataSetChanged();


                    }
                }
            }
        });

    }


    public void uploadChangeableCategoryTwo(){
        CollectionReference collectionReference = firebaseFirestore.collection("Movies");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    movieID_List.clear();
                    movieImageList.clear();
                    movieNameList.clear();
                    movieStoryList.clear();
                    movieDirectorList.clear();
                    movieYearList.clear();
                    movieRatingList.clear();
                    movieRuntimeList.clear();
                    movieGenreList.clear();
                    movieChildGenreList.clear();
                    movieWatchPltList.clear();
                    movieYoutubeCodeList.clear();
                    movieViewerList.clear();
                    movieFavCountList.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String movieImage = (String) data.get("movieImage");
                        String movieName = (String) data.get("movieName");
                        String movieStory = (String) data.get("movieStory");
                        String movieDirector = (String) data.get("movieDirector");
                        String movieYear = (String) data.get("movieYear");
                        String movieRating = (String) data.get("movieRating");
                        String movieRuntime = (String) data.get("movieRuntime");
                        String movieGenre = (String) data.get("mainCategory");
                        String movieChildGenre = (String) data.get("childCategory");
                        String movieWatchPlt = (String) data.get("watchPlatform");
                        String movieYoutubeCode = (String) data.get("trailerCode");
                        String movieViewerCount = (String) data.get("clickCounter");
                        String movieFavCount = (String) data.get("addedList");

                        movieID_List.add(documentName);
                        movieImageList.add(movieImage);
                        movieNameList.add(movieName);
                        movieStoryList.add(movieStory);
                        movieDirectorList.add(movieDirector);
                        movieYearList.add(movieYear);
                        movieRatingList.add(movieRating);
                        movieRuntimeList.add(movieRuntime);
                        movieGenreList.add(movieGenre);
                        movieChildGenreList.add(movieChildGenre);
                        movieWatchPltList.add(movieWatchPlt);
                        movieYoutubeCodeList.add(movieYoutubeCode);
                        movieViewerList.add(movieViewerCount);
                        movieFavCountList.add(movieFavCount);

                        fragmentHomeRVA_Adapter = new FragmentHomeRVA(getActivity(),movieID_List,movieImageList,movieNameList,movieStoryList,movieDirectorList,movieYearList,movieRatingList,movieRuntimeList,movieGenreList,movieChildGenreList,movieWatchPltList,movieYoutubeCodeList,movieViewerList,movieFavCountList);
                        recyclerView_popular.setAdapter(fragmentHomeRVA_Adapter);
                        fragmentHomeRVA_Adapter.notifyDataSetChanged();


                    }
                }
            }
        });

    }


    public void uploadChangeableCategoryThree(){
        CollectionReference collectionReference = firebaseFirestore.collection("Movies");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    movieID_List.clear();
                    movieImageList.clear();
                    movieNameList.clear();
                    movieStoryList.clear();
                    movieDirectorList.clear();
                    movieYearList.clear();
                    movieRatingList.clear();
                    movieRuntimeList.clear();
                    movieGenreList.clear();
                    movieChildGenreList.clear();
                    movieWatchPltList.clear();
                    movieYoutubeCodeList.clear();
                    movieViewerList.clear();
                    movieFavCountList.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String movieImage = (String) data.get("movieImage");
                        String movieName = (String) data.get("movieName");
                        String movieStory = (String) data.get("movieStory");
                        String movieDirector = (String) data.get("movieDirector");
                        String movieYear = (String) data.get("movieYear");
                        String movieRating = (String) data.get("movieRating");
                        String movieRuntime = (String) data.get("movieRuntime");
                        String movieGenre = (String) data.get("mainCategory");
                        String movieChildGenre = (String) data.get("childCategory");
                        String movieWatchPlt = (String) data.get("watchPlatform");
                        String movieYoutubeCode = (String) data.get("trailerCode");
                        String movieViewerCount = (String) data.get("clickCounter");
                        String movieFavCount = (String) data.get("addedList");

                        movieID_List.add(documentName);
                        movieImageList.add(movieImage);
                        movieNameList.add(movieName);
                        movieStoryList.add(movieStory);
                        movieDirectorList.add(movieDirector);
                        movieYearList.add(movieYear);
                        movieRatingList.add(movieRating);
                        movieRuntimeList.add(movieRuntime);
                        movieGenreList.add(movieGenre);
                        movieChildGenreList.add(movieChildGenre);
                        movieWatchPltList.add(movieWatchPlt);
                        movieYoutubeCodeList.add(movieYoutubeCode);
                        movieViewerList.add(movieViewerCount);
                        movieFavCountList.add(movieFavCount);

                        fragmentHomeRVA_Adapter = new FragmentHomeRVA(getActivity(),movieID_List,movieImageList,movieNameList,movieStoryList,movieDirectorList,movieYearList,movieRatingList,movieRuntimeList,movieGenreList,movieChildGenreList,movieWatchPltList,movieYoutubeCodeList,movieViewerList,movieFavCountList);
                        recyclerView_popular.setAdapter(fragmentHomeRVA_Adapter);
                        fragmentHomeRVA_Adapter.notifyDataSetChanged();


                    }
                }
            }
        });

    }






    private void addDots(int position){

        slide_dots = new TextView[size];
        linearLayout_dots.removeAllViews();
        for (int i = 0; i < slide_dots.length; i++){
            slide_dots[i] = new TextView(getActivity());
            slide_dots[i].setText(Html.fromHtml("&#8226;"));
            slide_dots[i].setTextColor(getResources().getColor(R.color.sliderOffline));
            slide_dots[i].setTextSize(20);

            linearLayout_dots.addView(slide_dots[i]);
        }

        if (slide_dots.length > 0 ){

            slide_dots[position].setTextColor(getResources().getColor(R.color.sliderOnline));
        }

    }



    public void loadMovieHeader(){
        CollectionReference collectionReference = firebaseFirestore.collection("Contents");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){


                    movieID_List.clear();
                    movieImageList.clear();
                    movieNameList.clear();
                    movieStoryList.clear();
                    movieDirectorList.clear();
                    movieYearList.clear();
                    movieRatingList.clear();
                    movieRuntimeList.clear();
                    movieGenreList.clear();
                    movieChildGenreList.clear();
                    movieWatchPltList.clear();
                    movieYoutubeCodeList.clear();
                    movieViewerList.clear();
                    movieFavCountList.clear();


                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String movieImage = (String) data.get("movieImage");
                        String movieName = (String) data.get("movieName");
                        String movieStory = (String) data.get("movieStory");
                        String movieDirector = (String) data.get("movieDirector");
                        String movieYear = (String) data.get("movieYear");
                        String movieRating = (String) data.get("movieRating");
                        String movieRuntime = (String) data.get("movieRuntime");
                        String movieGenre = (String) data.get("mainCategory");
                        String movieChildGenre = (String) data.get("childCategory");
                        String movieWatchPlt = (String) data.get("watchPlatform");
                        String movieYoutubeCode = (String) data.get("trailerCode");
                        String movieViewerCount = (String) data.get("clickCounter");
                        String movieFavCount = (String) data.get("addedList");


                        movieID_List.add(documentName);
                        movieImageList.add(movieImage);
                        movieNameList.add(movieName);
                        movieStoryList.add(movieStory);
                        movieDirectorList.add(movieDirector);
                        movieYearList.add(movieYear);
                        movieRatingList.add(movieRating);
                        movieRuntimeList.add(movieRuntime);
                        movieGenreList.add(movieGenre);
                        movieChildGenreList.add(movieChildGenre);
                        movieWatchPltList.add(movieWatchPlt);
                        movieYoutubeCodeList.add(movieYoutubeCode);
                        movieViewerList.add(movieViewerCount);
                        movieFavCountList.add(movieFavCount);

                        size = movieImageList.size();

                        sliderAdapter = new SliderAdapter(getActivity(),movieID_List,movieImageList,movieNameList,movieStoryList,movieDirectorList,movieYearList,movieRatingList,movieRuntimeList,movieGenreList,
                                movieChildGenreList,movieWatchPltList,movieYoutubeCodeList,movieViewerList,movieFavCountList);
                        viewPager.setAdapter(sliderAdapter);

                    }
                }
            }
        });

    }


}
