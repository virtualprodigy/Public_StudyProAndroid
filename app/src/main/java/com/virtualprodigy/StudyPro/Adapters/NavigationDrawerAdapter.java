package com.virtualprodigy.studypro.Adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.virtualprodigy.studypro.R;

import java.util.ArrayList;

/**
 * Created by virtualprodigyllc on 02/01/16.
 */
public class NavigationDrawerAdapter extends BaseAdapter {
    private ArrayList<String> navTitles;
    private TypedArray navImages;
    private Context context;

    public NavigationDrawerAdapter(Context context, ArrayList<String> navTitles, TypedArray navImages){
        this.context = context;
        this.navTitles = navTitles;
        this.navImages = navImages;
    }

    @Override
    public int getCount() {
        return navTitles.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.nav_drawer_item,null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.navTitle);
        ImageView image = (ImageView) convertView.findViewById(R.id.navImage);

        title.setText(navTitles.get(position));
        image.setImageDrawable(navImages.getDrawable(position));
        return convertView;
    }
}
