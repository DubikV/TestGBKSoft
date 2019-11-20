package com.gmail.all.vanyadubik.testgbksoft.ui.map;

import com.gmail.all.vanyadubik.testgbksoft.model.Point;
import com.gmail.all.vanyadubik.testgbksoft.repository.RepositoryDB;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapViewModel extends ViewModel {

    private MutableLiveData<List<Point>> list;
    private RepositoryDB repositoryDB;

    public MapViewModel() {
        list = new MutableLiveData<>();

        repositoryDB = RepositoryDB.getInstance();
        repositoryDB.addDBListener(new RepositoryDB.DBListener() {
            @Override
            public void onDataChange(List<Point> listResult) {
                list.postValue(listResult);
            }

            @Override
            public void onError() {

            }
        });
    }

    public LiveData<List<Point>> getList() {
        return list;
    }

    public void addPoint(Point point) {
        repositoryDB.createPoint(point);
    }
}