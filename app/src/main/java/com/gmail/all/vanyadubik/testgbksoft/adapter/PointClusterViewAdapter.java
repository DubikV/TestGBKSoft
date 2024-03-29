package com.gmail.all.vanyadubik.testgbksoft.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gmail.all.vanyadubik.testgbksoft.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class PointClusterViewAdapter implements GoogleMap.InfoWindowAdapter {

  private final LayoutInflater mInflater;

  public PointClusterViewAdapter(LayoutInflater inflater) {
    this.mInflater = inflater;
  }

  @Override
  public View getInfoWindow(Marker marker) {
    return null;
  }

  @Override
  public View getInfoContents(Marker marker) {
    final View popup = mInflater.inflate(R.layout.info_window_layout, null);

    ((TextView) popup.findViewById(R.id.title)).setText(marker.getTitle());

    return popup;
  }
}