package com.tolgahanoktay.suggestme.Fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tolgahanoktay.suggestme.R;
import com.tolgahanoktay.suggestme.RecyclerViewsAdapters.FavoriesRVA;
import com.tolgahanoktay.suggestme.RecyclerViewsAdapters.SeeItemRVA;
import com.tolgahanoktay.suggestme.SliderAdapter;
import com.tolgahanoktay.suggestme.SliderAdapterTwo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class FragmentSeeItem extends Fragment {

    SeeItemRVA seeItemRVA;
    RecyclerView recyclerView,recyclerView2;
    Spinner spinner;

    ViewPager viewPager;
    TextView[] slide_dots;
    SliderAdapterTwo sliderAdapter;
    LinearLayout linearLayout_dots,linearLayoutRCV1,linearLayoutRCV2,linearLayoutViewPager;
    ImageView imageView;

    int size = 0;
    String mainCategory = "";
    //String cursorSelected = "";

    LinearLayout linear_Category;
    TextView writeCategory;

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

    FavoriesRVA favoriesRVA;
    ArrayList<String> documentID_array;
    ArrayList<Integer> id_array;

    FirebaseFirestore firebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_see_item,container,false);

        viewPager = view.findViewById(R.id.view_pager_item);
        imageView = view.findViewById(R.id.image_spinnerCategory);
        linearLayout_dots = view.findViewById(R.id.slider_dots_item);
        linearLayoutRCV1 = view.findViewById(R.id.linear_rcv1);
        linearLayoutRCV2 = view.findViewById(R.id.linear_rcv2);
        linearLayoutViewPager = view.findViewById(R.id.linear_viewPager);


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


        documentID_array = new ArrayList<>();
        id_array = new ArrayList<>();


        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView= view.findViewById(R.id.recycler_view_showItems);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        linear_Category = view.findViewById(R.id.linear_bundleCategory);
        writeCategory = view.findViewById(R.id.textView_writeBundleCategory);


        recyclerView2 = view.findViewById(R.id.recycler_view_myList);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));


        spinner = view.findViewById(R.id.spinner_item);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String getCategory = bundle.getString("categoryName");
            if (getCategory.equals("Tv Shows")) {

                mainCategory = "Tv Show";

                writeCategory.setText("Tv Shows");
                linearLayoutRCV2.setVisibility(View.GONE);

                loadMovieHeader(mainCategory);
                uploadContents(mainCategory);

                String[] arrayOne = {"Category","Action", "Anime", "Science Fiction", "Kid and Family", "Drama", "Fantasy", "Triller", "Classics", "Comedy", "Horror", "Musicals", "Award-winning productions",
                        "Romance", "Sports", "Stand up"};

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),R.layout.custom_spinner_dropdown,arrayOne);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);


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


            } else if (getCategory.equals("Movies")) {

                mainCategory = "Movie";

                writeCategory.setText("Movies");
                linearLayoutRCV2.setVisibility(View.GONE);

                String[] arrayTwo = {"Action", "Anime", "Science Fiction", "Kid and Family", "Drama", "Fantasy", "Triller", "Classics", "Comedy", "Horror", "Youth","Romance", "Crime","Mystery"};

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),R.layout.custom_spinner_dropdown,arrayTwo);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);


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

                loadMovieHeader(mainCategory);
                uploadContents(mainCategory);

            } else if (getCategory.equals("My List")) {

                mainCategory = "My List";
                writeCategory.setText("My List");

                linearLayoutViewPager.setVisibility(View.GONE);
                linearLayoutRCV1.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);

                getData();

                Iterator<String> iterator = documentID_array.iterator();

                while (iterator.hasNext()){
                    loadContents(iterator.next());
                }

            }else {
                System.out.println("Not Found");
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String cursorSelected = adapterView.getItemAtPosition(position).toString();
                uploadSpesificContent(mainCategory,cursorSelected);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        return view;
    }




    public void uploadContents(String category){
        CollectionReference collectionReference = firebaseFirestore.collection("Contents");

        collectionReference.whereEqualTo("mainCategory",category).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                ArrayList<String> movieID_HeaderList = new ArrayList<>();
                ArrayList<String> movieImageHeaderList = new ArrayList<>();
                ArrayList<String> movieNameHeaderList = new ArrayList<>();
                ArrayList<String> movieStoryHeaderList = new ArrayList<>();
                ArrayList<String> movieDirectorHeaderList = new ArrayList<>();
                ArrayList<String> movieYearHeaderList = new ArrayList<>();
                ArrayList<String> movieRatingHeaderList = new ArrayList<>();
                ArrayList<String> movieRuntimeHeaderList = new ArrayList<>();
                ArrayList<String> movieGenreHeaderList = new ArrayList<>();
                ArrayList<String> movieChildGenreHeaderList = new ArrayList<>();
                ArrayList<String> movieWatchPltHeaderList = new ArrayList<>();
                ArrayList<String> movieYoutubeCodeHeaderList = new ArrayList<>();
                ArrayList<String> movieViewerHeaderList = new ArrayList<>();
                ArrayList<String> movieFavCountHeaderList = new ArrayList<>();

                if (queryDocumentSnapshots != null){

                    movieID_HeaderList.clear();
                    movieImageHeaderList.clear();
                    movieNameHeaderList.clear();
                    movieStoryHeaderList.clear();
                    movieDirectorHeaderList.clear();
                    movieYearHeaderList.clear();
                    movieRatingHeaderList.clear();
                    movieRuntimeHeaderList.clear();
                    movieGenreHeaderList.clear();
                    movieChildGenreHeaderList.clear();
                    movieWatchPltHeaderList.clear();
                    movieYoutubeCodeHeaderList.clear();
                    movieViewerHeaderList.clear();
                    movieFavCountHeaderList.clear();

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

                        movieID_HeaderList.add(documentName);
                        movieImageHeaderList.add(movieImage);
                        movieNameHeaderList.add(movieName);
                        movieStoryHeaderList.add(movieStory);
                        movieDirectorHeaderList.add(movieDirector);
                        movieYearHeaderList.add(movieYear);
                        movieRatingHeaderList.add(movieRating);
                        movieRuntimeHeaderList.add(movieRuntime);
                        movieGenreHeaderList.add(movieGenre);
                        movieChildGenreHeaderList.add(movieChildGenre);
                        movieWatchPltHeaderList.add(movieWatchPlt);
                        movieYoutubeCodeHeaderList.add(movieYoutubeCode);
                        movieViewerHeaderList.add(movieViewerCount);
                        movieFavCountHeaderList.add(movieFavCount);

                        seeItemRVA =  new SeeItemRVA(getActivity(),movieID_HeaderList,movieImageHeaderList,movieNameHeaderList,movieStoryHeaderList,movieDirectorHeaderList,movieYearHeaderList,movieRatingHeaderList,
                                movieRuntimeHeaderList,movieGenreHeaderList, movieChildGenreHeaderList,movieWatchPltHeaderList,movieYoutubeCodeHeaderList,movieViewerHeaderList,movieFavCountHeaderList);
                        recyclerView.setAdapter(seeItemRVA);
                        seeItemRVA.notifyDataSetChanged();

                    }
                }
            }
        });

    }

    public void uploadSpesificContent(String mainCategory,String childCategory){

        CollectionReference collectionReference = firebaseFirestore.collection("Contents");

        collectionReference.whereEqualTo("mainCategory",mainCategory).whereEqualTo("childCategory",childCategory).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

                        seeItemRVA =  new SeeItemRVA(getActivity(),movieID_List,movieImageList,movieNameList,movieStoryList,movieDirectorList,movieYearList,movieRatingList,movieRuntimeList,movieGenreList,movieChildGenreList,movieWatchPltList,movieYoutubeCodeList,movieViewerList,movieFavCountList);
                        recyclerView.setAdapter(seeItemRVA);
                        seeItemRVA.notifyDataSetChanged();

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

    public void loadMovieHeader(String category){
        CollectionReference collectionReference = firebaseFirestore.collection("Contents");

        collectionReference.whereEqualTo("mainCategory",category).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                ArrayList<String> movieID_HeaderList = new ArrayList<>();
                ArrayList<String> movieImageHeaderList = new ArrayList<>();
                ArrayList<String> movieNameHeaderList = new ArrayList<>();
                ArrayList<String> movieStoryHeaderList = new ArrayList<>();
                ArrayList<String> movieDirectorHeaderList = new ArrayList<>();
                ArrayList<String> movieYearHeaderList = new ArrayList<>();
                ArrayList<String> movieRatingHeaderList = new ArrayList<>();
                ArrayList<String> movieRuntimeHeaderList = new ArrayList<>();
                ArrayList<String> movieGenreHeaderList = new ArrayList<>();
                ArrayList<String> movieChildGenreHeaderList = new ArrayList<>();
                ArrayList<String> movieWatchPltHeaderList = new ArrayList<>();
                ArrayList<String> movieYoutubeCodeHeaderList = new ArrayList<>();
                ArrayList<String> movieViewerHeaderList = new ArrayList<>();
                ArrayList<String> movieFavCountHeaderList = new ArrayList<>();



                if (queryDocumentSnapshots != null){

                    movieID_HeaderList.clear();
                    movieImageHeaderList.clear();
                    movieNameHeaderList.clear();
                    movieStoryHeaderList.clear();
                    movieDirectorHeaderList.clear();
                    movieYearHeaderList.clear();
                    movieRatingHeaderList.clear();
                    movieRuntimeHeaderList.clear();
                    movieGenreHeaderList.clear();
                    movieChildGenreHeaderList.clear();
                    movieWatchPltHeaderList.clear();
                    movieYoutubeCodeHeaderList.clear();
                    movieViewerHeaderList.clear();
                    movieFavCountHeaderList.clear();


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


                        movieID_HeaderList.add(documentName);
                        movieImageHeaderList.add(movieImage);
                        movieNameHeaderList.add(movieName);
                        movieStoryHeaderList.add(movieStory);
                        movieDirectorHeaderList.add(movieDirector);
                        movieYearHeaderList.add(movieYear);
                        movieRatingHeaderList.add(movieRating);
                        movieRuntimeHeaderList.add(movieRuntime);
                        movieGenreHeaderList.add(movieGenre);
                        movieChildGenreHeaderList.add(movieChildGenre);
                        movieWatchPltHeaderList.add(movieWatchPlt);
                        movieYoutubeCodeHeaderList.add(movieYoutubeCode);
                        movieViewerHeaderList.add(movieViewerCount);
                        movieFavCountHeaderList.add(movieFavCount);

                        size = movieImageHeaderList.size();


                        sliderAdapter = new SliderAdapterTwo(getActivity(),movieID_HeaderList,movieImageHeaderList,movieNameHeaderList,movieStoryHeaderList,movieDirectorHeaderList,movieYearHeaderList,movieRatingHeaderList,
                                movieRuntimeHeaderList,movieGenreHeaderList,movieChildGenreHeaderList,movieWatchPltHeaderList,movieYoutubeCodeHeaderList,movieViewerHeaderList,movieFavCountHeaderList);
                        viewPager.setAdapter(sliderAdapter);

                    }
                }
            }
        });

    }


    public void getData() {

        try {
            SQLiteDatabase database = getActivity().openOrCreateDatabase("MyFavContentsList", MODE_PRIVATE, null);

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

    public void loadContents(String documentID){

        DocumentReference documentReference = firebaseFirestore.collection("Contents").document(documentID);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Map <String, Object> data =document.getData();

                        String documentName = document.getId();
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


                        favoriesRVA = new FavoriesRVA(getActivity(), movieID_List, movieImageList, movieNameList, movieStoryList, movieDirectorList, movieYearList,movieRatingList,movieRuntimeList,movieGenreList,movieChildGenreList,
                                movieWatchPltList,movieYoutubeCodeList,movieViewerList,movieFavCountList);

                        recyclerView2.setAdapter(favoriesRVA);

                        favoriesRVA.notifyDataSetChanged();


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }
        });
    }


}
