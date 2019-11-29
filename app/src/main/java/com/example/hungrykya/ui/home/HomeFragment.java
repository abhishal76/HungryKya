package com.example.hungrykya.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.hungrykya.BuildConfig;
import com.example.hungrykya.Modal.modal.Cuisine.Cuisine;
import com.example.hungrykya.Modal.modal.Cuisine.CuisineList;
import com.example.hungrykya.Modal.modal.Restaurant.Photo;
import com.example.hungrykya.Modal.modal.Restaurant.Restaurant;
import com.example.hungrykya.Modal.modal.Restaurant.RestaurantList;
import com.example.hungrykya.R;
import com.example.hungrykya.api.ZomataApi;
import com.example.hungrykya.ui.collection.CollectionRestaurantDetailActivity;
import com.example.hungrykya.ui.collection.list.CollectionFragmentList;
import com.example.hungrykya.ui.home.adapter.HomeFragmentAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static androidx.core.content.PermissionChecker.checkSelfPermission;
import static com.google.android.gms.analytics.internal.zzy.t;

public class HomeFragment extends Fragment implements LocationListener {


    private HomeViewModel homeViewModel;
    private static final int PERMISSION_REQUEST_CODE = 100;
    String userKey = BuildConfig.ApiKey;;

    AutoCompleteTextView searchRes;
    private LocationManager locationManager;
    private double latitude, longitude;
    ZomataApi zomataApi;
    ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();
   // ArrayList<Cuisine> cuisinesArrayList = new ArrayList<>(); //Array list for cuisines from Cuisine class
    ArrayList<String> cuisinesList = new ArrayList<>(); // String list for cuisines name
    TextView nearByRest;
    HomeFragmentAdapter homeFragmentAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        searchRes = root.findViewById(R.id.searchRest);
        nearByRest = root.findViewById(R.id.nearByRest);
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        final ListView latLongRest = root.findViewById(R.id.latLongRestList);
        ListView cuisineListView = root.findViewById(R.id.cuisineList);

        if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET},
                    10);
            Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            if (location != null){
                onLocationChanged(location);
            }
            return root;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        if (location != null){
            onLocationChanged(location);
        }

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://developers.zomato.com/api/v2.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        zomataApi = retrofit.create(ZomataApi.class);

        Call<RestaurantList> restaurantListCall = zomataApi.getRestaurantByLatlong(userKey, latitude, longitude);
        restaurantListCall.enqueue(new Callback<RestaurantList>() {
            @Override
            public void onResponse(Call<RestaurantList> call, Response<RestaurantList> response) {
                if (response.code() == 200){
                    nearByRest.setText("Popular Near By Restaurant");
                    RestaurantList restaurantList = response.body();
                    for (int i = 0; i<restaurantList.getRestaurants().size(); i++){
                        restaurantArrayList.add(restaurantList.getRestaurants().get(i));
                        cuisinesList.add(restaurantList.getRestaurants().get(i).getRestaurant().getName());
                    }

                    homeFragmentAdapter = new HomeFragmentAdapter(getContext(), R.layout.restaurant_collection, restaurantArrayList);
                    latLongRest.setAdapter(homeFragmentAdapter);
                    latLongRest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                }else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("RETRY", "retry");
                }
            }

            @Override
            public void onFailure(Call<RestaurantList> call, Throwable t) {

            }
        });


        Call<CuisineList> cuisineListCall= zomataApi.getCuisines(userKey, latitude, longitude);
        cuisineListCall.enqueue(new Callback<CuisineList>() {
            @Override
            public void onResponse(Call<CuisineList> call, Response<CuisineList> response) {
                if (response.code() == 200){
                    CuisineList cuisineList = response.body();
                    for (int i = 0; i<cuisineList.getCuisines().size(); i++){
                        //cuisinesArrayList.add(cuisineList.getCuisines().get(i));
                        cuisinesList.add(cuisineList.getCuisines().get(i).getCuisine().getCuisineName());
                    }
                }else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("RETRY", "retry");
                }
            }

            @Override
            public void onFailure(Call<CuisineList> call, Throwable t) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1, cuisinesList);
        searchRes.setAdapter(adapter);
        searchRes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                restaurantArrayList.clear();
                nearByRest.setVisibility(View.INVISIBLE);
            final String name = (String) adapterView.getItemAtPosition(i);

                Call<RestaurantList> restaurantListCall = zomataApi.getRestaurantByCuisineAndName(userKey,name, latitude, longitude);
                restaurantListCall.enqueue(new Callback<RestaurantList>() {
                    @Override
                    public void onResponse(Call<RestaurantList> call, Response<RestaurantList> response) {
                        if (response.code() == 200){
                            nearByRest.setVisibility(View.VISIBLE);
                            nearByRest.setText(name);
                            RestaurantList restaurantList = response.body();
                            for (int i = 0; i<restaurantList.getRestaurants().size(); i++){
                                restaurantArrayList.add(restaurantList.getRestaurants().get(i));
                            }

                            homeFragmentAdapter = new HomeFragmentAdapter(getContext(), R.layout.restaurant_collection, restaurantArrayList);
                            latLongRest.setAdapter(homeFragmentAdapter);
                            latLongRest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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


                        }else {
                            Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                            Log.e("RETRY", "retry");
                        }
                    }

                    @Override
                    public void onFailure(Call<RestaurantList> call, Throwable t) {

                    }
                });
                InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });
        return root;
    }


    @Override
    public void onLocationChanged(Location location) {
         latitude = location.getLatitude();
         longitude = location.getLongitude();

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}