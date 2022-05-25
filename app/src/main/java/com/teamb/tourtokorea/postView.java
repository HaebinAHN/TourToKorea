package com.teamb.tourtokorea;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class postView extends AppCompatActivity {

    TextView text1;
    TextView text2;
    imgUrl urls;
    ArrayList<String> s_uri = new ArrayList<>();

    String Location ;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postview);
        Intent recived = getIntent();
        String Title = recived.getStringExtra("Title");
        String imgId = recived.getStringExtra("ImgID");
        Location = recived.getStringExtra("Loc");

        Button Loc_button = findViewById(R.id.Location_view);

        if (Location == null){
            Loc_button.setVisibility(View.INVISIBLE);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView_content);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://tourtokorea-a7f98-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("board");
        text1 = (TextView)findViewById(R.id.title_tv);
        text2 = (TextView)findViewById(R.id.content_tv);

        Loc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),diplomatic_mission.class);
                intent.putExtra("adr",Location);
                startActivity(intent);
            }
        });


        DatabaseReference imgRefer = database.getReference("Img");
        Query myImg = imgRefer.orderByChild("imgID").equalTo(imgId);

        myImg.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                urls = snapshot.getValue(imgUrl.class);
                s_uri = urls.getArr();

                MultiImageAdapter adapter = new MultiImageAdapter(s_uri, getApplicationContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, true));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
