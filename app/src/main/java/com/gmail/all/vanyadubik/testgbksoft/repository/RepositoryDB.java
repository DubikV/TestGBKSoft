package com.gmail.all.vanyadubik.testgbksoft.repository;

import android.util.Log;

import com.gmail.all.vanyadubik.testgbksoft.model.Point;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.gmail.all.vanyadubik.testgbksoft.db.FireBaseDbContract.NANE_TABLE;
import static com.gmail.all.vanyadubik.testgbksoft.db.FireBaseDbContract.NANE_TABLE_COLUM_LOCATION;
import static com.gmail.all.vanyadubik.testgbksoft.db.FireBaseDbContract.NANE_TABLE_COLUM_NAME;

public class RepositoryDB {

    private static RepositoryDB repositoryDB;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private RepositoryDB() {

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

    }

    public static RepositoryDB getInstance() {
        if(repositoryDB == null){
            repositoryDB = new RepositoryDB();
        }

        return repositoryDB;
    }

    public void addDBListener(DBListener dbListener) {

        if(dbListener==null){
            mDatabaseReference.child(NANE_TABLE).addValueEventListener(null);
        }else {
            mDatabaseReference.child(NANE_TABLE)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<Point> list = new ArrayList<>();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Point point = postSnapshot.getValue(Point.class);
                                list.add(point);
                            }
                            if (dbListener != null) {
                                dbListener.onDataChange(list);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, databaseError.getMessage());
                            if (dbListener != null) {
                                dbListener.onError();
                            }

                        }
                    });
        }
    }

    public void deletePoint(Point point) {
        mDatabaseReference.child(NANE_TABLE)
                .child(point.getName())
                .removeValue();
    }

    public void updateUser(Point point) {
        mDatabaseReference.child(NANE_TABLE)
                .child(point.getName())
                .child(NANE_TABLE_COLUM_NAME)
                .setValue(point.getName());
        mDatabaseReference.child(NANE_TABLE)
                .child(point.getName())
                .child(NANE_TABLE_COLUM_LOCATION)
                .setValue(point);
    }

    public void createPoint(Point point) {
       mDatabaseReference.child(NANE_TABLE).child(point.getName()).setValue(point);
    }

    public interface DBListener{
        void onDataChange(List<Point> list);

        void onError();
    }

}
