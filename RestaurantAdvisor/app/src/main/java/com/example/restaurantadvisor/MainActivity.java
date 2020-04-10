package com.example.restaurantadvisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.restaurantadvisor.restaurant.Restaurant;
import com.example.restaurantadvisor.restaurant.RestaurantActivity;
import com.example.restaurantadvisor.restaurant.RestaurantAddActivity;
import com.example.restaurantadvisor.restaurant.RestaurantListAdapter;
import com.example.restaurantadvisor.restaurant.RestaurantLocalStore;
import com.example.restaurantadvisor.user.UserLocalStore;
import com.example.restaurantadvisor.user.UserLoginActivity;
import com.example.restaurantadvisor.user.UserProfileActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private final String BASE_URL = "http://10.0.2.2:8000/api/";
    private ListView listView;
    private Retrofit retrofit;
    private RestaurantListAdapter listAdapter;
    private ApiRequest restaurantApi;
    private List<Restaurant> restaurantList;
    private FloatingActionButton fab;
    private boolean loggedIn;
    private UserLocalStore userLocalStore;
    private RestaurantLocalStore restaurantLocalStore;

    public String getBASE_URL() {
        return BASE_URL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.restaurantList = new ArrayList<>();
        this.listView = findViewById(R.id.listView);
        this.listAdapter = new RestaurantListAdapter(getApplicationContext(), restaurantList);
        this.listView.setAdapter(listAdapter);
        this.fab = findViewById(R.id.fab);
        this.userLocalStore = new UserLocalStore(this);
        this.restaurantLocalStore = new RestaurantLocalStore(this);
        this.loggedIn = userLocalStore.getUserLoggedIn();

        this.retrofitConfig();
        getRestaurantViewAPI();

        if(loggedIn)
            fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RestaurantAddActivity.class));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant restaurant = restaurantList.get(position);
                restaurantLocalStore.clearRestaurantData();
                restaurantLocalStore.storeRestaurantData(restaurant);
                startActivity(new Intent(MainActivity.this, RestaurantActivity.class));
            }
        });
    }

    private void getRestaurantViewAPI() {
        restaurantApi.getRestaurants().enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                Log.d(TAG, "onResponse");
                List<Restaurant> restaurantList1 = response.body();
                if (restaurantList1 != null) {
                    restaurantList.addAll(restaurantList1);
                    listAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse : Restaurants is empty" + response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Log.e(TAG, "onFailure : " + t.getMessage());
            }
        });
    }

    //region Common function

    private void retrofitConfig() {
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        restaurantApi = retrofit.create(ApiRequest.class);
    }

    //region buttons
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainbuttons, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnLog:
                System.out.println(loggedIn);
                if (!loggedIn)
                    startActivity(new Intent(this, UserLoginActivity.class));
                else
                    startActivity(new Intent(this, UserProfileActivity.class));
                return true;
            case R.id.btnReload:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion
    //endregion
}
