package com.teamb.tourtokorea;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class diplomatic_mission extends AppCompatActivity implements OnMapReadyCallback{

    // 요청받은 위치를 구글 맵 api를 이용한 프래그먼트에 표시하고 주변 지역 탐색 및 길찾기 안내 액티비티 호출하는 클래스.

    //요청받은 위치 주소 스트링.
    String dipLocation = null;


    //위도 경도 정보.
    double lat = 0;
    double lon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diplomatic_mission);


        // 인텐트를 통하여 주소 정보를 adr로 넘겨받음.
        Intent received_intent = getIntent();
        dipLocation = received_intent.getStringExtra("adr");

        //구글 맵 프래그먼트 활성화 부분.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_Location);
        mapFragment.getMapAsync(this);

        //버튼 처리.
        Button transport = (Button) findViewById(R.id.transport);
        Button look = (Button) findViewById(R.id.Look_around);

        transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //길찾기 안내 액티비티 호출, 목적지 정보 daddr 로 전달.
                Intent intent = new Intent(getApplicationContext(),DirectionActivity.class);
                intent.putExtra("daddr",dipLocation);
                startActivity(intent);

            }
        });

        look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //구글 맵 이용 목적지 주변 식당, 카페, 랜드마크 정보 호출.

                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(),view);
                getMenuInflater().inflate(R.menu.lookaround_popup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.popup_restaurants){
                            Uri gmmIntentUri = Uri.parse("geo:"+lat+","+lon+"?q=restaurants");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }else if (menuItem.getItemId() == R.id.popup_Cafe){
                            Uri gmmIntentUri = Uri.parse("geo:"+lat+","+lon+"?q=cafe");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }else {
                            Uri gmmIntentUri = Uri.parse("geo:"+lat+","+lon+"?q=landmark");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

    //구글 맵 api 호출 메소드 (목적지 주소 정보 위동 경도 변환. : 에러발생시 위치 서울시청으로 고정되고 에러 정보 표시.)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        final Geocoder geocoder = new Geocoder(this);
        String marker_name = null;

        List<Address> list = null;

        lat = 0;
        lon = 0;

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
                    adr.setText(dipLocation);
                }

            }

        }

        LatLng Location = new LatLng(lat, lon);
        googleMap.addMarker(new MarkerOptions()
                .position(Location)
                .title(marker_name));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Location,16));
    }

}