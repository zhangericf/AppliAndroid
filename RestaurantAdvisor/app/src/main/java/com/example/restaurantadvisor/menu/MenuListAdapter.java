package com.example.restaurantadvisor.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.restaurantadvisor.R;

import java.util.List;

public class MenuListAdapter extends BaseAdapter {

    private Context context;
    private List<MenuRestaurant> menuRestaurantList;

    public MenuListAdapter(Context context, List menuList) {
        this.context = context;
        this.menuRestaurantList = menuList;
    }

    @Override
    public int getCount() {
        return menuRestaurantList.size();
    }

    @Override
    public Object getItem(int position) {
        return menuRestaurantList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.textview_row, null);

        MenuRestaurant menuRestaurant = menuRestaurantList.get(position);

        TextView textViewRestaurantName = convertView.findViewById(R.id.name);

        textViewRestaurantName.setText(menuRestaurant.getName());

        return convertView;
    }
}
