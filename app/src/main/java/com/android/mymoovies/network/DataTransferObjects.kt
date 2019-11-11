package com.android.mymoovies.network

import com.android.mymoovies.database.DatabaseMovie
import com.android.mymoovies.domain.Movie
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkMovieContainer(val videos: List<NetworkMovie>)

@JsonClass(generateAdapter = true)
data class NetworkMovie(
    val title: String,
    val release_date: String,
    val vote_average: Double,
    val poster_path: String)

/**
 * Convert Network results to domain objects
 */
fun NetworkMovieContainer.asDomainModel(): List<Movie> {
    return videos.map {
        Movie(
            title = it.title,
            release_date = it.release_date,
            vote_average = it.vote_average,
            poster_path = it.poster_path)
    }
}

/**
 *  Convert Network results to database objects
 */
fun NetworkMovieContainer.asDatabaseModel(): Array<DatabaseMovie> {
    return videos.map {
        DatabaseMovie(
            title = it.title,
            release_date = it.release_date,
            vote_average = it.vote_average,
            poster_path = it.poster_path)
    }.toTypedArray()
}