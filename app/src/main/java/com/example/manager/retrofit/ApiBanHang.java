package com.example.manager.retrofit;

import com.example.manager.model.Category;
import com.example.manager.model.MessageModel;
import com.example.manager.model.Product;
import com.example.manager.model.ProductModel;
import com.example.manager.model.User;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiBanHang {
    @GET("getcategory.php")
    Observable<Category> getAPICategory();

    @GET("getproduct.php")
    Observable<Product> getAPIProduct();

    @POST("getinfoproduct.php")
    @FormUrlEncoded
    Observable<Product> getAPIProduct(
            @Field("page") int page,
            @Field("info") int info
    );

    @POST("register.php")
    @FormUrlEncoded
    Observable<User> registerAPI(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("mobile") String mobile,
            @Field("uid") String uid
    );

    @POST("login.php")
    @FormUrlEncoded
    Observable<User> loginAPI(
            @Field("email") String email,
            @Field("pass") String pass
    );

    @POST("reset.php")
    @FormUrlEncoded
    Observable<User> resetpassAPI(
            @Field("email") String email
    );

    @POST("bill.php")
    @FormUrlEncoded
    Observable<User> billAPI(
            @Field("email") String email,
            @Field("total") String total,
            @Field("phone") String phone,
            @Field("address") String address,
            @Field("quanlity") int quanlity,
            @Field("iduser") int iduser,
            @Field("billinfo") String billinfo
    );

    @POST("xoa.php")
    @FormUrlEncoded
    Observable<MessageModel> xoaSanPham (
            @Field("MaSPMoi") int id
    );

    @POST("insertsp.php")
    @FormUrlEncoded
    Observable<MessageModel> insertSp(
            @Field("tensp") String tensp,
            @Field("gia") String gia,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int id
    );

    @POST("updatesp.php")
    @FormUrlEncoded
    Observable<MessageModel> updatesp (
            @Field("tensp") String tensp,
            @Field("gia") String gia,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int idloai,
            @Field("MaSPMoi") int id
    );

    @POST("updatetoken.php")
    @FormUrlEncoded
    Observable<MessageModel> updateToken (
            @Field("id") int id,
            @Field("token") String token
    );

    @Multipart
    @POST("upload.php")
    Call<MessageModel> uploadFile(@Part MultipartBody.Part file);
}
