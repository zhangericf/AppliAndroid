package com.example.restaurantadvisor.comments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantadvisor.ApiRequest;
import com.example.restaurantadvisor.MainActivity;
import com.example.restaurantadvisor.R;
import com.example.restaurantadvisor.restaurant.Restaurant;
import com.example.restaurantadvisor.restaurant.RestaurantActivity;
import com.example.restaurantadvisor.restaurant.RestaurantLocalStore;
import com.example.restaurantadvisor.user.User;
import com.example.restaurantadvisor.user.UserLocalStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentsActivity extends AppCompatActivity {

    private final String TAG = "CommentsActivity";
    private MainActivity mainActivity = new MainActivity();
    private final String BASE_URL = mainActivity.getBASE_URL();

    private ListView listView;
    private TextView username;
    private EditText gradeET, comment;
    private Button btnComments;
    private CommentsListAdapter listAdapter;
    private Retrofit retrofit;
    private ApiRequest commentsApi;
    private List<Comments> commentsList;
    private boolean loggedIn;
    private UserLocalStore userLocalStore;
    private Restaurant restaurant;
    private float grade;
    private String currentComment;
    private float currentGrade = 0;
    private List<User> userList;
    private RestaurantLocalStore restaurantLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        this.username = findViewById(R.id.username);
        this.comment = findViewById(R.id.comment);
        this.gradeET = findViewById(R.id.grade);
        this.commentsList = new ArrayList<>();
        this.userList = new ArrayList<>();
        this.listView = findViewById(R.id.listView);
        this.listAdapter = new CommentsListAdapter(getApplicationContext(), commentsList, userList);
        this.listView.setAdapter(listAdapter);
        this.userLocalStore = new UserLocalStore(this);
        this.loggedIn = userLocalStore.getUserLoggedIn();
        this.btnComments = findViewById(R.id.btnComments);
        this.restaurantLocalStore = new RestaurantLocalStore(this);
        this.retrofitConfig();

        if (loggedIn) {
            this.username.setText(userLocalStore.getLoggedInUser().getName());
            username.setVisibility(View.VISIBLE);
            comment.setVisibility(View.VISIBLE);
            gradeET.setVisibility(View.VISIBLE);
            btnComments.setVisibility(View.VISIBLE);
            findViewById(R.id.TV10).setVisibility(View.VISIBLE);
        }

        //region IntentData
        restaurant = restaurantLocalStore.getRestaurant();
        //endregion

        getUserVieAPI();
        getCommentsViewAPI();

        btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentComment = comment.getText().toString();
                if (!gradeET.getText().toString().equals(""))
                    currentGrade = Float.parseFloat(gradeET.getText().toString());
                if (currentComment.equals("") || currentGrade > 10 || currentGrade == 0) {
                    if (currentComment.equals("")) {
                        comment.setError("Please enter comment");
                        comment.requestFocus();
                    }
                    if (currentGrade == 0) {
                        gradeET.setError("Grade is required");
                        gradeET.requestFocus();
                    }
                    if (currentGrade > 10) {
                        gradeET.setError("Grade must be under 10");
                        gradeET.requestFocus();
                    }
                } else
                    postComment();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Comments comments = commentsList.get(position);
                Log.d(TAG, "Comments ID : " + comments.getId());
            }
        });

    }

    private void postComment() {
        commentsApi.postComments(restaurant.getId(), currentComment, currentGrade, userLocalStore.getLoggedInUser().getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse : " + response.code() + " " + response.message());
                if (response.message().equals("Created")) {
                    Toast.makeText(CommentsActivity.this, "Commenting successful", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(CommentsActivity.this, CommentsActivity.class));
                } else
                    Toast.makeText(CommentsActivity.this, "Commenting failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure : " + t.getMessage());
            }
        });
    }

    private void getUserVieAPI() {
        commentsApi.getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d(TAG, "getUser onResponse " + response.code() + " " + response.message());
                List<User> users = response.body();
                boolean isEmpty = true;

                try {
                    isEmpty = !users.isEmpty();
                } catch (NullPointerException e) {
                    e.getMessage();
                }

                if (isEmpty) {
                    try {
                        userList.addAll(users);
                    } catch (NullPointerException e) {
                        e.getMessage();
                    }
                } else {
                    Log.d(TAG, "onResponse : Users is empty" + response.body());
                }

            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e(TAG, "onFailure " + t.getMessage());
            }
        });
    }

    private void changeRestaurantGrade(final float grade) {
        commentsApi.putRestaurant(restaurant.getId(), restaurant.getName(), restaurant.getDescription(), grade, restaurant.getLocalization(), restaurant.getPhone_number(), restaurant.getWebsite(), restaurant.getHours())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        restaurant.setGrade(grade);
                        Log.d(TAG, "changeGrade onResponse " + response.code() + " " + response.message());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, "onFailure " + t.getMessage());
                    }
                });
    }

    private void getCommentsViewAPI() {
        commentsApi.getComments(restaurant.getId()).enqueue(new Callback<List<Comments>>() {
            @Override
            public void onResponse(Call<List<Comments>> call, Response<List<Comments>> response) {
                Log.d(TAG, "getComments onResponse " + response.code() + " " + response.message());
                List<Comments> commentsList1 = response.body();
                boolean isEmpty = true;

                try {
                    isEmpty = !commentsList1.isEmpty();
                } catch (NullPointerException e) {
                    e.getMessage();
                }

                if (isEmpty) {
                    int i = 0;
                    try {
                        for (Comments comments : commentsList1) {
                            commentsList.add(comments);
                            grade += comments.getGrade();
                            i++;
                        }
                    } catch (NullPointerException e) {
                        e.getMessage();
                    }
                    grade = grade/i;
                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.CEILING);
                    grade = Float.parseFloat(df.format(grade));
                    listAdapter.notifyDataSetChanged();
                    changeRestaurantGrade(grade);
                } else {
                    Log.d(TAG, "onResponse : Comments is empty" + response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Comments>> call, Throwable t) {
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
        commentsApi = retrofit.create(ApiRequest.class);
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
            restaurantLocalStore.clearRestaurantData();
            restaurantLocalStore.storeRestaurantData(restaurant);
            startActivity(new Intent(this, RestaurantActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //endregion
}
