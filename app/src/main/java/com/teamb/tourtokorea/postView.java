package com.teamb.tourtokorea;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class postView extends AppCompatActivity {

    TextView text1;
    TextView text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postview);
        Intent recived = getIntent();
        String Title = recived.getStringExtra("Title");

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://tourtokorea-a7f98-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("board");
        text1 = (TextView)findViewById(R.id.title_tv);
        text2 = (TextView)findViewById(R.id.content_tv);

        myRef.orderByChild("postTitle").equalTo(Title).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                  text1.setText(childDataSnapshot.child("postTitle").getValue().toString());
                  text2.setText(childDataSnapshot.child("postContent").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
