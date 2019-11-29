package com.example.hungrykya.ui.collection.list;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.hungrykya.BuildConfig;
import com.example.hungrykya.Modal.modal.collections.Collection;
import com.example.hungrykya.Modal.modal.collections.CollectionsList;
import com.example.hungrykya.R;
import com.example.hungrykya.api.ZomataApi;
import com.example.hungrykya.ui.collection.CollectionViewModel;
import com.example.hungrykya.ui.collection.adapter.CollectionAdapter;

import java.util.ArrayList;

import static android.content.Intent.getIntent;


public class CollectionFragmentList extends Fragment implements LocationListener {

    private CollectionViewModel collectionViewModel;
    LocationManager locationManager;
    double latitude, longitude;
    ZomataApi zomataApi;
    String userKey = BuildConfig.ApiKey;
    ArrayList<Collection> collection_arrayList = new ArrayList<>();
    ListView cListView;
    Location location;

    public CollectionFragmentList() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        collectionViewModel =
                ViewModelProviders.of(this).get(CollectionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET},
                        10);
                return root;
            }

        } catch (SecurityException e) {
            Log.d("sss", "ddddd");
        }


        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            onLocationChanged(location);
        } else {
            Log.e("Nolocation","Location not available");

        }

        cListView = root.findViewById(R.id.collectionRecycleListView);
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://developers.zomato.com/api/v2.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        zomataApi = retrofit.create(ZomataApi.class);


        final Call<CollectionsList> collectionCall = zomataApi.getCollections(userKey, latitude,
                longitude);

        collectionCall.enqueue(new Callback<CollectionsList>() {
            @Override
            public void onResponse(Call<CollectionsList> call, Response<CollectionsList> response) {

                if (response.code() == 200) {
                    final CollectionsList collectionsList = response.body();
                    if (collectionsList.getCollections() != null) {

                        for (int i = 0; i < collectionsList.getCollections().size(); i++) {
                            collection_arrayList.add(collectionsList.getCollections().get(i));

                        }
                    }
                    CollectionAdapter collectionAdapter = new CollectionAdapter(getContext(), R.layout.collection_list_view, collection_arrayList);
                    cListView.setAdapter(collectionAdapter);
                    cListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            int collectionId = collectionsList.getCollections().get(i).getCollection().getCollectionId();
                            double lat = latitude;
                            double lon = longitude;
                            Intent collectionIdIntent = new Intent(getContext(), CollectionRestaurantList.class);
                            collectionIdIntent.putExtra("collection_id", String.valueOf(collectionId));
                            collectionIdIntent.putExtra("lat", String.valueOf(lat));
                            collectionIdIntent.putExtra("lon", String.valueOf(lon));
                            startActivity(collectionIdIntent);
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<CollectionsList> call, Throwable t) {

            }
        });


        collectionViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }


    @Override
    public void onLocationChanged(Location location) {
         latitude = location.getLatitude();
         longitude = location.getLongitude();


        Log.e("lati", String.valueOf(latitude));
        Log.e("long", String.valueOf(longitude));


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(i);

    }

    private void getL() {

        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET},
                        10);
                return;
            }

        } catch (SecurityException e) {
            Log.d("sss", "ddddd");
        }


        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            onLocationChanged(location);
        } else {
            Log.e("Nolocation","Location not available");

        }

    }




}