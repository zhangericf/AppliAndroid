package com.example.restaurantadvisor.restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.restaurantadvisor.R;

import java.util.List;

public class RestaurantListAdapter extends BaseAdapter {

    private Context context;
    private List<Restaurant> restaurantList;

    public RestaurantListAdapter(Context context, List restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
    }

    @Override
    public int getCount() {
        return restaurantList.size();
    }

    @Override
    public Object getItem(int position) {
        return restaurantList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.textview_row, null);

        Restaurant restaurant = restaurantList.get(position);

        TextView textViewRestaurantName = convertView.findViewById(R.id.name);
        TextView textViewRestaurantId = convertView.findViewById(R.id.id);

        textViewRestaurantName.setText(restaurant.getName());
        textViewRestaurantId.setText(Float.toString(restaurant.getGrade()));

        return convertView;
    }
}
