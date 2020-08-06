package com.rs.videotring;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiDao {


    @POST("services.aspx")
    Call<List<Users>> getUsers(
            @Query("request") String request,
            @Query("mobile") String mobile);

}
