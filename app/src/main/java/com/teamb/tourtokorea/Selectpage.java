package com.teamb.tourtokorea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Selectpage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button logoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectpage);

        logoutBtn = (Button)findViewById(R.id.logoutButton);

        mAuth = FirebaseAuth.getInstance();

        logoutBtn.setOnClickListener(view -> {
            signOut();
            finishAffinity();
        });
    }
    /**
     * 로그아웃 메서드
     * */
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }
    
    /**
     * 회원탈퇴 메서드
     * */
    private void revokeAccess() {
        mAuth.getCurrentUser().delete();
    }
}