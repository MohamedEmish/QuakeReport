package com.example.android.quakereport;

import java.util.Date;

public class PlaceHolder {

    private String mPrimLocation;
    private String mlocationOffset;
    private double mMagnitude;
    private String mDate;
    private int mMagnitudeColor;
    private String mUrl;

    public PlaceHolder(String locationOffset,String primLocation, double Magnitude, String Date ,int MagnitudeColor,String url) {
        mPrimLocation = primLocation;
        mlocationOffset= locationOffset;
        mMagnitude = Magnitude;
        mDate = Date;
        mMagnitudeColor = MagnitudeColor;
        mUrl= url;
    }


    public String getmPrimLocation() {
        return mPrimLocation;
    }
    public String getmlocationOffset() {
        return mlocationOffset;
    }
    public double getmMagnitude() {
        return mMagnitude;
    }
    public String getmDate() {
        return mDate;
    }
    public int getmMagnitudeColor() {
        return mMagnitudeColor;
    }

    public String getmUrl() {
        return mUrl;
    }
}

