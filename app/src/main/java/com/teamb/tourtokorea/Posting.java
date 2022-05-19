package com.teamb.tourtokorea;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Posting extends AppCompatActivity {

    EditText title, content;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://tourtokorea-a7f98-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference rootRef = firebaseDatabase.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        title = findViewById(R.id.title_et);
        content = findViewById(R.id.content_et);

        Button postingbtn = findViewById(R.id.reg_button2);



        postingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                String usrId = user.getUid();
                String usrCountry = MainActivity.userNation;
                String titleString = title.getText().toString();
                String contentString = content.getText().toString();

                Post post = new Post(titleString, contentString, usrId ,usrCountry);
                rootRef.child("/Post/").push().setValue(post);

                Intent intent = new Intent(getApplicationContext(), PostList.class);
                startActivity(intent);
            }
        });
    }

    public class Post {
        public String postTitle;
        public String postContent;
        public String userId;
        public String userCountry;

        public Post() {}

        public Post(String postTitle, String postContent, String userId, String userCountry) {
            this.postTitle = postTitle;
            this.postContent = postContent;
            this.userId = userId;
            this.userCountry = userCountry;
        }
    }

    public void clickSave(View view) {
        String data = title.getText().toString();
        rootRef.setValue(data);

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               String data = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
