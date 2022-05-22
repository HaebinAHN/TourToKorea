package com.teamb.tourtokorea;

import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class Http implements Runnable{
    String mURL = null;
    Search_list.MyHandler mhandler = null;

    public Http(String input, Search_list.MyHandler handler){

        String encoded = null;

        try {
            encoded = URLEncoder.encode(input,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mURL = "http://apis.data.go.kr/1262000/InkoEmblgbdInfoService/getInkoEmblgbdInfoList?serviceKey=z70jaurFdFd6Ead0U90sxj3i%2BEKOAUEyNLMKHHKOFwuYtd5LiVKqkFeQJpxhlF6TUanosi558lywuejHBCCVfQ%3D%3D&pageNo=1&numOfRows=10&cond[embassy_nm::LIKE]="+encoded;

        mhandler = handler;
    }

    @Override
    public void run() {
        String result = "";
        String line = null;
        InputStream in = null;
        BufferedReader reader = null;
        try{

            URL url = new URL(mURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            /*con.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });*/

            con.setDoInput(true);
            con.setUseCaches(false);
            con.setReadTimeout(1000);
            con.setConnectTimeout(1000);
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            con.connect();

            int responseCode = con.getResponseCode();
            Log.d("http","responsecode : "+responseCode);
            Log.d("http","response : "+con.getResponseMessage());

            con.setInstanceFollowRedirects(true);

            if(responseCode == HttpsURLConnection.HTTP_OK){

                in = con.getInputStream();

            }else{
                in = con.getErrorStream();
            }

            reader = new BufferedReader(new InputStreamReader(in));
            while((line = reader.readLine())!=null){
                result += line;
            }

            reader.close();

            if(con !=null){
                con.disconnect();
            }

        }catch(Exception e){

            result = "Error!";
            Message message = mhandler.obtainMessage(404,result);
            mhandler.sendMessage(message);

        }finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.d("http","data : "+result);

        JSONArray addr_obj = null;

        try {
            //String to Json
            JSONObject jsonObj = new JSONObject(result);

            Message message = mhandler.obtainMessage(101,jsonObj);

            mhandler.sendMessage(message);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}