package com.example.restaurantadvisor.comments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.restaurantadvisor.ApiRequest;
import com.example.restaurantadvisor.MainActivity;
import com.example.restaurantadvisor.R;
import com.example.restaurantadvisor.restaurant.RestaurantLocalStore;
import com.example.restaurantadvisor.user.User;
import com.example.restaurantadvisor.user.UserLocalStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentsListAdapter extends BaseAdapter {

    private final String TAG = "CommentsActivity";
    private MainActivity mainActivity = new MainActivity();
    private final String BASE_URL = mainActivity.getBASE_URL();
    private Retrofit retrofit;
    private Context context;
    private List<Comments> commentsList;
    private List<User> userList;
    private ApiRequest commentsApi;
    private String name;
    private UserLocalStore userLocalStore;
    private RestaurantLocalStore restaurantLocalStore;

    public CommentsListAdapter(Context context, List commentsList, List usersList) {
        this.context = context;
        this.commentsList = commentsList;
        this.userList = usersList;
        this.retrofitConfig();
        userLocalStore = new UserLocalStore(context);
        restaurantLocalStore = new RestaurantLocalStore(context);
    }

    @Override
    public int getCount() {
        return commentsList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.textview_row2, null);

        final Comments comments = commentsList.get(position);
        final Button button = convertView.findViewById(R.id.delete);

        for (User user: userList) {
            if (comments.getUser_id() == user.getId()) {
                name = user.getName();
            }
        }

        if (comments.getUser_id() == userLocalStore.getLoggedInUser().getId())
            button.setVisibility(View.VISIBLE);
        else
            button.setVisibility(View.GONE);

        putComments(comments, convertView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteComment(restaurantLocalStore.getRestaurant().getId(), comments.getId());
            }
        });

        return convertView;
    }

    public void deleteComment(int restoId, int id) {
        commentsApi.deleteComments(restoId, id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "onResponse : " + response.code() + " " + response.message());
                if (response.message().equals("OK")) {
                    Toast.makeText(context, "Deleting successful", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, CommentsActivity.class));
                } else
                    Toast.makeText(context, "Deleting failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "onFailure " + t.getMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void putComments(Comments comments, View convertView) {
        TextView tvCommentsName = convertView.findViewById(R.id.name);
        TextView tvCommentsContent = convertView.findViewById(R.id.content);
        TextView tvCommentsGrade = convertView.findViewById(R.id.grade);
        tvCommentsName.setText(name + " :");
        tvCommentsContent.setText(comments.getContent());
        tvCommentsGrade.setText(comments.getGrade() +" / 10");
        if(comments.getGrade() == 10)
            tvCommentsGrade.setText(10 +" / 10");
    }

    private void retrofitConfig() {
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        commentsApi = retrofit.create(ApiRequest.class);
    }

}
