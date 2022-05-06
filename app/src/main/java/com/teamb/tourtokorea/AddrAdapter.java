package com.teamb.tourtokorea;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AddrAdapter extends BaseAdapter {

    //custom 리스트뷰 어댑터 객체.

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Addr_data> sample;

    public AddrAdapter(Context context, ArrayList<Addr_data> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Addr_data getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.addr_list_view_item, null);

        TextView addrName = (TextView)view.findViewById(R.id.addrName);
        addrName.setPaintFlags(addrName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        TextView addr = (TextView)view.findViewById(R.id.addr);

        addrName.setText(sample.get(position).getaddrName());
        addr.setText(sample.get(position).getaddr());

        return view;
    }
}
