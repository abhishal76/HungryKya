package com.example.hungrykya.ui.collection.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hungrykya.Modal.modal.Restaurant.Restaurant;
import com.example.hungrykya.R;
import com.example.hungrykya.ui.collection.CollectionRestaurantDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CollectionRestaurantListAdapter extends ArrayAdapter<Restaurant> {

    ArrayList<Restaurant> restaurantArrayList;
    Context context;
    int res;
    public CollectionRestaurantListAdapter(@NonNull Context context, int resource, ArrayList<Restaurant> restaurantArrayList) {
        super(context, resource, restaurantArrayList);
        this.context = context;
        this.restaurantArrayList = restaurantArrayList;
        this.res = resource;

    }

    @Nullable
    @Override
    public Restaurant getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Restaurant item) {
        return super.getPosition(item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        convertView = layoutInflater.inflate(res, null);

        TextView resName = convertView.findViewById(R.id.resNameTV);
        TextView cuisine = convertView.findViewById(R.id.cuisinesTV);
        TextView rating = convertView.findViewById(R.id.ratingTV);
        ImageView imageView = convertView.findViewById(R.id.imageView1);

        Restaurant restaurant = getItem(position);
        resName.setText(restaurant.getRestaurant().getName());
        cuisine.setText(restaurant.getRestaurant().getCuisines());
        rating.setText(restaurant.getRestaurant().getUserRating().getAggregateRating());


        if (restaurant.getRestaurant().getFeaturedImage().isEmpty()){
            Log.e("NO_IMAGE","no image available");
        } else{
            Picasso.get().load(restaurant.getRestaurant().getThumb()).resize(650, 480).into(imageView);
        }


        return convertView;
    }
}
