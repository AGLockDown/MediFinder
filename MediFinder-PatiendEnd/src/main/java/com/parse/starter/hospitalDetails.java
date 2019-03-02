package com.parse.starter;

public class hospitalDetails {
    public String name;
    public String distance;
    public String phoneNum;

    public String getName() {
        return name;
    }

    public String getPhone() { return phoneNum; }

    public void setPhone(String phoneNum){ this.phoneNum = phoneNum; }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

}
