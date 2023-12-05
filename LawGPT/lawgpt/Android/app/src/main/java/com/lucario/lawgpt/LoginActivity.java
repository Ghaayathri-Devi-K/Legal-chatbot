package com.lucario.lawgpt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;

import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LoginActivity extends Fragment {
    Button loginButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_activity, container, false);
        loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v ->{ startActivity(new Intent(getActivity(), ChatView.class));
            System.out.println("sdjfskjdhfksdjfh");});
        return view;
    }
}