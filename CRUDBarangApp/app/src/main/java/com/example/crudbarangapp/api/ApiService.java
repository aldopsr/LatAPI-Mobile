package com.example.crudbarangapp.api;

import com.example.crudbarangapp.model.Barang;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @GET("barang")
    Call<List<Barang>> getBarang();

    @POST("barang")
    Call<Barang> tambahBarang(@Body Barang barang);

    @PUT("barang/{id}")
    Call<Barang> updateBarang(@Path("id") String id, @Body Barang barang);

    @DELETE("barang/{id}")
    Call<Void> deleteBarang(@Path("id") String id);
}