package com.example.hungrykya.api;

import com.example.hungrykya.Modal.modal.Cuisine.CuisineList;
import com.example.hungrykya.Modal.modal.Restaurant.RestaurantList;
import com.example.hungrykya.Modal.modal.collections.CollectionsList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ZomataApi {

    @GET("collections")
    Call<CollectionsList> getCollections(@Header("user-key") String userKey,
                                         @Query("lat") double latitude,
                                         @Query("lon") double longitude);

    @GET("search")
    Call<RestaurantList> getCollectionRestaurant(@Header("user-key") String userKey,
                                                 @Query("collection_id") String collectionId,
                                                 @Query("lat") double latitude,
                                                 @Query("lon") double longitude);

    @GET("search")
    Call<RestaurantList> getRestaurantByLatlong(@Header("user-key") String userKey,
                                                @Query("lat") double latitude,
                                                @Query("lon") double longitude);

    @GET("search")
    Call<RestaurantList> getRestaurantByCuisineAndName(@Header("user-key") String userKey,
                                                @Query("q") String query,
                                                @Query("lat") double latitude,
                                                @Query("lon") double longitude);
    @GET("cuisines")
    Call<CuisineList> getCuisines(@Header("user-key") String userKey,
                                  @Query("lat") double latitude,
                                  @Query("lon") double longitude);
}
