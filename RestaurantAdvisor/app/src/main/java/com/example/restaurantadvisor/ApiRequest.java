package com.example.restaurantadvisor;

import com.example.restaurantadvisor.comments.Comments;
import com.example.restaurantadvisor.menu.MenuRestaurant;
import com.example.restaurantadvisor.restaurant.Restaurant;
import com.example.restaurantadvisor.user.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiRequest {

    //region Restaurant APi
    @GET("restaurants")
    Call<List<Restaurant>> getRestaurants();

    @FormUrlEncoded
    @POST("restaurant")
    Call<ResponseBody> postRestaurant(
            @Field("name") String name,
            @Field("description") String description,
            @Field("grade") float grade,
            @Field("localization") String localization,
            @Field("phone_number") String phone,
            @Field("website") String website,
            @Field("hours") String hours
    );

    @FormUrlEncoded
    @PUT("restaurant/{id}")
    Call<ResponseBody> putRestaurant(
            @Path("id") int id,
            @Field("name") String name,
            @Field("description") String description,
            @Field("grade") float grade,
            @Field("localization") String localization,
            @Field("phone_number") String phone,
            @Field("website") String website,
            @Field("hours") String hours
    );

    @DELETE("restaurant/{id}")
    Call<Void> deleteRestaurant(@Path("id") int id);
    //endregion
    //region Comments APi
    @GET("restaurant/{restoId}/avis")
    Call<List<Comments>> getComments(
            @Path("restoId") int restoId
    );

    @FormUrlEncoded
    @POST("restaurant/{restoId}/avis")
    Call<ResponseBody> postComments(
            @Path("restoId") int restoId,
            @Field("content") String content,
            @Field("grade") float grade,
            @Field("user_id") int user_id
    );

    @FormUrlEncoded
    @PUT("restaurant/{restoId}/avis/{id}")
    Call<ResponseBody> putComments(
            @Path("restoId") int restoId,
            @Path("id") int id,
            @Field("name") String name,
            @Field("description") String description,
            @Field("grade") float grade,
            @Field("localization") String localization,
            @Field("phone_number") String phone,
            @Field("website") String website,
            @Field("hours") String hours
    );

    @DELETE("restaurant/{restoId}/avis/{id}")
    Call<Void> deleteComments(
            @Path("restoId") int restoId,
            @Path("id") int id
    );
    //endregion
    //region Menu APi
    @GET("restaurant/{id}/menus")
    Call<List<MenuRestaurant>> getMenus(@Path("id") int id);

    @FormUrlEncoded
    @POST("restaurant/{restoId}/menu")
    Call<ResponseBody> postMenu(
            @Path("restoId") int restoId,
            @Field("name") String name,
            @Field("description") String description,
            @Field("price") float price
    );

    @FormUrlEncoded
    @PUT("restaurant/{restoId}/menu/{id}")
    Call<ResponseBody> putMenu(
            @Path("restoId") int restoId,
            @Path("id") int id,
            @Field("name") String name,
            @Field("description") String description,
            @Field("price") float price
    );

    @DELETE("restaurant/{restoId}/menu/{id}")
    Call<Void> deleteMenu(
            @Path("restoId") int restoId,
            @Path("id") int id
    );
    //endregion
    //region User APi
    @GET("users")
    Call<List<User>> getUsers();

    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> createUser(
            @Field("login") String login,
            @Field("password") String password,
            @Field("email") String email,
            @Field("name") String name,
            @Field("firstname") String firstname,
            @Field("age") int age
    );

    @FormUrlEncoded
    @POST("auth")
    Call<ResponseBody> authUser(
            @Field("login") String login,
            @Field("password") String password
    );
    //endregion

}
