package com.gmail.all.vanyadubik.testgbksoft.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterPoint implements ClusterItem {
    private LatLng mPosition;
    private String mTitle;

    protected MarkerOptions marker;

    public ClusterPoint(double lat, double lng, String title) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
    }


    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return "";
    }

    public MarkerOptions getMarker() {
        return marker;
    }

    public void setMarker(MarkerOptions marker) {
        this.marker = marker;
    }
}