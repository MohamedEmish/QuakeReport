package com.example.android.quakereport;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import java.text.NumberFormat;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;

import java.util.ArrayList;

public class PlaceHolderAdapter extends ArrayAdapter<PlaceHolder> {

    private static final String LOG_TAG = PlaceHolderAdapter.class.getSimpleName();

    public PlaceHolderAdapter(Activity context, ArrayList<PlaceHolder> placeArrayList) {
        super(context, 0, placeArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_list, parent, false);
        }
        PlaceHolder currentPlace = getItem(position);


        TextView placeMagnitudeTextView = (TextView) listItemView.findViewById(R.id.magnitude);
        double magi=currentPlace.getmMagnitude();
        String magnit = String.valueOf(magi);
        placeMagnitudeTextView.setText(magnit);


        TextView locationOffsetTextView = (TextView) listItemView.findViewById(R.id.locationOffset);
        locationOffsetTextView.setText(currentPlace.getmlocationOffset());


        TextView primLocationTextView = (TextView) listItemView.findViewById(R.id.primaryPlace);
        primLocationTextView.setText(currentPlace.getmPrimLocation());


        TextView placeDateTextView = (TextView) listItemView.findViewById(R.id.date);
        placeDateTextView.setText(currentPlace.getmDate());

        View magnitudeView = (View) listItemView.findViewById(R.id.magnitude);
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentPlace.getmMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        View textContainer = listItemView.findViewById(R.id.text_container);

        // Return the whole list item layout (containing 2 TextViews)
        // so that it can be shown in the ListView
        return listItemView;


    }
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}

