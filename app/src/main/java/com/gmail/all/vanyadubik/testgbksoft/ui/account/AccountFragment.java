package com.gmail.all.vanyadubik.testgbksoft.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gmail.all.vanyadubik.testgbksoft.R;
import com.gmail.all.vanyadubik.testgbksoft.ui.activity.LoginActivity;
import com.gmail.all.vanyadubik.testgbksoft.utils.SharedStorage;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gmail.all.vanyadubik.testgbksoft.common.Consts.TOKEN;

public class AccountFragment extends Fragment {

    @BindView(R.id.log_out)
    Button logOut;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, root);

        logOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            SharedStorage
                    .setString(getActivity(), TOKEN, null);
            startActivity(new Intent(getActivity(), LoginActivity.class));
            new Handler().postDelayed(() -> {
                getActivity().finish();
            }, 2000);
        });

        return root;
    }
}