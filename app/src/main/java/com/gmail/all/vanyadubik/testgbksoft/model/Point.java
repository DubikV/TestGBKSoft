package com.gmail.all.vanyadubik.testgbksoft.model;

import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(Parcel.Serialization.BEAN)
@IgnoreExtraProperties
public class Point {
    public String name;
    public double latitude;
    public double longitude;

    public Point() {}

    @ParcelConstructor
    public Point(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
