package com.teamb.tourtokorea;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PostList extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlist);
        Intent recived_intent = getIntent();
        String country = recived_intent.getStringExtra("country");
        ListView listview ;

        String ImgID;

        listview = (ListView)findViewById(R.id.listView);

        ArrayList<String> ImgList = new ArrayList<>();
        ArrayList<String> LocationList = new ArrayList<>();

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,android.R.id.text1);
        listview.setAdapter(adapter);


        FirebaseDatabase database = FirebaseDatabase.getInstance("https://tourtokorea-a7f98-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("board");
        DatabaseReference data = database.getReference();
        if(country.equals("global")) {
            data.child("board").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Post board = snapshot.getValue(Post.class);
                    adapter.add(board.getPostTitle());
                    ImgList.add(board.getImgID());
                    LocationList.add(board.getLocation());

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
        }else{
            myRef.orderByChild("userCountry").equalTo(country).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        Log.d("asdf", "PARENT: " + childDataSnapshot.getKey());
                        Log.d("asdf", "" + childDataSnapshot.child("postTitle").getValue().toString());
                        Log.d("asdf",""+childDataSnapshot.child("ImgID").getValue());
                        Log.d("asdf", "" + childDataSnapshot.child("postContent").getValue());
                        adapter.add(childDataSnapshot.child("postTitle").getValue());

                        String Img;

                        if(childDataSnapshot.child("imgID").getValue() != null){
                            Img = childDataSnapshot.child("imgID").getValue().toString();
                        }else{
                            Img = null;
                        }

                        ImgList.add(Img);

                        String Location;

                        if(childDataSnapshot.child("location").getValue() != null){
                            Location = childDataSnapshot.child("location").getValue().toString();
                        }else{
                            Location = null;
                        }

                        LocationList.add(Location);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String data = (String) adapterView.getItemAtPosition(position);
                String Img = (String) ImgList.get(position);
                String Loc = (String) LocationList.get(position);
                Log.d("asddsf", "" + data);
                Intent intent = new Intent(getApplicationContext(), postView.class);
                intent.putExtra("Title", data);
                intent.putExtra("ImgID",Img);
                intent.putExtra("Loc",Loc);
                startActivity(intent);
            }
        });



        Button postbtn = findViewById(R.id.reg_button);


        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Posting.class);
                intent.putExtra("country", country);
                startActivity(intent);
            }
        });



    }
}
