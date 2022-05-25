package com.teamb.tourtokorea;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Posting extends AppCompatActivity {

    ArrayList<String> urilist = new ArrayList<>();
    imgUrl URLlist = new imgUrl();

    String Location=null;

    private GPS_Background gpsTracker;

    RecyclerView recyclerView;
    MultiImageAdapter adapter;

    EditText title, content;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://tourtokorea-a7f98-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference rootRef = firebaseDatabase.getReference();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        verifyStoragePermissions(this);

        gpsTracker = new GPS_Background(Posting.this);

        Intent recived_intent = getIntent();
        String country = recived_intent.getStringExtra("country");

        title = findViewById(R.id.title_et);
        content = findViewById(R.id.content_et);

        Button postingbtn = findViewById(R.id.reg_button2);
        Button img_button = findViewById(R.id.addimg);
        Button Loc_button = findViewById(R.id.addLocation);
        recyclerView = findViewById(R.id.recyclerView_img);

        img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2222);
            }
        });

        Loc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(),view);
                getMenuInflater().inflate(R.menu.location_option,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.current_loc){

                            final Geocoder geocoder = new Geocoder(getApplicationContext());

                            List<Address> list = null;

                            try {
                                double d1 = gpsTracker.getLatitude();
                                double d2 = gpsTracker.getLongitude();
                                list = geocoder.getFromLocation(
                                        d1, // 위도
                                        d2, // 경도
                                        10); // 얻어올 값의 개수
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
                            }
                            if (list != null) {
                                if (list.size()==0) {
                                    Toast.makeText(getApplicationContext(), "Failed to change address Please use input address", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d("Location",list.get(0).getAddressLine(0));
                                    Location = list.get(0).getAddressLine(0);
                                }
                            }

                        } else if(menuItem.getItemId() == R.id.input) {
                            AlertDialog.Builder ad = new AlertDialog.Builder(Posting.this);

                            ad.setMessage("Input Address :");
                            ad.setTitle("Address input");

                            final EditText et = new EditText(getApplicationContext());
                            ad.setView(et);

                            ad.setPositiveButton("clear", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Location = et.getText().toString();
                                    Toast.makeText(getApplicationContext(), "input complete", Toast.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();
                                }
                            });

                            ad.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            ad.show();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });


        postingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                String usrId = user.getUid();
                String usrCountry = MainActivity.userNation;
                String titleString = title.getText().toString();
                String contentString = content.getText().toString();
                DatabaseReference Img = rootRef.child("Img").push();

                String Imguri = Img.getKey();
                URLlist.setImgID(Imguri);

                int index;

                Post post = new Post(titleString, contentString, usrId ,usrCountry, Imguri, Location);
                rootRef.child("board").push().setValue(post);

                uploadSingleImg(urilist,Img);

                Intent intent = new Intent(getApplicationContext(), PostList.class);
                intent.putExtra("country", country);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            Toast.makeText(getApplicationContext(), "선택하신 이미지가 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            if (data.getClipData() == null) {

                Uri imageUri = data.getData();
                urilist.add(imageUri.toString());

                adapter = new MultiImageAdapter(urilist, getApplicationContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

            } else {
                ClipData clipData = data.getClipData();
                if (clipData.getItemCount() > 5) {
                    Toast.makeText(getApplicationContext(), "이미지는 최대 5장 까지 첨부 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {

                    for (int i = 0; i < clipData.getItemCount(); i++) {

                        Uri imageUri = clipData.getItemAt(i).getUri();
                        try {

                            urilist.add(
                                    imageUri.toString()
                            );

                        } catch (Exception e) {
                            Log.e("File", "File select error", e);
                        }
                    }

                    adapter = new MultiImageAdapter(urilist, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

                }
            }
        }
    }

    private String getRealPathFromUri(Uri uri)
    {
        String[] proj=  {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String url = cursor.getString(columnIndex);
        cursor.close();
        return  url;
    }

    private void uploadSingleImg(ArrayList<String> uri,DatabaseReference newPostImgRef){
        for(int i=0;i<uri.size();i++){
            try{

                String url = getRealPathFromUri(Uri.parse(uri.get(i)));

                StorageReference storageReference = storage.getReference();

                Uri file = Uri.fromFile(new File(url));
                final StorageReference riversRef = storageReference.child("images/"+file.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(file);

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful()){

                            throw task.getException();
                        }
                        return riversRef.getDownloadUrl();
                    }

                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Log.d("upload","clear!");
                            Uri downloadUrl = task.getResult();
                            URLlist.setipnut(downloadUrl.toString());
                            newPostImgRef.setValue(URLlist);
                        }else{
                            Toast.makeText(getApplicationContext(),"업로드 실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }catch(Exception e){
                Log.e("UPFILE","upload uri fail",e);
            }
        }

    }

}
