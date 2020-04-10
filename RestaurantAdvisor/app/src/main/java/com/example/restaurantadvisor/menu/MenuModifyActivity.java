package com.example.restaurantadvisor.menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantadvisor.ApiRequest;
import com.example.restaurantadvisor.MainActivity;
import com.example.restaurantadvisor.R;
import com.example.restaurantadvisor.restaurant.Restaurant;
import com.example.restaurantadvisor.restaurant.RestaurantActivity;
import com.example.restaurantadvisor.restaurant.RestaurantLocalStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuModifyActivity extends AppCompatActivity {

    private final String TAG = "AddRestaurantActivity";
    private MainActivity mainActivity = new MainActivity();
    private final String BASE_URL = mainActivity.getBASE_URL();

    private Retrofit retrofit;
    private RestaurantLocalStore restaurantLocalStore;
    private Button bModMenu;
    private ApiRequest restaurantApi;
    private int id, restoId;
    private EditText etName, etDesc, etPrice;
    private Restaurant restaurant;
    private MenuRestaurant menuRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_modify);

        this.bModMenu = findViewById(R.id.bModMenu);
        this.etName = findViewById(R.id.etMenuName);
        this.etDesc = findViewById(R.id.etMenuDescription);
        this.etPrice = findViewById(R.id.etMenuPrice);
        this.restaurantLocalStore = new RestaurantLocalStore(this);
        this.restaurant = restaurantLocalStore.getRestaurant();
        //region MenuData
        String menuName = getIntent().getStringExtra("menuName");
        etName.setText(menuName);

        String menuDesc = getIntent().getStringExtra("menuDesc");
        etDesc.setText(menuDesc);

        float menuPrice = getIntent().getFloatExtra("menuPrice", 0);
        etPrice.setText(Float.toString(menuPrice));

        id = getIntent().getIntExtra("id", 0);

        menuRestaurant = new MenuRestaurant();
        menuRestaurant.setId(id);
        menuRestaurant.setName(menuName);
        menuRestaurant.setDescription(menuDesc);
        menuRestaurant.setPrice(menuPrice);
        //endregion

        this.retrofitConfig();

        bModMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String description = etDesc.getText().toString();
                float price = 0;
                if (!etPrice.getText().toString().equals(""))
                    price = Float.parseFloat(etPrice.getText().toString());

                if (name.equals("") || description.equals("") || price == 0) {
                    //region error
                    if (name.equals(""))
                        etName.setError("Name is required");
                    if (description.equals(""))
                        etDesc.setError("Description is required");
                    if (price == 0)
                        etPrice.setError("Price is required");
                    //endregion
                } else {
                    modifyMenu(restaurant.getId(), id, name, description, price);
                }
            }
        });

    }

    private void modifyMenu(int restoId, int id, String name, String description, float price) {
        restaurantApi.putMenu(restoId, id, name, description, price).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse : " + response.code() + " " + response.message());

                if (response.message().equals("OK")) {
                    Toast.makeText(MenuModifyActivity.this, "Modify successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MenuModifyActivity.this, RestaurantActivity.class));
                } else
                    Toast.makeText(MenuModifyActivity.this, "Modify failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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

    //region returnButton
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.returnbutton, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btnReturn) {
            startActivity(menuRestaurant);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion
    //endregion
}
