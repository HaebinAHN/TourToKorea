package com.teamb.tourtokorea;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommunitySelect extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://tourtokorea-a7f98-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference rootRef = firebaseDatabase.getReference();

    ArrayAdapter<String> arrayAdapter;
    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();
    private String commNation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        //spinner adapter
        Spinner spinner = (Spinner) findViewById(R.id.spn_Nation);
        ArrayAdapter<CharSequence> sAdapter = ArrayAdapter.createFromResource(this, R.array.nationList, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sAdapter);

        Button globalbtn = findViewById(R.id.globalbtn);

        Button userbtn = findViewById(R.id.userbtn);

        Button otherbtn = findViewById(R.id.otherbtn);

        //유저 국가 게시판으로 이동
        userbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PostList.class);
                intent.putExtra("country", MainActivity.userNation);
                startActivity(intent);

            }
        });

        //스피너로 선택한 국가 게시판으로 이동
        otherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PostList.class);
                intent.putExtra("country", commNation);
                startActivity(intent);

            }
        });


    //글로벌 게시판으로 이동
        globalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PostList.class);
                intent.putExtra("country", "global");
                startActivity(intent);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                commNation = (String) sAdapter.getItem(i);
                Log.d("Nation check", commNation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}
