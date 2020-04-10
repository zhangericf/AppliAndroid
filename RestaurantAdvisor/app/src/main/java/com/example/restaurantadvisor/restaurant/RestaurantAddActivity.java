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

public class RestaurantAddActivity extends AppCompatActivity {

    private final String TAG = "AddRestaurantActivity";
    private MainActivity mainActivity = new MainActivity();
    private final String BASE_URL = mainActivity.getBASE_URL();

    private Retrofit retrofit;
    private Button bAddRestaurant;
    private ApiRequest restaurantApi;
    private EditText etName, etDesc, etGrade, etLocal, etPhone, etWeb, etHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_add);

        this.bAddRestaurant = findViewById(R.id.bAddRestaurant);
        this.etName = findViewById(R.id.etRestaurantName);
        this.etDesc = findViewById(R.id.etRestaurantDescription);
        this.etGrade = findViewById(R.id.etRestaurantGrade);
        this.etLocal = findViewById(R.id.etRestaurantLocalization);
        this.etPhone = findViewById(R.id.etRestaurantPhone);
        this.etWeb = findViewById(R.id.etRestaurantWebsite);
        this.etHours = findViewById(R.id.etRestaurantHours);

        this.retrofitConfig();

        bAddRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //region get variable
                String name = etName.getText().toString();
                String description = etDesc.getText().toString();
                float grade = 0;
                if (!etGrade.getText().toString().equals(""))
                    grade = Float.parseFloat(etGrade.getText().toString());
                String localization = etLocal.getText().toString();
                String phone_number = etPhone.getText().toString();
                String website = etWeb.getText().toString();
                String hours = etHours.getText().toString();
                //endregion

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
                    createRestaurant(name, description, grade, localization, phone_number, website, hours);
                    startActivity(new Intent(RestaurantAddActivity.this, MainActivity.class));
                }
            }
        });
    }

    private void createRestaurant(String name, String description, float grade, String localization, String phone_number, String website, String hours) {
        restaurantApi.postRestaurant(name, description, grade, localization, phone_number, website, hours).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse : " + response.code() + " " + response.message());
                if (response.message().equals("Created"))
                    Toast.makeText(RestaurantAddActivity.this, "Adding successful", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(RestaurantAddActivity.this, "Adding failed", Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion
    //endregion
}
