package com.example.jake.chance_chain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> imgStr = new ArrayList<String>();

    public ImageAdapter(Context c, List<String> str) {
        mContext = c;
        this.imgStr = str;
    }

    public int getCount() {
        return imgStr.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(250, 250));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.get().load(imgStr.get(position)).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),fsizepic.class);
                ArrayList<String> uriList = new ArrayList<>(imgStr);
                intent.putStringArrayListExtra("uri",uriList);
                intent.putExtra("pos",position);
                v.getContext().startActivity(intent);

            }
        });
        return imageView;
    }


    // references to our images
}