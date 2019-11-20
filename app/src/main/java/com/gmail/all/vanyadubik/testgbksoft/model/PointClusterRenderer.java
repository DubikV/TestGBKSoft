package com.gmail.all.vanyadubik.testgbksoft.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.gmail.all.vanyadubik.testgbksoft.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import androidx.core.content.ContextCompat;

public class PointClusterRenderer extends DefaultClusterRenderer<ClusterPoint> {

  private final IconGenerator mClusterIconGenerator;
  private final Context mContext;
  private final LatLng mInfoPoint;

  public PointClusterRenderer(Context context, GoogleMap map,
                              ClusterManager<ClusterPoint> clusterManager, LatLng infoPoint) {
    super(context, map, clusterManager);

    mContext = context;
    mClusterIconGenerator = new IconGenerator(mContext.getApplicationContext());
    mInfoPoint = infoPoint;
  }

  @Override
  protected void onBeforeClusterItemRendered(ClusterPoint item,
                                             MarkerOptions markerOptions) {
    markerOptions.icon(item.getMarker().getIcon());
  }

  @Override
  protected void onBeforeClusterRendered(Cluster<ClusterPoint> cluster,
                                         MarkerOptions markerOptions) {

    mClusterIconGenerator.setBackground(
        ContextCompat.getDrawable(mContext, R.drawable.background_circle));

    mClusterIconGenerator.setTextAppearance(R.style.AppTheme_WhiteTextAppearance);

    final Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
  }

  @Override
  protected void onClusterItemRendered(ClusterPoint clusterItem, Marker marker) {
    super.onClusterItemRendered(clusterItem, marker);

    if(mInfoPoint!=null){
      if(mInfoPoint.longitude == clusterItem.getPosition().longitude &&
              mInfoPoint.latitude == clusterItem.getPosition().latitude){
        getMarker(clusterItem).showInfoWindow();
      }
    }
  }
}