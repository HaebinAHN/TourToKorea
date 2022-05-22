package com.teamb.tourtokorea;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostList extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlist);
        Intent recived_intent = getIntent();
        String country = recived_intent.getStringExtra("country");
        ListView listview ;


        listview = (ListView)findViewById(R.id.listView);


        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,android.R.id.text1);
        listview.setAdapter(adapter);


        FirebaseDatabase database = FirebaseDatabase.getInstance("https://tourtokorea-a7f98-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("board");
        DatabaseReference data = database.getReference();
        if(country.equals("global")) {
            data.child("board").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    FirebasePost board = snapshot.getValue(FirebasePost.class);
                    adapter.add(board.getPostTitle());
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
                        Log.d("asdf", "" + childDataSnapshot.child("postContent").getValue());
                        adapter.add(childDataSnapshot.child("postTitle").getValue());
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
                Log.d("asddsf", "" + data);
                Intent intent = new Intent(getApplicationContext(), postView.class);
                intent.putExtra("Title", data);
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
