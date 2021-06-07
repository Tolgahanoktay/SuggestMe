package com.tolgahanoktay.suggestme.Fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tolgahanoktay.suggestme.R;
import com.tolgahanoktay.suggestme.RecyclerViewsAdapters.FavoriesRVA;
import com.tolgahanoktay.suggestme.RecyclerViewsAdapters.FragmentHomeRVA;
import com.tolgahanoktay.suggestme.RecyclerViewsAdapters.NewsRVA;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class FragmentNews extends Fragment {

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
    NewsRVA newsRVA;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,container,false);

        firebaseFirestore = FirebaseFirestore.getInstance();

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

        recyclerView = view.findViewById(R.id.recycler_view_listNews);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        uploadNews();

        return view;
    }

    public void uploadNews(){
        CollectionReference collectionReference = firebaseFirestore.collection("News");

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
                        String movieName = (String) data.get("title");
                        String movieStory = (String) data.get("newsDetail");
                        String movieViewerCount = (String) data.get("clickCounter");

                        movieID_List.add(documentName);
                        movieImageList.add(movieImage);
                        movieNameList.add(movieName);
                        movieStoryList.add(movieStory);
                        movieViewerList.add(movieViewerCount);

                        newsRVA = new NewsRVA(getActivity(),movieID_List,movieImageList,movieNameList,movieStoryList,movieViewerList);
                        recyclerView.setAdapter(newsRVA);
                        newsRVA.notifyDataSetChanged();

                    }
                }
            }
        });

    }


}
