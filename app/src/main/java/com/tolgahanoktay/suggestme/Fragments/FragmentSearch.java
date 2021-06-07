package com.tolgahanoktay.suggestme.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.tolgahanoktay.suggestme.R;
import com.tolgahanoktay.suggestme.RecyclerViewsAdapters.SearchRVA;
import com.tolgahanoktay.suggestme.SliderAdapter;

import java.util.ArrayList;
import java.util.Map;

public class FragmentSearch extends Fragment {

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

    private FirebaseFirestore firebaseFirestore;

    RecyclerView recyclerView;
    EditText editText;
    SearchRVA searchRVA;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);

        recyclerView = view.findViewById(R.id.recycler_view_searchBox);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        editText = view.findViewById(R.id.search_dataText);

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


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //loadNewsBefore();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String getQuery = String.valueOf(editText.getText());
                //String getToText = s.toString();
                if (!getQuery.trim().isEmpty()){
                    loadNewsQuery(getQuery);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        return view;
    }

    public void loadNewsQuery(String infoText) {

        CollectionReference collectionReference = firebaseFirestore.collection("Contents");

            /*collectionReference.orderBy("title").startAt(infoText.toUpperCase()).endAt(infoText.toUpperCase() + "\uf8ff")
            collectionReference.whereEqualTo("title",infoText).startAt(infoText.toUpperCase()).endAt(infoText.toLowerCase() + "\uf8ff")
            collectionReference.whereGreaterThan("title",infoText).orderBy("title").startAt(infoText.toUpperCase()).endAt(infoText.toUpperCase() + "\uf8ff")*/

        collectionReference.whereGreaterThanOrEqualTo("movieName",infoText).limit(3).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null) {

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

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();

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


                        searchRVA = new SearchRVA(getActivity(), movieID_List, movieImageList, movieNameList, movieStoryList, movieDirectorList, movieYearList,movieRatingList,movieRuntimeList,movieGenreList,movieChildGenreList,
                        movieWatchPltList,movieYoutubeCodeList,movieViewerList,movieFavCountList);

                        recyclerView.setAdapter(searchRVA);

                        searchRVA.notifyDataSetChanged();

                    }
                }
            }
        });
    }

}
