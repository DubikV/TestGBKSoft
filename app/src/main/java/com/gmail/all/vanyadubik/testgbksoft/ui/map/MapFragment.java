package com.gmail.all.vanyadubik.testgbksoft.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.all.vanyadubik.testgbksoft.R;
import com.gmail.all.vanyadubik.testgbksoft.adapter.PointClusterViewAdapter;
import com.gmail.all.vanyadubik.testgbksoft.model.ClusterPoint;
import com.gmail.all.vanyadubik.testgbksoft.model.Point;
import com.gmail.all.vanyadubik.testgbksoft.model.PointClusterRenderer;
import com.gmail.all.vanyadubik.testgbksoft.utils.ActivityUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import org.parceler.Parcels;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private static final int REQUEST_PERMISSIONS = 556;
    public static final String START_POINT = "start_point";

    GoogleMap mMap;

    private MapViewModel mapViewModel;
    private LocationManager locationManager;
    private Point startPoint;
    private ClusterManager<ClusterPoint> mClusterManager;
    private boolean showInfoWindow;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapViewModel =
                ViewModelProviders.of(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        mapViewModel.getList().observe(this, (points)->{
             showListPoints(points);
        });

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationFound();
        }else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS);
        }

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_PERMISSIONS){
            startLocationFound();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            SupportMapFragment mapFragment = (SupportMapFragment)
                    getChildFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }

            try {
                startPoint = Parcels.unwrap(getArguments().getParcelable(START_POINT));
            }catch (Exception e){

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMyLocationEnabled(true);

        mMap.setOnMapLongClickListener((point)->{
            ActivityUtils.showDialogText(getActivity(), getString(R.string.name), text -> {
                mapViewModel.addPoint(new Point(text, point.latitude, point.longitude));
            });
        });
        if(startPoint!=null){
            LatLng latLng = new LatLng(startPoint.getLatitude(), startPoint.getLongitude());
            moveCamera(latLng);
        }

        setUpClusterManager();
    }

    @Override
    public void onLocationChanged(Location location) {

        if(startPoint==null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            moveCamera(latLng);
        }
        locationManager.removeUpdates(this);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @SuppressLint("MissingPermission")
    private void startLocationFound(){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 10, this);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                this);
    }

    private void moveCamera(LatLng latLng){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
    }

    private MarkerOptions showMarker(Point point){

        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(point.getLatitude(), point.getLongitude()))
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_marker))
                .anchor(0.5f, 1)
                .title(point.getName());

        return markerOptions;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void showListPoints(List<Point> points){

        mMap.clear();
        for(Point point: points){
            MarkerOptions markerOptions = showMarker(point);
            if(points.size()>3){
                ClusterPoint offsetItem = new ClusterPoint(point.getLatitude(), point.getLongitude(), point.getName());
                offsetItem.setMarker(markerOptions);
                mClusterManager.addItem(offsetItem);
                mClusterManager.cluster();
            }
        }
    }

    private void setUpClusterManager(){
        mClusterManager = new ClusterManager<ClusterPoint>(getActivity(), mMap);

        LatLng startLocation = null;
        if(startPoint!=null){
            startLocation = new LatLng(startPoint.getLatitude(), startPoint.getLongitude());
        }

        final PointClusterRenderer renderer = new PointClusterRenderer(getActivity(), mMap, mClusterManager,
                startLocation);
        mClusterManager.setRenderer(renderer);

        mClusterManager.getMarkerCollection()
                .setOnInfoWindowAdapter(new PointClusterViewAdapter(LayoutInflater.from(getActivity())));

        mClusterManager.setOnClusterItemClickListener((clusterItem)-> {
            Marker marker = renderer.getMarker(clusterItem);
//            if (marker.isInfoWindowShown()) {
            if (showInfoWindow) {
                new Runnable() { @Override public void run() {
                    marker.hideInfoWindow();
                }};
                showInfoWindow = false;
            } else {
                showInfoWindow = true;
            }
            return false;
        });

        mClusterManager.setOnClusterClickListener((cluster)-> {
            new Runnable() { @Override public void run() {
                moveCamera(cluster.getPosition());
            }};
            return false;
        });

        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnMarkerClickListener(mClusterManager);

    }
}