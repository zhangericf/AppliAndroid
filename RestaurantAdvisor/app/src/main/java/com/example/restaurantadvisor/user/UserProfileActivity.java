package com.example.restaurantadvisor.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.restaurantadvisor.MainActivity;
import com.example.restaurantadvisor.R;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bLogout;
    private TextView TVLogin, TVEmail, TVName, TVFirstName, TVAge;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TVLogin = findViewById(R.id.TVLogin);
        TVEmail = findViewById(R.id.TVEmail);
        TVName = findViewById(R.id.TVName);
        TVFirstName = findViewById(R.id.TVFirstName);
        TVAge = findViewById(R.id.TVAge);
        bLogout = findViewById(R.id.btnLogout);

        bLogout.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
        displayUserDetails();
    }

    @SuppressLint("SetTextI18n")
    private void displayUserDetails() {
        User user = userLocalStore.getLoggedInUser();
        TVLogin.setText(user.login);
        TVEmail.setText(user.email);
        TVName.setText(user.name);
        TVFirstName.setText(user.firstname);
        TVAge.setText(Integer.toString(user.age));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogout) {
            userLocalStore.clearUserData();
            userLocalStore.setUserLoggedIn(false);
            startActivity(new Intent(this, UserLoginActivity.class));
        }
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

}
