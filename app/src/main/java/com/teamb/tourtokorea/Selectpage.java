package com.teamb.tourtokorea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

public class Selectpage extends AppCompatActivity {

    private static final String TAG = "StoreData";
    private FirebaseAuth mAuth;
    Button logoutBtn;
    private boolean isExist=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectpage);

        Intent intent = getIntent();

        String userNation = intent.getStringExtra("userNation");

        logoutBtn = (Button)findViewById(R.id.logoutButton);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        /**
         * 유저 로그인 세션 유지중일때
         * 유저 데이터를 mAuth 토큰에서 가져옵니다.
         * */
        if(user != null)
        {
            String userEmail = user.getEmail();
            String userId = user.getUid();
            String userName = user.getDisplayName();

            checkUserExist(userNation, userEmail, userId, userName);
        }

        /**
         * 로그아웃 버튼 리스너
         * */
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

    /**
     * userId로 user가 db에 존재하는 지 validation
     * 만약 존재하면 새로 쓰지 않으며
     * 존재하지 않는다면 새로 쓴다.
     * */
    private void checkUserExist(String userNation, String userEmail, String userId, String userName)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("user").document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                //존재할때
                                Log.d(TAG, "User Exist then skip db write");
                            }
                            else {
                                //존재하지 않을때
                                putUserFirebase(userNation, userEmail, userId, userName);
                                Log.d(TAG, "User is not Exist then write in db");
                            }
                        }else{
                            //Database error
                            Log.d(TAG, "DATA_BASE_ERROR", task.getException());
                        }
                    }
                });

        Log.d(TAG, String.valueOf(isExist));
    }

    /**
     * 유저정보 Cloud Firestore에 저장하는 메서드
     * */
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
                //쓰기 성공
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                //쓰기 실패
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }
}