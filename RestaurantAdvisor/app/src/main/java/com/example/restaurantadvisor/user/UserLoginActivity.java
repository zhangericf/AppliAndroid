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
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantadvisor.ApiRequest;
import com.example.restaurantadvisor.MainActivity;
import com.example.restaurantadvisor.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bLogin;
    private EditText etLogin, etPassword;
    private TextView registerLink;
    private UserLocalStore userLocalStore;
    private final String TAG = "LoginActivity";
    private MainActivity mainActivity = new MainActivity();
    private final String BASE_URL = mainActivity.getBASE_URL();

    private Retrofit retrofit;
    private ApiRequest userApi;
    private String login, email, name, firstname, password;
    private int age = -1, id = -1;
    private boolean loginIsWrong, passIsWrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        registerLink = findViewById(R.id.registerLink);
        bLogin = findViewById(R.id.btnLogin);

        bLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);

        this.retrofitConfig();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                login = etLogin.getText().toString();
                password = etPassword.getText().toString();
                if(login.equals("") || password.equals("")) {
                    //region error
                    if (login.equals("")) {
                        etLogin.setError("Login is required");
                        etLogin.requestFocus();
                    }
                    if (password.equals("")) {
                        etPassword.setError("Password is required");
                        etPassword.requestFocus();
                    }
                    //endregion
                } else {
                    getUserViewAPI();
                }
                break;
            case R.id.registerLink:
                startActivity(new Intent(this, UserRegisterActivity.class));
                break;
        }
    }
    private void getUserViewAPI() {
        userApi.getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d(TAG, "onResponse");
                List<User> userList = response.body();
                if (userList != null) {
                    for (User user: userList) {
                        passIsWrong = false;
                        loginIsWrong = false;
                        if (user.getLogin().equals(login)) {
                            if (user.getPassword().equals(password)) {
                                id = user.getId();
                                email = user.getEmail();
                                name = user.getName();
                                firstname = user.getFirstname();
                                age = user.getAge();
                                authUser();
                                return;
                            } else {
                                passIsWrong = true;
                                break;
                            }
                        } else {
                            loginIsWrong = true;
                        }
                    }
                    if (passIsWrong) {
                        etPassword.setError("Password incorrect");
                        etPassword.requestFocus();
                    }
                    if (loginIsWrong) {
                        etLogin.setError("Login do not exist");
                        etLogin.requestFocus();
                    }
                } else {
                    Log.d(TAG, "onResponse : Restaurants is empty" + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e(TAG, "onFailure : " + t.getMessage());
            }
        });
    }

    private void authUser() {
        userApi.authUser(login, password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse : " + response.code() + " " + response.message());
                if (response.message().equals("OK")) {
                    userLocalStore.storeUserData(new User(id, login, email, name, firstname, password, age));
                    userLocalStore.setUserLoggedIn(true);
                    Toast.makeText(UserLoginActivity.this, "LoggedIn !", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UserLoginActivity.this, MainActivity.class));
                } else
                    Toast.makeText(UserLoginActivity.this, "LoggedIn failed", Toast.LENGTH_SHORT).show();
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
        userApi = retrofit.create(ApiRequest.class);
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
