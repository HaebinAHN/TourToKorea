package com.teamb.tourtokorea;

public class Addr_data {

    //커스텀 리스트뷰 데이터 객체.

    private String addrName;
    private String addr;

    public Addr_data(String addrName, String addr){
        this.addrName = addrName;
        this.addr = addr;
    }

    public String getaddrName()
    {
        return this.addrName;
    }

    public String getaddr()
    {
        return this.addr;
    }

}
