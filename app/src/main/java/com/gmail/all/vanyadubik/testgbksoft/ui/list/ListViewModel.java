package com.gmail.all.vanyadubik.testgbksoft.ui.list;

import com.gmail.all.vanyadubik.testgbksoft.model.Point;
import com.gmail.all.vanyadubik.testgbksoft.repository.RepositoryDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListViewModel extends ViewModel {

    private MutableLiveData<List<Point>> list;
    private RepositoryDB repositoryDB;

    public ListViewModel() {

        list = new MutableLiveData<>();

        repositoryDB = RepositoryDB.getInstance();
        repositoryDB.addDBListener(new RepositoryDB.DBListener() {
            @Override
            public void onDataChange(List<Point> listResult) {

                Collections.sort(listResult, (o1, o2) ->{
                    return o1.getName().compareTo(o2.getName());
                });

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

    public void detelePoint(Point point) {
        repositoryDB.deletePoint(point);
    }
}