package com.example.hungrykya.ui.collection;

import android.content.Intent;
import android.os.Bundle;

import com.example.hungrykya.Modal.modal.Restaurant.Photo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hungrykya.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CollectionRestaurantDetailActivity extends AppCompatActivity {

    TextView resNameTV, resAddressTV, resLocalityTV,resCuisinesTV,resAverageCostTV,resTimingsTV,resRatingTV;
    String hi = "";
    ListView highLightListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_restuarant_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Intent getCollectionResIntent = getIntent();
        String resName = getCollectionResIntent.getStringExtra("resName");
        String resAddress =getCollectionResIntent.getStringExtra("resAddress");
        String resLocality = getCollectionResIntent.getStringExtra("resLocality");
        String resCuisines = getCollectionResIntent.getStringExtra("resCuisines");
        String resAverageCost = getCollectionResIntent.getStringExtra("resAverageCost");
        String resTimings = getCollectionResIntent.getStringExtra("resTimings");
        String resRating = getCollectionResIntent.getStringExtra("resRating");
        ArrayList<String> resHighlights = getCollectionResIntent.getStringArrayListExtra("resHighlights");
        ArrayList<Photo> resPhoto = (ArrayList<Photo>) getCollectionResIntent.getSerializableExtra("test");




        resNameTV = findViewById(R.id.resName);
        resAddressTV = findViewById(R.id.resAddress);
        resLocalityTV = findViewById(R.id.resLocality);
        resCuisinesTV = findViewById(R.id.resCuisine);
        resLocalityTV = findViewById(R.id.resLocality);
        resAverageCostTV = findViewById(R.id.resAverageCost);
        resTimingsTV = findViewById(R.id.resTimings);
        resRatingTV = findViewById(R.id.resRating);
        highLightListView = findViewById(R.id.highLightLV);

        resNameTV.setText(resName);
        resAddressTV.setText(resAddress);
        resLocalityTV.setText(resLocality);
        resCuisinesTV.setText(resCuisines);
        resAverageCostTV.setText("$" +resAverageCost);
        resTimingsTV.setText(resTimings);
        resRatingTV.setText(resRating);
        ArrayList<String> stringArray= new ArrayList<>();

        for (int i=0; i<resHighlights.size(); i++){
            stringArray.add(resHighlights.get(i));
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.highlight_text ,stringArray);
        highLightListView.setAdapter(adapter);

        //resHighlightsTV.setText(hi);



        LinearLayout photos = findViewById(R.id.photos);
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        if (resPhoto !=null){
            for (int i =0; i<resPhoto.size(); i++) {


                View view = layoutInflater.inflate(R.layout.photos_restaurant, photos, false);

                ImageView photosView = view.findViewById(R.id.photoRes);
                Picasso.get().load(resPhoto.get(i).getPhoto().getUrl()).resize(650, 550).into(photosView);
                photos.addView(view);

            }
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }
        return true;
    }
}
