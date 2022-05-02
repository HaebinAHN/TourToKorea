package com.teamb.tourtokorea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class diplomatic_mission extends AppCompatActivity implements OnMapReadyCallback{

    String dipLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diplomatic_mission);
/*
        GetOpen_api_data api = null;

        try {
            api = new GetOpen_api_data(
                    "https://apis.data.go.kr/1262000/InkoEmblgbdInfoService/getInkoEmblgbdInfoList?serviceKey=z70jaurFdFd6Ead0U90sxj3i%2BEKOAUEyNLMKHHKOFwuYtd5LiVKqkFeQJpxhlF6TUanosi558lywuejHBCCVfQ%3D%3D&pageNo=1&numOfRows=10&cond[embassy_nm::LIKE]="+ URLEncoder.encode("가나","UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            String result = api.execute().get();

            Log.e("test","http result : " + result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/
        Intent received_intent = getIntent();

        dipLocation = received_intent.getStringExtra("adr");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_Location);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        final Geocoder geocoder = new Geocoder(this);
        String marker_name = null;

        List<Address> list = null;

        double lat = 0;
        double lon = 0;

        TextView adr = (TextView) findViewById(R.id.Location_adr);

        if(dipLocation == null){

            lat = 37.5666805;
            lon = 126.9784147;
            marker_name = "Seoul City Hall";
            adr.setText("No location information available.");

        }else{

            try {
                list = geocoder.getFromLocationName
                        (dipLocation, // 지역 이름
                                10); // 읽을 개수
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
                lat = 37.5666805;
                lon = 126.9784147;
                adr.setText("No location information available.");
                marker_name = "Seoul City Hall";
            }

            if (list != null) {
                if (list.size() == 0) {
                    Log.e("test", "해당되는 주소 정보는 없습니다");
                    lat = 37.5666805;
                    lon = 126.9784147;
                    adr.setText("No location information available.");
                    marker_name = "Seoul City Hall";
                } else {
                    // 해당되는 주소로 인텐트 날리기
                    Address addr = list.get(0);
                    lat = addr.getLatitude();
                    lon = addr.getLongitude();
                    marker_name = "Destination Location";
                }

            }

        }

        LatLng Location = new LatLng(lat, lon);
        googleMap.addMarker(new MarkerOptions()
                .position(Location)
                .title(marker_name));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(Location));
    }
}