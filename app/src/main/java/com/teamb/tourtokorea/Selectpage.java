package com.teamb.tourtokorea;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Selectpage extends AppCompatActivity {

    private String userNation;
    private static final String TAG = "StoreData";
    private FirebaseAuth mAuth;
    Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectpage);

        Intent intent = getIntent();

        String userNation = intent.getStringExtra("userNation");
        int selectpage = intent.getIntExtra("user_nation_index",0);

        WebView web = findViewById(R.id.webView);

        web.setWebChromeClient(new WebChromeClient());
        WebSettings webset = web.getSettings();
        webset.setJavaScriptEnabled(true);
        webset.setAllowContentAccess(true);
        webset.setAllowFileAccess(true);

        web.loadUrl("file:///android_asset/USD.html");

        String[] kor_list = getResources().getStringArray(R.array.nationkorList);

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
                Intent intent = new Intent(getApplicationContext(), CommunitySelect.class);
                startActivity(intent);
            }
        });


        Button directionbtn = findViewById(R.id.direction);

        directionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DirectionActivity.class);
                startActivity(intent);
            }
        });

        Button diplomaticbtn = findViewById(R.id.diplomatic);

        diplomaticbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Search_list.class);
                intent.putExtra("search",kor_list[selectpage]);
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