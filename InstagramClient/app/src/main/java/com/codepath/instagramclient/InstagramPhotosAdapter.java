package com.codepath.instagramclient;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rsukhi on 10/11/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    //What data we need from the activity
    // Context, Data Source
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1 , objects);
    }

    //What item looks like
    //Use the template to display each photo
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the data item for this position
        InstagramPhoto photo = getItem(position);
        //Check if we are using a recycled view, if not we need to inflate
        if(convertView == null){
            //create a view from the template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout,parent,false);
        }
        //Lookup views for populating in the data (image, caption)
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        ImageView ivProfilePic = (ImageView) convertView.findViewById(R.id.ivProfilePic);
        //Insert the model data into each of the items
        tvCaption.setText(photo.caption);
        tvUsername.setText(photo.username);
        tvLikes.setText("❤ " + photo.likesCount + " likes");
        //Calculate Relative Time
        tvTime.setText("🕛" + DateUtils.getRelativeTimeSpanString(photo.time * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
        //Clear out the imageView
        ivPhoto.setImageResource(0);
        //Insert the image using picasso
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);

        //Profile Image
        //Clear out the imageView
        ivProfilePic.setImageResource(0);
        //Insert the image using picasso
        Picasso.with(getContext()).load(photo.profileImageUrl).into(ivProfilePic);

        //Return the created item as a view
        return convertView;

    }
}
