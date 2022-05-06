package com.teamb.tourtokorea;

import android.content.Intent;
import android.net.Uri;

import java.net.URLEncoder;

public class Google_map_call {
    // 구글 맵 길찾기 인텐트 콜 부분.
    public Intent call_Google_map(String to, String from, double lat, double lon){

        String uri;

        if(from == "null"){

            uri ="http://maps.google.com/maps?saddr="+lat+","+lon+"&daddr="+ URLEncoder.encode(to);

        } else{
            uri ="http://maps.google.com/maps?saddr="+URLEncoder.encode(from)+"&daddr="+ URLEncoder.encode(to);
        }

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(uri));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_LAUNCHER );
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        return intent;

    }

}
