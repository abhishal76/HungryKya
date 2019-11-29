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

import com.example.hungrykya.Modal.modal.collections.Collection;
import com.example.hungrykya.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CollectionAdapter extends ArrayAdapter<Collection> {

    Context context;
    ArrayList<Collection> collectionArrayList;
    int id;
    public CollectionAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Collection> objects) {
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.collectionArrayList = objects;
    }


    @Nullable
    @Override
    public Collection getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        convertView = layoutInflater.inflate(id, null);

        TextView description = convertView.findViewById(R.id.descriptionText);
        ImageView collectionImageView = convertView.findViewById(R.id.collectionImageView);
        Collection collection = getItem(position);

        Picasso.get().load(collection.getCollection().getImageUrl()).resize(650, 500).into(collectionImageView);

        description.setText(collection.getCollection().getTitle());




        return convertView;
    }

}
