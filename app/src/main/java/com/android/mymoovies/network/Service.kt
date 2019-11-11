package com.android.mymoovies.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/{id}")
    fun getDetails(@Path("id") id: Int, @Query("api_key") apiKey: String) : Deferred<NetworkMovieContainer>


    @GET("movies/get-now-playing")
    fun searchAndQueryDetails(@Query("query") query: String, @Query("api_key") apiKey: String) : Deferred<NetworkMovieContainer>

    @GET("movies/get-now-playing")
    fun getPlaylistAsync(@Query("api_key") apiKey: String, @Query("language") language: String): Deferred<NetworkMovieContainer>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object Network {
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val devbytes = retrofit.create(MovieService::class.java)
}