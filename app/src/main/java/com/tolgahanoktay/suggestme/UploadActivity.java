package com.tolgahanoktay.suggestme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    private EditText movie_name,movie_year,movie_rating,movie_runtime,movieStory,movieTrailer,movieDirector,imageNameActor_1,imageNameActor_2,imageNameActor_3;
    private ImageView imageMovie;
    private TextView chooseCategory,chooseChildCategory,chooseWatchPlt;
    private Spinner spinnerCategory,spinnerChildCategory,spinnerWatchPlt;

    private Button button,button_update,button_delete,button_otherDB;

    private Bitmap selectedImage;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseDatabase database;
    DatabaseReference databaseReference;
    private Uri imageData;

    String getDocReference;
    int succesCounter = 0;
    int genreCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        imageMovie = findViewById(R.id.send_imageMovie);
        movie_name = findViewById(R.id.send_movie_name);
        movie_year = findViewById(R.id.send_movie_year);
        movie_rating = findViewById(R.id.send_movie_rating);
        movie_runtime = findViewById(R.id.send_movie_runtime);
        movieStory = findViewById(R.id.editText_movieStory);
        movieTrailer = findViewById(R.id.editText_youtubeCode);
        movieDirector = findViewById(R.id.send_movie_director);


        imageNameActor_1 = findViewById(R.id.send_movie_actorsOne);
        imageNameActor_2 = findViewById(R.id.send_movie_actorsTwo);
        imageNameActor_3 = findViewById(R.id.send_movie_actorsThree);

        chooseCategory = findViewById(R.id.textView_content_genre);
        chooseChildCategory = findViewById(R.id.textView__content_childGenre);
        chooseWatchPlt = findViewById(R.id.textView_watch_Platform);

        spinnerCategory = findViewById(R.id.category_spinner);
        spinnerChildCategory = findViewById(R.id.childCategory_spinner);
        spinnerWatchPlt = findViewById(R.id.watchPlatform_spinner);

        button = findViewById(R.id.buttonSend);
        button_update = findViewById(R.id.buttonUpdate);
        button_delete = findViewById(R.id.buttonDelete);
        button_otherDB = findViewById(R.id.buttonActorPhoto_send);


        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        /*action, anime, science fiction, kids and family, drama, fantasy, thriller, classics, comedy, horror, musicals, award-winning productions, romance, sports, stand up*/

        /*String[] arrayOne = {"Action", "Anime", "Science Fiction", "Kid and Family", "Drama", "Fantasy", "Triller", "Classics", "Comedy", "Horror", "Musicals", "Award-winning productions",
                "romance", "sports", "stand up"};

        String[] arrayTwo = {"Action", "Anime", "Science Fiction", "Kid and Family", "Drama", "Fantasy", "Triller", "Classics", "Comedy", "Horror", "Youth","Romance", "Crime","Mystery"};*/



        List<String> cursorsItem = new ArrayList<>();
        cursorsItem.add("");
        cursorsItem.add("Movie");
        cursorsItem.add("Tv Show");
        cursorsItem.add("Documentary");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,cursorsItem);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(dataAdapter);


        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                genreCounter++;
                String cursorSelected = adapterView.getItemAtPosition(position).toString();

                chooseCategory.setText(cursorSelected);

                if (cursorSelected.equals("Movie")){

                    String[] arrayOne = {"","Action", "Anime", "Science Fiction", "Kid and Family", "Drama", "Fantasy", "Triller", "Classics", "Comedy", "Horror", "Musicals", "Award-winning productions",
                            "Romance", "Sports", "Stand up"};

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,arrayOne);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerChildCategory.setAdapter(dataAdapter2);

                   spinnerChildCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                       @Override
                       public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                           chooseChildCategory.setText(adapterView.getItemAtPosition(position).toString());

                       }

                       @Override
                       public void onNothingSelected(AdapterView<?> adapterView) {

                       }
                   });



                }else if (cursorSelected.equals("Tv Show")){

                    String[] arrayTwo = {"","Action","Anime","Science Fiction","Kid and Family","Drama","Fantasy","Triller","Classics","Comedy","Horror","Youth","Romance","Crime","Mystery"};

                    ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,arrayTwo);
                    dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerChildCategory.setAdapter(dataAdapter3);

                    spinnerChildCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            //String cursorSelected = adapterView.getItemAtPosition(position).toString();
                            chooseChildCategory.setText(adapterView.getItemAtPosition(position).toString());

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }else {
                    System.out.println("Not Found");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(),"nothing Selected",Toast.LENGTH_LONG).show();
            }
        });


        List<String> cursorsItem3 = new ArrayList<>();
        cursorsItem3.add("");
        cursorsItem3.add("Netflix");
        cursorsItem3.add("Amazon");
        cursorsItem3.add("Both of These");
        cursorsItem3.add("Everywhere");


        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,cursorsItem3);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWatchPlt.setAdapter(dataAdapter3);

        spinnerWatchPlt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                chooseWatchPlt.setText(adapterView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    public void selectImage(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null){

            imageData = data.getData();

            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    imageMovie.setImageBitmap(selectedImage);
                } else{
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                    imageMovie.setImageBitmap(selectedImage);
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void upload(View view){

        if (imageData != null){
            UUID uuid = UUID.randomUUID();
            final  String imageName = "images/" + uuid + ".png";

            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(final Uri uri) {
                            String downloadUrl = uri.toString();

                            String firebase_movieName = movie_name.getText().toString();
                            String firebase_movieYear = movie_year.getText().toString();
                            String firebase_movieRating = movie_rating.getText().toString();
                            String firebase_movieRuntime = movie_runtime.getText().toString();
                            String firebase_movieGenre = chooseCategory.getText().toString();
                            String firebase_movieChildGenre = chooseChildCategory.getText().toString();
                            String firebase_movieWatchPlt = chooseWatchPlt.getText().toString();
                            String firebase_movieStory = movieStory.getText().toString();
                            String firebase_movieYoutube = movieTrailer.getText().toString();
                            String firebase_movieDirector = movieDirector.getText().toString();

                            //int clickCounter = 0;
                            //String counter = String.valueOf(clickCounter);

                            HashMap<String,Object> postData = new HashMap<>();
                            postData.put("movieImage",downloadUrl);
                            postData.put("movieName", firebase_movieName);
                            postData.put("movieYear" ,firebase_movieYear);
                            postData.put("movieRating", firebase_movieRating);
                            postData.put("movieRuntime", firebase_movieRuntime);
                            postData.put("movieStory", firebase_movieStory);
                            postData.put("movieDirector", firebase_movieDirector);
                            postData.put("trailerCode", firebase_movieYoutube);
                            postData.put("mainCategory",firebase_movieGenre);
                            postData.put("date", FieldValue.serverTimestamp());
                            postData.put("addedList","0");
                            postData.put("childCategory", firebase_movieChildGenre);

                            if (firebase_movieWatchPlt.equals("Both of These")){
                                postData.put("watchPlatform", "Netflix," + "Amazon");
                            }else{
                                postData.put("watchPlatform", firebase_movieWatchPlt);
                            }

                            postData.put("clickCounter", "0");

                            //postData.put("clickCounter", counter);

                            firebaseFirestore.collection("Contents").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    succesCounter++;
                                    getDocReference = documentReference.getId();

                                    if (succesCounter == 1){

                                        movie_name.setVisibility(View.GONE);
                                        movie_year.setVisibility(View.GONE);
                                        movie_rating.setVisibility(View.GONE);
                                        movie_runtime.setVisibility(View.GONE);
                                        movieStory.setVisibility(View.GONE);
                                        movieTrailer.setVisibility(View.GONE);
                                        movieDirector.setVisibility(View.GONE);
                                        chooseCategory.setVisibility(View.GONE);
                                        chooseChildCategory.setVisibility(View.GONE);
                                        chooseWatchPlt.setVisibility(View.GONE);
                                        spinnerCategory.setVisibility(View.GONE);
                                        spinnerChildCategory.setVisibility(View.GONE);
                                        spinnerWatchPlt.setVisibility(View.GONE);
                                        button.setVisibility(View.GONE);

                                        button_otherDB.setVisibility(View.VISIBLE);
                                        imageNameActor_1.setEnabled(true);

                                        button_otherDB.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                        if (imageData != null) {
                                            UUID uuid = UUID.randomUUID();
                                            final String imageName = "images/" + uuid + ".png";

                                            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                                                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {

                                                            String actorName = imageNameActor_1.getText().toString();

                                                            String downloadUrlOne = uri.toString();

                                                            databaseReference.child("Contents").child(getDocReference).child(getDocReference+"One").child("actorPhoto").setValue(downloadUrlOne);
                                                            databaseReference.child("Contents").child(getDocReference).child(getDocReference+"One").child("actorName").setValue(actorName);

                                                            succesCounter++;

                                                            imageNameActor_1.setVisibility(View.GONE);
                                                            imageNameActor_2.setEnabled(true);

                                                            button_otherDB.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    succesCounter++;

                                                                    if (imageData != null) {
                                                                        UUID uuid = UUID.randomUUID();
                                                                        final String imageName = "images/" + uuid + ".png";

                                                                        storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                                                StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                                                                                newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                    @Override
                                                                                    public void onSuccess(Uri uri) {

                                                                                        String actorName = imageNameActor_2.getText().toString();

                                                                                        String downloadUrlTwo = uri.toString();

                                                                                        databaseReference.child("Contents").child(getDocReference).child(getDocReference+"Two").child("actorPhoto").setValue(downloadUrlTwo);
                                                                                        databaseReference.child("Contents").child(getDocReference).child(getDocReference+"Two").child("actorName").setValue(actorName);

                                                                                        succesCounter++;

                                                                                        imageNameActor_2.setVisibility(View.GONE);
                                                                                        imageNameActor_3.setEnabled(true);

                                                                                        button_otherDB.setOnClickListener(new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View view) {

                                                                                                if (imageData != null) {
                                                                                                    UUID uuid = UUID.randomUUID();
                                                                                                    final String imageName = "images/" + uuid + ".png";

                                                                                                    storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                                            StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                                                                                                            newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(Uri uri) {

                                                                                                                    String actorName = imageNameActor_3.getText().toString();

                                                                                                                    String downloadUrlThree = uri.toString();

                                                                                                                    databaseReference.child("Contents").child(getDocReference).child(getDocReference+"Three").child("actorPhoto").setValue(downloadUrlThree);
                                                                                                                    databaseReference.child("Contents").child(getDocReference).child(getDocReference+"Three").child("actorName").setValue(actorName);

                                                                                                                    succesCounter++;

                                                                                                                    imageNameActor_3.setVisibility(View.GONE);

                                                                                                                    Toast.makeText(getApplicationContext(),"Succes Counter = " + succesCounter,Toast.LENGTH_LONG).show();
                                                                                                                    Toast.makeText(getApplicationContext(),"Yuklemeler Basarili",Toast.LENGTH_LONG).show();


                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                    });
                                                                                                }

                                                                                            }
                                                                                        });

                                                                                    }
                                                                                });

                                                                            }
                                                                        });
                                                                    }


                                                                }
                                                            });

                                                        }
                                                    });

                                                }
                                            });

                                        }

                                    }
                                });







                            }


                                }


                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UploadActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });

        }

    }
}