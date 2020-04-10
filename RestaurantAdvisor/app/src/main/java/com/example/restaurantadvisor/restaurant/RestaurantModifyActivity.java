package com.example.restaurantadvisor.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.restaurantadvisor.ApiRequest;
import com.example.restaurantadvisor.MainActivity;
import com.example.restaurantadvisor.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantModifyActivity extends AppCompatActivity {

    private final String TAG = "AddRestaurantActivity";
    private MainActivity mainActivity = new MainActivity();
    private final String BASE_URL = mainActivity.getBASE_URL();

    private Retrofit retrofit;
    private Button bModRestaurant;
    private ApiRequest restaurantApi;
    private int id = 0;
    private EditText etName, etDesc, etGrade, etLocal, etPhone, etWeb, etHours;
    private Restaurant restaurant;
    private RestaurantLocalStore restaurantLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_modify);

        this.restaurantLocalStore = new RestaurantLocalStore(this);
        this.bModRestaurant = findViewById(R.id.bModRestaurant);
        this.etName = findViewById(R.id.etRestaurantName);
        this.etDesc = findViewById(R.id.etRestaurantDescription);
        this.etGrade = findViewById(R.id.etRestaurantGrade);
        this.etLocal = findViewById(R.id.etRestaurantLocalization);
        this.etPhone = findViewById(R.id.etRestaurantPhone);
        this.etWeb = findViewById(R.id.etRestaurantWebsite);
        this.etHours = findViewById(R.id.etRestaurantHours);

        restaurant = restaurantLocalStore.getRestaurant();
        //region TextViewData
        String restaurantName = restaurant.getName();
        etName.setText(restaurantName);

        String restaurantDesc = restaurant.getDescription();
        etDesc.setText(restaurantDesc);

        float restaurantGrade = restaurant.getGrade();
        etGrade.setText(Float.toString(restaurantGrade));

        final String restaurantLocal = restaurant.getLocalization();
        etLocal.setText(restaurantLocal);

        final String restaurantPhone = restaurant.getPhone_number();
        etPhone.setText(restaurantPhone);

        final String restaurantWeb = restaurant.getWebsite();
        etWeb.setText(restaurantWeb);

        String restaurantHours = restaurant.getHours();
        etHours.setText(restaurantHours);

        id =  restaurant.getId();
        //endregion

        this.retrofitConfig();

        bModRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String description = etDesc.getText().toString();
                float grade = 0;
                if (!etGrade.getText().toString().equals(""))
                    grade = Float.parseFloat(etGrade.getText().toString());
                String localization = etLocal.getText().toString();
                String phone_number = etPhone.getText().toString();
                String website = etWeb.getText().toString();
                String hours = etHours.getText().toString();

                if (name.equals("") || description.equals("") || localization.equals("") || phone_number.equals("") || website.equals("") || hours.equals("") || grade > 10 || grade == 0) {
                    //region error
                    if (name.equals(""))
                        etName.setError("Name is required");
                    if (description.equals(""))
                        etDesc.setError("Desc is required");
                    if (grade == 0)
                        etGrade.setError("Grade is required");
                    if (grade > 10) {
                        etGrade.setError("Grade must be under 10");
                        etGrade.requestFocus();
                    }
                    if (localization.equals(""))
                        etLocal.setError("Localization is required");
                    if (phone_number.equals(""))
                        etPhone.setError("Phone is required");
                    if (website.equals(""))
                        etWeb.setError("Website is required");
                    if (hours.equals(""))
                        etHours.setError("Hours is required");
                    //endregion
                } else {
                    modifyRestaurant(id, name, description, grade, localization, phone_number, website, hours);
                }
            }
        });

    }

    private void modifyRestaurant(final int id, final String name, final String description, final float grade, final String localization, final String phone_number, final String website, final String hours) {
        restaurantApi.putRestaurant(id, name, description, grade, localization, phone_number, website, hours).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse : " + response.code() + " " + response.message());
                if (response.message().equals("OK")) {
                    Toast.makeText(RestaurantModifyActivity.this, "Modify successful", Toast.LENGTH_SHORT).show();
                    restaurantLocalStore.clearRestaurantData();
                    restaurantLocalStore.storeRestaurantData(new Restaurant(id, name, description, grade, localization, phone_number, website, hours));
                    startActivity(new Intent(RestaurantModifyActivity.this, RestaurantActivity.class));
                } else
                    Toast.makeText(RestaurantModifyActivity.this, "Modify failed", Toast.LENGTH_SHORT).show();
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
