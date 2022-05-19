package com.teamb.tourtokorea;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class DirectionActivity extends AppCompatActivity implements OnMapReadyCallback{

    // 구글맵 길찾기 정보 호출을 위한 액티비티.

    // GPS로 현재 위치 파악하기 위한 클래스 -> GPS 사용 권한 확인 메소드는 메인 액티비티 부분에 따로 정의되어 있음.
    private GPS_Background gpsTracker;

    // 목적지 출발지 스트링.
    String daddr = null;
    String laddr = null;

    // 구글 맵 마커 스트링.
    String marker_name = null;

    //위도 경도
    double lat = 0;
    double lon = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        //인텐트 정보에서 목적지 출발지 정보를 확인.
        Intent recived_intent = getIntent();
        daddr = recived_intent.getStringExtra("daddr");
        laddr = recived_intent.getStringExtra("laddr");

        // GPS 클래스 객체 생성.
        gpsTracker = new GPS_Background(DirectionActivity.this);

        // 구글맵 길찾기 클래스 생성.
        Google_map_call map_call = new Google_map_call();

        // 텍스트 입력 확인.
        EditText totext = (EditText) findViewById(R.id.ToText);
        EditText fromText = (EditText) findViewById(R.id.FromText);

        // 목적지 정보가 있는 경우.
        if(daddr !=null){

            //목적지 정보 위도 경도로 변환.

            final Geocoder geocoder = new Geocoder(this);
            List<Address> list = null;
            try {
                list = geocoder.getFromLocationName
                        (daddr, // 지역 이름
                                10); // 읽을 개수
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
                lat = 37.5666805;
                lon = 126.9784147;
                marker_name = "location find_error";
            }
            if (list != null) {
                if (list.size() == 0) {
                    Log.e("test", "해당되는 주소 정보는 없습니다");
                    lat = 37.5666805;
                    lon = 126.9784147;
                    marker_name = "location cannot_find";
                } else {
                    // 해당되는 주소로 인텐트 날리기
                    Address addr = list.get(0);
                    lat = addr.getLatitude();
                    lon = addr.getLongitude();
                    totext.setText(daddr);
                    marker_name = daddr;
                }
            }

        } else if (daddr == null){
            // 목적지 정보가 없는 경우 서울시청으로 고정됨.
            lat = 37.5666805;
            lon = 126.9784147;
        }

        // 출발지 정보가 있는 경우 출발지 입력 부분에 데이터 표시.
        if(laddr !=null){
            fromText.setText(laddr);
        }


        // 구글 맵 프래그먼트 생성 부분.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // 버튼 선언.
        Button placebutton = (Button) findViewById(R.id.placeButton);

        // 버튼 입력 행동 처리.
        placebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (totext.getText().toString().length() != 0 && fromText.getText().toString().length() != 0){ // 만약 출발지 정보가 있고 목적지 정보도 있는 경우.

                    Intent intent = map_call.call_Google_map(totext.getText().toString(),fromText.getText().toString(),0,0);

                    startActivity(intent);

                } else if(totext.getText().toString().length() != 0 && fromText.getText().toString().length() == 0) { // 만약 목적지 정보만 있고 출발지 정보가 없는 경우 : 현재 GPS 위치를 기반으로 안내.

                    Intent intent = map_call.call_Google_map(totext.getText().toString(),"null",gpsTracker.getLatitude(),gpsTracker.getLongitude());

                    startActivity(intent);

                } else { // 목적지 정보가 없는 경우 : 목적지 정보 입력 요청하는 토스트 메세지 생성.
                    Toast.makeText(getApplicationContext(),"Please enter destination information at least.",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    // 구글 맵 api 관련 메소드.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        LatLng Location = new LatLng(lat, lon);
        googleMap.addMarker(new MarkerOptions()
                .position(Location)
                .title(marker_name));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Location,16));
    }

}