package com.example.restaurantadvisor.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Animation;
import com.daimajia.androidanimations.library.Techniques;
import com.example.restaurantadvisor.ApiRequest;
import com.example.restaurantadvisor.MainActivity;
import com.example.restaurantadvisor.R;
import com.example.restaurantadvisor.restaurant.Restaurant;
import com.example.restaurantadvisor.restaurant.RestaurantActivity;
import com.example.restaurantadvisor.restaurant.RestaurantLocalStore;
import com.example.restaurantadvisor.user.UserLocalStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuActivity extends AppCompatActivity {

    private Restaurant restaurant;
    private MenuRestaurant menuRestaurant;
    private final String TAG = "AddRestaurantActivity";
    private MainActivity mainActivity = new MainActivity();
    private final String BASE_URL = mainActivity.getBASE_URL();
    private boolean show = false;
    private Retrofit retrofit;
    private FloatingActionButton fab, fabDel, fabMod;
    private UserLocalStore userLocalStore;
    private ApiRequest requestAPI;
    private RestaurantLocalStore restaurantLocalStore;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.fab = findViewById(R.id.fab);
        this.fabDel = findViewById(R.id.fabDel);
        this.fabMod = findViewById(R.id.fabModify);
        this.userLocalStore = new UserLocalStore(this);
        this.restaurantLocalStore = new RestaurantLocalStore(this);
        this.retrofitConfig();

        if(userLocalStore.getUserLoggedIn())
            fab.setVisibility(View.VISIBLE);

        //region TextViewData
        String menuName = getIntent().getStringExtra("menuName");
        TextView tvName = findViewById(R.id.tvName);
        tvName.setText(menuName);

        String menuDesc = getIntent().getStringExtra("menuDesc");
        TextView tvDesc = findViewById(R.id.tvDesc);
        tvDesc.setText(menuDesc);

        float menuPrice = getIntent().getFloatExtra("menuPrice", 0);
        TextView tvPrice = findViewById(R.id.tvPrice);
        tvPrice.setText(Float.toString(menuPrice) + " $");
        id = getIntent().getIntExtra("id", 0);
        //endregion
        restaurant = restaurantLocalStore.getRestaurant();
        //region Menu
        menuRestaurant = new MenuRestaurant();
        menuRestaurant.setId(id);
        menuRestaurant.setName(menuName);
        menuRestaurant.setDescription(menuDesc);
        menuRestaurant.setPrice(menuPrice);
        //endregion

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!show)
                    show = true;
                else
                    show = false;

                if (show) {
                    fabDel.setVisibility(View.VISIBLE);
                    fabMod.setVisibility(View.VISIBLE);
                    Animation.with(Techniques.FadeIn)
                            .duration(500)
                            .playOn(fabDel);
                    Animation.with(Techniques.FadeIn)
                            .duration(500)
                            .playOn(fabMod);
                } else {
                    fabDel.setVisibility(View.GONE);
                    fabMod.setVisibility(View.GONE);
                }
            }
        });

        fabDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMenu();
            }
        });

        fabMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(menuRestaurant);
            }
        });
    }

    private void deleteMenu() {
        requestAPI.deleteMenu(restaurant.getId(), id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "onResponse : " + response.code() + " " + response.message());
                Toast.makeText(MenuActivity.this, "Deleting successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MenuActivity.this, RestaurantActivity.class));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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
        requestAPI = retrofit.create(ApiRequest.class);
    }

    private void startActivity(MenuRestaurant menuRestaurant) {
        Intent intent = new Intent(this, MenuModifyActivity.class);
        //region putExtra
        intent.putExtra("id", menuRestaurant.getId());
        intent.putExtra("menuName", menuRestaurant.getName());
        intent.putExtra("menuDesc", menuRestaurant.getDescription());
        intent.putExtra("menuPrice", menuRestaurant.getPrice());
        //endregion
        super.startActivity(intent);
    }

    //region returnButton
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.returnbutton, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btnReturn) {
            startActivity(new Intent(this, RestaurantActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion
    //endregion

}
