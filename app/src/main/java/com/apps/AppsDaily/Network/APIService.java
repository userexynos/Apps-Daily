package com.apps.AppsDaily.Network;

import com.apps.AppsDaily.Network.Response.Auth;
import com.apps.AppsDaily.Network.Response.GetCity;
import com.apps.AppsDaily.Network.Response.GetProvince;
import com.apps.AppsDaily.Network.Response.GetUsersProfile;
import com.apps.AppsDaily.Network.Response.LatestStore;
import com.apps.AppsDaily.Network.Response.Register;
import com.apps.AppsDaily.Network.Response.SendData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {
    @GET("users/profile")
    Call<GetUsersProfile> getProfile(@Header("Authorization") String token);

    @GET("data/province")
    Call<GetProvince> getProvince();

    @GET("data/province/{id}")
    Call<GetCity> getKota(@Path("id") Integer id);

    @GET("users/get_latest_store")
    Call<LatestStore> getLatest(@Header("Authorization") String token);

    @POST("users/do_update_profile")
    @FormUrlEncoded
    Call<SendData> update(@Header("Authorization") String token,
                          @Field("fullname") String name, @Field("no_handphone") String nohp,
                          @Field("gender") int usia, @Field("email") String email, @Field("work") String work,
                          @Field("last_education") String last_edu, @Field("bod") String ttl,
                          @Field("location") String location, @Field("income") String pendapatan,
                          @Field("price_range") String harga, @Field("preference") String preferensi,
                          @Field("brand_handphone") String merkHP);

    @POST("users/login")
    @FormUrlEncoded
    Call<Auth> auth(@Field("email") String username, @Field("password") String password);

    @POST("users/register")
    @FormUrlEncoded
    Call<Register> register(@Field("fullname") String name, @Field("no_handphone") String nohp,
                            @Field("gender") int usia, @Field("email") String email, @Field("work") String work,
                            @Field("last_education") String last_edu, @Field("bod") String ttl,
                            @Field("password") String password, @Field("location") String location,
                            @Field("province") Integer province, @Field("city") Integer city, @Field("income") String income,
                            @Field("price_range") String price, @Field("preference") String preference,
                            @Field("brand_handphone") String handphone);

    @POST("users/data")
    @FormUrlEncoded
    Call<SendData> send(@Header("Authorization") String token,
                        @Field("name_application") String app, @Field("name_activity") String activity,
                        @Field("duration_usage") String usage, @Field("date") String date,
                        @Field("time") String time);
}
