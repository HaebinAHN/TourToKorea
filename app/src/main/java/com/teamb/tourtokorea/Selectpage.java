package com.teamb.tourtokorea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;
>>>>>>> main

public class Selectpage extends AppCompatActivity {

    private static final String TAG = "StoreData";
    private FirebaseAuth mAuth;
    Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectpage);

        Intent intent = getIntent();

        String userNation = intent.getStringExtra("userNation");

        logoutBtn = (Button)findViewById(R.id.logoutButton);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        if(user != null)
        {
            String userEmail = user.getEmail();
            String userId = user.getUid();
            String userName = user.getDisplayName();
            putUserFirebase(userNation, userEmail, userId, userName);
        }

        logoutBtn.setOnClickListener(view -> {
            signOut();
            finishAffinity();
        });
        
        Button commubtn = findViewById(R.id.commubtn);

        commubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), communitymain.class);
                startActivity(intent);
            }
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

    private void putUserFirebase(String userNation, String userEmail, String userId, String userName)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userNation", userNation);
        userInfo.put("userEmail", userEmail);
        userInfo.put("userId", userId);
        userInfo.put("userName", userName);

        db.collection("user").document(userId)
                .set(userInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}