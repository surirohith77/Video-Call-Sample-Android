package com.rs.videotring;

import com.google.gson.annotations.SerializedName;

public class Users {

    @SerializedName("name")
    String name;

    @SerializedName("mobile")
    String mobile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
