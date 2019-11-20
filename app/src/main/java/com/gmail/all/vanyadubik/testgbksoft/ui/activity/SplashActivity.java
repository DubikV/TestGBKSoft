package com.gmail.all.vanyadubik.testgbksoft.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.gmail.all.vanyadubik.testgbksoft.utils.PermissionUtils;
import com.gmail.all.vanyadubik.testgbksoft.utils.SharedStorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.gmail.all.vanyadubik.testgbksoft.common.Consts.TOKEN;


public class SplashActivity  extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 555;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!PermissionUtils.checkPermissions(this, REQUEST_PERMISSIONS)){
            startActivity();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_PERMISSIONS){

            startActivity();
        }
    }

    private void startActivity(){
        if(TextUtils.isEmpty(SharedStorage
                .getString(SplashActivity.this, TOKEN, null))){
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        new Handler().postDelayed(() -> {
            finish();
        }, 2000);
    }
}
