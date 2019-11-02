package com.canblack.mallapp.network

import retrofit2.http.GET
import io.reactivex.Observable

interface Service{
    @GET("avm.json")
    fun getMallsRx(): Observable<Response>

    @GET("marka.json")
    fun getShopRx():Observable<Response>

    @GET("marka.json")
    fun gettShopRx():Observable<Response>
}