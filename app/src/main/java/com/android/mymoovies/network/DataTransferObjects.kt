package com.android.mymoovies.network

import com.android.mymoovies.database.DatabaseMovie
import com.android.mymoovies.domain.Movie
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkMovieContainer(val movies: List<NetworkMovie>)

@JsonClass(generateAdapter = true)
data class NetworkMovie(
    val title: String,
    val release_date: String,
    val vote_average: Double,
    val poster_path: String)

fun NetworkMovieContainer.asDomainModel(): List<Movie> {
    return movies.map {
        Movie(
            title = it.title,
            release_date = it.release_date,
            vote_average = it.vote_average,
            poster_path = it.poster_path)
    }
}

fun NetworkMovieContainer.asDatabaseModel(): Array<DatabaseMovie> {
    return movies.map {
        DatabaseMovie(
            title = it.title,
            release_date = it.release_date,
            vote_average = it.vote_average,
            poster_path = it.poster_path)
    }.toTypedArray()
}