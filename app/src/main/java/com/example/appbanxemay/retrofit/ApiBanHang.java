package com.example.appbanxemay.retrofit;

import com.example.appbanxemay.model.LoaiSpModel;
import com.example.appbanxemay.model.SanPhamMoiModel;
import com.example.appbanxemay.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanHang {
    @GET("getloaisp.php")
    Observable<LoaiSpModel> getLoaiSp();
    @GET("getproduct.php")
    Observable<SanPhamMoiModel> getSpMoi();

    @POST("chitietsp.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPham(
            @Field("page") int page,
            @Field("idsanpham") int idsanpham
    );

    @POST("dangki.php")
    @FormUrlEncoded
    Observable<UserModel> dangKi(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("mobile") String mobile


    );
    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangNhap(
            @Field("email") String email,
            @Field("pass") String pass
    );

    @POST("reset.php")
    @FormUrlEncoded
    Observable<UserModel> resetPass(
            @Field("email") String email

    );
    @POST("timkiem.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> search(
            @Field("search") String search

    );
}
