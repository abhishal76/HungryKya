package com.example.hungrykya.ui.collection.list;

import android.content.Intent;
import android.os.Bundle;

import com.example.hungrykya.Modal.modal.Restaurant.Photo;
import com.example.hungrykya.Modal.modal.Restaurant.Restaurant;
import com.example.hungrykya.Modal.modal.Restaurant.RestaurantList;
import com.example.hungrykya.api.ZomataApi;
import com.example.hungrykya.ui.collection.CollectionRestaurantDetailActivity;
import com.example.hungrykya.ui.collection.adapter.CollectionRestaurantListAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hungrykya.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CollectionRestaurantList extends AppCompatActivity {
    ZomataApi zomataApi;

    ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();

    ListView resCollectionListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_restaurant_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent getCollectionIdIntent = getIntent();
        String collectionId  =getCollectionIdIntent.getStringExtra("collection_id");


        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://developers.zomato.com/api/v2.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        zomataApi = retrofit.create(ZomataApi.class);
        resCollectionListView = findViewById(R.id.resCollectionLV);

        Call<RestaurantList> collectionRestaurantListCall = zomataApi.getCollectionRestaurant("0a1faec1157469592a4ba67285a7c837", collectionId,
                Double.valueOf(getCollectionIdIntent.getStringExtra("lat")) , Double.valueOf(getCollectionIdIntent.getStringExtra("lon")));

        collectionRestaurantListCall.enqueue(new Callback<RestaurantList>() {
            @Override
            public void onResponse(Call<RestaurantList> call, Response<RestaurantList> response) {
                if (response.code() == 200){
                    RestaurantList restaurantList = response.body();
                    for (int i = 0; i<restaurantList.getRestaurants().size(); i++){
                        restaurantArrayList.add(restaurantList.getRestaurants().get(i));
                    }
                    CollectionRestaurantListAdapter collectionRestaurantListAdapter =
                            new CollectionRestaurantListAdapter(getApplicationContext(), R.layout.restaurant_collection, restaurantArrayList );
                        resCollectionListView.setAdapter(collectionRestaurantListAdapter);
                        resCollectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String resName = restaurantArrayList.get(i).getRestaurant().getName();
                                String resAddress = restaurantArrayList.get(i).getRestaurant().getLocation().getAddress();
                                String resLocality = restaurantArrayList.get(i).getRestaurant().getLocation().getLocality();
                                String resCuisines = restaurantArrayList.get(i).getRestaurant().getCuisines();
                                String resAverageCost = restaurantArrayList.get(i).getRestaurant().getAverageCostForTwo().toString();
                                String resTimings = restaurantArrayList.get(i).getRestaurant().getTimings();
                                ArrayList<String> resHighlights = restaurantArrayList.get(i).getRestaurant().getHighlights();
                                String resRating = restaurantArrayList.get(i).getRestaurant().getUserRating().getAggregateRating();
                                ArrayList<Photo> test =restaurantArrayList.get(i).getRestaurant().getPhotos();



                                Intent collectionRestaurantActivityIntent = new Intent(view.getContext(), CollectionRestaurantDetailActivity.class);
                                collectionRestaurantActivityIntent.putExtra("resName", resName);
                                collectionRestaurantActivityIntent.putExtra("resAddress", resAddress);
                                collectionRestaurantActivityIntent.putExtra("resLocality", resLocality);
                                collectionRestaurantActivityIntent.putExtra("resCuisines", resCuisines);
                                collectionRestaurantActivityIntent.putExtra("resAverageCost", resAverageCost);
                                collectionRestaurantActivityIntent.putExtra("resTimings", resTimings);
                                collectionRestaurantActivityIntent.putExtra("resRating", resRating);
                                collectionRestaurantActivityIntent.putStringArrayListExtra("resHighlights", resHighlights);
                                collectionRestaurantActivityIntent.putExtra("test",(ArrayList<Photo>) test );

                                startActivity(collectionRestaurantActivityIntent);
                            }
                        });

                }
                else
                    {
                    Log.e("RETRY", "retry");
                }
            }

            @Override
            public void onFailure(Call<RestaurantList> call, Throwable t) {

            }
        });



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
