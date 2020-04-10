package com.example.restaurantadvisor.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.Animation;
import com.example.restaurantadvisor.ApiRequest;
import com.example.restaurantadvisor.MainActivity;
import com.example.restaurantadvisor.MapsActivity;
import com.example.restaurantadvisor.R;
import com.example.restaurantadvisor.comments.CommentsActivity;
import com.example.restaurantadvisor.menu.MenuActivity;
import com.example.restaurantadvisor.menu.MenuAddActivity;
import com.example.restaurantadvisor.menu.MenuListAdapter;
import com.example.restaurantadvisor.menu.MenuRestaurant;
import com.example.restaurantadvisor.user.UserLocalStore;
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

public class RestaurantActivity extends AppCompatActivity {

    private final String TAG = "RestaurantActivity";
    private MainActivity mainActivity = new MainActivity();
    private final String BASE_URL = mainActivity.getBASE_URL();

    private ListView listView;
    private Retrofit retrofit;
    private ApiRequest requestAPI;
    private MenuListAdapter listAdapter;
    private boolean show = false;
    private FloatingActionButton fab, fabDel, fabAdd, fabMod;
    private Restaurant restaurant;
    private UserLocalStore userLocalStore;
    private RestaurantLocalStore restaurantLocalStore;
    private List<MenuRestaurant> menuRestaurantList;
    private int id;
    private TextView tvPhone;
    private Button btnComments;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        this.menuRestaurantList = new ArrayList<>();
        this.btnComments = findViewById(R.id.btnComments);
        this.fab = findViewById(R.id.fab);
        this.fabDel = findViewById(R.id.fabDel);
        this.listView = findViewById(R.id.listView);
        this.listAdapter = new MenuListAdapter(getApplicationContext(), menuRestaurantList);
        this.listView.setAdapter(listAdapter);
        this.fabAdd = findViewById(R.id.fabAdd);
        this.fabMod = findViewById(R.id.fabModify);
        this.userLocalStore = new UserLocalStore(this);
        this.restaurantLocalStore = new RestaurantLocalStore(this);
        this.id = getIntent().getIntExtra("id", 0);
        this.retrofitConfig();

        if (userLocalStore.getUserLoggedIn())
            fab.setVisibility(View.VISIBLE);

        restaurant = restaurantLocalStore.getRestaurant();
        //region TextViewData
        String restaurantName = restaurant.getName();
        TextView tvName = findViewById(R.id.tvName);
        tvName.setText(restaurantName);

        String restaurantDesc = restaurant.getDescription();
        TextView tvDesc = findViewById(R.id.tvDesc);
        tvDesc.setText(restaurantDesc);

        float restaurantGrade = restaurant.getGrade();
        TextView tvGrade = findViewById(R.id.tvGrade);
        tvGrade.setText(Float.toString(restaurantGrade));

        final String restaurantLocal = restaurant.getLocalization();
        TextView tvLocal = findViewById(R.id.tvLocal);
        tvLocal.setText(restaurantLocal);

        final String restaurantPhone = restaurant.getPhone_number();
        tvPhone = findViewById(R.id.tvPhone);
        tvPhone.setText(restaurantPhone);

        final String restaurantWeb = restaurant.getWebsite();
        TextView tvWeb = findViewById(R.id.tvWeb);
        tvWeb.setText(restaurantWeb);

        String restaurantHours = restaurant.getHours();
        TextView tvHours = findViewById(R.id.tvHours);
        tvHours.setText(restaurantHours);

        id =  restaurant.getId();
        //endregion

        tvWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + restaurantWeb));
                startActivity(intent);
            }
        });

        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

        tvLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantActivity.this, MapsActivity.class);
                intent.putExtra("localization", restaurantLocal);
                startActivity(intent);
            }
        });

        btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantActivity.this, CommentsActivity.class);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!show)
                    show = true;
                else
                    show = false;

                if (show) {
                    fabDel.setVisibility(View.VISIBLE);
                    fabAdd.setVisibility(View.VISIBLE);
                    fabMod.setVisibility(View.VISIBLE);
                    Animation.with(Techniques.FadeIn)
                            .duration(500)
                            .playOn(fabDel);
                    Animation.with(Techniques.FadeIn)
                            .duration(500)
                            .playOn(fabAdd);
                    Animation.with(Techniques.FadeIn)
                            .duration(500)
                            .playOn(fabMod);
                } else {
                    fabDel.setVisibility(View.GONE);
                    fabAdd.setVisibility(View.GONE);
                    fabMod.setVisibility(View.GONE);
                }
            }
        });

        fabDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRestaurant();
            }
        });

        fabMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RestaurantActivity.this, RestaurantModifyActivity.class));
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RestaurantActivity.this, MenuAddActivity.class));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuRestaurant menuRestaurant = menuRestaurantList.get(position);
                startActivity(menuRestaurant);
            }
        });

        this.getMenuViewAPI();
    }

    private void makePhoneCall() {
        String number = tvPhone.getText().toString();
        if (number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(RestaurantActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(RestaurantActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Toast.makeText(RestaurantActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getMenuViewAPI() {
        requestAPI.getMenus(id).enqueue(new Callback<List<MenuRestaurant>>() {
            @Override
            public void onResponse(Call<List<MenuRestaurant>> call, Response<List<MenuRestaurant>> response) {
                Log.d(TAG, "getMenu onResponse " + response.code() + " " + response.message());
                List<MenuRestaurant> menuRestaurantList1 = response.body();
                boolean isEmpty = true;

                try {
                   isEmpty = !menuRestaurantList1.isEmpty();
                } catch (NullPointerException e) {
                    e.getMessage();
                }

                if (isEmpty) {
                    try {
                        menuRestaurantList.addAll(menuRestaurantList1);
                    } catch (NullPointerException e) {
                        e.getMessage();
                    }
                    listAdapter.notifyDataSetChanged();
                } else {
                    TextView tvEmpty = findViewById(R.id.tvEmpty);
                    tvEmpty.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onResponse : Menu is empty " + response.body());
                }
            }

            @Override
            public void onFailure(Call<List<MenuRestaurant>> call, Throwable t) {

            }
        });
    }

    private void deleteRestaurant() {
        requestAPI.deleteRestaurant(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "onResponse : " + response.code() + " " + response.message());
                Toast.makeText(RestaurantActivity.this, "Deleting successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RestaurantActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "onFailure : " + t.getMessage());
            }
        });
    }

    //region Common function

    private void startActivity(MenuRestaurant menuRestaurant) {
        Intent intent = new Intent(this, MenuActivity.class);
        //region putExtra
        intent.putExtra("id", menuRestaurant.getId());
        intent.putExtra("menuName", menuRestaurant.getName());
        intent.putExtra("menuDesc", menuRestaurant.getDescription());
        intent.putExtra("menuPrice", menuRestaurant.getPrice());
        //endregion
        super.startActivity(intent);
    }

    private void retrofitConfig() {
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        requestAPI = retrofit.create(ApiRequest.class);
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
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //endregion
}
