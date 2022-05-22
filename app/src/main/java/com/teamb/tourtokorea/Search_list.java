package com.teamb.tourtokorea;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Search_list extends AppCompatActivity {

    // open api 에서 응답한 JSON 데이터의 정보를 리스트뷰에 표시해주는 클래스.

    String search = null; // open api 검색 키워드.

    // 리스트뷰 생성을 위한 커스텀 어댑터. + 리스트뷰 객체.
    AddrAdapter adapter = null;
    ListView list = null;

    // 스레드간 통신을 위한 핸들러 객체 : Http 클래스 스레드에서 JSON 데이터 수신.
    private final MyHandler mHandler = new MyHandler(this);
    ArrayList<Addr_data> addrDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        //인텐트 수신.
        Intent received_intent = getIntent();

        search = received_intent.getStringExtra("search");

        //주소 데이터 리스트 객체 생성 및 초기 데이터 생성.
        addrDataList = new ArrayList<Addr_data>();
        addrDataList.add(new Addr_data("country","address"));

        //리스트뷰 어댑터 생성.
        list = (ListView) findViewById(R.id.search_listview);
        adapter = new AddrAdapter(this,addrDataList);
        list.setAdapter(adapter);


        // 안드로이드 단에서 UI 스레드상에서의 웹 접속을 차단하기 때문에 별도 class 를 스레드로 추가 생성 및 open-api 호출.
        Http request = new Http(search,mHandler);

        try{
            Thread thread = new Thread(request);
            thread.start();

            try{
                thread.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        //리스트뷰 아이템 클릭 이벤트
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i !=0){
                    //위치정보 표시 액티비티로 인텐트에 adr 정보 담아서 전달.
                    Intent intent = new Intent(getApplicationContext(),diplomatic_mission.class);
                    intent.putExtra("adr",adapter.getItem(i).getaddr());
                    startActivity(intent);
                }
            }
        });


    }

    public void refresh(){
        adapter = new AddrAdapter(this,addrDataList);
        list.setAdapter(adapter);
    }

    public static class MyHandler extends Handler {
        private final WeakReference<Search_list> weakReference;

        public MyHandler(Search_list Activity) {
            weakReference = new WeakReference<Search_list>(Activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Search_list activity = weakReference.get();

            TextView error = activity.findViewById(R.id.error);

            if (activity != null) {
                switch (msg.what) {
                    // http 클래스에서 JSON 데이터를 넘겨받은 경우.
                    case 101:
                        JSONObject jsonobj = (JSONObject) msg.obj;
                        String result = "";

                        try {

                            int count = jsonobj.getInt("currentCount");

                            // count 값이 0 인경우 검색 결과 없음. , 이외 경우 검색 결과 리스트뷰에 담아서 리프레쉬.
                            if(count !=0){

                                JSONArray data = (JSONArray) jsonobj.getJSONArray("data");

                                for(int i=0;i<count;i++){

                                    JSONObject data_tmp = (JSONObject) data.opt(i);
                                    activity.addrDataList.add(new Addr_data(data_tmp.getString("embassy_nm")+" :","address : "+ data_tmp.getString("addr")));
                                }
                                activity.refresh();

                            }else{

                                activity.addrDataList.add(new Addr_data("Search result NULL","Search result NULL"));
                                activity.refresh();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    // http 클래스에서 JSON 데이터를 넘겨받지 못한 경우.
                    case 404:

                        error.setText("Internet error occurred");

                        break;

                }
            }
        }
    }

}

