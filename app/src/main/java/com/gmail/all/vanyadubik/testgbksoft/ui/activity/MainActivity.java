package com.gmail.all.vanyadubik.testgbksoft.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.gmail.all.vanyadubik.testgbksoft.R;
import com.gmail.all.vanyadubik.testgbksoft.model.Point;
import com.gmail.all.vanyadubik.testgbksoft.utils.SharedStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.parceler.Parcels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gmail.all.vanyadubik.testgbksoft.common.Consts.TAG;
import static com.gmail.all.vanyadubik.testgbksoft.common.Consts.TOKEN;
import static com.gmail.all.vanyadubik.testgbksoft.ui.map.MapFragment.START_POINT;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.nav_view)
    BottomNavigationView navView;

    private FirebaseAuth mAuth;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportActionBar().hide();

        initFirebase();

        firebaseAuthWithGoogle();

        initNavigationView();
    }

    private void initFirebase() {

        mAuth = FirebaseAuth.getInstance();

        FirebaseApp.initializeApp(this);
    }

    private void firebaseAuthWithGoogle() {

        String token = SharedStorage
                .getString(MainActivity.this, TOKEN, null);

        Log.d(TAG, "firebaseAuthWithGoogle:" + token);

        if(!TextUtils.isEmpty(token)) {
            AuthCredential credential = GoogleAuthProvider.getCredential(token, null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                initNavigationView();
                            } else {
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                new Handler().postDelayed(() -> {
                                    finish();
                                }, 2000);
                            }

                            // ...
                        }
                    });
        }else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            new Handler().postDelayed(() -> {
                finish();
            }, 2000);
        }
    }

    private void initNavigationView(){
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_list, R.id.navigation_map, R.id.navigation_account)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void showMapView(Point point){

        Bundle bundle = new Bundle();
        bundle.putParcelable(START_POINT, Parcels.wrap(point));
        navController.navigate(R.id.navigation_map, bundle);

    }

}

