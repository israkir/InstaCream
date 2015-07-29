package com.hakkicaner.instacream;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto instagramPhoto = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.ivAvatar);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);

        TextView tvCreatedTime = (TextView) convertView.findViewById(R.id.tvCreatedTime);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);

        ivAvatar.setImageResource(0);
        ivPhoto.setImageResource(0);

        Picasso.with(getContext()).load(instagramPhoto.imageUrl).into(ivPhoto);
        Picasso.with(getContext()).load(instagramPhoto.avatarUrl).into(ivAvatar);

        tvCreatedTime.setText(instagramPhoto.createdTime);
        tvUsername.setText(instagramPhoto.username);
        tvLikesCount.setText(String.valueOf(instagramPhoto.likesCount) + " liked this photo");
        tvCaption.setText(instagramPhoto.caption);

        return convertView;
    }
}
