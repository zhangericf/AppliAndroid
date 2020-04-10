package com.example.restaurantadvisor.user;

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

public class UserRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "AddRestaurantActivity";
    private MainActivity mainActivity = new MainActivity();
    private final String BASE_URL = mainActivity.getBASE_URL();

    private Retrofit retrofit;
    private ApiRequest userAPI;
    private Button bRegister;
    private EditText etLogin, etEmail, etName, etFirstName, etAge, etPassword;
    private String login, email, name, firstname, password, strAge;
    int age = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        this.etLogin = findViewById(R.id.etLogin);
        this.etEmail = findViewById(R.id.etEmail);
        this.etName = findViewById(R.id.etName);
        this.etFirstName = findViewById(R.id.etFirstName);
        this.etAge = findViewById(R.id.etAge);
        this.etPassword = findViewById(R.id.etPassword);
        this.bRegister = findViewById(R.id.btnRegister);

        this.retrofitConfig();

        bRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                //region get variable
                login = etLogin.getText().toString();
                email = etEmail.getText().toString();
                name = etName.getText().toString();
                firstname = etFirstName.getText().toString();
                password = etPassword.getText().toString();
                strAge = etAge.getText().toString();
                if (!strAge.equals(""))
                    age = Integer.parseInt(strAge);
                //endregion
                if (login.equals("") || email.equals("") || name.equals("") || firstname.equals("") || password.equals("") || strAge.equals("")) {

                } else {
                    registerUser();
                    startActivity(new Intent(this, UserLoginActivity.class));
                }
                break;
        }
    }

    private void registerUser() {
        userAPI.createUser(login, password, email, name, firstname, age).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse : " + response.code() + " " + response.message());
                if (response.message().equals("Created"))
                    Toast.makeText(UserRegisterActivity.this, "Registered ! Please login", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(UserRegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
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
        userAPI = retrofit.create(ApiRequest.class);
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
            startActivity(new Intent(this, UserLoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion
    //endregion
}
