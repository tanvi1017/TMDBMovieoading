package com.tanvi.apicallingdemo

 //import android.telecom.Call
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.tanvi.apicallingdemo.PopularMovies

interface TmdbEndpoint {
    @GET("/3/movie/popular")
    fun getMovies(
        @Query("api_key") key: String,
        @Query("page") page:Int
    ): Call<PopularMovies>
}