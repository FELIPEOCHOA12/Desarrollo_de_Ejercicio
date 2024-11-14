package com.tecsup.lab10tarea.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductApiService {
    @GET("product")
    suspend fun selectProducts(): ArrayList<ProductModel>

    @GET("product/{id}")
    suspend fun selectProduct(@Path("id") id: String): Response<ProductModel>

    @Headers("Content-Type: application/json")
    @POST("product")
    suspend fun insertProduct(@Body product: ProductModel): Response<ProductModel>

    @PUT("product/{id}")
    suspend fun updateProduct(@Path("id") id: String, @Body product: ProductModel): Response<ProductModel>

    @DELETE("product/{id}")
    suspend fun deleteProduct(@Path("id") id: String): Response<ProductModel>
}
